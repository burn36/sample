package com.example.pelita.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
public class JSON {

	private JSONObject jObject;
	private String xResult = "";
	private String url = koneksi.url;
	private URLHttpCheck c;
	private Context ctx;
	public JSON(Context ctx) {
		this.ctx=ctx;
		
		c=new URLHttpCheck(koneksi.urlUpdateDev, ctx);
				
	}

	public String InsertTag(HashMap<String, String> data){
		String x = "gagal";

		if (c.isNetworkAvailable() && c.getStatus()) {
			try {
				x = postData(data);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{

		}
		return x;
		
	}
	public HashMap<String, Integer> getStatus(String id) {
		HashMap<String, Integer> x = new HashMap<String, Integer>();
		try {
			xResult = getRequest(url+koneksi.urlGetStatus+id);
			jObject = new JSONObject(xResult);
			JSONArray menuitemArray = jObject.getJSONArray("status");
			for (int i = 0; i < menuitemArray.length(); i++) {
				String string=menuitemArray.getJSONObject(i).getString("id_button");
				x.put("dev"+string.substring(string.length()-1),menuitemArray.getJSONObject(i).getInt("nama_status"));
			Log.d("dev"+string.substring(string.length()-1),menuitemArray.getJSONObject(i).getInt("nama_status")+"");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return x;
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

	@SuppressWarnings("finally")
	public String postData(HashMap<String, String> data) throws JSONException {
		// Create a new HttpClient and Post Header
		Log.d("JSON","postdata");
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url+koneksi.urlUpdateDev);
		JSONObject json = new JSONObject();
		String text = "", s;
		Iterator<String> Siterator;
		try {
			// JSON data:
			Siterator = data.keySet().iterator();
			while (Siterator.hasNext()) {
				s = Siterator.next();
				json.put(s, data.get(s));
			}
			JSONArray postjson = new JSONArray();
			postjson.put(json);

			// Post the data:
			httppost.setHeader("json", json.toString());
			httppost.getParams().setParameter("jsonpost", postjson);

			// Execute HTTP Post Request
			System.out.print(json);
			HttpResponse response = httpclient.execute(httppost);

			// for JSON:
			if (response != null) {
				InputStream is = response.getEntity().getContent();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				text = sb.toString();
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} finally {
			return text;
		}
	}

}