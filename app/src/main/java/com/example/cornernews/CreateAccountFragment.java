package com.example.cornernews;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateAccountFragment extends Fragment implements View.OnClickListener {
    private AppCompatEditText input_email;
    private AppCompatEditText create_username;
    private AppCompatEditText create_password;
    private AppCompatEditText confirm_password;
    private AppCompatButton sign_in;
    private  boolean goodsizepassword=false;
    private  boolean goodpasswordconfirm=false;
    private  boolean goodemailtextconfirm=false;
    private boolean usernameexist=false;
    private boolean emailexist=false;
    private boolean containsDigit=false;
    private boolean containsUpperCase=false;
    private boolean containsSpecialCh=false;
    private  AppCompatTextView title;
    private AppCompatTextView back_arrow;
    private AppCompatImageButton log_out;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_create_account, container, false);
        input_email=view.findViewById(R.id.input_email);
        create_username=view.findViewById(R.id.create_username);
        create_password=view.findViewById(R.id.create_password);
        confirm_password=view.findViewById(R.id.confirm_created_password);
        sign_in=view.findViewById(R.id.button_sign_in);
        title=view.findViewById(R.id.title);
        progressBar=view.findViewById(R.id.progressSign_in);
        back_arrow=view.findViewById(R.id.back_perso);
        back_arrow.setOnClickListener(this);
        log_out=view.findViewById(R.id.button_logout);
        log_out.setVisibility(View.GONE);
        title.setText("Corner News");
        sign_in.setOnClickListener(this);
        return view;
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_sign_in:
                if(DAO.isConnected((AppCompatActivity) getActivity())){
                if (!(create_username.getText().toString().trim().isEmpty() || create_password.getText().toString().trim().isEmpty() ||
                        confirm_password.getText().toString().trim().isEmpty() ||
                        input_email.getText().toString().trim().isEmpty())) {
                    if(!Patterns.EMAIL_ADDRESS.matcher(input_email.getText().toString().trim()).matches()){
                        input_email.setError("Please provide valid email");
                        input_email.requestFocus();
                    }
                    else {
                        goodemailtextconfirm=true;
                    }

                    if(create_password.getText().toString().trim().length()>=8){
                        goodsizepassword=true;
                    }
                    else {
                        create_password.setError("Password length must be 8 or more characters");
                        create_password.requestFocus();
                        confirm_password.setError("Password length must be 8 or more characters");
                        confirm_password.requestFocus();
                    }

                    for(int i=0;i<create_password.getText().toString().trim().length();i++) {
                        char charAtDigit = create_password.getText().toString().trim().charAt(i);
                        if (Character.isDigit(charAtDigit)) {
                            containsDigit = true;
                            break;
                        }
                    }

                    if(containsDigit){
                        containsDigit=false;
                        for(int i=0;i<create_password.getText().toString().trim().length();i++) {
                            char charAtUpper = create_password.getText().toString().trim().charAt(i);
                            if(Character.isUpperCase(charAtUpper)){
                                containsUpperCase=true;
                                break;
                            }
                        }
                    } else {
                        create_password.setError("Password  must contains 8 or more characters at least one digit, one capital letter, one special character");
                        create_password.requestFocus();
                    }

                    if(containsUpperCase){
                        containsUpperCase=false;
                        char[] specialCh = {'!','@',']','#','$','%','^','&','*'};
                        for(int i=0;i<create_password.getText().toString().trim().length();i++) {
                            char charAtSpecial = create_password.getText().toString().trim().charAt(i);
                            for (Character character:specialCh) {
                                if(character.equals(charAtSpecial)){
                                    containsSpecialCh=true;
                                    break;
                                }
                            }
                        }
                    } else {
                        create_password.setError("Password must contains 8 or more characters at least one digit, one capital letter, one special character");
                        create_password.requestFocus();
                    }

                    if(containsSpecialCh){
                        containsSpecialCh=false;
                        if(goodsizepassword){
                            goodsizepassword=false;
                            if(create_password.getText().toString().trim().equals(confirm_password.getText().toString().trim())){
                                goodpasswordconfirm = true;
                            }else {
                                create_password.setError("Password don't match");
                                create_password.requestFocus();
                                confirm_password.setError("Password don't match");
                                confirm_password.requestFocus();
                            }
                        }
                    }else {
                        create_password.setError("Password must contains 8 or more characters at least one digit, one capital letter, one special character");
                        create_password.requestFocus();
                    }


                    if (goodpasswordconfirm && goodemailtextconfirm) {
                        goodemailtextconfirm=false;
                        goodpasswordconfirm = false;
                        for (int i = 0; i < DAO.loginTab.size(); i++) {
                            if (DAO.loginTab.get(i).getUsername().trim().equals(create_username.getText().toString().trim())) {
                                usernameexist = true;
                                break;
                            }
                        }

                        for (int i = 0; i < DAO.loginTab.size(); i++) {
                            if (DAO.loginTab.get(i).getEmail().trim().equals(input_email.getText().toString().trim())) {
                                emailexist = true;
                                break;
                            }
                        }

                        if (usernameexist) {
                            usernameexist = false;
                            create_username.setError("Username already exist");
                            create_username.requestFocus();
                        } else if(emailexist){
                            emailexist=false;
                            input_email.setError("Email already exist");
                            input_email.requestFocus();
                        }
                        else {
                            saveloginToDatabase(new Login(input_email.getText().toString().trim(), create_username.getText().toString().trim()), create_password.getText().toString().trim());
                        }
                    }

                } else {
                    if (create_username.getText().toString().trim().isEmpty()) {
                        create_username.setError("Required");
                        create_username.requestFocus();
                    }
                    if(input_email.getText().toString().trim().isEmpty() ){
                        input_email.setError("Required");
                        input_email.requestFocus();
                    }
                    if (create_password.getText().toString().trim().isEmpty()) {
                       create_password.setError("Required");
                       create_password.requestFocus();
                    }
                    if(confirm_password.getText().toString().trim().isEmpty()){
                        confirm_password.setError("Required");
                        confirm_password.requestFocus();
                    }
                }
        }else{
                    Toast.makeText(getContext(),"Check your internet connection ",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back_perso:
                requireActivity().finish();
                break;
        }
    }

    public void saveloginToDatabase(Login log,String password){
        progressBar.setVisibility(View.VISIBLE);
        DAO.UserAuth.createUserWithEmailAndPassword(log.getEmail(),password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
//                    logDatabase.child(log.getUsername()).setValue(log);
                DAO.logDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(log).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        FirebaseUser user=DAO.UserAuth.getCurrentUser();
                        if(!user.isEmailVerified()){
                            user.sendEmailVerification();
                            progressBar.setVisibility(View.INVISIBLE);
                            input_email.getText().clear();
                            create_username.getText().clear();
                            create_password.getText().clear();
                            confirm_password.getText().clear();
                            Toast.makeText(this.getContext(), "Account created successfully! Check your mail to verify your account before log in  ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(),"Something wrong happened! Try again please! ",
                                Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }else {
                Toast.makeText(getContext(),"Something wrong happened! You may already have account with this email! ",
                        Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}