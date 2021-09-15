package com.example.cornernews;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListCircleForEditFragment extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback, GoogleMap.OnCircleClickListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener {

    AppCompatImageButton help,close_help;
    AppCompatTextView title;
    AppCompatTextView back_arrow;
    AppCompatImageButton log_out;
    SupportMapFragment supportMapFragment;
    static GoogleMap gmap;
    List<Marker> markerList = new ArrayList<>();
    AppCompatSeekBar seekBar;
    CircleInstance circleInstance;
    LinearLayoutCompat linearLayoutCompat;
    RadioGroup radioGroup;
    RelativeLayout relativeLayoutPopup;
    PopupWindow popupWindowhelp;
    HelperDB helperDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list_circle_for_edit, container, false);
        supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.google_map_for_delete);
        help=view.findViewById(R.id.help_button_for_edited);
        relativeLayoutPopup=view.findViewById(R.id.pathRelativeOnEdit);
        close_help=view.findViewById(R.id.close_help_button_for_edit);
        radioGroup = view.findViewById(R.id.radio_group_color_edit);
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
        seekBar=view.findViewById(R.id.seekbar_for_resize_circle);
        linearLayoutCompat=view.findViewById(R.id.linear_for_edit_content);
        seekBar.setOnSeekBarChangeListener(this);
        radioGroup.setOnCheckedChangeListener(this);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                MapFragment.CircleBufToMap(gmap);
            }
        });
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Toast.makeText(requireContext(), "Coming Soon",
                Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gmap = googleMap;
        LatLng lastestCircleAddedPosition= new LatLng(DAO.TabCircleBuf.get(DAO.TabCircleBuf.size()-1).getRond().getRondLat(),
                DAO.TabCircleBuf.get(DAO.TabCircleBuf.size()-1).getRond().getRondLng());
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastestCircleAddedPosition,20));
        GetMapLocationPossible();
        gmap.setOnMarkerClickListener(this);
        gmap.setOnCircleClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MapFragment.CircleBufToMap(gmap);
        }
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

    public CircleInstance redrawColorCircle(GoogleMap googleMap,LatLng latLng, int color, String name,double radius,float markerColor ){
        int k3 = (int) (Math.random() * 10 * Math.random() * 100);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title(DAO.Whologin.get(0).getUsername()+name+k3).icon(BitmapDescriptorFactory.defaultMarker(markerColor));
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
        return new CircleInstance(DAO.Whologin.get(0).getUsername(), markerOptions.getTitle(), new Rond(circ));
    }

    public CircleInstance redrawSizeCircle(GoogleMap googleMap,LatLng latLng, int color, String name,double radius,float markerColor ){
        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title(name).icon(BitmapDescriptorFactory.defaultMarker(markerColor));
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
        return new CircleInstance(DAO.Whologin.get(0).getUsername(), markerOptions.getTitle(), new Rond(circ));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_perso:
                requireActivity().finish();
                break;
//            case R.id.help_button_for_edited:
//                popupWindowhelp=showPopup();
//                help.setVisibility(View.GONE);
//                close_help.setVisibility(View.VISIBLE);
//                break;
//            case R.id.close_help_button_for_edit:
//                popupWindowhelp.dismiss();
//                close_help.setVisibility(View.GONE);
//                help.setVisibility(View.VISIBLE);
//                break;
        }

    }

    @Override
    public void onCircleClick(@NonNull Circle circle) {
        if (DAO.TabCircleBuf.size() > 0) {
            for (int i = 0; i < DAO.TabCircleBuf.size(); i++) {
                if (circle.getCenter().latitude == DAO.TabCircleBuf.get(i).getRond().getRondLat() ||
                        circle.getCenter().longitude == DAO.TabCircleBuf.get(i).getRond().getRondLng()) {
                    circleInstance=DAO.TabCircleBuf.get(i);
                    break;
                }
            }
        }
        linearLayoutCompat.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        try{
            if(circleInstance!=null){
                for (int i = 0; i < DAO.TabCircleBuf.size(); i++) {
                    if (DAO.TabCircleBuf.get(i).getCirclename().equals(circleInstance.getCirclename())) {
                        DAO.TabCircleBuf.remove(i);
                        break;
                    }
                }
                gmap.clear();
                LatLng latLng=new LatLng(circleInstance.getRond().getRondLat(),circleInstance.getRond().getRondLng());
                if(circleInstance.getCirclename().contains("Green")){
                    DAO.addCircleBuf(redrawSizeCircle(gmap,latLng,circleInstance.getRond().getRondFillColor(),circleInstance.getCirclename(),Double.parseDouble(String.valueOf(progress)),BitmapDescriptorFactory.HUE_GREEN));
                }else if(circleInstance.getCirclename().contains("Orange")){
                    DAO.addCircleBuf(redrawSizeCircle(gmap,latLng,circleInstance.getRond().getRondFillColor(),circleInstance.getCirclename(),Double.parseDouble(String.valueOf(progress)),BitmapDescriptorFactory.HUE_ORANGE));
                }else {
                    DAO.addCircleBuf(redrawSizeCircle(gmap,latLng,circleInstance.getRond().getRondFillColor(),circleInstance.getCirclename(),Double.parseDouble(String.valueOf(progress)),BitmapDescriptorFactory.HUE_RED));
                }
                circleInstance=DAO.TabCircleBuf.get(DAO.TabCircleBuf.size()-1);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekBar.setProgress(Integer.valueOf(circleInstance.getRond().getRondRadius().intValue()));
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MapFragment.CircleBufToMap(gmap);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radio_green_edited:
                try{
                    if(circleInstance!=null){
                        for (int i = 0; i < DAO.TabCircleBuf.size(); i++) {
                            if (DAO.TabCircleBuf.get(i).getCirclename().equals(circleInstance.getCirclename())) {
                                DAO.TabCircleBuf.remove(i);
                                break;
                            }
                        }
                        gmap.clear();
                        LatLng latLng=new LatLng(circleInstance.getRond().getRondLat(),circleInstance.getRond().getRondLng());
                        DAO.addCircleBuf(redrawColorCircle(gmap,latLng,Color.rgb(34, 139, 34),"_Circle_Green_",circleInstance.getRond().getRondRadius(),BitmapDescriptorFactory.HUE_GREEN));
                        circleInstance=DAO.TabCircleBuf.get(DAO.TabCircleBuf.size()-1);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.radio_orange_edited:
                try{
                    if(circleInstance!=null){
                        for (int i = 0; i < DAO.TabCircleBuf.size(); i++) {
                            if (DAO.TabCircleBuf.get(i).getCirclename().equals(circleInstance.getCirclename())) {
                                DAO.TabCircleBuf.remove(i);
                                break;
                            }
                        }
                        gmap.clear();
                        LatLng latLng=new LatLng(circleInstance.getRond().getRondLat(),circleInstance.getRond().getRondLng());
                        DAO.addCircleBuf(redrawColorCircle(gmap,latLng,Color.rgb(255, 140, 0),"_Circle_Orange_",circleInstance.getRond().getRondRadius(),BitmapDescriptorFactory.HUE_ORANGE));
                        circleInstance=DAO.TabCircleBuf.get(DAO.TabCircleBuf.size()-1);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.radio_red_edited:
                try{
                    if(circleInstance!=null){
                        for (int i = 0; i < DAO.TabCircleBuf.size(); i++) {
                            if (DAO.TabCircleBuf.get(i).getCirclename().equals(circleInstance.getCirclename())) {
                                DAO.TabCircleBuf.remove(i);
                                break;
                            }
                        }
                        gmap.clear();
                        LatLng latLng=new LatLng(circleInstance.getRond().getRondLat(),circleInstance.getRond().getRondLng());
                        DAO.addCircleBuf(redrawColorCircle(gmap,latLng,Color.RED,"_Circle_Red_",circleInstance.getRond().getRondRadius(),BitmapDescriptorFactory.HUE_RED));
                        circleInstance=DAO.TabCircleBuf.get(DAO.TabCircleBuf.size()-1);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public PopupWindow showPopup(){
        PopupWindow popupWindow;
        LayoutInflater layoutInflater=(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView=layoutInflater.inflate(R.layout.popup_help_layout,null);
        popupWindow=new PopupWindow(customView, RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(relativeLayoutPopup, Gravity.TOP,0,0);
        return popupWindow;
    }
}