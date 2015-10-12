package com.android.demo.wifisampling;

import java.util.Date;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;

public class WiFiScanReceiverRnT extends BroadcastReceiver 
{
    private String TAG = this.getClass().getSimpleName();
	private RecordNTransmit recordNTransmit;
	private String signalData="";
	public WiFiScanReceiverRnT(RecordNTransmit recNTran)
	{
		super();
		this.recordNTransmit = recNTran;
	}

  public void onReceive(Context c, Intent intent) 
  {
      List<ScanResult> results = recordNTransmit.getWifi().getScanResults();
      Date dd=new Date(); 
      for (ScanResult result : results)
          signalData=signalData+dd.toString()+";"+result.BSSID+";"+result.level+";"+result.frequency+";"+result.SSID+"%%";
      recordNTransmit.setSendCode(signalData);
      signalData="";
  }
 }
