package com.example.cornernews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "WhoLogin.db";
    public static final String WHO_LOGIN_TABLE = "WhoLogin";

    public HelperDB(Context context) {
        super(context, DATABASE_NAME,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS "+WHO_LOGIN_TABLE+
                        "(_id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT NOT NULL UNIQUE, username TEXT NOT NULL UNIQUE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertLogin (Login login) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", login.getEmail());
        contentValues.put("username", login.getUsername());
        long insertId =db.insert(WHO_LOGIN_TABLE, null, contentValues);
        return insertId;
    }

    public String GetUserName() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor getting =  db.rawQuery( "Select * from "+WHO_LOGIN_TABLE, null );
        getting.moveToFirst();
        String username=getting.getString(getting.getColumnIndex("username"));
        return username;
    }

    public String GetEmail() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor getting =  db.rawQuery( "Select * from "+WHO_LOGIN_TABLE, null );
        getting.moveToFirst();
        String email=getting.getString(getting.getColumnIndex("email"));
        return email;
    }

    public void DeleteWhologin(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ WHO_LOGIN_TABLE);
    }
}
