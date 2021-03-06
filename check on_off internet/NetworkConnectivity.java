package com.strategy.intecom.vtc.connection;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public class NetworkConnectivity { 
	
	private static NetworkConnectivity sharedNetworkConnectivity = null;
	
	private Activity activity = null;

	private final Handler handler = new Handler();
	private Runnable runnable = null;
	
	private boolean stopRequested = false;
	private boolean monitorStarted = false;
	
	private static final int NETWORK_CONNECTION_YES= 1;
	private static final int NETWORK_CONNECTION_NO = -1;
	private static final int NETWORK_CONNECTION_UKNOWN = 0;
	
	private int connected = NETWORK_CONNECTION_UKNOWN;
	 
	public static final int MONITOR_RATE_WHEN_CONNECTED_MS = 1000;
	public static final int MONITOR_RATE_WHEN_DISCONNECTED_MS = 1000;
	
	private final List<NetworkMonitorListener> networkMonitorListeners = new ArrayList<NetworkMonitorListener>();
	
	private NetworkConnectivity() { 
	}
	
	public synchronized static NetworkConnectivity sharedNetworkConnectivity() {
		if (sharedNetworkConnectivity == null) {
			sharedNetworkConnectivity = new NetworkConnectivity();
		}
		
		return sharedNetworkConnectivity;
	}
	
	/**
	 * The network monitor must be configured prior to use. The {@code Activity} is used to retrieve the connectivity service.
	 * @param activity the activity that will be used to retrieve connectivity service and make UI Thread calls on connection state changes
	 */
	public void configure(Activity activity) {
		this.activity = activity;
	}
	 
	/**
	 * Starts network monitor. This call is asynchronous and returns immediately. 
	 * 
	 * @return returns {@code true} if the {@code NetworkConnectivity} is configured and ready to start network monitor; {@code false} otherwise.
	 */
	public synchronized boolean startNetworkMonitor() {
		if (this.activity == null) {
			return false;
		}
		
		if (monitorStarted) {
			return true;
		}
		
		stopRequested = false;
		monitorStarted = true;
		
		(new Thread(new Runnable() { 
			@Override
			public void run() { 
				doCheckConnection() ;
			} 
		})).start(); 
		
		return true;
	}
	
	public synchronized void stopNetworkMonitor() {
		stopRequested = true;
		monitorStarted = false;
	}
	
	public void addNetworkMonitorListener(NetworkMonitorListener l) {
		this.networkMonitorListeners.add(l);
		this.notifyNetworkMonitorListener(l);
	}
	
	public boolean removeNetworkMonitorListener(NetworkMonitorListener l) {
		return this.networkMonitorListeners.remove(l);
	}
	
	private void doCheckConnection() {
		
		if (stopRequested) {
			runnable = null;
			return;
		}
		
		final boolean connectedBool = this.isConnected();

		final int _connected = (connectedBool ? NETWORK_CONNECTION_YES : NETWORK_CONNECTION_NO);

		if (this.connected != _connected) { 
			
			this.connected = _connected;
			
			activity.runOnUiThread(new Runnable() { 
				@Override
				public void run() { 
					notifyNetworkMonitorListeners();
				}
			});
		}
		
		runnable = new Runnable() { 
			@Override
			public void run() {
				doCheckConnection();
			}
		};
		
		handler.postDelayed(runnable, (connectedBool ? MONITOR_RATE_WHEN_CONNECTED_MS : MONITOR_RATE_WHEN_DISCONNECTED_MS));
	}
	
	/**
	 * A synchronous call to check if network connectivity exists.
	 * 
	 * @return {@true} if network is connected, {@false} otherwise.
	 */
	public boolean isConnected() {
		try {
			ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();

			if (netInfo != null && netInfo.isConnected()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	private void notifyNetworkMonitorListener(NetworkMonitorListener l) {
		try {
			if (this.connected == NETWORK_CONNECTION_YES) {
				l.connectionEstablished();
			} else if (this.connected == NETWORK_CONNECTION_NO) {
				l.connectionLost();
			} else {
				l.connectionCheckInProgress();
			}
		} catch (Exception e) {
		}
	}
	
	private void notifyNetworkMonitorListeners() {
		for (NetworkMonitorListener l : this.networkMonitorListeners) {
			this.notifyNetworkMonitorListener(l);
		}
	}
}
