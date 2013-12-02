package com.example.pelita.json;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;

public class JSONcekId {


	private String xResult = "";
	private String url = koneksi.url;
	private URLHttpCheck c;
	private Context ctx;

	public JSONcekId(Context ctx) {
		this.ctx = ctx;
		c = new URLHttpCheck(koneksi.urlCekid, ctx);
	}

	private String decodeUtf8(String ss) {
		String x = null;
		try {
			x = URLDecoder.decode(ss, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return x;
	}

	public String getRequest(String Url) {

		String sret = "";
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(Url);
		try {
			HttpResponse response = client.execute(request);
			sret = request(response);
		} catch (Exception ex) {
		}
		return sret;

	}

	/**
	 * Method untuk Menenrima data dari server
	 * 
	 * @param response
	 * @return
	 */
	public static String request(HttpResponse response) {
		String result = "";
		try {
			InputStream in = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			StringBuilder str = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				str.append(line + "\n");
			}
			in.close();
			result = str.toString();
		} catch (Exception ex) {
			result = "Error";
		}
		return result;
	}

	public String Cekid(String id, String pass) {
		
		if (c.isNetworkAvailable() && c.getStatus()) {
			xResult = getRequest(url +koneksi.urlCekid+"?id=" + id + "&pass="
					+ pass);
		}else{
			xResult="Network Unavailable";
		}

		return xResult;

	}

	/**
	 * Method untuk Menenrima data dari server
	 * 
	 * @param response
	 * @return
	 */

}