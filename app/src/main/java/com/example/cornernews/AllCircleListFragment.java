package com.example.cornernews;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AllCircleListFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    ListView redList,orangeList,greenList;
    AppCompatTextView back_arrow,title_red,title_orange,title_green;
    AppCompatImageButton log_out;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_all_circle_list, container, false);
        redList=view.findViewById(R.id.red_circle_list);
        orangeList=view.findViewById(R.id.orange_circle_list);
        greenList=view.findViewById(R.id.green_circle_list);
        back_arrow=view.findViewById(R.id.back_perso);
        log_out=view.findViewById(R.id.button_logout);
        title_red=view.findViewById(R.id.red_circle_tile);
        title_orange=view.findViewById(R.id.orange_circle_tile);
        title_green=view.findViewById(R.id.green_circle_tile);
        log_out.setVisibility(View.GONE);
        back_arrow.setOnClickListener(this);
//        TitleToGetInVisible();
        ToSortZones("Red", redList);
        ToSortZones("Green", greenList);
        ToSortZones("Orange", orangeList);
        return view;
    }

    public void ToSortZones(String colorName, ListView listView){
        ArrayList<ArrayList<String>> ZoneInfo=new ArrayList<>();
        ArrayList<CircleInstance> arrayList=new ArrayList<>();
        ArrayList<Uri> imageUris;
        ArrayList<Uri> videoUris;
        String ZoneName="";
        String DateZone="";
        String TimeZone="";
        if(DAO.TabCircle.size()>0){
            for(int i=0;i<DAO.TabCircle.size();i++){
                if(DAO.TabCircle.get(i).getCirclename().contains(colorName)){
                    arrayList.add(DAO.TabCircle.get(i));
                }
            }
        }
        if(arrayList.size()>0){
            for(int i=0;i<arrayList.size();i++){
                ArrayList<String> stringArrayList=new ArrayList<>();
                for(int j=0; j<DAO.listEventZoneFromDb.size();j++){
                    CircleInstance circleInstanceBuf = (CircleInstance) DAO.listEventZoneFromDb.get(j).get(1);
                    if(arrayList.get(i).getCirclename().equals(circleInstanceBuf.getCirclename())) {
                        ZoneName=arrayList.get(i).getCirclename();
                        DateZone=(String) DAO.listEventZoneFromDb.get(j).get(2);
                        TimeZone=(String) DAO.listEventZoneFromDb.get(j).get(3);
                        stringArrayList.add(ZoneName);
                        stringArrayList.add(DateZone);
                        stringArrayList.add(TimeZone);
                        ZoneInfo.add(stringArrayList);
                    }
                }
                if(DAO.tableDesTrioCircleImageLinkListVideoLinkList.size()>0){
                    for(int m=0;m<DAO.tableDesTrioCircleImageLinkListVideoLinkList.size();m++){
                        String nameCircleToGetMedia= (String) DAO.tableDesTrioCircleImageLinkListVideoLinkList.get(m).get(0);
                        if(arrayList.get(i).getCirclename().equals(nameCircleToGetMedia)){
                            imageUris=(ArrayList<Uri>) DAO.tableDesTrioCircleImageLinkListVideoLinkList.get(m).get(1);
                            videoUris=(ArrayList<Uri>) DAO.tableDesTrioCircleImageLinkListVideoLinkList.get(m).get(2);
                            System.out.println("**************************************************************** "+nameCircleToGetMedia+" "+imageUris.size()+" "+ videoUris.size());
                            stringArrayList.add(String.valueOf(imageUris.size()));
                            stringArrayList.add(String.valueOf(videoUris.size()));
                        }
                    }
                }
            }
        }
        ArrayAdapter adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, ZoneInfo);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

//    public void TitleToGetInVisible(){
//        if(DAO.TabCircle.size()>0){
//            for(int i=0;i<DAO.TabCircle.size();i++){
//                if(!DAO.TabCircle.get(i).getCirclename().contains("Red")){
//                    title_red.setVisibility(View.GONE);
//                }else {
//                    title_red.setVisibility(View.VISIBLE);
//                }
//
//                if(!DAO.TabCircle.get(i).getCirclename().contains("Orange")) {
//                    title_orange.setVisibility(View.GONE);
//                }else {
//                    title_orange.setVisibility(View.VISIBLE);
//                }
//
//                if(!DAO.TabCircle.get(i).getCirclename().contains("Green")) {
//                    title_green.setVisibility(View.GONE);
//                }else {
//                    title_green.setVisibility(View.VISIBLE);
//                }
//            }
//        }
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_perso:
                requireActivity().finish();
                break;
        }
    }
}