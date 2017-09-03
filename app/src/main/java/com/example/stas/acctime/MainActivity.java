package com.example.stas.acctime;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import static com.example.stas.acctime.MysqlHelper.getWifi;
import static com.example.stas.acctime.R.id.container;
import static com.example.stas.acctime.MyService.macAddress;

public class MainActivity extends AppCompatActivity{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static int finalCount,status, pass;;
    private static int currentTime;
    private static String string1;
    public static Statement stmt;
    public static ResultSet rs;
    private ViewPager mViewPager;
    public static Calendar c;
    public static android.os.Handler mainHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        startService(new Intent(this, MyService.class));

        mainHandler = new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what){
                        case 1:
                            string1 = String.valueOf(finalCount);
                            PlaceholderFragment.textView2.setText("     Вы на работе");
                        break;
                        case 2:
                            PlaceholderFragment.textView2.setText("     Вы не на работе");
                        break;
                }
            }
        };



        c = Calendar.getInstance();
        currentTime = c.get(Calendar.MINUTE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        string1 = null;
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_logout) {
            DBHelp dbHelper;
            dbHelper = new DBHelp(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("userAbout", null, null);
            db.delete("userFullInfo", null, null);
            Intent backToAuth = new Intent(this, Main2Activity.class);
            startActivity(backToAuth);
            stopService(new Intent(this, MyService.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static class PlaceholderFragment extends Fragment implements View.OnClickListener {

        private static final String ARG_SECTION_NUMBER = "section_number";

        private ImageButton btn;
        private TextView textView;
        private static TextView textView2;
        private Button btn2;
        private ImageView statusImg;
        private CountDownTimer ctd;
        private TextView textView3;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = null;
            status = 0;

            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    rootView = inflater.inflate(R.layout.third_fragment, container, false);
                    textView = (TextView) rootView.findViewById(R.id.section_label);
                    textView3 = (TextView) rootView.findViewById(R.id.statusView);
                    btn = (ImageButton) rootView.findViewById(R.id.startButton);
                    btn.setOnClickListener(this);
                    statusImg = (ImageView) rootView.findViewById(R.id.imageView2);
                    ctd = new CountDownTimer(60*1000*60 - currentTime*-1000*60, 1000) {
                        public void onTick(long millisUntilFinished) {
                            textView.setText("Вы проработали: " + millisUntilFinished / (60000*60) + ":" +millisUntilFinished / 60000 % 60+ ":" + millisUntilFinished / 1000 % 60);
                            //here you can have your logic to set text to edittext
                        }

                        public void onFinish() {
                            textView.setText("done!");
                        }

                    };






                    //textView.setText("Это секция " + getArguments().getInt(ARG_SECTION_NUMBER));
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.second_fragment, container, false);
                    textView2 = (TextView) rootView.findViewById(R.id.workersCount);
                    btn2 = (Button) rootView.findViewById(R.id.button);
                    btn2.setOnClickListener(this);
                    break;
                }
            return rootView;

        }


        @Override
        public void onClick(View v) {
            currentTime = c.get(Calendar.MINUTE);
            switch (v.getId()){
                case R.id.startButton:
                   //MysqlHelper.mysqlConnection();
                    string1 = String.valueOf(finalCount);
                    System.out.println(string1);
                    if(status==1) {
                        // textView.setText(string1);
                        //statusImg.setImageResource(R.drawable.pause);
                        textView3.setText("Вы покинуи место работы. \n Рабочий не окончен.");
                        //ctd.start();
                        ctd.cancel();
                        status=0;
                    } else {
                        ctd.start();
                        //textView3.setText("Работаю");
                       // statusImg.setImageResource(R.drawable.start);
                        status=1;
                    }
                    break;
                case R.id.button:
                    mainHandler.sendEmptyMessage(5);
                    break;
                default:
                    break;

            }
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }



        @Override
        public int getCount() { return 2;}

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Статус";
                case 1:
                    return "Cообщения" + "\n" + "об отсутствии";
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
                finish();            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }


    }

