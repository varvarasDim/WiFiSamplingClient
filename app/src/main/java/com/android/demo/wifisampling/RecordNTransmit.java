package com.android.demo.wifisampling;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.net.InetAddress;
import java.net.Socket;
import android.app.Activity;
import java.util.Date;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;
import android.os.Handler;
import android.widget.*;

public class RecordNTransmit extends Activity 
{
	private Integer numberOfSample = 0;
	private String sendCode ="";

    public void setSendCode(String sc)
    {
        this.sendCode = sc;
    }

    private String TAG = this.getClass().getSimpleName();

	final Runnable repeat= new Runnable() {
		@Override
		public void run() {
			getRssi();

			handler.postDelayed(this, 5000);
			if (!(sendCode.equalsIgnoreCase("")))
			{
				numberOfSample++;
				notifyServer(sendCode);
				sendCode ="";
			}
			handler.post(mUpdateResults);
		}
	};

    private TextView Add;
	private static Context context;
	private WifiManager wifi;
    public WifiManager getWifi()
    {
        return this.wifi;
    }
	private BroadcastReceiver receiver;
	private String mac="";
    private Button buttonRegister;  
    private int registered=0;
    private Handler handler = new Handler();
    private Button buttonUnregister;

	private String serverIpAddress="";

	Runnable mUpdateResults = new Runnable() {
        public void run() {
            Date date;
            date=new Date();
        		Add.setText("Time: "+date.getHours()+":" + date.getMinutes() +":" + date.getSeconds()+" Number Of Sample: "+numberOfSample);
        }
    };

    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        RecordNTransmit.context=getApplicationContext();
        setContentView(R.layout.sendsamples);
        Add=(TextView) findViewById(R.id.addT);
        buttonRegister = (Button) findViewById(R.id.recordT_start);
        buttonRegister.setOnClickListener(connectListener);
        buttonUnregister = (Button) findViewById(R.id.recordT_end);
        buttonUnregister.setOnClickListener(connectList);
     	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

      	serverIpAddress=prefs.getString("serverIpAddress", "");

        Thread thread = new Thread(null, repeat,"Background");
        thread.start();
    }

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
			receiver = new WiFiScanReceiverRnT(this);
     	mac=info.getMacAddress();
        String tempMac=mac.substring(0,2)+"-"+mac.substring(3, 5)+"-"+mac.substring(6, 8)+"-"+mac.substring(9, 11)+"-"+mac.substring(12, 14)+"-"+mac.substring(15, 17);
        mac=tempMac;
    	wifi.startScan();
    }

    public void notifyServer(String data)
    {
        try {
            InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
            Log.d(TAG, "Connecting to server");
            Socket socket = new Socket(serverAddr, 8731);
            CodeWord cw=new CodeWord();
            String code="SAMPLE"+"##"+mac+"##"+data;
            try
            {
          	  cw.setCodeWord(code);
          	  cw.sendCodeWord(socket);
          	  cw.receiveCodeWord(socket);

          	  if (cw.getCodeWord().equalsIgnoreCase("SAMPLE_OK"))
          	  {  socket.close();  }

            }
           	catch (Exception e) {
                Log.e(TAG, "Error in communication", e);
            }
            socket.close();
            Log.d(TAG, "Closed socket error");
        } catch (Exception e) {
            Log.e(TAG, "General Error", e);
        }
    }
	
}