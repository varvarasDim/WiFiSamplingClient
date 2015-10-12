package com.android.demo.wifisampling;

import java.io.*;
import java.net.*;
import android.util.Log;

public class CodeWord {
    private String TAG = this.getClass().getSimpleName();
	private String code;

	public CodeWord()
	{
		this.code="";
	}
	public String getCodeWord()
	{
		return this.code;
	}
	public void setCodeWord(String cw)
	{
		this.code=cw;
	}
    
    public void receiveCodeWord(Socket sock)
    {
        try {
           	BufferedReader in
                  = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            code=in.readLine();

          } catch (Exception e) {
              Log.e(TAG, "Error in receiving codeword ", e);
          }
    }
    
    public void sendCodeWord(Socket sock)
    {
        try
        {
            PrintStream out = new PrintStream(sock.getOutputStream());
                            out.println(code);

       

        } 
        catch (IOException ioe) 
        {
          Log.e(TAG, "Error in sending codeword ", ioe);
          ioe.printStackTrace();
        }
    }
    
}
