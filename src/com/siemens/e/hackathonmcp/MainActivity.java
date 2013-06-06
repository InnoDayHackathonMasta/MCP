package com.siemens.e.hackathonmcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private static boolean DEBUG = true;
	private static final String TAG = "MCP:MainActivity";

	BluetoothAdapter mBluetoothAdapter = null;
	ClientQueryThread cqt = null;

	Button btnStartMCP = null;

	app appObj = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		appObj = (app)getApplication();


		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		Log.i(TAG, "enabling Bluetooth");
		mBluetoothAdapter.enable();

		btnStartMCP = (Button)findViewById(R.id.main_btn_start_mcp);

		btnStartMCP.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cqt = new ClientQueryThread(false);
				cqt.start();

			}
		});



	}


	private class ClientQueryThread extends Thread {
		// The local server socket
		private final BluetoothServerSocket mmServerSocket;
		private String mSocketType;

		// Name for the SDP record when creating server socket
		private static final String NAME_INSECURE = "BluetoothChainInsecure";

		// Constants that indicate the current connection state
		public static final int STATE_NONE = 0;       // we're doing nothing
		public static final int STATE_LISTEN = 1;     // now listening for incoming connections
		public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
		public static final int STATE_CONNECTED = 3;  // now connected to a remote device

		private int mState = 0;

		@SuppressLint("NewApi")
		public ClientQueryThread(boolean secure) {

			if (DEBUG) Log.i(TAG, "+++ Accept Thread started +++");
			BluetoothServerSocket tmp = null;
			mSocketType = secure ? "Secure":"Insecure";

			// Create a new listening server socket
			try {

				tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME_INSECURE, appObj.getBT_UUID_CLIENTINFO());


			} catch (IOException e) {
				Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
			}

			mmServerSocket = tmp;

		}

		public void run() {
			if (DEBUG) Log.d(TAG, "Socket Type: " + mSocketType +
					"BEGIN mAcceptThread" + this);
			setName("ClientQueryThread" + mSocketType);

			BluetoothSocket socket = null;

			// Listen to the server socket if we're not connected
			while (mState != STATE_CONNECTED) {
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception
					socket = mmServerSocket.accept();
				} catch (IOException e) {
					Log.e(TAG, "Socket Type: " + mSocketType + "accept() failed", e);
					break;
				}

				// If a connection was accepted
				if (socket != null) {
					if (DEBUG) Log.i(TAG, "+++++++ WE GOT A CONNECTION +++++++");
				}
			}
			if (DEBUG) Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);

		}

		public void cancel() {
			if (DEBUG) Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this);
			try {
				mmServerSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "Socket Type" + mSocketType + "close() of server failed", e);
			}
		}
	}

	
	private class ConnectedThread extends Thread {
	    private final BluetoothSocket mmSocket;
	    private final InputStream mmInStream;
	    private final OutputStream mmOutStream;
	 
	    public ConnectedThread(BluetoothSocket socket) {
	        mmSocket = socket;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	 
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } catch (IOException e) { }
	 
	        mmInStream = tmpIn;
	        mmOutStream = tmpOut;
	    }
	 
	    public void run() {
	        byte[] buffer = new byte[1024];  // buffer store for the stream
	        int bytes; // bytes returned from read()
	        Log.d("ConnectedThread", "Server: We made it to connect!");

	        // Keep listening to the InputStream until an exception occurs
	        while (true) {
	            try {
	                // Read from the InputStream
	            	Log.d("Incoming Message", "We are waiting for BYTES!");
	            	bytes = mmInStream.read(buffer);
	        		Log.d("Incoming Message", buffer.toString());
	                // Send the obtained bytes to the UI activity
	                // mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
	            } catch (IOException e) {
	                break;
	            }
	        }
	    }
	 
	    /* Call this from the main activity to send data to the remote device */
	    public void write(byte[] bytes) {
	        try {
	            mmOutStream.write(bytes);
	        } catch (IOException e) { }
	    }
	 
	    /* Call this from the main activity to shutdown the connection */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}


}
