package com.example.cornernews;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkerToEventDataChange extends Worker {
    public WorkerToEventDataChange(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        DAO.updateDaoAlertInstanceFromDatabase();
        return Result.success();
    }
}
