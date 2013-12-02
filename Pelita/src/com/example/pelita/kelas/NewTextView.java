package com.example.pelita.kelas;



import com.example.pelita.model.listener.LongClickListener;

import android.view.View;
import android.view.View.OnLongClickListener;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class NewTextView extends TextView implements OnLongClickListener {
	private LongClickListener LngClk;
	private String tag;
	
	public NewTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public NewTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NewTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setOnLongClickListener(LongClickListener l, String id) {
		LngClk=l;
		tag=id;
		this.setOnLongClickListener(this);
	}
	
	@Override
	public boolean onLongClick(View v) {
		Log.d("newText","longClick");
		LngClk.onLongClick(v, tag);
		return false;
	}
	
}
