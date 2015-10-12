package com.android.demo.wifisampling;

import java.io.BufferedWriter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import android.app.Activity;
import java.util.Date;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;
import android.os.Handler;
import android.widget.*;


public class RecordData extends Activity
{
    private Integer numberOfSamplesSet = 0;
    private Integer numberOfSample = 0;
    private String sendCode ="";

    public void setSendCode(String sc)
    {
        this.sendCode = sc;
    }

    private String TAG = this.getClass().getSimpleName();
    private static final int ACTIVITY_CLIENT=4;


    private final Runnable repeat= new Runnable() {
        @Override
        public void run() {
            getRssi();

            handler.postDelayed(this, 5000); //every 5 seconds
            if (!(sendCode.equalsIgnoreCase("")))
            {
                numberOfSample++;
                writeToFile(sendCode);
                sendCode ="";
            }
            handler.post(mUpdateResults);
        }
    };


    private TextView info;
    private static Context context;
    private WifiManager wifi;

    public WifiManager getWifi()
    {
        return this.wifi;
    }
    private BroadcastReceiver receiver;
    private String filename ="";
    private String mac="";
    private Button buttonRegister;
    private Button buttonChangeSet;
    private int registered=0;
    private Handler handler = new Handler();
    private Button buttonUnregister;

    Runnable mUpdateResults = new Runnable() {
        Date date;
        public void run() {
            date=new Date();
            info.setText("Time: "+date.getHours()+":" + date.getMinutes() +":" + date.getSeconds()+" Number Of Sample: "+numberOfSample);
        }
    };

    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        RecordData.context=getApplicationContext();
        setContentView(R.layout.recdata);
        info=(TextView) findViewById(R.id.info);
        buttonRegister = (Button) findViewById(R.id.record_start);
        buttonChangeSet = (Button) findViewById(R.id.changeSet);
        buttonChangeSet.setOnClickListener(changeSetListener);
        buttonRegister.setOnClickListener(connectListener);
        buttonUnregister = (Button) findViewById(R.id.record_end);
        buttonUnregister.setOnClickListener(connectList);
        Thread thread = new Thread(null, repeat,"Background");
        thread.start();


    }

    private OnClickListener changeSetListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            numberOfSamplesSet++;
            numberOfSample = 0;
        }
    };



    private OnClickListener connectList = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (registered==1)
            {
                registered=0;
                handler.removeCallbacks(repeat);
            }
        }
    };
    private OnClickListener connectListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (registered==0)
            {
                registered=1;
                registerReceiver(receiver, new IntentFilter(
                        WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                handler.removeCallbacks(repeat);
                handler.postDelayed(repeat,1000);


            }
        }
    };



    protected void onResume() {
        super.onResume();
        handler.postDelayed(repeat,5000);
    }

    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(repeat);
    }
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(repeat);
    }

    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(repeat);
    }

    private void getRssi()
    {
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (receiver == null)
            receiver = new WiFiScanReceiverRec(this);
        mac=info.getMacAddress();
        wifi.startScan();
    }

    public String makeName()
    {
        String tempName="";
        tempName=mac.substring(0,2)+"-"+mac.substring(3, 5)+"-"+mac.substring(6, 8)+"-"+mac.substring(9, 11)+"-"+mac.substring(12, 14)+"-"+mac.substring(15, 17);
        tempName=tempName +"_"+ numberOfSamplesSet +".txt";
        tempName=tempName.replace(':', '_');
        tempName=tempName.replace(' ', '_');
        return tempName;
    }

    public void writeToFile(String s)
    {
        try
        {
            File root = Environment.getExternalStorageDirectory();
            if (root.canWrite())
            {
                filename = makeName();
                File file = new File(root+ File.separator +"wificlient" , filename);
                FileWriter writer = new FileWriter(file,true);
                BufferedWriter out = new BufferedWriter(writer);
                Log.e(TAG, "writeToFile");
                out.write(""+s);
                out.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not write file " + e.getMessage());
        }

    }



}
    
    
    
    
    
    
    
    
    
    
    
    


	