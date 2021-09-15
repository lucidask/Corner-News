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
//        int SizeTabCircle=DAO.TabCircle.size();
        DAO.updateDaoZoneFromDatabase();
//        if(DAO.TabCircle.size()>SizeTabCircle){
//////            String LastCircleName = getInputData().getString("CircleInstanceName");
//////            String LastCircleCenter = getInputData().getString("CircleInstanceCenter");
//////            System.out.println("%%%%%%%%%%%%%%%%%@@@@@@@@@@@@@@@&&&&&&&&&&&&&&&&&&&&&&&& New Circle Added"+ LastCircleName+"  "+LastCircleCenter);
//            System.out.println("%%%%%%%%%%%%%%%%%@@@@@@@@@@@@@@@&&&&&&&&&&&&&&&&&&&&&&&& New Circle Added");
//        }else {
//            System.out.println("%%%%%%%%%%%%%%%%%@@@@@@@@@@@@@@@&&&&&&&&&&&&&&&&&&&&&&&& No");
//        }
        return Result.success();
    }
}
