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
        ToSortAlert("Red",redList);
        ToSortAlert("Orange",orangeList);
        ToSortAlert("Green",greenList);
        return view;
    }

    public void ToSortAlert(String colorName,ListView listView) {
//        gran tab ki pral afiche a
        ArrayList<AlertInfo> AlertInfoTab = new ArrayList<>();
//        tab kap gen AlertInstance menm koule yo
        ArrayList<CircleInstance> arrayList = new ArrayList<>();
//        tab ki pral kenbe lyen imaj ak videyo yo
        ArrayList<Uri> imageUris = null;
        ArrayList<Uri> videoUris = null;
//        varyab ki pral kenbe enfomasyon AlertInstance nan
        String AlertInstanceName = "";
        String DateAlertInstance = "";
        String TimeAlertInstance = "";

        if (DAO.TabCircle.size() > 0) {
            for (int i = 0; i < DAO.TabCircle.size(); i++) {
                if (DAO.TabCircle.get(i).getAlertName().contains(colorName)) {
                    arrayList.add(DAO.TabCircle.get(i));
                }
            }
        }
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                for (int j = 0; j < DAO.listEventAlertInstanceFromDb.size(); j++) {
                    CircleInstance circleInstanceBuf = (CircleInstance) DAO.listEventAlertInstanceFromDb.get(j).get(1);
                    if (arrayList.get(i).getAlertName().equals(circleInstanceBuf.getAlertName())) {
                        AlertInstanceName = arrayList.get(i).getAlertName();
                        DateAlertInstance = (String) DAO.listEventAlertInstanceFromDb.get(j).get(2);
                        TimeAlertInstance = (String) DAO.listEventAlertInstanceFromDb.get(j).get(3);
                    }
                }
                if (DAO.tableDesTrioCircleImageLinkListVideoLinkList.size() > 0) {
                    for (int m = 0; m < DAO.tableDesTrioCircleImageLinkListVideoLinkList.size(); m++) {
                        String nameCircleToGetMedia = (String) DAO.tableDesTrioCircleImageLinkListVideoLinkList.get(m).get(0);
                        if (arrayList.get(i).getAlertName().equals(nameCircleToGetMedia)) {
                            imageUris = (ArrayList<Uri>) DAO.tableDesTrioCircleImageLinkListVideoLinkList.get(m).get(1);
                            videoUris = (ArrayList<Uri>) DAO.tableDesTrioCircleImageLinkListVideoLinkList.get(m).get(2);
                        }
                    }
                }
                if(colorName.equals("Red")){
                    AlertInfoTab.add(new AlertInfo(R.drawable.ic_baseline_circle_red,AlertInstanceName,DateAlertInstance+" "+ TimeAlertInstance,
                            "Image: "+imageUris.size()+" Video: "+videoUris.size()));
                    title_red.setText(AlertInfoTab.size()+" Red Alert(s)");
                }else if(colorName.equals("Orange")){
                    AlertInfoTab.add(new AlertInfo(R.drawable.ic_baseline_circle_orange,AlertInstanceName,DateAlertInstance+" "+ TimeAlertInstance,
                            "Image: "+imageUris.size()+" Video: "+videoUris.size()));
                    title_orange.setText(AlertInfoTab.size()+" Orange Alert(s)");
                }else {
                    AlertInfoTab.add(new AlertInfo(R.drawable.ic_baseline_circle_green,AlertInstanceName,DateAlertInstance+" "+ TimeAlertInstance,
                            "Image: "+imageUris.size()+" Video: "+videoUris.size()));
                    title_green.setText(AlertInfoTab.size()+" Green Alert(s)");
                }
            }
        }
        AlertAdapter alertAdapter = new AlertAdapter(requireContext(),R.layout.list_row,AlertInfoTab);
        listView.setAdapter(alertAdapter);
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