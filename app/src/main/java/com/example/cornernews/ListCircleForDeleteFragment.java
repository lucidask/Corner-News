package com.example.cornernews;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListCircleForDeleteFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, View.OnClickListener, GoogleMap.OnCircleClickListener {
    AppCompatImageButton help, close_help;
    AppCompatTextView title;
    AppCompatTextView back_arrow;
    AppCompatImageButton log_out;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    static GoogleMap gmap;
    List<Marker> markerList = new ArrayList<>();
    RelativeLayout relativeLayoutPopup;
    PopupWindow popupWindowhelp;
    HelperDB helperDB;



    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list_circle_for_delete, container, false);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map_for_delete);
        help=view.findViewById(R.id.help_button_for_delete);
        relativeLayoutPopup=view.findViewById(R.id.pathRelativeOnDelete);
        close_help=view.findViewById(R.id.close_help_button_for_delete);
        title=view.findViewById(R.id.title);
        back_arrow=view.findViewById(R.id.back_perso);
        back_arrow.setOnClickListener(this);
        helperDB=new HelperDB(getContext());
        try {
            title.setText("\uD83D\uDE42  " +helperDB.GetUserName().toUpperCase());
        }catch (Exception e){
            e.printStackTrace();
        }
        log_out=view.findViewById(R.id.button_logout);
        log_out.setVisibility(View.GONE);
        help.setOnClickListener(this);
        close_help.setOnClickListener(this);
        supportMapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        supportMapFragment.getMapAsync(googleMap -> {
            gmap = googleMap;
            gmap.clear();
            getCircletoMapforDelete(gmap);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_perso:
                requireActivity().finish();
                break;
//            case R.id.help_button_for_delete:
//                popupWindowhelp=showPopup();
//                help.setVisibility(View.GONE);
//                close_help.setVisibility(View.VISIBLE);
//                break;
//            case R.id.close_help_button_for_delete:
//                popupWindowhelp.dismiss();
//                close_help.setVisibility(View.GONE);
//                help.setVisibility(View.VISIBLE);
//                break;
        }
    }

    @Override
    public void onCircleClick(@NonNull Circle circle) {
        for (int i = 0; i < DAO.TabCircleForUserConnected.size(); i++) {
            if (circle.getCenter().latitude == DAO.TabCircleForUserConnected.get(i).getRond().getRondLat() &&
                    circle.getCenter().longitude == DAO.TabCircleForUserConnected.get(i).getRond().getRondLng()) {
                deleteAlertInstance(DAO.TabCircleForUserConnected.get(i).getAlertName());
                DAO.TabCircleForUserConnected.remove(i);
                gmap.clear();
                getCircletoMapforDelete(gmap);
                break;
            }
        }
    }

    public PopupWindow showPopup(){
        PopupWindow popupWindow;
        LayoutInflater layoutInflater=(LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View customView=layoutInflater.inflate(R.layout.popup_help_layout,null);
        popupWindow=new PopupWindow(customView, RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(relativeLayoutPopup, Gravity.TOP,0,0);
        return popupWindow;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gmap = googleMap;
        LatLng lastestCircleAddedPosition= new LatLng(DAO.TabCircleForUserConnected.get(DAO.TabCircleForUserConnected.size()-1).getRond().getRondLat(),
                DAO.TabCircleForUserConnected.get(DAO.TabCircleForUserConnected.size()-1).getRond().getRondLng());
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastestCircleAddedPosition,20));
        GetMapLocationPossible();
        gmap.setOnCircleClickListener(this);
        gmap.setOnMarkerClickListener(this);
        getCircletoMapforDelete(gmap);
    }

    public void GetMapLocationPossible(){
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gmap.setMyLocationEnabled(true);
        gmap.getMyLocation();
    }

    public void getCircletoMapforDelete(GoogleMap googleMap) {
        if (DAO.TabCircleForUserConnected.size() > 0) {
            for (int i = 0; i < DAO.TabCircleForUserConnected.size(); i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(DAO.TabCircleForUserConnected.get(i).getAlertName().contains("Green")){
                        MapFragment.drawCircleInstance(googleMap,DAO.TabCircleForUserConnected.get(i), BitmapDescriptorFactory.HUE_GREEN,markerList);
                    }else if(DAO.TabCircleForUserConnected.get(i).getAlertName().contains("Orange")){
                        MapFragment.drawCircleInstance(googleMap,DAO.TabCircleForUserConnected.get(i),BitmapDescriptorFactory.HUE_ORANGE,markerList);
                    }else {
                        MapFragment.drawCircleInstance(googleMap,DAO.TabCircleForUserConnected.get(i),BitmapDescriptorFactory.HUE_RED,markerList);
                    }
                }
            }
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Toast.makeText(getContext(), "Coming Soon",
                Toast.LENGTH_SHORT).show();
        return false;
    }

    public static void deleteAlertInstance(String AlertInstanceName){
        DAO.AlertDatabase.child(AlertInstanceName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
               DAO.imageMediaDatabase.child(AlertInstanceName).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            DAO.updateDaoAlertInstanceFromDatabase();
                        }
                    }
                });
            }
        });
    }
}