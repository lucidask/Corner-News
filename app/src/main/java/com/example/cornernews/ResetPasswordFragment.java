package com.example.cornernews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordFragment extends Fragment implements View.OnClickListener {
    AppCompatImageButton log_out;
    AppCompatTextView back_arrow;
    AppCompatButton bt_reset;
    AppCompatEditText email_input;
    ProgressBar progressBar;
    Boolean emailexist=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_reset_password, container, false);
        log_out=view.findViewById(R.id.button_logout);
        log_out.setVisibility(View.GONE);
        back_arrow=view.findViewById(R.id.back_perso);
        back_arrow.setOnClickListener(this);
        email_input=view.findViewById(R.id.email_input_reset_password);
        bt_reset=view.findViewById(R.id.button_reset_password);
        progressBar=view.findViewById(R.id.progressLResetting);
        bt_reset.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_perso:
                requireActivity().finish();
                break;
            case R.id.button_reset_password:
                if(!email_input.getText().toString().trim().isEmpty()){
                    if(!Patterns.EMAIL_ADDRESS.matcher(email_input.getText().toString().trim()).matches()){
                        email_input.setError("Please provide valid email");
                        email_input.requestFocus();
                    }
                    else {
                       for (int i=0;i<DAO.loginTab.size();i++){
                           if(DAO.loginTab.get(i).getEmail().equals(email_input.getText().toString().trim())){
                               emailexist=true;
                           }
                       }
                       if(emailexist){
                           resetPassword();
                       }else {
                           email_input.setError("This email does not exist");
                           email_input.requestFocus();
                       }
                    }
                }else {
                    email_input.setError("Required");
                    email_input.requestFocus();
                }
                break;
        }
    }

    private void resetPassword() {
        progressBar.setVisibility(View.VISIBLE);
       DAO.UserAuth.sendPasswordResetEmail(email_input.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(),"Check your email to reset your password",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    email_input.getText().clear();
                }else {
                    Toast.makeText(getContext(),"Try again! Something wrong happened!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}