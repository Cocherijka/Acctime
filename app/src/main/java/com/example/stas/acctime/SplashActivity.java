package com.example.stas.acctime;

/**
 * Created by Stas on 13.05.17.
 */

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        DBHelp dbHelper;


        dbHelper = new DBHelp(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor curs = db.query("userAbout", null, null, null, null, null, null);

        if (!curs.moveToFirst()) {
            intent = new Intent(this, Main2Activity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}

