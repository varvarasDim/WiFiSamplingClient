package com.android.demo.wifisampling;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.demo.wifisampling.R;

public class WiFiSamplingNav extends Activity implements OnClickListener
{
    private String TAG = this.getClass().getSimpleName();

	private static final int ACTIVITY_RECORD=10;
	private static final int ACTIVITY_RECNTRAN=11;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        WiFiSamplingNav.context=getApplicationContext();
        setContentView(R.layout.wifipos);
        setTitle(R.string.posnav);

        Button recorddata = (Button) findViewById(R.id.recorddata);
        Button recntran = (Button) findViewById(R.id.recntran);
        

        recorddata.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
    		Log.d(TAG, "recording");
        		RecordData();
          }
    	});
        
        recntran.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
    		Log.d(TAG, "recording n transmiting");
        		RecordNTransmit();
          }
    	});

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) 
    {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    private void RecordNTransmit()
    {
		Log.d(TAG, "record n transmit");
        Intent i = new Intent(this, RecordNTransmit.class);
        startActivityForResult(i, ACTIVITY_RECNTRAN);
    	   
    }
    
    private void RecordData()
    {
		Log.d(TAG, "record data");
        Intent i = new Intent(this, RecordData.class);
        startActivityForResult(i, ACTIVITY_RECORD);
    	   
    }
    
   	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
