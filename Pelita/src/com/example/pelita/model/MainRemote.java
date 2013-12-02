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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.app.SherlockActivity;
import com.example.pelita.R;
import com.example.pelita.arrayadapter.DeviceListAdapter;
import com.example.pelita.database.DatabaseDevName;
import com.example.pelita.database.DatabaseDevStatus;
import com.example.pelita.fragment.BluetoothFrag;
import com.example.pelita.fragment.WifiiFrag;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import android.widget.ListView;
import android.widget.Toast;

/**
 * This is the main Activity that displays the current chat session.
 */
@SuppressLint("NewApi")
public class MainRemote extends SherlockActivity {
	// Debugging
	private static final String TAG = "BluetoothChat";
	private static final boolean D = true;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	public static final int Timer = 212;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";
	public static final String DelArray = "deleteArray";
	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	// Layout Views
	// private TextView mTitle;
	private ListView mConversationView;

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Array adapter for the conversation thread
	private DeviceListAdapter mConversationArrayAdapter;
	private ArrayList<String> mConversationArrayList;
	// String buffer for outgoing messages

	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BluetoothChatService mChatService = null;
	private HashMap<String, TimerTask> ArrTask;
	private Timer t;
	private TimerTask Mtask;
	private DatabaseDevStatus ds;
	private DatabaseDevName d;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(SampleList.THEME); // Used for theme switching in samples
		
		if (D)
			Log.e(TAG, "+++ ON CREATE +++");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.text);

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		ArrTask = new HashMap<String, TimerTask>();
		ds = new DatabaseDevStatus(getApplicationContext());
	}

	private void syncDeviceIntr(final String id) {
		t = new Timer();

		Mtask = new TimerTask() {

			@Override
			public void run() {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ds.SynDev(id);
					}
				});

			}
		};
		ArrTask.put(id, Mtask);

		t.schedule(Mtask, 0, 5000);
	}

	private void syncDeviceBlue(final String id) {
		t = new Timer();

		Mtask = new TimerTask() {

			@Override
			public void run() {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						sendMessage("s");
					}
				});

			}
		};
		ArrTask.put(id, Mtask);

		t.schedule(Mtask, 0, 5000);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (D)
			Log.e(TAG, "++ ON START ++");

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			if (mChatService == null) {
				setupChat();
			}
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		if (D)
			Log.e(TAG, "+ ON RESUME +");

		if (mChatService != null) {
			if (D)
				Log.d(TAG, "mChatService != null");

			if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
				// Start the Bluetooth chat services
				if (D)
					Log.d(TAG, "mChatService.start()");
				mChatService.start();
			}
		}
	}

	private void setupChat() {
		Log.d(TAG, "setupChat()");

		mConversationView = (ListView) findViewById(R.id.in);
		mConversationArrayList = new ArrayList<String>();

		mConversationArrayAdapter = new DeviceListAdapter(this, R.id.in,
				mConversationArrayList);

		mConversationView.setAdapter(mConversationArrayAdapter);

		mChatService = new BluetoothChatService(this, mHandler);

	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		if (D)
			Log.e(TAG, "- ON PAUSE -");
	}

	@Override
	public void onStop() {
		super.onStop();
		if (D)
			Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null)
			mChatService.stop();
		if (D)
			Log.e(TAG, "--- ON DESTROY ---");

		for (TimerTask as : ArrTask.values()) {
			as.cancel();
		}
	}

	private void ensureDiscoverable() {
		if (D)
			Log.d(TAG, "ensure discoverable");
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	public void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, "not connected", Toast.LENGTH_SHORT).show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			mChatService.write(send);

			// Reset out string buffer to zero and clear the edit text field

		}
	}

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (D)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
					Log.d("connected", mConnectedDeviceName);

					/*
					 * mTitle.setText("connected to");
					 * mTitle.append(mConnectedDeviceName);
					 */

					mConversationArrayList.add(mConnectedDeviceName);
					//syncDeviceBlue(mConnectedDeviceName);
					d = new DatabaseDevName(getApplicationContext());
					if (!d.cek_id(mConnectedDeviceName)) {
						Toast.makeText(getApplicationContext(),
								"insert to database", Toast.LENGTH_SHORT)
								.show();
						d.InsertDev_blue(mConnectedDeviceName);
					}

					mConversationArrayAdapter.notifyDataSetChanged();
					break;
				case BluetoothChatService.STATE_CONNECTING:
					/* mTitle.setText("coonecting"); */
					break;
				case BluetoothChatService.STATE_LISTEN:
				case BluetoothChatService.STATE_NONE:
					/* mTitle.setText("not connected"); */
					mConversationArrayList.remove(mConnectedDeviceName);
					if (ArrTask.containsValue(mConnectedDeviceName)) {
						ArrTask.get(mConnectedDeviceName).cancel();
					}
					mConversationArrayAdapter.notifyDataSetChanged();
					break;
				}
				break;

			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				Log.v("READ", readMessage);
				if(readMessage.length()<10){
					setStatus(readMessage);
				}else{
					SynCron(readMessage);
				}
				
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);

				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D)
			Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (data != null) {
				if (resultCode == Activity.RESULT_OK) {
					if (data.hasExtra(BluetoothFrag.EXTRA_DEVICE_ADDRESS)) {
						// Get the device MAC address
						String address = data.getExtras().getString(
								BluetoothFrag.EXTRA_DEVICE_ADDRESS);
						// Get the BLuetoothDevice object
						BluetoothDevice device = mBluetoothAdapter
								.getRemoteDevice(address);
						// Attempt to connect to the device
						mChatService.connect(device);

					} else if (data
							.hasExtra(WifiiFrag.EXTRA_DEVICE_ADDRESS_Internet)) {

						String[] tampung = data
								.getExtras()
								.getString(
										WifiiFrag.EXTRA_DEVICE_ADDRESS_Internet)
								.split("@");
						String id = tampung[0], pass = tampung[1];
						d = new DatabaseDevName(getApplicationContext());
						if (!d.cek_id(id)) {
							d.InsertDev_wifii(id, pass);
						}
						if (!mConversationArrayList
								.contains(data
										.getExtras()
										.getString(
												WifiiFrag.EXTRA_DEVICE_ADDRESS_Internet))) {

							mConversationArrayList
									.add(data
											.getExtras()
											.getString(
													WifiiFrag.EXTRA_DEVICE_ADDRESS_Internet));
							mConversationArrayAdapter.notifyDataSetChanged();
							syncDeviceIntr(id);
						}

						Log.d("asdas",
								data.getExtras()
										.getString(
												WifiiFrag.EXTRA_DEVICE_ADDRESS_Internet));
					}
				}
				if (data.hasExtra(DelArray)) {
					Log.d("delarray", "sd");
					String del = data.getExtras().getString(DelArray);

					String id[] = del.split("/");
					for (int i = 0; id.length > i; i++) {
						ArrTask.get(id[i]).cancel();
						mConversationArrayList.remove(id[i]);

					}
					mConversationArrayAdapter.notifyDataSetChanged();
				}
			}

			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				setupChat();
			} else {
				// User did not enable Bluetooth or an error occured
				Log.d(TAG, "BT not enabled");
				Toast.makeText(this, "bluettooth not active",
						Toast.LENGTH_SHORT).show();
				finish();
			}
			break;
		case Timer:
			if (resultCode == Activity.RESULT_OK) {
				Bundle b = data.getExtras();
				Log.d("timer", "t");
				Log.d("asd", b.getString("id"));
				Log.d("blue", b.getString("id").split("@").length + "");
				if (b.getString("id").split("@").length < 2) {
					Log.d("blue", b.getString("message"));
					sendMessage(b.getString("message"));

				} else {
					Log.d("inter", b.getString("message"));
					mConversationArrayAdapter.sendDev(
							b.getString("id").split("@")[0],
							b.getString("message"));
				}
			}

			break;
		}

	}
	private void SynCron(String status) {
		Log.d(TAG, "setStatus " + status);
		//Toast.makeText(getApplicationContext(), "setStatus " + status,Toast.LENGTH_SHORT).show();
		// TextView addTextStatus = (TextView) findViewById(R.id.toggleButton1);

		if (status.length() >= 10) {
			setStatus(status.substring(0,2));
			setStatus(status.substring(2,4));
			setStatus(status.substring(4,6));
			setStatus(status.substring(6,8));
			setStatus(status.substring(8,10));

		}
	}

	private void setStatus(String status) {
		Log.d(TAG, "setStatus " + status);
		//Toast.makeText(getApplicationContext(), "setStatus " + status,Toast.LENGTH_SHORT).show();
		// TextView addTextStatus = (TextView) findViewById(R.id.toggleButton1);

		if (status.length() >= 2) {
			char[] ops;
			ops = status.toCharArray();
			switch (ops[0]) {
			case '1':
				if (ops[1] == '0') {
					ds.UpdateDev1(mConnectedDeviceName, 0);
				} else {
					ds.UpdateDev1(mConnectedDeviceName, 1);
				}
				break;
			case '2':
				if (ops[1] == '0') {
					ds.UpdateDev2(mConnectedDeviceName, 0);
				} else {
					ds.UpdateDev2(mConnectedDeviceName, 1);
				}
				break;
			case '3':
				if (ops[1] == '0') {
					ds.UpdateDev3(mConnectedDeviceName, 0);
				} else {
					ds.UpdateDev3(mConnectedDeviceName, 1);
				}
				break;
			case '4':
				if (ops[1] == '0') {
					ds.UpdateDev4(mConnectedDeviceName, 0);
				} else {
					ds.UpdateDev4(mConnectedDeviceName, 1);
				}
				break;
			case '5':
				if (ops[1] == '0') {
					ds.UpdateDev5(mConnectedDeviceName, 0);
				} else {
					ds.UpdateDev5(mConnectedDeviceName, 1);
				}
				break;

			default:
				break;
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Refresh")
				.setIcon(R.drawable.refresh)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		menu.add("add_device")
				.setIcon(R.drawable.hardware_dock)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		
		if (item.getTitle().toString().equals("Refresh")) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					sendMessage("s");
				}
			});
			mConversationArrayAdapter.notifyDataSetChanged();// ensureDiscoverable();
		} else if (item.getTitle().toString().equals("add_device")) {
			Log.d("main","add device");
			Intent serverIntent = new Intent(this, Setting.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
		}
		return super.onOptionsItemSelected(item);
	}

}