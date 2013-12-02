package com.example.pelita.json;


import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class URLHttpCheck {
	private String Url=koneksi.url,Strerror="";
	private int timeoutConnection = koneksi.TimeOut, timeoutSocket = koneksi.TimeOut;
	private HttpParams httpParameters = new BasicHttpParams();
	private DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
	private HttpGet httpGet = null;
	private boolean error =false;
	private Context ctx=null;
	
	public URLHttpCheck(Context ctx){
		this.ctx=ctx;
	}
	public URLHttpCheck(String URL,Context ctx) {
		this.ctx=ctx;
		Url = Url+URL;
		httpGet = new HttpGet(Url);
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
	
	}
	public boolean getError(){
		return error;
	}
	public String getStrE(){
		return Strerror;
	}
	public boolean isNetworkAvailable() {
		boolean x = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		// NetworkInfo activeNetworkInfo =
		// connectivityManager.getActiveNetworkInfo();
		if (connectivityManager.getActiveNetworkInfo() != null)
			if (connectivityManager.getActiveNetworkInfo().isConnected()) {
				x = true;
			}
		
		return x;
	}
	@SuppressWarnings("finally")
	public boolean getStatus(){
		boolean reply=false;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			if (response!=null) {
				reply=true;
			}
		
		} catch (ClientProtocolException e) {
			Strerror="Connection Error";
			error=true;
			e.printStackTrace();
	
		} catch (IOException e) {
			Strerror="System Error";
			error=true;

			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			Log.d("URLCHECK","reply");
			return reply;
		}
		
		
	}
	
	
	
}
