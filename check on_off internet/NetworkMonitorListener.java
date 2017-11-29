package com.strategy.intecom.vtc.connection;

public interface NetworkMonitorListener {
	public void connectionEstablished();
	public void connectionLost();
	public void connectionCheckInProgress();
}
