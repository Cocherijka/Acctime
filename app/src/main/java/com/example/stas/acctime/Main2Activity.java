package com.example.stas.acctime;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.sql.SQLException;


public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    static EditText passText;
    static EditText logText;
    Intent intent;
    DBHelp dbHelper;
    Handler loginHandler;
    ImageButton logButt;
    ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Авторизация");
        dbHelper = new DBHelp(this);
        intent = new Intent(this, MainActivity.class);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        loginHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                LogInListener();
                dialog.dismiss();
            }
        };

        logButt = (ImageButton) findViewById(R.id.logButton);
        logButt.setOnClickListener(this);
        logText = (EditText) findViewById(R.id.editText2);
        passText = (EditText) findViewById(R.id.editText);

    }

    public void LogInListener(){

        ContentValues cv = new ContentValues();
        int id = 0;
        if (MysqlHelper.isInternetAvailable()) {
            SQLiteDatabase  db = dbHelper.getWritableDatabase();
            try {
                id = MysqlHelper.LogIn(logText.getText().toString(), passText.getText().toString());
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (id != 0){
                Cursor curs = db.query("userAbout", null, null, null, null, null, null);
                cv.put("id", id);
                db.insert("userAbout", null, cv);
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                if (curs.moveToFirst()) {
                    do {
                        System.out.println("из базы данных " + curs.getInt(curs.getColumnIndex("id")));
                    } while (curs.moveToNext());
                }
                dbHelper.close();
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Main2Activity.this,"Неверный логин или пароль",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Main2Activity.this,"Нет подлючения к интернету",Toast.LENGTH_LONG).show();
        }
                    }



    @Override
    public void onClick(View v) {
        dialog.setIndeterminate(true);
        dialog.show();
        // String name = etName.getText().toString();
        loginHandler.sendEmptyMessageDelayed(0, 300);


        //  String email = etEmail.getText().toString();
    }

}


