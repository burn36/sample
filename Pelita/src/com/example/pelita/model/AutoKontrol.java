package com.example.pelita.model;

import java.util.Calendar;

import com.example.pelita.R;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class AutoKontrol extends Activity {

	private	TextView textAlarmPrompt;
	private	int menit;
	private	char kirim[];
	private	char charArray[];
	private	String skirim;
	private	int check = 0;
	private int Prkt;
	private String id;

	private	TimePickerDialog timePickerDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bn = getIntent().getExtras();
		if (bn != null) {
			Prkt=bn.getInt("prk");
			id=bn.getString("id");
			setTitle("Perangkat : "+id.split("@")[0]+" : "+Prkt);
		}
		
		setContentView(R.layout.autokontrol);
		textAlarmPrompt = (TextView) findViewById(R.id.getTime);
		Button back = (Button) findViewById(R.id.save);
		back.setText("Save");
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("waktu sekarang", "panjang check" + check);
				if (check != 0) {
					Intent autokon2 = new Intent();
					autokon2.putExtra("message", skirim);
					autokon2.putExtra("id", id);
					setResult(Activity.RESULT_OK, autokon2);
					finish();
				} else {
					Intent autokon2 = new Intent();
					setResult(Activity.RESULT_CANCELED, autokon2);
					finish();
				}
			}
		});

		Button autoOn = (Button) findViewById(R.id.setTimerOn);
		autoOn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// buat textnya kosong dulu
				textAlarmPrompt.setText("");
				openTimePickerDialog1(false);

			}
		});
		Button autoOff = (Button) findViewById(R.id.settimerOff);
		autoOff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				textAlarmPrompt.setText("");
				openTimePickerDialog2(false);
			}
		});
	}

	private void openTimePickerDialog1(boolean is24r) {
		Calendar calendar = Calendar.getInstance();

		timePickerDialog = new TimePickerDialog(AutoKontrol.this,
				onTimeSetListener1, calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), true);
		timePickerDialog.setTitle("Set Auto on Time");

		timePickerDialog.show();

	}

	private void openTimePickerDialog2(boolean is24r) {
		Calendar calendar = Calendar.getInstance();

		timePickerDialog = new TimePickerDialog(AutoKontrol.this,
				onTimeSetListener2, calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), true);
		timePickerDialog.setTitle("Set Auto off Time");

		timePickerDialog.show();

	}

	OnTimeSetListener onTimeSetListener1 = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			Calendar calNow = Calendar.getInstance();
			Calendar calSet = (Calendar) calNow.clone();

			calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calSet.set(Calendar.MINUTE, minute);
			calSet.set(Calendar.SECOND, 0);
			calSet.set(Calendar.MILLISECOND, 0);

			long requestTime = (long) calSet.getTimeInMillis();
			Log.d("waktu sekarang", "waktu req adalah" + requestTime);
			long nowTime = (long) calNow.getTimeInMillis();
			Log.d("waktu sekarang", "waktu now adalah" + nowTime);

			if (requestTime >= nowTime) {
				long count = requestTime - nowTime;
				double count1 = (double) count / 60000;
				menit = (int) Math.ceil(count1);
			}

			if (requestTime < nowTime) {
				long count = requestTime - nowTime;
				double count1 = (double) count / 60000;
				int premenit = (int) Math.ceil(count1);
				menit = premenit + 1440;

			}
			textAlarmPrompt.setText("\n\n***\n" + "Auto on is set " + menit
					+ " minute from now" + "\n" + "***\n");
			String smenit = Integer.toString(menit);
			charArray = smenit.toCharArray();
			kirim = new char[6];
			kirim[0] = Integer.toString(Prkt).toCharArray()[0];
			kirim[1] = '1';
			int panjang = charArray.length;
			Log.d("waktu sekarang", "panjang = " + panjang);
			switch (panjang) {
			case 1:
				for (int i = 2; i < 5; i++) {
					kirim[i] = '0';
				}
				kirim[5] = charArray[0];
				skirim = new String(kirim);
				check = 1;
				Log.d("waktu sekarang", "string jadinya = " + skirim);
				break;
			case 2:
				for (int i = 2; i < 4; i++) {
					kirim[i] = '0';
				}
				kirim[4] = charArray[0];
				kirim[5] = charArray[1];
				skirim = new String(kirim);
				check = 1;
				break;
			case 3:
				for (int i = 2; i < 3; i++) {
					kirim[i] = '0';
				}
				kirim[3] = charArray[0];
				kirim[4] = charArray[1];
				kirim[5] = charArray[2];
				skirim = new String(kirim);
				check = 1;
				break;
			case 4:
				int q = 0;
				for (int i = 2; i < 6; i++) {
					kirim[i] = charArray[q];
					q++;
				}
				skirim = new String(kirim);
				check = 1;
				break;
			}
		}
	};

	OnTimeSetListener onTimeSetListener2 = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			Calendar calNow = Calendar.getInstance();
			Calendar calSet = (Calendar) calNow.clone();

			calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calSet.set(Calendar.MINUTE, minute);
			calSet.set(Calendar.SECOND, 0);
			calSet.set(Calendar.MILLISECOND, 0);

			long requestTime = (long) calSet.getTimeInMillis();
			Log.d("waktu sekarang", "waktu req adalah" + requestTime);
			long nowTime = (long) calNow.getTimeInMillis();
			Log.d("waktu sekarang", "waktu now adalah" + nowTime);

			if (requestTime >= nowTime) {
				long count = requestTime - nowTime;
				double count1 = (double) count / 60000;
				menit = (int) Math.ceil(count1);
			}

			if (requestTime < nowTime) {
				long count = requestTime - nowTime;
				double count1 = (double) count / 60000;
				int premenit = (int) Math.ceil(count1);
				menit = premenit + 1440;
			}

			textAlarmPrompt.setText("\n\n***\n" + "Auto off is set " + menit
					+ " minute from now" + "\n" + "***\n");
			String smenit = Integer.toString(menit);
			charArray = smenit.toCharArray();
			kirim = new char[6];
			kirim[0] = Integer.toString(Prkt).toCharArray()[0];
			kirim[1] = '0';
			int panjang = charArray.length;
			Log.d("waktu sekarang", "panjang = " + panjang);
			switch (panjang) {
			case 1:
				for (int i = 2; i < 5; i++) {
					kirim[i] = '0';
				}
				kirim[5] = charArray[0];
				skirim = new String(kirim);
				check = 1;
				break;
			case 2:
				for (int i = 2; i < 4; i++) {
					kirim[i] = '0';
				}
				kirim[4] = charArray[0];
				kirim[5] = charArray[1];
				skirim = new String(kirim);
				check = 1;
				break;
			case 3:
				for (int i = 2; i < 3; i++) {
					kirim[i] = '0';
				}
				kirim[3] = charArray[0];
				kirim[4] = charArray[1];
				kirim[5] = charArray[2];
				skirim = new String(kirim);
				check = 1;
				break;
			case 4:
				int q = 0;
				for (int i = 2; i < 6; i++) {
					kirim[i] = charArray[q];
					q++;
				}
				skirim = new String(kirim);
				check = 1;
				break;
			}
		}
	};
}
