package com.example.cornernews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    AppCompatEditText username;
    AppCompatEditText password;
    AppCompatButton login;
    AppCompatTextView title;
    AppCompatTextView back_arrow;
    AppCompatImageButton log_out;
    AppCompatTextView create_account,forgot_password;
    public static final String WHO_CALL="who_call";
    boolean tem=false;
    AlertDialog dialog;
    private   String[] PERMISSIONS;
    ProgressBar progressBar;
    SignInButton bt_google_sign_in;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title=findViewById(R.id.title);
        back_arrow=findViewById(R.id.back_perso);
        log_out=findViewById(R.id.button_logout);
        log_out.setVisibility(View.GONE);
        username=(AppCompatEditText)findViewById(R.id.edit_username);
        password=(AppCompatEditText)findViewById(R.id.edit_pass);
        login=(AppCompatButton)findViewById(R.id.button_login);
        create_account=(AppCompatTextView)findViewById(R.id.create_account);
        forgot_password=findViewById(R.id.text_view_forgot_password);
        progressBar=findViewById(R.id.progressLogin);
        bt_google_sign_in=findViewById(R.id.bt_sign_with_google);
        bt_google_sign_in.setOnClickListener(this);
        login.setOnClickListener(this);
        create_account.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
//        DAO.saveloginToDatabase(new Login("daskbertrand@gmail.com","Akin"),"Connaistoi");
        back_arrow.setVisibility(View.GONE);
        title.setText("Corner News");
        AskMultiplePermission();
        DAO.updateDaoZoneFromDatabase();
        DAO.updateDaoLoginFromDatabase();
        DAO.updateDaoMediaFromDatabase();
        DAO.updateListCircleListImageAndListVideo();
        GoogleSignInOptions googleSignInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("65763909467-1fgqm1hfdk1v6gvfjivg1jsgct6o5ucb.apps.googleusercontent.com")
                .requestEmail().build();
        googleSignInClient= GoogleSignIn.getClient(MainActivity.this,googleSignInOptions);

        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            for(int i=0;i<DAO.loginTab.size();i++){
                if(DAO.loginTab.get(i).getEmail().equals(firebaseUser.getEmail())){
                    break;
                }
            }
            DAO.Whologin.clear();
            DAO.addWhoLogin(new Login( firebaseUser.getEmail(),firebaseUser.getDisplayName()));
            Toast.makeText(MainActivity.this,"Welcome "+DAO.Whologin.get(0).getUsername().toUpperCase(),
                    Toast.LENGTH_SHORT).show();
            Intent gotomap=new Intent(MainActivity.this,ContainerFrag.class);
            gotomap.putExtra(WHO_CALL,R.id.bt_sign_with_google);
            startActivity(gotomap);
        }
//        checkFirstRun();
    }

    @Override
    protected void onResume() {
        progressBar.setVisibility(View.INVISIBLE);
        super.onResume();
        login.setEnabled(true);
        DAO.updateDaoZoneFromDatabase();
        DAO.updateDaoLoginFromDatabase();
        DAO.updateDaoMediaFromDatabase();
        DAO.updateListCircleListImageAndListVideo();
        username.getText().clear();
        password.getText().clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_login:
                    if(DAO.isConnected(MainActivity.this)){
                        if(!(username.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty())){
                            if(DAO.loginTab.size()>0){
                                for(int i=0;i<DAO.loginTab.size();i++){
                                    if(DAO.loginTab.get(i).getUsername().equals(username.getText().toString()) ||
                                            DAO.loginTab.get(i).getEmail().equals(username.getText().toString())){
                                        tem=true;
                                        if(DAO.Whologin.size()>0){
                                            DAO.Whologin.clear();
                                        }
                                        DAO.addWhoLogin(DAO.loginTab.get(i));
                                        break;
                                    }
                                }
                                if(tem){
                                    tem=false;
                                    progressBar.setVisibility(View.VISIBLE);
                                    DAO.UserAuth.signInWithEmailAndPassword(DAO.Whologin.get(0).getEmail(),password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                FirebaseUser user=DAO.UserAuth.getCurrentUser();
                                                assert user != null;
                                                if(user.isEmailVerified()){
                                                    login.setEnabled(false);
                                                    Toast.makeText(MainActivity.this,"Welcome "+DAO.Whologin.get(0).getUsername().toUpperCase(),
                                                            Toast.LENGTH_SHORT).show();
                                                    Intent gotomap=new Intent(MainActivity.this,ContainerFrag.class);
                                                    gotomap.putExtra(WHO_CALL,v.getId());
                                                    startActivity(gotomap);
                                                }else {
                                                    user.sendEmailVerification();
                                                    Toast.makeText(MainActivity.this,"Check your email to validate your account",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                password.getText().clear();
                                                Toast.makeText(MainActivity.this,"Username or Password Incorrect",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else{
                                    username.setError("Username or Password Incorrect");
                                    username.requestFocus();
                                    password.setError("Username or Password Incorrect");
                                    password.requestFocus();
                                }
                            }
                        }else {
                            if (username.getText().toString().trim().isEmpty()) {
                                username.setError("Required");
                                username.requestFocus();
                            }
                            if(password.getText().toString().trim().isEmpty()){
                                password.setError("Required");
                                password.requestFocus();
                            }
                        }
                    } else {
                        Toast.makeText(MainActivity.this,"Check your internet connection ",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.create_account:
                case R.id.text_view_forgot_password:
                    Intent GotoCreateAccountorResetPassord=new Intent(this,ContainerFrag.class);
                    GotoCreateAccountorResetPassord.putExtra(WHO_CALL,v.getId());
                    startActivity(GotoCreateAccountorResetPassord);
                    break;
                case R.id.bt_sign_with_google:
                    Intent intent=googleSignInClient.getSignInIntent();
                    startActivityForResult(intent,100);
                    break;
            }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressBar.setVisibility(View.VISIBLE);
        if(requestCode==100){
            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            if(signInAccountTask.isSuccessful()){
                try {
                    GoogleSignInAccount googleSignInAccount=signInAccountTask.getResult(ApiException.class);
                    if(googleSignInAccount!=null){
                        AuthCredential authCredential= GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);
                        DAO.UserAuth.signInWithCredential(authCredential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    progressBar.setVisibility(View.INVISIBLE);
                                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                    assert user != null;
                                    DAO.Whologin.clear();
                                    DAO.addWhoLogin(new Login( user.getEmail(),user.getDisplayName()));
                                    Toast.makeText(MainActivity.this,"Welcome "+DAO.Whologin.get(0).getUsername().toUpperCase(),
                                            Toast.LENGTH_SHORT).show();
                                    Intent gotomap=new Intent(MainActivity.this,ContainerFrag.class);
                                    gotomap.putExtra(WHO_CALL,R.id.bt_sign_with_google);
                                    startActivity(gotomap);
                                }else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(MainActivity.this,"Authentication Failed ",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public AlertDialog setdialogonstart(){
        AlertDialog.Builder dialogforstart = new AlertDialog.Builder(this);
        dialogforstart.setTitle("");
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.alertdialogforstart, null);
        dialogforstart.setView(view);
        dialogforstart.create();
        return dialogforstart.show();
    }

    public void SendLogcatMail(){

        // save logcat in file
        File outputFile = new File(Environment.getExternalStorageDirectory(),
                "logcat "+Math.random()*1000+Math.random()*100+".txt");
        try {
            Runtime.getRuntime().exec(
                    "logcat -f " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //send file using email
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // Set type to "email"
        emailIntent.setType("vnd.android.cursor.dir/email");
        String[] to = {"daskbertrand@gmail.com"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
        // the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, outputFile.getAbsolutePath());
        // the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Subject");
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }

    private void AskMultiplePermission(){
        PERMISSIONS= new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if(!hasPermissions(getApplicationContext(),PERMISSIONS)){
            ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS,1);
        }
    }

    private boolean hasPermissions(Context context, String... PERMISSIONS){
        if(context != null && PERMISSIONS != null){
            for(String permission:PERMISSIONS){
                if(ActivityCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(MainActivity.this,"Location Permission Granted",
//                        Toast.LENGTH_SHORT).show();
            }else {
//                Toast.makeText(MainActivity.this,"Location Permission denied",
//                        Toast.LENGTH_SHORT).show();
            }

            if(grantResults[1]==PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(MainActivity.this,"Location Permission Granted",
//                        Toast.LENGTH_SHORT).show();
            }else {
//                Toast.makeText(MainActivity.this," Location Permission denied",
//                        Toast.LENGTH_SHORT).show();
            }

            if(grantResults[2]==PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(MainActivity.this,"Camera  Permission Granted",
//                        Toast.LENGTH_SHORT).show();
            }else {
//                Toast.makeText(MainActivity.this,"Camera Permission denied",
//                        Toast.LENGTH_SHORT).show();
            }

            if(grantResults[3]==PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(MainActivity.this,"Storage Permission Granted",
//                        Toast.LENGTH_SHORT).show();
            }else {
//                Toast.makeText(MainActivity.this,"Storage Permission denied",
//                        Toast.LENGTH_SHORT).show();
            }

            if(grantResults[4]==PackageManager.PERMISSION_GRANTED){
//                Toast.makeText(MainActivity.this," Storage Permission Granted",
//                        Toast.LENGTH_SHORT).show();
            }else {
//                Toast.makeText(MainActivity.this," Storage Permission denied",
//                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {
            dialog=setdialogonstart();
            // TODO This is a new install (or the user cleared the shared preferences)

        } else if (currentVersionCode > savedVersionCode) {

            // TODO This is an upgrade
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }
}
