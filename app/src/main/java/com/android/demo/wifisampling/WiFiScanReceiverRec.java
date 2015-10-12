package com.android.demo.wifisampling;

import java.util.Date;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;

public class WiFiScanReceiverRec extends BroadcastReceiver 
{
    private String TAG = this.getClass().getSimpleName();
	private RecordData recordData;
	private String signalData="";
	public WiFiScanReceiverRec(RecordData recData)
	{
		super();
		this.recordData = recData;
	}
  public void onReceive(Context c, Intent intent)
  {
      List<ScanResult> results = recordData.getWifi().getScanResults();
      Date date=new Date();
      for (ScanResult result : results)
      {
          signalData=signalData+" "+date.toString()+";"+result.BSSID+";"+result.level+";"+result.frequency+";"+result.SSID+"\n";
      }
      recordData.setSendCode(signalData);
      signalData="";
  }
 }
