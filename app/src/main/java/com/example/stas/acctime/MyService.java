package com.example.stas.acctime;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.text.format.Formatter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import android.text.format.DateFormat;
import android.util.Log;


import static android.content.ContentValues.TAG;
import static android.net.sip.SipErrorCode.TIME_OUT;


public class MyService extends Service {

/*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }
*/
    static int seconds;
    public static android.os.Handler hnd;
    public static final int STATUS_NONE = 0; // нет подключения
    static final int STATUS_CONNECTING = 1; // подключаемся
    final int STATUS_CONNECTED = 2; // подключено
    final int STATUS_DOWNLOAD_START = 3; // загрузка началась
    final int STATUS_DOWNLOAD_FILE = 4; // файл загружен
    final int STATUS_DOWNLOAD_END = 5; // загрузка закончена
    final int STATUS_DOWNLOAD_NONE = 6; // нет файлов для загрузки
    public static Connection con;
    public static String macAddress;
    static WifiManager wifiManager;
    static String wifIp;
    static String bssid;
    WifiInfo wInfo;
    static NetworkInfo mWifi;
    MyTask mt;
    Timer t;
    DBHelp dbHelper;


    public MyService() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        hnd = new Handler() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MyService.STATUS_NONE:
                        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                        wInfo = wifiManager.getConnectionInfo();
                        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        break;

                    case MyService.STATUS_CONNECTING:
                        SimpleDateFormat format= new SimpleDateFormat("dd", Locale.getDefault());
                        String myDate = format.format(new Date());
                        System.out.println(myDate);
                        try {
                            MysqlHelper.startWork(1,myDate);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        //Cursor curs = db.query("userAbout", null, null, null, null, null, null);
                           // id = curs.getInt(curs.getColumnIndex("id"));
                        System.out.println(0);
                       // System.out.println("Seconds:" + MyService.seconds);
                        //DateValidator.isThisDateValid(myDate, "dd");
                        break;
                }
            }
        };
//Declare the timer
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
                                  @Override
                                  public void run() {
                                      mt = new MyTask();
                                      mt.execute();
                                      hnd.sendEmptyMessage(STATUS_NONE);
                                  }
                              },
                0,
                10000);
    }

    @Override
    public void onDestroy() {
        t.cancel();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

/*
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";
    }
*/

    static String BSSID(){
        //MysqlHelper.mysqlConnection();
        //MysqlHelper.isInternetAvailable();
        if (mWifi.isConnected()) {
            wifIp = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
            bssid  = wifiManager.getConnectionInfo().getBSSID();
            System.out.println(wifIp);
            return bssid;
        } else {
            System.out.println(macAddress);
            return "";
        }
    }


    static boolean bssidEqual () throws SQLException, ClassNotFoundException {
        String neededBSSID = MysqlHelper.getWifi();
                if(BSSID().equals(neededBSSID)){
                    System.out.println("Yes");
                    return true;
                } else {
                    System.out.println("No");
                    return false;
                }
            }


    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                if(bssidEqual()){
                    hnd.sendEmptyMessage(STATUS_CONNECTING);
                    try {
                        MainActivity.mainHandler.sendEmptyMessage(1);
                    } catch (Exception e) {}
                } else {
                    try {
                        MainActivity.mainHandler.sendEmptyMessage(2);
                        //System.out.println("day of m " + day);
                    } catch (Exception e) {}
                }

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}


