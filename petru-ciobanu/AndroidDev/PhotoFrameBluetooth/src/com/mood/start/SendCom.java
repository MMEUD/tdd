package com.mood.start;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
public class SendCom extends Activity {
	
	private static final String TAG = "THINBTCLIENT";
	private static final boolean D = true;
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothSocket btSocket = null;
	private OutputStream outStream = null;
	// Well known SPP UUID (will *probably* map to
	// RFCOMM channel 1 (default) if not in use);
	// see comments in onResume().
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// ==> hardcode your server's MAC address here <==
	private static String address = "00:12:1C:78:B3:E8";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_list);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, 
				"Bluetooth is not available.", 
				Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		if (!mBluetoothAdapter.isEnabled()) {
			Toast.makeText(this, 
				"Please enable your BT and re-run this program.", 
				Toast.LENGTH_LONG).show();
			finish();
			return;
		}
	}

	
	
	
	public void startData(View view){
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		// are in use on your Android device.
				try {
					
					btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
					
					
					btSocket.connect();
				} catch (IOException e) {
					
					Toast.makeText(this, "ON RESUME: Socket creation failed."+e, Toast.LENGTH_LONG).show();
				}
				
				//mBluetoothAdapter.cancelDiscovery();
				
			/*	try {
					btSocket.connect();
					Toast.makeText(this, "Socket creation failed.", Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					try {
						btSocket.close();
					} catch (IOException e2) {
						Toast.makeText(this, "Socket creation failed."+e2, Toast.LENGTH_LONG).show();
					}
				}*/
							
				
	}
	
	
	
	
	


	@Override
	public void onStop() {
		super.onStop();
		if (D)
			Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (D)
			Log.e(TAG, "--- ON DESTROY ---");
	}
}
