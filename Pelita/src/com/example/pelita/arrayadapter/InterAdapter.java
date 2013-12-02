package com.example.pelita.arrayadapter;


import java.util.ArrayList;

import com.example.pelita.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class InterAdapter extends ArrayAdapter<String> {
	private TextView nama,pass;
	private String[] tampung;
	public InterAdapter(Context context, int textViewResourceId,
			ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.List = objects;
	}

	private ArrayList<String> List;
	
	@Override
	public View getView(int position, View Convert, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = Convert;
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.device_nameintr, null);
		}
		 nama = (TextView) view
				.findViewById(R.id.text);
		 pass = (TextView) view
				.findViewById(R.id.pass);
		 tampung=List.get(position).split("@");
		nama.setText(tampung[0]);
		pass.setText(tampung[1]);
		return view;
	}
}
