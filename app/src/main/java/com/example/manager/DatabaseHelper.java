package com.example.manager;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.airbnb.lottie.animation.content.Content;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "notificationStorage.db";
    public static String TABLE_NAME = "notificationTable";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "type";
    public static final String COL_3 = "subject";
    public static final String COL_4 = "message";

    public DatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,1);
    }
    public DatabaseHelper(@Nullable Context context, String user)
    {
        super(context, DATABASE_NAME, null, 1);
        TABLE_NAME = "notificationTable"+user;
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TYPE TEXT, SUBJECT TEXT, MESSAGE TEXT)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String type, String subject, String message, String user)
    {
        TABLE_NAME = "notificationTable"+user;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, type);
        contentValues.put(COL_3, subject);
        contentValues.put(COL_4, message);
        long result = db.insert(TABLE_NAME, null, contentValues);
        Log.i("NCheck", TABLE_NAME);
        Log.i("NCheck" ,String.valueOf(result));
        if(result==-1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(String user)
    {

        TABLE_NAME = "notificationTable"+user;
        Log.i("NCheck table", TABLE_NAME);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+ TABLE_NAME + " ORDER BY ID DESC",null);
    }

}
