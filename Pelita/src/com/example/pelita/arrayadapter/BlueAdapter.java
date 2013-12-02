package com.example.pelita.arrayadapter;


import java.util.ArrayList;

import com.example.pelita.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BlueAdapter extends ArrayAdapter<String> {
        public BlueAdapter(Context context, int textViewResourceId,
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
                        view = vi.inflate(R.layout.device_name, null);
                }
                TextView nama = (TextView) view
                                .findViewById(R.id.text);
                nama.setText(List.get(position));
                
                return view;
        }
}