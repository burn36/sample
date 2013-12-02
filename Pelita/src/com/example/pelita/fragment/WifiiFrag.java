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


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemLongClickListener;
import android.view.View.OnClickListener;

import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.pelita.R;
import com.example.pelita.arrayadapter.InterAdapter;
import com.example.pelita.database.DatabaseDevIntr;
import com.example.pelita.database.DatabaseDevName;
import com.example.pelita.json.JSONcekId;
import com.example.pelita.model.DeviceListActivity;
import com.example.pelita.model.MainRemote;
import com.example.pelita.model.SampleList;
import com.example.pelita.model.Setting;
import android.widget.AdapterView;

import android.widget.AdapterView.OnItemClickListener;

public class WifiiFrag extends SherlockFragmentActivity {
	int mStackLevel = 1;

	public static String EXTRA_DEVICE_ADDRESS_Internet = "device_address_internet";
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
			Fragment newFragment = CountingFragment.newInstance(mStackLevel);
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
		Fragment newFragment = CountingFragment.newInstance(mStackLevel);

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

	public static class CountingFragment extends FragmentNew {
		int mNum;
		private static final int REQUEST_CODE = 1;
		static final int POPULATE_ID = Menu.FIRST;
		private View v;
		private String del;
		private ArrayList<String> ListData;
		private ListView pairedListView;
		private InterAdapter	madapter;
		/**
		 * Create a new instance of CountingFragment, providing "num" as an
		 * argument.
		 */
		static CountingFragment newInstance(int num) {
			CountingFragment f = new CountingFragment();

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
			v = inflater.inflate(R.layout.wifii, container, false);
			del="";
			ListData = new ArrayList<String>();
			pairedListView = (ListView) v.findViewById(R.id.paired_devices);
			// pairedListView.setAdapter(mPairedDevicesArrayAdapter);
			madapter=new InterAdapter(getActivity(),
					R.id.paired_devices, ListData);
			pairedListView.setAdapter(madapter);
			pairedListView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					 View v=arg1;
						if (v == null) {
        					LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        					v = vi.inflate(R.layout.device_nameintr, null);
        				}
						TextView nama = (TextView) v
        						.findViewById(R.id.text);
						TextView pass = (TextView) v
								.findViewById(R.id.pass);
						final String info = (String) nama.getText(), pasword=(String) pass.getText();
					new AlertDialog.Builder(getActivity())
	                .setIcon(R.drawable.alert_dialog_icon)
	                .setTitle("Delete Device")
	                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                   Log.d("LongClick", info);	
	                   DatabaseDevIntr d =new DatabaseDevIntr(getActivity());
	                   d.DeleteDev_wifii(info);
	                   DatabaseDevName d2 =new DatabaseDevName(getActivity());
	                   d2.Delete(info, pasword);
	                   if(del.equals("")){
	                	   del=info+"@"+pasword;
	                	   Intent i = new Intent();
	                	   i.putExtra(MainRemote.DelArray, del);
	                	   getActivity().setResult(RESULT_CANCELED, i);
	                   }else{
	                	   del=del+"/"+info+"@"+pasword;; 
	                	   Intent i = new Intent();
	                	   i.putExtra(MainRemote.DelArray, del);
	                	   getActivity().setResult(RESULT_CANCELED, i);
	                   }
	                  
	                   UpdateList();
	                    }
	                }).setNegativeButton("Cancel",null)
	                .create().show();
				
					return false;
				}
			});
			pairedListView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> av, View v, int arg2,
						long arg3) {
					
					if (v == null) {
						LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						v = vi.inflate(R.layout.device_nameintr, null);
					}
					TextView nama = (TextView) v
							.findViewById(R.id.text);
					TextView pass = (TextView) v
							.findViewById(R.id.pass);
					String address=(String) nama.getText()+"@"+pass.getText();
					JSONcekId jc= new JSONcekId(getActivity());
					String result =jc.Cekid((String) nama.getText(), (String)pass.getText());
					if(result.equals("ok\n")){
						Intent intent = new Intent();
						intent.putExtra(EXTRA_DEVICE_ADDRESS_Internet, address);
						if(!del.equals("")){
							intent.putExtra(MainRemote.DelArray, del);
						}
						// Set result and finish this Activity
						 getActivity().setResult(Activity.RESULT_OK, intent);
						 getActivity().finish();
					}else{
						Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
					}
				
				
				
					// Create the result Intent and include the MAC address
					
				}
			});
				
			DatabaseDevIntr d =new DatabaseDevIntr(getActivity());
			
			ListData.addAll(d.getDevice_wifii());
			
			madapter.notifyDataSetChanged();
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

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			// Place an action bar item for searching.

			menu.add("plus")
					.setIcon(R.drawable.plus)
					.setShowAsAction(
							MenuItem.SHOW_AS_ACTION_IF_ROOM
									| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			Log.d("fragment", "plus");
			if (item.getTitle().equals("plus")) {

			
				Intent serverIntent = new Intent(getActivity(),
						DeviceListActivity.class);
				startActivityForResult(serverIntent, REQUEST_CODE);

				
			}
			return super.onOptionsItemSelected(item);
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			if(resultCode==RESULT_OK){
				if (data.hasExtra("die")) {
					UpdateList();
				}
			}
			super.onActivityResult(requestCode, resultCode, data);
		}

		public void UpdateList() {
			DatabaseDevIntr d =new DatabaseDevIntr(getActivity());
			ListData.clear();
			ListData.addAll(d.getDevice_wifii());
			madapter.notifyDataSetChanged();
		}
	}
	

}
