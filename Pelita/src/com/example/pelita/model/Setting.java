package com.example.pelita.model;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.LayoutParams;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.widget.IcsAdapterView;
import com.actionbarsherlock.internal.widget.IcsAdapterView.OnItemSelectedListener;
import com.actionbarsherlock.internal.widget.IcsLinearLayout;
import com.actionbarsherlock.internal.widget.IcsSpinner;
import com.actionbarsherlock.view.Menu;
import com.example.pelita.R;

import com.example.pelita.fragment.*;

public class Setting extends SherlockFragmentActivity {

	private String connection = "";

	private Context ctx;

	// private String[] mLocations;
	private TabHost mTabHost;
	Fragment lastfragment;

	int mContainerId;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(SampleList.THEME); // Used for theme switching in samples
		super.onCreate(savedInstanceState);
	

		setContentView(R.layout.setting);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mContainerId = R.id.realtabcontent;

		ctx = getSupportActionBar().getThemedContext();
		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(ctx,
				R.array.Connection, R.layout.sherlock_spinner_item);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		IcsSpinner spinner = new IcsSpinner(this, null,
				R.attr.actionDropDownStyle);

		spinner.setAdapter(list);
		spinner.setOnItemSelectedListener(new ListernerMenu());

		IcsLinearLayout listNavLayout = (IcsLinearLayout) getLayoutInflater()
				.inflate(R.layout.abs__action_bar_tab_bar_view, null);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.CENTER;

		listNavLayout.addView(spinner, params);
		listNavLayout.setGravity(Gravity.RIGHT); // <-- align the spinner to the
													// right

		getSupportActionBar().setCustomView(listNavLayout,
				new ActionBar.LayoutParams(Gravity.RIGHT));
		getSupportActionBar().setDisplayShowCustomEnabled(true);

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}

	}

	private class ListernerMenu implements OnItemSelectedListener {

		@Override
		public void onNothingSelected(IcsAdapterView<?> parent) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onItemSelected(IcsAdapterView<?> parent, View view,
				int position, long id) {

			Toast.makeText(
					ctx,
					"Item selected: "
							+ getResources().getStringArray(R.array.Connection)[position],
					Toast.LENGTH_SHORT).show();

			invalidateOptionsMenu();

			connection = getResources().getStringArray(R.array.Connection)[position];

			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();

			Fragment fragment;
			if (lastfragment != null) {
				Log.d("setting", "detach");
				ft.detach(lastfragment);
			}
			if (connection.toString().equals("Internet")) {

				Log.d("Setting", "Internet");
				fragment = Fragment.instantiate(getApplication(),
						WifiiFrag.CountingFragment.class.getName(), null);

				ft.add(mContainerId, fragment, "custom");

			} else {
				Log.d("Setting", "bluetooth");
				fragment = Fragment.instantiate(getApplication(),
						BluetoothFrag.BlueFrag.class.getName(), null);
				ft.add(mContainerId, fragment, "simple");

			}

			if (fragment != null) {
				// ft.attach(fragment);
				ft.commit();
				Log.d("setting", "last");
				lastfragment = fragment;
			}

		}

	}



	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}

}