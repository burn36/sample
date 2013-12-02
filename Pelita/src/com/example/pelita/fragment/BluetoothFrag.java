/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.example.pelita.fragment;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.pelita.R;
import com.example.pelita.arrayadapter.BlueAdapter;
import com.example.pelita.model.SampleList;
import com.example.pelita.model.Setting;
import android.content.IntentFilter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class BluetoothFrag extends SherlockFragmentActivity {
	int mStackLevel = 1;
	public static String EXTRA_DEVICE_ADDRESS = "device_address";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(SampleList.THEME); // Used for theme switching in samples
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_stack);

		// Watch for button clicks.
		Button button = (Button) findViewById(R.id.new_fragment);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				addFragmentToStack();
			}
		});

		if (savedInstanceState == null) {
			// Do first time initialization -- add initial fragment.
			Fragment newFragment = BlueFrag.newInstance(mStackLevel);
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.add(R.id.simple_fragment, newFragment).commit();
		} else {
			mStackLevel = savedInstanceState.getInt("level");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("level", mStackLevel);
	}

	void addFragmentToStack() {
		mStackLevel++;

		// Instantiate a new fragment.
		Fragment newFragment = BlueFrag.newInstance(mStackLevel);

		// Add the fragment to the activity, pushing this transaction
		// on to the back stack.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.simple_fragment, newFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		menu.add("refresh")
				.setIcon(R.drawable.refresh)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);

	}

	public static class BlueFrag extends FragmentNew {
		int mNum;
		static final int POPULATE_ID = Menu.FIRST;
		private static final int REQUEST_CONNECT_DEVICE = 1;
	
		private BluetoothAdapter mBtAdapter;
		private boolean mBtCancel=false;
		private BlueAdapter mAdapterNew;
		private ArrayList<String> ListData;
		private ArrayList<String> ListNewdata;
		private ListView pairedListView, newDevicesListView;
		private View v;
		private int ScreenY = 0;

		/**
		 * Create a new instance of CountingFragment, providing "num" as an
		 * argument.
		 */
		static BlueFrag newInstance(int num) {
			BlueFrag f = new BlueFrag();

			// Supply num input as an argument.
			Bundle args = new Bundle();
			args.putInt("num", num);
			f.setArguments(args);

			return f;
		}

		/**
		 * When creating, retrieve this instance's number from its arguments.
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mNum = getArguments() != null ? getArguments().getInt("num") : 1;

		}

		/**
		 * The Fragment's UI is just a simple text view showing its instance
		 * number.
		 */

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			v = inflater.inflate(R.layout.bluetooth, container, false);
			ScreenY = getActivity().getWindowManager().getDefaultDisplay()
					.getHeight();
			// bluetooth
			ListData=new ArrayList<String>();
			ListNewdata=new ArrayList<String>();
		/*	mPairedDevicesArrayAdapter = new ArrayAdapter<String>(getActivity()
					.getApplicationContext(), R.layout.device_name);*/
			
			
		/*	mNewDevicesArrayAdapter = new ArrayAdapter<String>(getActivity()
					.getApplicationContext(), R.layout.device_name);
*/
			// Find and set up the ListView for paired devices
			pairedListView = (ListView) v.findViewById(R.id.paired_devices);
		//	pairedListView.setAdapter(mPairedDevicesArrayAdapter);
			pairedListView.setAdapter(new BlueAdapter(getActivity(), R.id.paired_devices, ListData));
			
			pairedListView.setOnItemClickListener(mDeviceClickListener);
			// Find and set up the ListView for newly discovered devices
			newDevicesListView = (ListView) v.findViewById(R.id.new_devices);
			mAdapterNew =new BlueAdapter(getActivity(),  R.id.paired_devices, ListNewdata);
			newDevicesListView.setAdapter(mAdapterNew);
			newDevicesListView.setOnItemClickListener(mDeviceClickListener);

			// Register for broadcasts when a device is discovered
			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			getActivity().registerReceiver(mReceiver, filter);

			// Register for broadcasts when discovery has finished
			filter = new IntentFilter(
					BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			getActivity().registerReceiver(mReceiver, filter);

			// Get the local Bluetooth adapter
			mBtAdapter = BluetoothAdapter.getDefaultAdapter();

			// Get a set of currently paired devices
			Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

			// If there are paired devices, add each one to the ArrayAdapter
			if (pairedDevices.size() > 0) {
				v.findViewById(R.id.title_paired_devices).setVisibility(
						View.VISIBLE);
				for (BluetoothDevice device : pairedDevices) {
					ListData.add(device.getName() + "\n"
							+ device.getAddress());
				}
			} else {
				String noDevices = getResources().getText(R.string.none_paired)
						.toString();
				ListData.add(noDevices);
			}
			setHasOptionsMenu(true);
			return v;
		}

		@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			super.onAttach(activity);
			Log.d("getTitle", activity.getTitle() + "");
			Log.d("getComponentName", activity.getComponentName() + "");
			Log.d("activity", activity + "");

			Setting s = (Setting) activity;
	

		}

		private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View v, int arg2,
					long arg3) {
				// Cancel discovery because it's costly and we're about to
				// connect
				mBtAdapter.cancelDiscovery();

				// Get the device MAC address, which is the last 17 chars in the
				// View
				if (v == null) {
					LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = vi.inflate(R.layout.device_name, null);
				}
				TextView nama = (TextView) v
						.findViewById(R.id.text);
				String info = (String) nama.getText();
				String address = info.substring(info.length() - 17);

				Log.d("listener", info);
				Log.d("listener2", address);
				// Create the result Intent and include the MAC address
				Intent intent = new Intent();
				intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
				
				// Set result and finish this Activity
				 getActivity().setResult(Activity.RESULT_OK, intent);
				 getActivity().finish();
			}
		};
		public void onDestroy() {
			 super.onDestroy();
			 	// Make sure we're not doing discovery anymore
		        if (mBtAdapter != null) {
		            mBtAdapter.cancelDiscovery();
		        }

		        // Unregister broadcast listeners
		        getActivity().unregisterReceiver(mReceiver);
		};
		private void doDiscovery() {
			Log.d("Do", "doDiscovery()");

			// Indicate scanning in the title
			// setProgressBarIndeterminateVisibility(true);
			// setTitle(R.string.scanning);
			int px = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 34, getResources()
							.getDisplayMetrics());
			pairedListView.setLayoutParams(new LinearLayout.LayoutParams(-2,
					(ScreenY / 2) - (15 + px)));
			// newDevicesListView.setLayoutParams(new
			// LinearLayout.LayoutParams(-2, (ScreenX/2)-(15)));

			// Turn on sub-title for new devices
			v.findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

			// If we're already discovering, stop it
			if (mBtAdapter.isDiscovering()) {
			
				mBtAdapter.cancelDiscovery();
				
			}
			// Request discover from BluetoothAdapter
			mBtAdapter.startDiscovery();
			ListNewdata.clear();
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			// Place an action bar item for searching.

			menu.add("refresh")
					.setIcon(R.drawable.refresh)
					.setShowAsAction(
							MenuItem.SHOW_AS_ACTION_IF_ROOM
									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			Log.d("fragment", "menu");
			if (item.getTitle().equals("refresh")) {

				// Intent serverIntent = new Intent(this,
				// DeviceListActivity.class);
				// startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

				doDiscovery();
				Toast.makeText(getActivity(), "refresh pressed",
						Toast.LENGTH_LONG).show();
			}
			return super.onOptionsItemSelected(item);
		}

		private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
			@Override
			
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
			
				// When discovery finds a device
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// Get the BluetoothDevice object from the Intent
					
					BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				
					// If it's already paired, skip it, because it's been listed
					// already
					if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
						ListNewdata.remove(getResources().getText(
								R.string.none_found).toString());
						ListNewdata.add(device.getName() + "\n"
								+ device.getAddress());
						mAdapterNew.notifyDataSetChanged();
					}
					// When discovery is finished, change the Activity title
				} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
						.equals(action)) {
					// setProgressBarIndeterminateVisibility(false);
					// setTitle(R.string.select_device);
					
					if (ListNewdata.size() == 0) {
						String noDevices = getResources().getText(
								R.string.none_found).toString();
						ListNewdata.add(noDevices);
					}
					mAdapterNew.notifyDataSetChanged();
				}
				
			}
		};
	}

}
