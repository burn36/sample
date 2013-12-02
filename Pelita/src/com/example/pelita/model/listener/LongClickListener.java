package com.example.pelita.model.listener;

import android.view.View;
import android.view.View.OnLongClickListener;



public interface LongClickListener extends OnLongClickListener{
	public void onLongClick(View view, 
          String id);
	@Override
	public boolean onLongClick(View v);
	
}