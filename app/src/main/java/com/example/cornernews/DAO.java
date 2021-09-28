package com.example.cornernews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;


public class DAO  {
    static ArrayList<Login> Whologin= new ArrayList<>();

    static ArrayList<CircleInstance> TabCircle =new ArrayList<>();
    static ArrayList<CircleInstance> TabCircleForUserConnected =new ArrayList<>();
    static String descriptionOut;
    static String TimeZone;
    static String DateZone;
    static CircleInstance circleInstanceOut;
    static boolean sureOutput=false;
    static int SizeTabCircleBefore;

    static ArrayList<CircleInstance> TabCircleBuf =new ArrayList<>();

    static public void addCircleBuf(CircleInstance circle){
        TabCircleBuf.add(circle);
    }

    static ArrayList<Login> loginTab= new ArrayList<>();
    static public void addLogin(Login login) {
        loginTab.add(login);
    }

    static ArrayList<ArrayList<Object>> listEventZoneFromDb= new ArrayList<>();
    static ArrayList<ArrayList<Object>> loginEventZoneFromDb= new ArrayList<>();
    static ArrayList<ArrayList<Object>> tableDesTrioCircleImageLinkListVideoLinkList=new ArrayList<>();//circle name -----> listimage, listvideo
    static ArrayList<Object> allImageLinkFromDb= new ArrayList<>();
    static ArrayList<Object> allVideoLinkFromDb= new ArrayList<>();

    static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("zone");
    static DatabaseReference logDatabase = FirebaseDatabase.getInstance().getReference("login");
    static DatabaseReference mediaDatabase = FirebaseDatabase.getInstance().getReference("media");
    static DatabaseReference imageMediaDatabase = FirebaseDatabase.getInstance().getReference("media").child("imageBranch");
    static DatabaseReference videoMediaDatabase = FirebaseDatabase.getInstance().getReference("media").child("videoBranch");
    static StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("ImageFolder");
    static StorageReference VideoFolder = FirebaseStorage.getInstance().getReference().child("VideoFolder");
    final static FirebaseAuth UserAuth = FirebaseAuth.getInstance();


    static public void updateDaoZoneFromDatabase(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("Recycle")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                listEventZoneFromDb.clear();
                for (DataSnapshot zoneContainer : dataSnapshot.getChildren()) {
                    Zone zone = zoneContainer.getValue(Zone.class);
                    assert zone != null;
                    listEventZoneFromDb.add(zone.getEventDetailFromThisZone());
                }

                SizeTabCircleBefore=TabCircle.size();
                if(TabCircle.size()>0) {
                    TabCircle.clear();
                }
                for (ArrayList<Object> zone : listEventZoneFromDb) { //add all circleinstanse from GroupallTabDb
                    TabCircle.add((CircleInstance)zone.get(1));
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("**************** Cancel Inserting Data");
            }
        });
    }


    static public void updateDaoMediaFromDatabase(){
        imageMediaDatabase.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                 List<Object> listImageForCircle = new ArrayList<>();
                 for (DataSnapshot pushedNodeContainer : dataSnapshot.getChildren()) {
                         listImageForCircle.add((MediaLink) pushedNodeContainer.getValue(MediaLink.class));
                     allImageLinkFromDb=(ArrayList<Object>) listImageForCircle;
                 }
                 updateListCircleListImageAndListVideo();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 System.out.println("**************** Cancel Inserting Data");
             }
        });

        videoMediaDatabase.addValueEventListener(new ValueEventListener() {
             public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                 List<Object> listVideoForCircle = new ArrayList<>();
                 for (DataSnapshot pushedNodeContainer : dataSnapshot.getChildren()) {
                         listVideoForCircle.add((MediaLink) pushedNodeContainer.getValue(MediaLink.class));
                 }
                 allVideoLinkFromDb= (ArrayList<Object>) listVideoForCircle;
                 updateListCircleListImageAndListVideo();
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 System.out.println("**************** Cancel Inserting Data");
             }
        });
    }

//    static String key;
    static ArrayList<String> listImageKeyToDelete=new ArrayList<>();
    static ArrayList<String> listVideoKeyToDelete=new ArrayList<>();

    static public void deleteMediaViaCircleName(String cirName){
        imageMediaDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                List<Object> listImageForCircle = new ArrayList<>();
                for (DataSnapshot pushedNodeContainer : dataSnapshot.getChildren()) {
                   MediaLink mediaTamp= pushedNodeContainer.getValue(MediaLink.class);
                    assert mediaTamp != null;
                    if(mediaTamp.getMediaCircleName().equals(cirName)){
                       listImageForCircle.add(dataSnapshot.getKey());
                   }
                }
                for(int i=0;i<listImageForCircle.size();i++) {
                    FirebaseDatabase.getInstance().getReference("media").child("imageBranch").child(listImageKeyToDelete.get(i)).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("**************** Cancel Inserting Data");
            }
        });

        videoMediaDatabase.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                List<Object> listVideoForCircle = new ArrayList<>();
                for (DataSnapshot pushedNodeContainer : dataSnapshot.getChildren()) {
                    MediaLink mediaTamp= pushedNodeContainer.getValue(MediaLink.class);
                    assert mediaTamp != null;
                    if(mediaTamp.getMediaCircleName().equals(cirName)){
                        listVideoForCircle.add(dataSnapshot.getKey());
                    }
                }
                for(int i=0;i<listVideoForCircle.size();i++) {
                    videoMediaDatabase.child(listVideoKeyToDelete.get(i)).removeValue();
                }

//                allVideoLinkFromDb= (ArrayList<Object>) listVideoForCircle;
//                updateListCircleListImageAndListVideo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("**************** Cancel Inserting Data");
            }
        });
    }
    public static void updateListCircleListImageAndListVideo (){
        tableDesTrioCircleImageLinkListVideoLinkList.clear();
        for (CircleInstance circleName:TabCircle) {
            ArrayList<Object> trioCircleImageListVideoList=new ArrayList<>();
            trioCircleImageListVideoList.add(circleName.getCirclename());
            trioCircleImageListVideoList.add( getAListLinkMediaFromCircleName(circleName.getCirclename(),allImageLinkFromDb));
            trioCircleImageListVideoList.add( getAListLinkMediaFromCircleName(circleName.getCirclename(),allVideoLinkFromDb));
            tableDesTrioCircleImageLinkListVideoLinkList.add(trioCircleImageListVideoList);
        }
    }

    public static ArrayList<String> getAListLinkMediaFromCircleName(String circleName, ArrayList<Object> mediaLink){
        ArrayList<String> listLinkToReturn=new ArrayList<>();

        for(int i=0;i<mediaLink.size();i++){
            MediaLink mediaLinkTamp=(MediaLink) mediaLink.get(i);
            if(mediaLinkTamp.getMediaCircleName().equals(circleName)){
                listLinkToReturn.add(mediaLinkTamp.getLinkToMedia());
            }
        }
        return listLinkToReturn;
    }

    static public void updateDaoLoginFromDatabase(){
        logDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loginEventZoneFromDb.clear();
                for (DataSnapshot loginContainer : dataSnapshot.getChildren()) {
                    Login login=loginContainer.getValue(Login.class);
                    assert login != null;
                    loginEventZoneFromDb.add(login.getEventDetailFromThisLogin());
                }
                updateTabloginFromDb ();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("**************** Cancel Inserting Data");
            }
        });
    }

//    static public void updateTabCircleFromDb (){
//        if(TabCircle.size()>0) {
//            TabCircle.clear();
//        }
//        for (ArrayList<Object> zone : listEventZoneFromDb) { //add all circleinstanse from GroupallTabDb
//            TabCircle.add((CircleInstance)zone.get(1));
//        }
//    }

    static public void updateTabloginFromDb (){
        if(loginTab.size()>0) {
            loginTab.clear();
        }
        for (int i=0;i<loginEventZoneFromDb.size();i++){
            String em=loginEventZoneFromDb.get(i).get(0).toString();
            String us=loginEventZoneFromDb.get(i).get(1).toString();
            addLogin(new Login(em,us));
        }
    }

    public static void addCircleToOutput(CircleInstance instance){
        for(int i=0; i<listEventZoneFromDb.size();i++){
            if(instance!=null) {
                circleInstanceOut = (CircleInstance) listEventZoneFromDb.get(i).get(1);
                if (instance.getCirclename().equals(circleInstanceOut.getCirclename())) {
                    descriptionOut = (String) listEventZoneFromDb.get(i).get(0);
                        DateZone=(String) listEventZoneFromDb.get(i).get(2);
                        TimeZone=(String) listEventZoneFromDb.get(i).get(3);
                    sureOutput = true;
                }
            }
        }
    }

    public static void MyWorkerLauncher(){
        WorkRequest WorkCheckUpdate = new OneTimeWorkRequest.Builder(WorkerToEventDataChange.class).build();
        WorkManager.getInstance().enqueue(WorkCheckUpdate);
    }

//    public static String convertPassMd5(String pass) {
//        String password = null;
//        MessageDigest mdEnc;
//        try {
//            mdEnc = MessageDigest.getInstance("MD5");
//            mdEnc.update(pass.getBytes(), 0, pass.length());
//            pass = new BigInteger(1, mdEnc.digest()).toString(16);
//            while (pass.length() < 32) {
//                pass = "0" + pass;
//            }
//            password = pass;
//        } catch (NoSuchAlgorithmException e1) {
//            e1.printStackTrace();
//        }
//        return password;
//    }

    public static boolean isConnected(AppCompatActivity Activity){
        ConnectivityManager connectivityManager= (ConnectivityManager) Activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected());
    }

//    public void showhidePassword(String passwrod) {
//        if(showHideBtn.text.toString().equals("Show")){
//            pwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
//            showHideBtn.text = "Hide"
//        } else{
//            pwd.transformationMethod = PasswordTransformationMethod.getInstance()
//            showHideBtn.text = "Show"
//        }
//    }
}
