package com.example.pelita.kelas;



import com.example.pelita.model.listener.ClickListener;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageButton;

public class NewButton extends ImageButton {
	private ClickListener clk = null;
	private String tag;
	
	public NewButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public NewButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NewButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public void setOnClickListener(ClickListener clk,String tag) {
	        this.clk = clk;
	        this.tag=tag;
	    }


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction()==1){
			if(clk!=null){
				clk.onClick(this, tag);
			}
		
		}
		return super.onTouchEvent(event);
	}

}
