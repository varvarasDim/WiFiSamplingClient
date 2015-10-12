package com.android.demo.wifisampling;
import android.content.Context;
import android.content.SharedPreferences;
import java.io.File;
import android.preference.PreferenceManager;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.util.Log;
import android.widget.*;

import com.android.demo.wifisampling.R;

public class WiFiSampling extends ListActivity implements OnClickListener
{
	private String TAG = this.getClass().getSimpleName();
    private static final int ACTIVITY_WFPOSNAV=0;
    private static Context context;
    private String serverIpAddress = "";
    private Button conButton;
    private EditText serverAddressText;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        boolean appFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +"wificlient").mkdir();
        setContentView(R.layout.splash);
        conButton = (Button) findViewById(R.id.connect);
        conButton.setOnClickListener(this);
        serverAddressText = (EditText) findViewById(R.id.serverip);
		Log.d(TAG, "onCreate");
        registerForContextMenu(getListView());
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
    {
        super.onActivityResult(requestCode, resultCode, intent);

        switch(requestCode) 
        {
            case ACTIVITY_WFPOSNAV:

            	Log.d(TAG,"Return from wifiPosNav");
                break;
        }
    }
	public void onClick(View view) 
	{  
		if (view.getId() == R.id.connect)
		{
	        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
	        SharedPreferences.Editor editor = preferences.edit();
	        
	        serverIpAddress = serverAddressText.getText().toString();
	        editor.putString("serverIpAddress", serverIpAddress);
	        editor.commit();

            Intent i = new Intent(this, WiFiSamplingNav.class);
            startActivityForResult(i, ACTIVITY_WFPOSNAV);

		}

		
	}

		
}

