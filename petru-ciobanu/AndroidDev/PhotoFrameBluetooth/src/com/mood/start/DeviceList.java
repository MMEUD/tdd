package com.mood.start;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceList extends Activity {
	//OBEXObjectPushServiceClass_UUID                 = '{00001105-0000-1000-8000-00805F9B34FB}';
	//OBEXFileTransferServiceClass_UUID               = '{00001106-0000-1000-8000-00805F9B34FB}'
	//SerialPortServiceClass_UUID                     = '{00001101-0000-1000-8000-00805F9B34FB}'
	//ImagingResponderServiceClass_UUID               = '{0000111B-0000-1000-8000-00805F9B34FB}'
	//http://bluecove.org/bluecove/apidocs/javax/bluetooth/UUID.html
	//http://kamrana.wordpress.com/2012/05/12/sending-images-over-bluetooth-in-android/
	TextView txt;
	Button  btn;
   ListView lista;
	private static final UUID generalUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static BluetoothSocket socket;
	private static final String LogBlue = "InfoService";
	private ArrayAdapter<String> mArrayAdapter;
	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter btAdapter; 
	private ArrayList<BluetoothDevice> btDeviceList = new ArrayList<BluetoothDevice>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_list);
		txt=(TextView)findViewById(R.id.txtInfo);
		//Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothDevice.ACTION_UUID);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		
		registerReceiver(ActionFoundReceiver, filter); // Don't forget to unregister during onDestroy
		// Register the BroadcastReceiver

		//registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
		// Getting the Bluetooth adapter 
		 mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		 lista = (ListView)findViewById(R.id.listView1);
		 lista.setAdapter(mArrayAdapter);
		 btAdapter = BluetoothAdapter.getDefaultAdapter();
	
		CheckBTState();
	}

	/* This routine is called when an activity completes.*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_ENABLE_BT) {
			CheckBTState();
		}
	}

	private void CheckBTState() {
		if(btAdapter==null) { 
			txt.setText("\nBluetooth NOT supported. Aborting.");
			return;
		} else {
			if (btAdapter.isEnabled()) {
				txt.setText("\nBluetooth is enabled...");
				// Starting the device discovery
				btAdapter.startDiscovery();
			} else {
				Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	}

	
	public void cacat() {
		btAdapter.cancelDiscovery();
		BluetoothSocket btSocket = null;
		
		BluetoothDevice btDevice = btAdapter.getRemoteDevice("00:12:1C:78:B3:E8");
	    Toast.makeText(getApplicationContext(), "Connected to " + btDevice.getBondState(), Toast.LENGTH_SHORT).show();	
		try {
			
			btSocket = btDevice.createRfcommSocketToServiceRecord(generalUuid);
			btSocket.connect();
		} catch (IOException e1) {
			txt.setText("ErrorSocket"+e1.toString());
		}
		
		if(btSocket.isConnected()){
			 Toast.makeText(getApplicationContext(), "Connected to " + btDevice.describeContents(), Toast.LENGTH_SHORT).show();	
		}else{
			
		}
		
		
		
}
	
	
	
	

	public void startData(View view){
		txt.setText("StartCommand");
		cacat();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (btAdapter != null) {
			btAdapter.cancelDiscovery();
		}
		unregisterReceiver(ActionFoundReceiver);
	}

	private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				btDeviceList.add(device);
				mArrayAdapter.add("Discovered devices");
				mArrayAdapter.add(device.getAddress());
			} else {
				if(BluetoothDevice.ACTION_UUID.equals(action)) {
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
					mArrayAdapter.add("Discovered Service UID");
					for (int i=0; i<uuidExtra.length; i++) {
						    //txt.append("\n  Device: " + device.getName() + ", " + device + ", Service: " + uuidExtra[i].toString());
						    mArrayAdapter.add(uuidExtra[i].toString());
						    
					}
				} else {
					if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
						txt.setText("\nDiscovery Started...");
					} else {
						if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
							txt.setText("\nDiscovery Finished");
							Iterator<BluetoothDevice> itr = btDeviceList.iterator();
							while (itr.hasNext()) {
								// Get Services for paired devices
								BluetoothDevice device = itr.next();
								//   out.append("\nGetting Services for " + device.getName() + ", " + device);
								if(!device.fetchUuidsWithSdp()) {
									txt.setText("\nSDP Failed for " + device.getName());
								}

							}
						}
					}
				}
			}
		}
	};


	public void mumu(){
		
	}

	
	
	
}
