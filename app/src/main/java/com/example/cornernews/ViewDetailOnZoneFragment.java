package com.example.cornernews;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class ViewDetailOnZoneFragment extends Fragment implements View.OnClickListener {
    RelativeLayout relativeLayout;
    SwipeListener swipeListener;
    ImageSwitcher switcher;
    VideoView videoView;
    ArrayList<Uri> imageUris;
    ArrayList<String> imageUrl;
    int count;
    int currentIndex=0;
    ArrayList<Uri> videoUris;
    ArrayList<String> videoUrl;
    int countvideo;
    int currentIndexVideo=0;
    AppCompatImageButton button_image_view,button_video_view,download_selected_item;
    Character whosmedia= ' ';
    AppCompatTextView title,alert_start_date,alert_start_time;
    AppCompatTextView title_in_view,amount_image,amount_video;
    AppCompatTextView back_arrow;
    AppCompatImageButton log_out;
    CircleInstance circleInstancetoviewname;
    AppCompatTextView descrition_view;
    ProgressBar progressBarDownload;
    LinearLayoutCompat linear_select_media_type,linear_amout_media;
    HelperDB helperDB;

    @SuppressLint({"SetTextI18n", "NewApi"})
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_view_detail_on_zone, container, false);
        relativeLayout=view.findViewById(R.id.layout_swipe_view);
        swipeListener = new SwipeListener(relativeLayout);
        switcher=view.findViewById(R.id.image_switcher_for_view);
        videoView=view.findViewById(R.id.video_view_for_view);
        button_image_view=view.findViewById(R.id.button_image_view);
        button_video_view=view.findViewById(R.id.button_video_view);
        title=view.findViewById(R.id.title);
        alert_start_date=view.findViewById(R.id.alert_start_date_in_view);
        alert_start_time=view.findViewById(R.id.alert_start_time_in_view);
        amount_image=view.findViewById(R.id.amount_image_view);
        amount_video=view.findViewById(R.id.amount_video_view);
        descrition_view=view.findViewById(R.id.description_view);
        back_arrow=view.findViewById(R.id.back_perso);
        log_out=view.findViewById(R.id.button_logout);
        progressBarDownload=view.findViewById(R.id.progress_download);
        linear_select_media_type=view.findViewById(R.id.linear_media_view_view);
        linear_amout_media=view.findViewById(R.id.layout_amount_in_view);
        download_selected_item=view.findViewById(R.id.download_selected_item);
        download_selected_item.setOnClickListener(this);
        log_out.setVisibility(View.GONE);
        back_arrow.setOnClickListener(this);
        helperDB=new HelperDB(getContext());
        try {
            title.setText("\uD83D\uDE42  " +helperDB.GetUserName().toUpperCase());
        }catch (Exception e){
            e.printStackTrace();
        }
        title_in_view=view.findViewById(R.id.circle_title_in_view_detail);
        button_image_view.setOnClickListener(this);
        button_video_view.setOnClickListener(this);
        SetFactoryForImageSwitcher(switcher);
        circleInstancetoviewname=ContainerFrag.circleInstancebuf;
        title_in_view.setText(circleInstancetoviewname.getCirclename());
        DAO.updateListCircleListImageAndListVideo();
        DAO.addCircleToOutput(circleInstancetoviewname);
        if(DAO.sureOutput){
            descrition_view.setText(DAO.descriptionOut);
            alert_start_date.setText(DAO.DateZone);
            alert_start_time.setText(DAO.TimeZone);
//            System.out.println("*********************************** Description "+ DAO.descriptionOut);
//            System.out.println("*********************************** Date Zone "+ DAO.DateTimeZone);
            resultmedia();
            DAO.sureOutput=false;
        }
        amount_image.setText(imageUrl.size()+" image(s)");
        amount_video.setText(videoUrl.size()+" video(s)");

        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this.requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this.requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        DAO.updateListCircleListImageAndListVideo();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_image_view:
                try {
                    if(imageUrl.size()>0){
                        videoView.setVisibility(View.GONE);
                        switcher.setVisibility(View.VISIBLE);
                        switcher.setBackground(null);
                        whosmedia='i';
                    }else {
                        switcher.setBackgroundResource(R.drawable.ic_baseline_photo_24);
                        Toast.makeText(this.getContext(),"Empty Image",
                                Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case  R.id.button_video_view:
                try {
                    if(videoUrl.size()>0){
                        progressBarDownload.setVisibility(View.VISIBLE);
                        switcher.setVisibility(View.GONE);
                        videoView.setVisibility(View.VISIBLE);
                        videoView.setBackground(null);
                        MediaController mediaController= new MediaController(getContext());
                        mediaController.setAnchorView(videoView);
                        videoView.setMediaController(mediaController);
                        videoView.setVideoURI(Uri.parse(videoUrl.get(0)));
                        videoView.setOnPreparedListener(mp -> {
                            progressBarDownload.setVisibility(View.GONE);
                            mp.start();
                        });
                        whosmedia='v';
                    }else {
                        videoView.setBackgroundResource(R.drawable.ic_baseline_photo_24);
                        Toast.makeText(this.getContext(),"Empty Video",
                                Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.back_perso:
                requireActivity().finish();
                break;
            case R.id.download_selected_item:
                if(whosmedia=='i'){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            DownloadContent(imageUrl.get(currentIndex),"img_"+ LocalDateTime.now().toString()+".jpg");
                        }
                    }
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            DownloadContent(videoUrl.get(currentIndexVideo),"video_"+ LocalDateTime.now().toString()+".mp4");
                        }
                    }
                }
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void resultmedia(){
       getmediaforCircleClicked(circleInstancetoviewname.getCirclename());
        try {
            if(imageUrl.size()>0) {
                tomakeViewVisible();
                videoView.setVisibility(View.GONE);
                switcher.setBackground(null);
                switcher.setVisibility(View.VISIBLE);
                Glide.with(requireContext()).load(Uri.parse(imageUrl.get(0))).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                       progressBarDownload.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                       progressBarDownload.setVisibility(View.GONE);
                        return false;
                    }
                }).into((ImageView) switcher.getCurrentView());
                currentIndex = 0;
                whosmedia = 'i';
            }else if(videoUrl.size()>0) {
                tomakeViewVisible();
                switcher.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                videoView.setBackground(null);
                MediaController mediaController= new MediaController(getContext());
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(Uri.parse(videoUrl.get(0)));
                videoView.setOnPreparedListener(mp -> {
                    progressBarDownload.setVisibility(View.GONE);
                    mp.start();
                });
                currentIndexVideo=0;
                whosmedia = 'v';
                }
            else{
                progressBarDownload.setVisibility(View.GONE);
                Toast.makeText(this.getContext(),"Empty Image and Video",
                        Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void SetFactoryForImageSwitcher( ImageSwitcher switcher){
        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView=new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });
    }

    private class SwipeListener implements View.OnTouchListener {
        GestureDetector gestureDetector;

        SwipeListener(View view){
            int treshold=100;
            int velocity_threshold=100;
            GestureDetector.SimpleOnGestureListener listener =
                    new GestureDetector.SimpleOnGestureListener(){
                        @Override
                        public boolean onDown(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            float xDiff=e2.getX()-e1.getX();
                            float yDiff=e2.getY()-e1.getY();
                            try {
                                count=imageUrl.size();
                                countvideo=videoUrl.size();
                                if(Math.abs(xDiff) > Math.abs(yDiff)){
                                    if(Math.abs(xDiff) > treshold && Math.abs(velocityX) > velocity_threshold){
                                        if(whosmedia=='i'){
                                            if(xDiff > 0){
                                                switcher.setInAnimation(getActivity(),R.anim.from_left);
                                                switcher.setOutAnimation(getActivity(),R.anim.to_right);
                                                --currentIndex;
                                                if(currentIndex<0){
                                                    currentIndex=imageUrl.size()-1;
                                                }
                                                Glide.with(requireContext()).load(Uri.parse(imageUrl.get(currentIndex))).into((ImageView) switcher.getCurrentView());
                                            }else {
                                                switcher.setInAnimation(getActivity(),R.anim.from_right);
                                                switcher.setOutAnimation(getActivity(),R.anim.to_left);
                                                currentIndex++;
                                                if(currentIndex==count){
                                                    currentIndex=0;
                                                }
                                                progressBarDownload.setVisibility(View.VISIBLE);
                                                Glide.with(requireContext()).load(Uri.parse(imageUrl.get(currentIndex))).listener(new RequestListener<Drawable>() {
                                                    @Override
                                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                        progressBarDownload.setVisibility(View.GONE);
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                        progressBarDownload.setVisibility(View.GONE);
                                                        return false;
                                                    }
                                                }).into((ImageView) switcher.getCurrentView());
                                            }
                                            return  true;
                                        }else if(whosmedia=='v') {
                                            if(xDiff > 0){
                                                --currentIndexVideo;
                                                if(currentIndexVideo<0){
                                                    currentIndexVideo=videoUrl.size()-1;
                                                }
                                                progressBarDownload.setVisibility(View.VISIBLE);
                                                MediaController mediaController= new MediaController(getContext());
                                                mediaController.setAnchorView(videoView);
                                                videoView.setMediaController(mediaController);
                                                videoView.setVideoURI(Uri.parse(videoUrl.get(currentIndexVideo)));
                                                videoView.setOnPreparedListener(mp -> {
                                                    progressBarDownload.setVisibility(View.GONE);
                                                    mp.start();
                                                });
                                            }else {
                                                currentIndexVideo++;
                                                if(currentIndexVideo==countvideo){
                                                    currentIndexVideo=0;
                                                }
                                                progressBarDownload.setVisibility(View.VISIBLE);
                                                MediaController mediaController= new MediaController(getContext());
                                                mediaController.setAnchorView(videoView);
                                                videoView.setMediaController(mediaController);
                                                videoView.setVideoURI(Uri.parse(videoUrl.get(currentIndexVideo)));
                                                videoView.setOnPreparedListener(mp -> {
                                                    progressBarDownload.setVisibility(View.GONE);
                                                    mp.start();
                                                });
                                            }
                                            return  true;
                                        }
                                    }
                                }else {
                                    if(Math.abs(yDiff) > treshold && Math.abs(velocityY) > velocity_threshold){
                                        if(whosmedia=='i'){
                                            if(yDiff > 0){
//                                                textView.setText("Swipe Down");
                                            }
                                            else {
//                                            textView.setText("Swipe Up");
                                            }
                                            return true;
                                        }else if(whosmedia=='v'){
                                            if(yDiff > 0){
//                                                textView.setText("Swipe Down");
                                            }
                                            else {
//                                            textView.setText("Swipe Up");
                                            }
                                            return true;
                                        }
                                    }
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            return false;
                        }
                    };
            gestureDetector = new GestureDetector(listener);
            view.setOnTouchListener(this);
        }
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("CheckResult")
    public void getmediaforCircleClicked(String namecircle){
        DAO.updateListCircleListImageAndListVideo();
       if(DAO.tableDesTrioCircleImageLinkListVideoLinkList.size()>0){
            for(int i=0;i<DAO.tableDesTrioCircleImageLinkListVideoLinkList.size();i++){
                if(DAO.tableDesTrioCircleImageLinkListVideoLinkList.get(i).get(0).equals(namecircle)){
                    if(DAO.tableDesTrioCircleImageLinkListVideoLinkList.get(i).get(1)!=null){
                        imageUris=(ArrayList<Uri>) DAO.tableDesTrioCircleImageLinkListVideoLinkList.get(i).get(1);
                        imageUrl=(ArrayList<String>) DAO.tableDesTrioCircleImageLinkListVideoLinkList.get(i).get(1);
                    }
                    if(DAO.tableDesTrioCircleImageLinkListVideoLinkList.get(i).get(2)!=null){
                        videoUris=(ArrayList<Uri>) DAO.tableDesTrioCircleImageLinkListVideoLinkList.get(i).get(2);
                        videoUrl=(ArrayList<String>) DAO.tableDesTrioCircleImageLinkListVideoLinkList.get(i).get(2);
                    }
                   break;
                }
            }
       }
    }

    public void tomakeViewVisible(){
        relativeLayout.setVisibility(View.VISIBLE);
        linear_select_media_type.setVisibility(View.VISIBLE);
        linear_amout_media.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void DownloadContent(String url, String outputFilename){
        DownloadManager.Request request= new DownloadManager.Request(Uri.parse(url));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,outputFilename);
        DownloadManager manager=(DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
}