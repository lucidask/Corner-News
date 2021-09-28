package com.example.cornernews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.ArrayList;

public class AlertAdapter extends ArrayAdapter<AlertInfo> {
    private Context mContext;
    private int mResource;

    public AlertAdapter(@NonNull Context context, int resource, @NonNull ArrayList<AlertInfo> alertInfos) {
        super(context, resource, alertInfos);
        this.mContext=context;
        this.mResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=LayoutInflater.from(mContext);
        convertView=layoutInflater.inflate(mResource,parent,false);

        ImageView imageViewAlertImage=convertView.findViewById(R.id.AlertImage);
        AppCompatTextView textViewAlertName=convertView.findViewById(R.id.AlertName);
        AppCompatTextView textViewAlertDateTime=convertView.findViewById(R.id.AlertDateTime);
        AppCompatTextView textViewAlertCountImageVideo=convertView.findViewById(R.id.AlertCountImageAndVideo);

        imageViewAlertImage.setImageResource(getItem(position).getAlertImage());
        textViewAlertName.setText(getItem(position).getAlertName());
        textViewAlertDateTime.setText(getItem(position).getAlertDateTime());
        textViewAlertCountImageVideo.setText(getItem(position).getCountImageAndVideo());
        return convertView;
    }
}
