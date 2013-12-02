package com.example.pelita.fragment;

import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;


public class FragmentNew extends SherlockFragment  {
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("fragment new", "menu");
		String itemTitle = (String) item.getTitle();
		Log.d("ITem", item.getTitle() + "");
		if (item.getItemId() == android.R.id.home) {
			Toast.makeText(getActivity(), "home pressed", Toast.LENGTH_LONG).show();
			getSherlockActivity().finish();
		}
	
		return super.onOptionsItemSelected(item);
	}
}
