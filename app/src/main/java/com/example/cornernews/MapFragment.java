package com.example.cornernews;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MapFragment extends Fragment implements View.OnClickListener,OnMapReadyCallback, GoogleMap.OnCircleClickListener,GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    AppCompatImageButton help,close_help;
    //    EditText editText;
    AppCompatTextView title;
    static GoogleMap gmap;
    RadioGroup radioGroup;
    int selectedradiobutton;
    AppCompatImageButton btClear, btDelete, log_out, bt_edit, bt_refresh;
    AppCompatTextView back_arrow;
    static List<Marker> markerList = new ArrayList<>();
    public static final String WHO_CALL = "who_call";
    public static final String INT_FOR_CIRCLE = "CIRCLE_WHO_CALL";
    RelativeLayout relativeLayoutPopup;
    PopupWindow popupWindowhelp;
    GoogleSignInClient googleSignInClient;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"SetTextI18n", "Range"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map);
        radioGroup = view.findViewById(R.id.radio_group_color);
//        editText = view.findViewById(R.id.sv_location);
        log_out = view.findViewById(R.id.button_logout);
        title = view.findViewById(R.id.title);
        title.setText("\uD83D\uDE42  " + DAO.Whologin.get(0).getUsername().toUpperCase());
        btClear = view.findViewById(R.id.bt_clear);
        back_arrow = view.findViewById(R.id.back_perso);
        back_arrow.setVisibility(View.GONE);
//        back_arrow.setOnClickListener(this);
        client= LocationServices.getFusedLocationProviderClient(this.requireContext());
        bt_edit = view.findViewById(R.id.bt_edit);
        btDelete = view.findViewById(R.id.bt_delete);
        bt_refresh = view.findViewById(R.id.bt_refresh);
        help=view.findViewById(R.id.help_button_for_MapFragment);
        relativeLayoutPopup=view.findViewById(R.id.pathRelativeOnMapFragment);
        close_help=view.findViewById(R.id.close_help_button_for_MapFragment);
        bt_refresh.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        bt_edit.setOnClickListener(this);
        btClear.setOnClickListener(this);
        log_out.setOnClickListener(this);
        help.setOnClickListener(this);
        close_help.setOnClickListener(this);
//        editText.setOnClickListener(this);
        supportMapFragment.getMapAsync(this);
//        Places.initialize(getContext(), "AIzaSyBR442ETc1ih8UI_amGf-FPE7FE7eC8yn4");
//        editText.setFocusable(false);
        checklocationpermission();
        DAO.updateDaoZoneFromDatabase();
        DAO.updateDaoMediaFromDatabase();
        DAO.updateListCircleListImageAndListVideo();
        DAO.getallcirclethatuserconnectedcreated();
        googleSignInClient= GoogleSignIn.getClient(requireContext(), GoogleSignInOptions.DEFAULT_SIGN_IN);
        if (DAO.TabCircleBuf.isEmpty()) {
            btClear.setVisibility(View.GONE);
            bt_edit.setVisibility(View.GONE);
        } else {
            btClear.setVisibility(View.VISIBLE);
            bt_edit.setVisibility(View.VISIBLE);
        }

        if (DAO.TabCircleForUserConnected.size() > 0) {
            btDelete.setVisibility(View.VISIBLE);
        } else {
            btDelete.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        checklocationpermission();
        DAO.updateDaoZoneFromDatabase();
        DAO.updateDaoMediaFromDatabase();
        DAO.updateListCircleListImageAndListVideo();
        radioGroup.clearCheck();
        supportMapFragment.getMapAsync(googleMap -> {
            gmap = googleMap;
            GetMapLocationPossible();
            gmap.clear();
            circletomap(gmap);
            circlebuftomap(gmap);
        });
        DAO.getallcirclethatuserconnectedcreated();
        if (DAO.TabCircleForUserConnected.size() > 0) {
            btDelete.setVisibility(View.VISIBLE);
        } else {
            btDelete.setVisibility(View.GONE);
        }

        if (DAO.TabCircleBuf.isEmpty()) {
            btClear.setVisibility(View.GONE);
            bt_edit.setVisibility(View.GONE);
        } else {
            btClear.setVisibility(View.VISIBLE);
            bt_edit.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_delete:
            case R.id.bt_edit:
                Intent gotodeleteoreditzone = new Intent(this.getContext(), ContainerFrag.class);
                gotodeleteoreditzone.putExtra(WHO_CALL, v.getId());
                startActivity(gotodeleteoreditzone);
                break;
            case R.id.bt_clear:
                if(DAO.TabCircleBuf.size()>0){
                    DAO.TabCircleBuf.clear();
                }
                bt_edit.setVisibility(View.GONE);
                gmap.clear();
                circletomap(gmap);
                btClear.setVisibility(View.GONE);
                break;
//            case R.id.sv_location:
//                List<Place.Field> fieldList= Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
//                Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(MapFragment.this.requireContext());
//                startActivityForResult(intent,100);
//                break;
//            case R.id.back_perso:
//                requireActivity().finish();
//                break;
            case R.id.button_logout:
                FirebaseUser firebaseuser= FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseuser!=null){
                    if(googleSignInClient!=null){
                        googleSignInClient.signOut().addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                FirebaseAuth.getInstance().signOut();
                                DAO.Whologin.clear();
                                FirebaseAuth.getInstance().signOut();
                                Intent start=new Intent(getContext(),MainActivity.class);
                                start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                start.putExtra("EXIT",true);
                                startActivity(start);
                                requireActivity().finish();
                            }else {
                                Toast.makeText(MapFragment.this.getContext(), "Authentication Failed"+ Objects.requireNonNull(task.getException()).getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        FirebaseAuth.getInstance().signOut();
                        DAO.Whologin.clear();
                        Intent start=new Intent(getContext(),MainActivity.class);
                        start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        start.putExtra("EXIT",true);
                        startActivity(start);
                        requireActivity().finish();
                    }
                }
//                DAO.Whologin.clear();
//                FirebaseAuth.getInstance().signOut();
//                Intent start=new Intent(getContext(),MainActivity.class);
//                start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                start.putExtra("EXIT",true);
//                startActivity(start);
//                requireActivity().finish();
                break;
            case R.id.bt_refresh:
                refreshingFragment();
                break;
//            case R.id.help_button_for_MapFragment:
//                popupWindowhelp=showPopup();
//                help.setVisibility(View.GONE);
//                close_help.setVisibility(View.VISIBLE);
//                break;
//            case R.id.close_help_button_for_MapFragment:
//                popupWindowhelp.dismiss();
//                close_help.setVisibility(View.GONE);
//                help.setVisibility(View.VISIBLE);
//                break;
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==100 && resultCode== RESULT_OK){
//            Place place=Autocomplete.getPlaceFromIntent(data);
//            editText.setText(place.getAddress());
//            LatLng latLng=new LatLng(place.getLatLng().latitude,place.getLatLng().longitude);
//            gmap.addMarker(new MarkerOptions().position(latLng).title("You're searching this place"));
//            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
//        }
//        else if(resultCode== AutocompleteActivity.RESULT_ERROR){
//            Status status= Autocomplete.getStatusFromIntent(data);
//            Toast.makeText(getContext(),status.getStatusMessage(),Toast.LENGTH_LONG).show();
//        }
//    }

    public void drawCircle(GoogleMap googleMap,LatLng latLng, int color, String name,double radius,float markerColor ){
        int k3 = (int) (Math.random() * 10 * Math.random() * 100);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title(DAO.Whologin.get(0).getUsername()+name+k3).icon(BitmapDescriptorFactory.defaultMarker(markerColor));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
        Marker marker = googleMap.addMarker(markerOptions);
        markerList.add(marker);
        Circle circ = googleMap.addCircle(
                new CircleOptions()
                        .center(latLng)
                        .radius(radius)
                        .strokeWidth(3f)
                        .strokeColor(Color.BLACK)
                        .fillColor(color)
                        .clickable(true)
        );
        DAO.addCirlcleBuf(new CircleInstance(DAO.Whologin.get(0).getUsername(), markerOptions.getTitle(), new Rond(circ)));
        btClear.setVisibility(View.VISIBLE);
        bt_edit.setVisibility(View.VISIBLE);
    }
    public void circletomap(GoogleMap googleMap) {
        if (DAO.TabCircle.size() > 0) {
            for (int i = 0; i < DAO.TabCircle.size(); i++) {
                if(DAO.TabCircle.get(i).getCirclename().contains("Green")){
                    drawCircleOnDBtoMap(googleMap,DAO.TabCircle.get(i),BitmapDescriptorFactory.HUE_GREEN,markerList);
//                    googleMap.addCircle(DAO.TabCircle.get(i).getRond().getcircleOptions());
//                    MarkerOptions markerOptions = new MarkerOptions().position(Objects.requireNonNull(DAO.TabCircle.get(i).getRond().getcircleOptions().getCenter()));
//                    markerOptions.title(DAO.TabCircle.get(i).getCirclename());
//                    Marker marker = gmap.addMarker(markerOptions);
//                    markerList.add(marker);
                }else if(DAO.TabCircle.get(i).getCirclename().contains("Orange")){
                    drawCircleOnDBtoMap(googleMap,DAO.TabCircle.get(i),BitmapDescriptorFactory.HUE_ORANGE,markerList);
//                    googleMap.addCircle(DAO.TabCircle.get(i).getRond().getcircleOptions());
//                    MarkerOptions markerOptions = new MarkerOptions().position(Objects.requireNonNull(DAO.TabCircle.get(i).getRond().getcircleOptions().getCenter()));
//                    markerOptions.title(DAO.TabCircle.get(i).getCirclename());
//                    Marker marker = gmap.addMarker(markerOptions);
//                    markerList.add(marker);
                }else {
                    drawCircleOnDBtoMap(googleMap,DAO.TabCircle.get(i),BitmapDescriptorFactory.HUE_RED,markerList);
//                    googleMap.addCircle(DAO.TabCircle.get(i).getRond().getcircleOptions());
//                    MarkerOptions markerOptions = new MarkerOptions().position(Objects.requireNonNull(DAO.TabCircle.get(i).getRond().getcircleOptions().getCenter()));
//                    markerOptions.title(DAO.TabCircle.get(i).getCirclename());
//                    Marker marker = gmap.addMarker(markerOptions);
//                    markerList.add(marker);
                }
            }
        }
    }

    public static void drawCircleOnDBtoMap(GoogleMap googleMap,CircleInstance circleInstance,float markerColor,List<Marker> markerList){
        googleMap.addCircle(circleInstance.getRond().getcircleOptions());
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(markerColor))
                .position(Objects.requireNonNull(circleInstance.getRond().getcircleOptions().getCenter()));
        markerOptions.title(circleInstance.getCirclename());
        Marker marker = googleMap.addMarker(markerOptions);
       markerList.add(marker);
    }


    public static void circlebuftomap(GoogleMap googleMap) {
        if (DAO.TabCircleBuf.size() > 0) {
            for (int i = 0; i < DAO.TabCircleBuf.size(); i++) {
                if (DAO.TabCircleBuf.get(i).getUsername() == DAO.Whologin.get(0).getUsername()) {
                    if(DAO.TabCircleBuf.get(i).getCirclename().contains("Green")){
                        drawCircleOnDBtoMap(googleMap,DAO.TabCircleBuf.get(i),BitmapDescriptorFactory.HUE_GREEN,markerList);
                    }else if(DAO.TabCircleBuf.get(i).getCirclename().contains("Orange")){
                        drawCircleOnDBtoMap(googleMap,DAO.TabCircleBuf.get(i),BitmapDescriptorFactory.HUE_ORANGE,markerList);
                    }else {
                        drawCircleOnDBtoMap(googleMap,DAO.TabCircleBuf.get(i),BitmapDescriptorFactory.HUE_RED,markerList);
                    }

//                    googleMap.addCircle(DAO.TabCircleBuf.get(i).getRond().getcircleOptions());
//                    MarkerOptions markerOptions = new MarkerOptions().position(Objects.requireNonNull(DAO.TabCircleBuf.get(i).getRond().getcircleOptions().getCenter()));
//                    markerOptions.title(DAO.TabCircleBuf.get(i).getCirclename());
//                    Marker marker = googleMap.addMarker(markerOptions);
//                    markerList.add(marker);
                }
            }
        }
    }

    @Override
    public void onCircleClick(@NonNull @NotNull Circle circle) {
        DAO.updateListCircleListImageAndListVideo();
        Intent gotoputmoreinfos = new Intent(this.getContext(), ContainerFrag.class);
        gotoputmoreinfos.putExtra(INT_FOR_CIRCLE, circle.getCenter().toString());
        startActivity(gotoputmoreinfos);
    }

    private void refreshingFragment(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                requireActivity().getSupportFragmentManager().beginTransaction().detach(MapFragment.this).commitNow();
                requireActivity().getSupportFragmentManager().beginTransaction().attach(MapFragment.this).commitNow();
            } else {
                requireActivity().getSupportFragmentManager().beginTransaction().detach(MapFragment.this).attach(MapFragment.this).commit();
            }
//            refresh(1000);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void refresh(int millisecond) {
//        final Handler handler=new Handler();
//        final  Runnable runnable= new Runnable() {
//            @Override
//            public void run() {
//                refreshingFragment();
//            }
//        };
//        handler.postDelayed(runnable,millisecond);
//    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gmap=googleMap;
//        LatLng Ayiti= new LatLng(19.0558462 ,-73.0513321);
//        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(Ayiti,8 ));
        getCurrentLocation(googleMap);
        GetMapLocationPossible();
        gmap.setOnMapClickListener(this);
        gmap.setOnMapLongClickListener(this);
        gmap.setOnMarkerClickListener(this);
        gmap.setOnCircleClickListener(this);
        circletomap(gmap);
        circlebuftomap(gmap);
    }

    public void GetMapLocationPossible(){
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gmap.setMyLocationEnabled(true);
        gmap.getMyLocation();
    }

    public void getCurrentLocation(GoogleMap googleMap){
        @SuppressLint("MissingPermission") Task<Location> task =client.getLastLocation();
        task.addOnSuccessListener(location -> {
            if(location !=null){
//                mapFragment.getMapAsync(new OnMapReadyCallback() {
//                    @Override
//                    public void onMapReady(@NotNull GoogleMap googleMap1) {
//                        gmap= googleMap1;
//                        gmap.clear();
//                        gmap.setOnCircleClickListener(MapFragment.this);
                        LatLng latLng= new LatLng(location.getLatitude(),location.getLongitude());
//                        MarkerOptions marker= new MarkerOptions().position(latLng)
//                                .title("You are here");
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,8 ));
//                        googleMap.addMarker(marker);
//                    }
//                });
            }
        });
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        selectedradiobutton = radioGroup.getCheckedRadioButtonId();
        switch (selectedradiobutton) {
            case R.id.radio_green:
                drawCircle(gmap, latLng, Color.rgb(34, 139, 34), "_Circle_Green_", 10,BitmapDescriptorFactory.HUE_GREEN);
                break;
            case R.id.radio_orange:
                drawCircle(gmap, latLng, Color.rgb(255, 140, 0), "_Circle_Orange_", 10.,BitmapDescriptorFactory.HUE_ORANGE);
                break;
            case R.id.radio_red:
                drawCircle(gmap, latLng, Color.RED, "_Circle_Red_", 10,BitmapDescriptorFactory.HUE_RED);
                break;
            default:
                Toast.makeText(MapFragment.this.getContext(), "Select Category",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        gmap.addMarker(markerOptions);
        btClear.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Toast.makeText(MapFragment.this.getContext(), "Coming Soon",
                Toast.LENGTH_SHORT).show();
        return false;
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void GPSAsking() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(createLocationRequest());
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(requireActivity()).checkLocationSettings(builder.build());
        result.addOnCompleteListener(task -> {
            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
                // All location settings are satisfied. The client can initialize location
                // requests here.
            } catch (ApiException exception) {
                switch (exception.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(getActivity(),
                                    LocationRequest.PRIORITY_HIGH_ACCURACY);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        } catch (ClassCastException e) {
                            // Ignore, should be an impossible error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LocationRequest.PRIORITY_HIGH_ACCURACY) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    break;
                case Activity.RESULT_CANCELED:
                    break;
                default:
                    break;
            }
        }
    }

    public void checklocationpermission() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            GPSAsking();
        } else {
            ActivityCompat.requestPermissions(MapFragment.this.requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GPSAsking();
            }
        }
    }

    public PopupWindow showPopup(){
        PopupWindow popupWindow;
        LayoutInflater layoutInflater=(LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View customView=layoutInflater.inflate(R.layout.popup2_help_layout,null);
        popupWindow=new PopupWindow(customView, RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(relativeLayoutPopup, Gravity.TOP,0,0);
        return popupWindow;
    }
}