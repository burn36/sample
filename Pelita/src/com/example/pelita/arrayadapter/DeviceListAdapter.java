package com.example.pelita.arrayadapter;

import java.util.ArrayList;
import java.util.HashMap;



import com.example.pelita.R;
import com.example.pelita.database.DatabaseDevName;
import com.example.pelita.database.DatabaseDevStatus;
import com.example.pelita.json.JSON;
import com.example.pelita.kelas.NewButton;
import com.example.pelita.kelas.NewTextView;

import com.example.pelita.model.AutoKontrol;
import com.example.pelita.model.MainRemote;
import com.example.pelita.model.listener.ClickListener;
import com.example.pelita.model.listener.LongClickListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceListAdapter extends ArrayAdapter<String> {
	private MainRemote parrent;
	private NewButton btn1, btn2, btn3, btn4, btn5,tmr1,tmr2,tmr3,tmr4,tmr5;
	private NewTextView tx1, tx2, tx3, tx4, tx5;
	private Button nama;
	private DatabaseDevName d;
	private DatabaseDevStatus ds;

	private String id, tag, message;
	private ArrayList<String> List;

	public DeviceListAdapter(Context context, int textViewResourceId,
			ArrayList<String> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.List = objects;
		parrent = (MainRemote) context;
		d = new DatabaseDevName(parrent);
		ds = new DatabaseDevStatus(parrent);

		// Tarray.add(t);
	}

	@Override
	public void notifyDataSetChanged() {

		super.notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View Convert, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = Convert;

		id = List.get(position).split("@")[0];
		if (view == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.device_list, null);
		}
		nama = (Button) view.findViewById(R.id.Button);
		tx1 = (NewTextView) view.findViewById(R.id.text1);
		tx2 = (NewTextView) view.findViewById(R.id.text2);
		tx3 = (NewTextView) view.findViewById(R.id.text3);
		tx4 = (NewTextView) view.findViewById(R.id.text4);
		tx5 = (NewTextView) view.findViewById(R.id.text5);
		
		btn1 = (NewButton) view.findViewById(R.id.Device1);
		btn2 = (NewButton) view.findViewById(R.id.Device2);
		btn3 = (NewButton) view.findViewById(R.id.Device3);
		btn4 = (NewButton) view.findViewById(R.id.Device4);
		btn5 = (NewButton) view.findViewById(R.id.Device5);

		tmr1 = (NewButton) view.findViewById(R.id.alrm1);
		tmr2 = (NewButton) view.findViewById(R.id.alrm2);
		tmr3 = (NewButton) view.findViewById(R.id.alrm3);
		tmr4 = (NewButton) view.findViewById(R.id.alrm4);
		tmr5 = (NewButton) view.findViewById(R.id.alrm5);
		
		tx1.setText(d.getDev1(id));
		tx2.setText(d.getDev2(id));
		tx3.setText(d.getDev3(id));
		tx4.setText(d.getDev4(id));
		tx5.setText(d.getDev5(id));

		tx1.setOnLongClickListener(listener, id);
		tx2.setOnLongClickListener(listener, id);
		tx3.setOnLongClickListener(listener, id);
		tx4.setOnLongClickListener(listener, id);
		tx5.setOnLongClickListener(listener, id);

		tmr1.setOnClickListener(listener3,  List.get(position));
		tmr2.setOnClickListener(listener3,  List.get(position));
		tmr3.setOnClickListener(listener3,  List.get(position));
		tmr4.setOnClickListener(listener3,  List.get(position));
		tmr5.setOnClickListener(listener3,  List.get(position));
	

		if (ds.getDev1(id) == 0) {
			Log.d("dev1", "0");
			btn1.setBackgroundResource(R.drawable.buttonsend);
		} else {
			Log.d("dev1", "1");
			btn1.setBackgroundResource(R.drawable.buttonsended);
		}
		if (ds.getDev2(id) == 0) {
			Log.d("dev2", "0");
			btn2.setBackgroundResource(R.drawable.buttonsend);
		} else {
			Log.d("dev2", "1");
			btn2.setBackgroundResource(R.drawable.buttonsended);
		}
		if (ds.getDev3(id) == 0) {
			Log.d("dev3", "0");
			btn3.setBackgroundResource(R.drawable.buttonsend);
		} else {
			Log.d("dev3", "1");
			btn3.setBackgroundResource(R.drawable.buttonsended);
		}
		if (ds.getDev4(id) == 0) {
			Log.d("dev4", "0");
			btn4.setBackgroundResource(R.drawable.buttonsend);
		} else {
			Log.d("dev4", "1");
			btn4.setBackgroundResource(R.drawable.buttonsended);
		}
		if (ds.getDev5(id) == 0) {
			Log.d("dev5", "0");
			btn5.setBackgroundResource(R.drawable.buttonsend);
		} else {
			Log.d("dev5", "1");
			btn5.setBackgroundResource(R.drawable.buttonsended);
		}

		if (List.get(position).split("@").length > 1) {
			nama.setText(id);
			btn1.setOnClickListener(listener2, id);
			btn2.setOnClickListener(listener2, id);
			btn3.setOnClickListener(listener2, id);
			btn4.setOnClickListener(listener2, id);
			btn5.setOnClickListener(listener2, id);
		} else {
			nama.setText(List.get(position));
			btn1.setOnClickListener(listener1, List.get(position));
			btn2.setOnClickListener(listener1, List.get(position));
			btn3.setOnClickListener(listener1, List.get(position));
			btn4.setOnClickListener(listener1, List.get(position));
			btn5.setOnClickListener(listener1, List.get(position));
		}

		return view;
	}

	private ClickListener listener1 = new ClickListener() {

		@Override
		public void onClick(View view, String tag) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.Device1:
				Log.d("Device1", tag);
				if (ds.getDev1(tag) == 0) {
					parrent.sendMessage("110000");

					view.setBackgroundResource(R.drawable.buttonsended);
				} else {
					parrent.sendMessage("100000");
					view.setBackgroundResource(R.drawable.buttonsend);
				}
				break;
			case R.id.Device2:
				Log.d("Device2", tag);
				if (ds.getDev2(tag) == 0) {
					parrent.sendMessage("210000");
					view.setBackgroundResource(R.drawable.buttonsended);
				} else {
					parrent.sendMessage("200000");
					view.setBackgroundResource(R.drawable.buttonsend);

				}
				break;
			case R.id.Device3:
				Log.d("Device3", tag);
				if (ds.getDev3(tag) == 0) {
					parrent.sendMessage("310000");
					Log.d("Device1", "1");
					view.setBackgroundResource(R.drawable.buttonsended);
				} else {
					parrent.sendMessage("300000");
					view.setBackgroundResource(R.drawable.buttonsend);
					Log.d("Device1", "0");
				}
				break;
			case R.id.Device4:
				Log.d("Device4", tag);
				if (ds.getDev4(tag) == 0) {
					parrent.sendMessage("410000");

					view.setBackgroundResource(R.drawable.buttonsended);
				} else {
					parrent.sendMessage("400000");
					view.setBackgroundResource(R.drawable.buttonsend);

				}
				break;
			case R.id.Device5:
				Log.d("Device5", tag);
				if (ds.getDev5(tag) == 0) {
					parrent.sendMessage("510000");

					view.setBackgroundResource(R.drawable.buttonsended);
				} else {
					parrent.sendMessage("500000");
					view.setBackgroundResource(R.drawable.buttonsend);

				}
				break;

			default:
				break;
			}

		}
	};
	
	private ClickListener listener2 = new ClickListener() {


		@Override
		public void onClick(View view, String tag) {
			// TODO Auto-generated method stub

			switch (view.getId()) {
			case R.id.Device1:

				if (ds.getDev1(tag) == 0) {
					sendDev(tag, "110000");
					Log.d("Device1", "1");
					view.setBackgroundResource(R.drawable.buttonsended);
				} else {
					sendDev(tag, "100000");

					view.setBackgroundResource(R.drawable.buttonsend);
					Log.d("Device1", "0");
				}
				break;
			case R.id.Device2:
				Log.d("Device2", tag);
				if (ds.getDev2(tag) == 0) {
					Log.d("Device2", "1");
					sendDev(tag, "210000");
					view.setBackgroundResource(R.drawable.buttonsended);
				} else {
					Log.d("Device2", "0");
					sendDev(tag, "200000");
					view.setBackgroundResource(R.drawable.buttonsend);
				}
				break;
			case R.id.Device3:
				Log.d("Device3", tag);
				if (ds.getDev3(tag) == 0) {
					Log.d("Device3", "1");
					view.setBackgroundResource(R.drawable.buttonsended);
					sendDev(tag, "310000");
				} else {
					Log.d("Device3", "0");
					sendDev(tag, "300000");
					view.setBackgroundResource(R.drawable.buttonsend);
				}
				break;
			case R.id.Device4:
				Log.d("Device4", tag);
				if (ds.getDev4(tag) == 0) {
					Log.d("Device4", "1");
					view.setBackgroundResource(R.drawable.buttonsended);
					sendDev(tag, "410000");
				} else {
					Log.d("Device4", "0");
					sendDev(tag, "400000");
					view.setBackgroundResource(R.drawable.buttonsend);
				}
				break;
			case R.id.Device5:
				Log.d("Device5", tag);
				if (ds.getDev5(tag) == 0) {
					Log.d("Device5", "1");
					sendDev(tag, "510000");
					view.setBackgroundResource(R.drawable.buttonsended);
				} else {
					Log.d("Device5", "0");
					sendDev(tag, "500000");
					view.setBackgroundResource(R.drawable.buttonsend);
				}
				break;

			default:
				break;
			}

		}

	
	};
	public void sendDev(String tag, String message) {
		this.tag = tag;
		this.message = message;
		new BackgroundAsyncTask().execute();

	}

	class BackgroundAsyncTask extends AsyncTask<Void, String, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			JSON j = new JSON(parrent);
			HashMap<String, String> x = new HashMap<String, String>();
			try {
				x.put("id", tag);
				x.put("message", message);
				

				publishProgress(j.InsertTag(x));
			} catch (Exception e) {
				Log.d("error", e.toString());
			}
			return null;
		}

		protected void onProgressUpdate(String[] values) {
			Toast.makeText(parrent, values[0], Toast.LENGTH_SHORT).show();
		};
	}
	private ClickListener listener3 = new ClickListener() {
		String tag, dev, status;

		@Override
		public void onClick(View view, String tag) {
			// TODO Auto-generated method stub
			Log.d("click lst 3", tag);
			Intent auto = new Intent(parrent,
					AutoKontrol.class);
			auto.putExtra("id", tag);
			switch (view.getId()) {
			case R.id.alrm1:
				auto.putExtra("prk", 1);
				parrent.startActivityForResult(auto, MainRemote.Timer);
				break;
			case R.id.alrm2:
				auto.putExtra("prk", 2);
				parrent.startActivityForResult(auto, MainRemote.Timer);
				break;
			case R.id.alrm3:
				auto.putExtra("prk", 3);
				parrent.startActivityForResult(auto, MainRemote.Timer);
				break;
			case R.id.alrm4:
				auto.putExtra("prk", 4);
				parrent.startActivityForResult(auto, MainRemote.Timer);
				break;
			case R.id.alrm5:
				auto.putExtra("prk", 5);
				parrent.startActivityForResult(auto, MainRemote.Timer);
				break;

			default:
				break;
			}

		}

	
	};
	
	private LongClickListener listener = new LongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			Log.d("Long click id", "asdasd");
			return false;
		}

		@Override
		public void onLongClick(final View view, final String id) {
			TextView t = (TextView) view;
			final EditText input = new EditText(parrent);
			input.setText(t.getText());
			new AlertDialog.Builder(parrent).setView(input)
					.setIcon(R.drawable.alert_dialog_icon)
					.setTitle("Ganti Nama")
					.setPositiveButton("Ok", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (view.getId()) {
							case R.id.text1:

								d.UpdateDev1(id, input.getText().toString());
								break;
							case R.id.text2:
								d.UpdateDev2(id, input.getText().toString());

								break;
							case R.id.text3:
								d.UpdateDev3(id, input.getText().toString());

								break;
							case R.id.text4:
								d.UpdateDev4(id, input.getText().toString());

								break;
							case R.id.text5:
								d.UpdateDev5(id, input.getText().toString());

								break;

							default:
								break;
							}

						}
					}).create().show();

		}
	};

}