/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.pelita.model;

import java.util.Set;

import com.example.pelita.R;
import com.example.pelita.database.DatabaseDevIntr;
import com.example.pelita.json.JSONcekId;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This Activity appears as a dialog. It lists any paired devices and
 * devices detected in the area after discovery. When a device is chosen
 * by the user, the MAC address of the device is sent back to the parent
 * Activity in the result Intent.
 */
@SuppressLint("NewApi")
public class DeviceListActivity extends Activity {
    // Debugging
    private static final String TAG = "DeviceListActivity";
    private static final boolean D = true;

    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    // Member fields
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the window
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.dia);
       
        Button OkBtn = (Button) findViewById(R.id.btnOk);
        OkBtn.setOnClickListener(new OnClickListener() {
		EditText id = (EditText) findViewById(R.id.id);	
		EditText pass = (EditText) findViewById(R.id.pass);	
			@Override
			public void onClick(View v) {
				if(id.getText().toString().equals("")||pass.getText().toString().equals("")){
					Toast.makeText(getApplication(), "Data null", Toast.LENGTH_LONG).show();
				}else{
					JSONcekId jc= new JSONcekId(getApplicationContext());
					String result =jc.Cekid(id.getText().toString(),pass.getText().toString());
					if(result.equals("ok\n")){
						 Intent  i = new Intent(); 
						 DatabaseDevIntr d = new DatabaseDevIntr(getApplicationContext());
						 d.InsertDev_wifii(id.getText().toString(),pass.getText().toString());
							i.putExtra("die", id.getText().toString()+" \n "+pass.getText().toString());
							setResult(RESULT_OK,i);
						finish();
					}else{
						Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
					}
					
				}
				
				
			}
		});
        Button Cancel = (Button) findViewById(R.id.btnCancel);
        Cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        setResult(RESULT_CANCELED);
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);

        // Turn on sub-title for new devices
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);



    }



}
