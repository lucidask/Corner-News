package com.example.cornernews;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class ContainerFrag extends AppCompatActivity {
    public static CircleInstance circleInstancebuf;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_frag);
        WhoCallToSetFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("NonConstantResourceId")
    public void WhoCallToSetFragment(){
        Intent intent =getIntent();
        Bundle extras=intent.getExtras();
        int ButtonWhoCall=extras.getInt("who_call",0);
        Fragment currentFragment = getSupportFragmentManager().
                findFragmentById(R.id.frame_layout);
        switch (ButtonWhoCall){
            case R.id.button_login:
            case R.id.bt_sign_with_google:
                if(currentFragment==null){
                    MapFragment fragment=new MapFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout,fragment)
                            .commit();
                }
                break;
            case R.id.bt_delete:
                if(currentFragment==null){
                    ListCircleForDeleteFragment fragment= new ListCircleForDeleteFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout,fragment)
                            .commit();
                }
                break;
            case R.id.create_account:
                if(currentFragment==null){
                    CreateAccountFragment fragment= new CreateAccountFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout,fragment)
                            .commit();
                }
                break;
            case R.id.bt_edit:
                if(currentFragment==null){
                    ListCircleForEditFragment fragment= new ListCircleForEditFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout,fragment)
                            .commit();
                }
                break;

            case R.id.bt_list_zone:
                if(currentFragment==null){
                    AllCircleListFragment fragment= new AllCircleListFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout,fragment)
                            .commit();
                }
                break;
            case R.id.text_view_forgot_password:
                if(currentFragment==null){
                    ResetPasswordFragment fragment= new ResetPasswordFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout,fragment)
                            .commit();
                }
                break;
            default:
                if(extras.getString("CIRCLE_WHO_CALL")!=null) {
                    String CircleWhoIAm = extras.getString("CIRCLE_WHO_CALL");
                    if (DAO.TabCircle.size() > 0 || DAO.TabCircleBuf.size() > 0  ) {
                        boolean eye=false;
                        for (int i = 0; i < DAO.TabCircle.size(); i++) {
                            if (DAO.TabCircle.get(i).getRond().getcircleOptions().getCenter().toString().equals(CircleWhoIAm)) {
                                circleInstancebuf=DAO.TabCircle.get(i);
                                DAO.updateListCircleListImageAndListVideo();
                                if (currentFragment == null) {
                                    ViewDetailOnZoneFragment fragment = new ViewDetailOnZoneFragment();
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.frame_layout, fragment)
                                            .commit();
                                }
                                eye=true;
                                break;
                            } else {
                                eye=false;
                            }
                        }
                        if (!eye){
                            for(int i=0;i<DAO.TabCircleBuf.size();i++){
                                if(DAO.TabCircleBuf.get(i).getRond().getcircleOptions().getCenter().toString().equals(CircleWhoIAm)){
                                    circleInstancebuf=DAO.TabCircleBuf.get(i);
                                }
                            }
                            if (currentFragment == null) {
                                AddDetailForZoneFragment fragment = new AddDetailForZoneFragment();
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.frame_layout, fragment)
                                        .commit();
                            }
                        }
                    }
                }
        }
    }
}