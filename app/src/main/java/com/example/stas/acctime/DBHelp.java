package com.example.stas.acctime;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelp extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "userInfo";
    public static final String TABLE_USER_INFO = "userAbout";
    public static final String TABLE_MAIN = "userFullInfo";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "shortAbout";
    public static final String KEY_TIME = "time";
    public static final String KEY_WORK = "work";


    public DBHelp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_USER_INFO + "(" + KEY_ID + " integer, " + KEY_NAME + "text" + ")");
        sqLiteDatabase.execSQL("create table " + TABLE_MAIN + "(" + KEY_ID + " integer primary key, " + "wifiID text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exist" + TABLE_MAIN);
        sqLiteDatabase.execSQL("drop table if exist" + TABLE_USER_INFO);
        onCreate(sqLiteDatabase);
    }



}
