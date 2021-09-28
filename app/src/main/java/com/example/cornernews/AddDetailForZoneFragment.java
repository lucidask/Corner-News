package com.example.cornernews;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewSwitcher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import static android.app.Activity.RESULT_OK;
import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.StorageReference;


public class AddDetailForZoneFragment extends Fragment implements View.OnClickListener {
    private static final int REQUEST_CAPTURE = 1;
    private  static  final int PICK_IMAGES_CODE=0;
    private  static  final int PICK_VIDEOS_CODE=2;
    RelativeLayout relativeLayout;
    SwipeListener swipeListener;
    ImageSwitcher switcher;
    VideoView videoView;
    AppCompatImageButton open_gallery,open_camera,bt_image,bt_video,log_out;
    AppCompatButton add_details;
    ArrayList<Uri> imageUris =new ArrayList<>();
    int count=imageUris.size();
    int currentIndex=0;
    ArrayList<Uri> videoUris =new ArrayList<>();
    int countvideo=videoUris.size();
    int currentIndexVideo=0;
    AppCompatImageButton selected_image;
    AppCompatImageButton selected_video;
    Character whosmedia= ' ';
    AppCompatTextView title,back_arrow,NowDate;
    EditText editTextDescrption;
    AppCompatTextView title_in_add,image_amount,video_amount;
    CircleInstance circleInstancetoviewname;
    AlertDialog dialog;
    AlertDialog.Builder builderfortypemedia;
    private String currentPhotoPath;
    CircleInstance cir;
    AppCompatTextView textProgress;
    ProgressBar barProgress;
    LinearLayoutCompat linearProgress,linearTake,linearView,linear_amount;
    HelperDB helperDB;
    DigitalClock clock;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_detail_for_zone, container, false);
        relativeLayout=view.findViewById(R.id.layout_swipe);
        swipeListener = new SwipeListener(relativeLayout);
        switcher=view.findViewById(R.id.image_switcher);
        videoView=view.findViewById(R.id.video_view);
        open_camera=view.findViewById(R.id.open_camera);
        open_gallery=view.findViewById(R.id.open_gallery);
        selected_image=view.findViewById(R.id.selected_image_view);
        selected_video=view.findViewById(R.id.selected_video_view);
        editTextDescrption=view.findViewById(R.id.edit_text_description);
        title_in_add=view.findViewById(R.id.circle_title_in_add_detail);
        add_details=view.findViewById(R.id.add_details_zone);
        title=view.findViewById(R.id.title);
        back_arrow=view.findViewById(R.id.back_perso);
        image_amount=view.findViewById(R.id.amount_image_added);
        video_amount=view.findViewById(R.id.amount_video_added);
        NowDate=view.findViewById(R.id.alert_start_date);
        clock=view.findViewById(R.id.alert_start_time);
        log_out=view.findViewById(R.id.button_logout);
        log_out.setVisibility(View.GONE);
        textProgress=view.findViewById(R.id.textProgress);
        barProgress=view.findViewById(R.id.progressUpload);
        linearProgress=view.findViewById(R.id.linearProgressUpload);
        linearTake=view.findViewById(R.id.linear_media_take);
        linearView=view.findViewById(R.id.linear_media_view);
        linear_amount=view.findViewById(R.id.linear_amount);
        back_arrow.setOnClickListener(this);
        helperDB=new HelperDB(getContext());
        try {
            title.setText("\uD83D\uDE42  " +helperDB.GetUserName().toUpperCase());
        }catch (Exception e){
            e.printStackTrace();
        }
        selected_image.setOnClickListener(this);
        selected_video.setOnClickListener(this);
        open_camera.setOnClickListener(this);
        open_gallery.setOnClickListener(this);
        add_details.setOnClickListener(this);
        circleInstancetoviewname=ContainerFrag.circleInstancebuf;
        title_in_add.setText(circleInstancetoviewname.getCirclename());
        NowDate.setText(NowDate().toString());
        getCameraPermission();
        SetFactoryForImageSwitcher(switcher);
        DAO.updateListCircleListImageAndListVideo();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        DAO.updateListCircleListImageAndListVideo();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_details_zone:
                Intent IntentToGetCircleCenter= requireActivity().getIntent();
                String CircleCenter=IntentToGetCircleCenter.getStringExtra("CIRCLE_WHO_CALL");
                for (int i=0;i<DAO.TabCircleBuf.size();i++){
                    if (DAO.TabCircleBuf.get(i).getUsername().equals(helperDB.GetUserName()) &&
                            Objects.requireNonNull(DAO.TabCircleBuf.get(i).getRond().getcircleOptions().getCenter()).toString().equals(CircleCenter)) { // if UserCircleName match to username
                        cir=DAO.TabCircleBuf.get(i);
                        Zone zone = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            zone = new Zone(cir,editTextDescrption.getText().toString(),NowDate().toString(),clock.getText().toString());
                        }
                        DAO.mDatabase.child(cir.getCirclename()).setValue(zone).addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(),"Successfully added circled area",
                                        Toast.LENGTH_SHORT).show();
                                if(imageUris.size()>0 || videoUris.size()>0){
                                    SaveMedia(imageUris,videoUris);
                                }
                                TodoAfterUpload();
                            }
                        });
                        DAO.TabCircleBuf.remove(i);
                    }
                }
                DAO.updateListCircleListImageAndListVideo();
                break;
            case R.id.open_camera:
                getCameraPermission();
                dispatchTakeMediaIntent();
                break;
            case R.id.open_gallery:
                dialog=SelectTypeMedia();
                break;
            case R.id.selected_image_view:
                try {
                    if(imageUris.size()>0){
                        videoView.setVisibility(View.GONE);
                        switcher.setVisibility(View.VISIBLE);
                        whosmedia='i';
                    }else {
                        switcher.setBackgroundResource(R.drawable.ic_baseline_photo_24);
                        Toast.makeText(getContext(),"Empty Image",
                                Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case  R.id.selected_video_view:
                try {
                    if(videoUris.size()>0){
                        switcher.setVisibility(View.GONE);
                        videoView.setVisibility(View.VISIBLE);
                        videoView.start();
                        whosmedia='v';
                    }else {
                        videoView.setBackgroundResource(R.drawable.ic_baseline_photo_24);
                        Toast.makeText(getContext(),"Empty Video",
                                Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.back_perso:
                requireActivity().finish();
                break;
            case R.id.bt_image:
                dialog.cancel();
                DispatchGetPictureOnGallery();
                break;
            case R.id.bt_video:
                dialog.cancel();
                DispatchGetVideoOnGallery();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate NowDate(){
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//        Date date = new Date(System.currentTimeMillis());
//        System.out.println(formatter.format(date));

        LocalDate date = LocalDate.now();
//        return formatter.format(date);
//        System.out.println("*********************************** now "+ date);
        return date;
    }

    private void dispatchTakeMediaIntent() {
        getCameraPermission();
        String filename="photo_"+ (int) (Math.random() * 100 +Math.random()*1000);
        File storageDirectory= requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            File imageFile=File.createTempFile(filename,".jpg",storageDirectory);
            currentPhotoPath=imageFile.getAbsolutePath();
            Uri UriImage=FileProvider.getUriForFile(requireContext(),"com.example.cornernews.fileprovider",imageFile);
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,UriImage);
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,30);
            Intent chooserIntent =Intent.createChooser(takePictureIntent,"");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,new Intent[]{takeVideoIntent});
            if(takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null
                    || takeVideoIntent.resolveActivity(requireActivity().getPackageManager()) != null){
                startActivityForResult(chooserIntent,1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void DispatchGetPictureOnGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent," "),PICK_IMAGES_CODE);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void DispatchGetVideoOnGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent," "),PICK_VIDEOS_CODE);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK){
            if(data!=null){
                tomakeViewVisible();
                switcher.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                videoView.setBackground(null);
                Uri VideoUri=data.getData();
//                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
//                new CompressVideo().execute("false",videouri.toString(),file.getPath());
                MediaController mediaController= new MediaController(getContext());
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoUris.add(VideoUri);
                videoView.setVideoURI(videoUris.get(0));
                videoView.start();
                currentIndexVideo=0;
                whosmedia='v';
            }
            else {
                tomakeViewVisible();
                videoView.setVisibility(View.GONE);
                switcher.setVisibility(View.VISIBLE);
                switcher.setBackground(null);
                imageUris.add(ConvertBimapToUri(rotatedImage(reducedPictureSize(currentPhotoPath, switcher),currentPhotoPath)));
                switcher.setImageURI(imageUris.get(0));
                currentIndex = 0;
                whosmedia='i';
            }
        }

        if(requestCode==PICK_IMAGES_CODE && resultCode == RESULT_OK) {
            if(data.getClipData() != null){
                for(int i=0;i<data.getClipData().getItemCount();i++){
                    Uri ImageUri=data.getClipData().getItemAt(i).getUri();
                    imageUris.add(ImageUri);
                }
                videoView.setVisibility(View.GONE);
            }
            else {
                Uri ImageUri = data.getData();
//                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
//                new CompressImage().execute("false",imageuri.toString(),file.getPath());
                videoView.setVisibility(View.GONE);
                imageUris.add(ImageUri);

            }
            tomakeViewVisible();
            switcher.setBackground(null);
            switcher.setVisibility(View.VISIBLE);
            switcher.setImageURI(imageUris.get(0));
            currentIndex = 0;
            whosmedia='i';
        }else
        if(requestCode==PICK_VIDEOS_CODE && resultCode == RESULT_OK) {
            if(data.getClipData() != null){
                for(int i=0;i<data.getClipData().getItemCount();i++){
                    Uri VideoUri=data.getClipData().getItemAt(i).getUri();
                    videoUris.add(VideoUri);
                }
            }
            else {
                Uri VideoUri = data.getData();
                videoUris.add(VideoUri);
            }
            tomakeViewVisible();
            videoView.setBackground(null);
            switcher.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            MediaController mediaController= new MediaController(getContext());
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(videoUris.get(0));
            videoView.start();
            currentIndexVideo=0;
            whosmedia='v';
        }
        image_amount.setText(imageUris.size()+" image(s)");
        video_amount.setText(videoUris.size()+" video(s)");
    }

    public void getCameraPermission(){
        if(ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this.requireActivity(), new String[]{Manifest.permission.CAMERA}, 100);
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

                        @SuppressLint("SetTextI18n")
                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            float xDiff=e2.getX()-e1.getX();
                            float yDiff=e2.getY()-e1.getY();
                            try {
                                count=imageUris.size();
                                countvideo=videoUris.size();
                                if(Math.abs(xDiff) > Math.abs(yDiff)){
                                    if(Math.abs(xDiff) > treshold && Math.abs(velocityX) > velocity_threshold){
                                        if(whosmedia=='i'){
                                            if(xDiff > 0){
                                                switcher.setInAnimation(getActivity(),R.anim.from_left);
                                                switcher.setOutAnimation(getActivity(),R.anim.to_right);
                                                --currentIndex;
                                                if(currentIndex<0){
                                                    currentIndex=imageUris.size()-1;
                                                }
                                                switcher.setImageURI(imageUris.get(currentIndex));
                                            }else {
                                                switcher.setOutAnimation(getActivity(),R.anim.to_left);
                                                currentIndex++;
                                                if(currentIndex==count){
                                                    currentIndex=0;
                                                }
                                                switcher.setImageURI(imageUris.get(currentIndex));
                                            }
                                            return  true;
                                        }else if(whosmedia=='v') {
                                            if(xDiff > 0){
                                                --currentIndexVideo;
                                                if(currentIndexVideo<0){
                                                    currentIndexVideo=videoUris.size()-1;
                                                }
                                                MediaController mediaController= new MediaController(getContext());
                                                mediaController.setAnchorView(videoView);
                                                videoView.setMediaController(mediaController);
                                            }else {
                                                currentIndexVideo++;
                                                if(currentIndexVideo==countvideo){
                                                    currentIndexVideo=0;
                                                }
                                                MediaController mediaController= new MediaController(getContext());
                                                mediaController.setAnchorView(videoView);
                                                videoView.setMediaController(mediaController);
                                            }
                                            videoView.setVideoURI(videoUris.get(currentIndexVideo));
                                            videoView.start();
                                            return  true;
                                        }
                                    }
                                }else {
                                    if(Math.abs(yDiff) > treshold && Math.abs(velocityY) > velocity_threshold){
                                        if(whosmedia=='i'){
                                            if(yDiff > 0){
                                                switcher.setOutAnimation(getActivity(),R.anim.to_down);
                                                imageUris.remove(currentIndex);
                                                image_amount.setText(imageUris.size()+" image(s)");
                                                if(imageUris.size()>0){
                                                    switcher.setImageURI(imageUris.get(currentIndex));
                                                }else {
                                                    switcher.setImageURI(null);
                                                    switcher.setBackgroundResource(R.drawable.ic_baseline_photo_24);
                                                    Toast.makeText(getContext(),"Empty Image",
                                                            Toast.LENGTH_SHORT).show();
                                                    image_amount.setText("0 image(s)");
                                                }
                                            }
                                            else {
//                                            textView.setText("Swipe Up");
                                            }
                                            return true;
                                        }else if(whosmedia=='v'){
                                            if(yDiff > 0){
                                                videoUris.remove(currentIndexVideo);
                                                video_amount.setText(videoUris.size()+" video(s)");
                                                if(videoUris.size()>0){
                                                    MediaController mediaController= new MediaController(getContext());
                                                    mediaController.setAnchorView(videoView);
                                                    videoView.setMediaController(mediaController);
                                                    videoView.setVideoURI(videoUris.get(currentIndexVideo));
                                                    videoView.start();
                                                }else {
                                                    videoView.suspend();
                                                    videoView.setVideoURI(null);
                                                    videoView.setVisibility(View.GONE);
                                                    switcher.setVisibility(View.VISIBLE);
                                                    Toast.makeText(getContext(),"Empty Video",
                                                            Toast.LENGTH_SHORT).show();
                                                    video_amount.setText("0 video(s)");
                                                }
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
            return gestureDetector.onTouchEvent(event) ;
        }
    }
    public AlertDialog SelectTypeMedia(){
        builderfortypemedia = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialog, null);
        builderfortypemedia.setView(view);
        bt_image=view.findViewById(R.id.bt_image);
        bt_video=view.findViewById(R.id.bt_video);
        bt_image.setOnClickListener(this);
        bt_video.setOnClickListener(this);
        builderfortypemedia.create();
        return builderfortypemedia.show();
    }

    public Uri ConvertBimapToUri(Bitmap bitmap){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(requireContext().getContentResolver(), bitmap, "val", null);
        return Uri.parse(path);
    }

    private Bitmap reducedPictureSize(String currentPhotoPath, ImageSwitcher switcher){
        int targetSwitcherWidth=switcher.getWidth();
        int targetSwitcherHeight=switcher.getHeight();
        BitmapFactory.Options bmOptions= new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(currentPhotoPath,bmOptions);
        int cameraImageWidth= bmOptions.outWidth;
        int cameraImageHeight= bmOptions.outHeight;
        bmOptions.inSampleSize= Math.min(cameraImageWidth/targetSwitcherWidth,cameraImageHeight/targetSwitcherHeight);
        bmOptions.inJustDecodeBounds=false;
        Bitmap photoResized=BitmapFactory.decodeFile(currentPhotoPath,bmOptions);
        return photoResized;
    }

    private Bitmap rotatedImage(Bitmap bitmap, String currentPhotoPath){
        ExifInterface exifInterface=null;
        try {
            exifInterface= new ExifInterface(currentPhotoPath);
        }catch (Exception e) {
            e.printStackTrace();
        }

        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix=new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
        }
        Bitmap bitmapRotate= Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return bitmapRotate;
    }


//    @SuppressLint("StaticFieldLeak")
//    private class CompressVideo extends AsyncTask<String,String,String> {
//        Dialog dialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            dialog= ProgressDialog.show(getContext(),"","Compressing Video");
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String videopath=null;
//            try {
//                Uri uri=Uri.parse(strings[1]);
//                videopath= SiliCompressor.with(getContext()).compressVideo(uri,strings[2]);
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//            return videopath;
//        }
//
//        @SuppressLint("DefaultLocale")
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            dialog.dismiss();
//            File file = new File(s);
//            Uri uri=Uri.fromFile(file);
//            MediaController mediaController= new MediaController(getContext());
//            mediaController.setAnchorView(videoView);
//            videoView.setMediaController(mediaController);
//            videoView.setBackground(null);
//            videoUris.add(uri);
//            videoView.setVideoURI(videoUris.get(0));
//            videoView.start();
//            currentIndexVideo=0;
//            whosmedia='v';
//            video_amount.setText(videoUris.size()+" video(s)");
//            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ video apres compress"+String.format("Size : % 2f KB",file.length()/1024f) );
//        }
//    }
//
//    @SuppressLint("StaticFieldLeak")
//    private class CompressImage extends AsyncTask<String,String,String> {
//        Dialog dialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            dialog= ProgressDialog.show(getContext(),"","Compressing Image...");
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String imagepath;
//            Uri uri=Uri.parse(strings[1]);
//            imagepath= SiliCompressor.with(getContext()).compress(uri.toString(), new File(strings[2]));
//            return imagepath;
//        }
//
//        @SuppressLint("DefaultLocale")
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            dialog.dismiss();
//            File file = new File(s);
//            Uri uri=Uri.fromFile(file);
//            videoView.setVisibility(View.GONE);
//            imageUris.add(uri);
//            switcher.setBackground(null);
//            switcher.setVisibility(View.VISIBLE);
//            switcher.setImageURI(imageUris.get(0));
//            currentIndex = 0;
//            whosmedia='i';
//            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ image apres compress"+String.format("Size : % 2f KB",file.length()/1024f) );
//        }
//    }

    public void SaveMedia(ArrayList<Uri> images,ArrayList<Uri> videos){
        if(images.size()>0){
            for(int i=0;i<images.size();i++){
                Uri IndividualImage = images.get(i);
                StorageReference ImageName= DAO.ImageFolder.child(IndividualImage.getEncodedPath());
                ImageName.putFile(IndividualImage).addOnSuccessListener(taskSnapshot -> ImageName.getDownloadUrl().addOnSuccessListener(uri -> {
                    try {
                        Uri url=uri;
                        MediaLink imageLink=new MediaLink(cir.getCirclename(),url.toString());
                        DAO.mediaDatabase.child("imageBranch").push().setValue(imageLink);
                        images.clear();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    barProgress.setProgress(100);
                    textProgress.setText("Uploaded 100%");
                })).addOnProgressListener(snapshot -> {
                    linearProgress.setVisibility(View.VISIBLE);
                    double progress=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                    barProgress.setProgress((int) progress);
                    textProgress.setText(progress+" %");
                });
            }
        }

        if(videos.size()>0){
            for(int i=0;i<videos.size();i++){
                Uri IndividualVideo = videos.get(i);
                StorageReference VideoName= DAO.VideoFolder.child(IndividualVideo.getEncodedPath());
                VideoName.putFile(IndividualVideo).addOnSuccessListener(taskSnapshot -> VideoName.getDownloadUrl().addOnSuccessListener(uri -> {
                    try {
                        Uri url=uri;
                        MediaLink videoLink=new MediaLink(cir.getCirclename(),url.toString());
                        DAO.mediaDatabase.child("videoBranch").push().setValue(videoLink);
                        videos.clear();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    barProgress.setProgress(100);
                    textProgress.setText("Uploaded 100%");
                })).addOnProgressListener(snapshot -> {
                    linearProgress.setVisibility(View.VISIBLE);
                    double progress=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                    barProgress.setProgress((int) progress);
                    textProgress.setText(progress+" %");
                });
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void TodoAfterUpload(){
        linearView.setVisibility(View.GONE);
        linearTake.setVisibility(View.GONE);
        linear_amount.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);
        switcher.setBackground(null);
        switcher.setVisibility(View.VISIBLE);
        Glide.with(requireContext()).load(Uri.parse("https://image.ibb.co/b5fN97/Pouce_Enl_Air_2.gif")).into((ImageView) switcher.getCurrentView());
        add_details.setVisibility(View.GONE);
        editTextDescrption.setVisibility(View.GONE);
        title_in_add.setText("Click on ");
        title_in_add.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_arrow_back_24,0);
    }

    public void tomakeViewVisible(){
        relativeLayout.setVisibility(View.VISIBLE);
        linearView.setVisibility(View.VISIBLE);
        linearTake.setVisibility(View.VISIBLE);
        linear_amount.setVisibility(View.VISIBLE);
    }

//    @SuppressLint("StaticFieldLeak")
//    private class DownloadMedia extends AsyncTask<ArrayList<Uri>,Integer,Long> {
//        Dialog dialog;
//        ProgressDialog progressDialog;
//
//        @SafeVarargs
//        @Override
//        protected final Long doInBackground(ArrayList<Uri>... arrayLists) {
//            ArrayList <Uri> images=arrayLists[0];
//            ArrayList <Uri> videos=arrayLists[1];
//            if(images.size()>0){
//                for(int i=0;i<images.size();i++){
//                    Uri IndividualImage = images.get(i);
//                    StorageReference ImageName= DAO.ImageFolder.child(IndividualImage.getEncodedPath());
//
//                    ImageName.putFile(IndividualImage).addOnSuccessListener(taskSnapshot -> ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            try {
//                                Uri url=uri;
//                                MediaLink imageLink=new MediaLink(cir.getCirclename(),url.toString());
//                                DAO.mediaDatabase.child("imageBranch").push().setValue(imageLink);
//                                images.clear();
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//                            barProgress.setProgress(0);
//                        }
//                    })).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                            linearProgress.setVisibility(View.VISIBLE);
//                            double progress=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
//                            barProgress.setProgress((int) progress);
//                        }
//                    });
//                }
//            }
//
//            if(videos.size()>0){
//                for(int i=0;i<videos.size();i++){
//                    Uri IndividualVideo = videos.get(i);
//                    StorageReference VideoName= DAO.VideoFolder.child(IndividualVideo.getEncodedPath());
//                    VideoName.putFile(IndividualVideo).addOnSuccessListener(taskSnapshot -> VideoName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            try {
//                                Uri url=uri;
//                                MediaLink videoLink=new MediaLink(cir.getCirclename(),url.toString());
//                                DAO.mediaDatabase.child("videoBranch").push().setValue(videoLink);
//                                videos.clear();
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//                        }
//                    }));
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
////            progressDialog.setProgress(values[0]);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            progressDialog= new ProgressDialog(getContext());
////            progressDialog.show();
//            dialog= ProgressDialog.show(getContext(),"","Uploading Media");
//        }
//
//        @Override
//        protected void onPostExecute(Long aLong) {
//            super.onPostExecute(aLong);
////            progressDialog.dismiss();
//            dialog.dismiss();
//        }
//    }
}