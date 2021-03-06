package com.siemens.e.hackathonmcp;

import java.util.UUID;

import android.app.Application;

public class app extends Application {
	
	public static enum ClientQueryThreadStatus {
		ACTIVE, OFF
	}
	
	public static enum ChainThreadStatus {
		ACTIVE, OFF
	}
	
	public static final String BT_UUID_CHAIN = "201BB686-672C-46FA-B86B-B9BA8BAACE71";
	public static final String BT_UUID_CLIENTINFO = "7F814AFE-90D0-4768-ADAD-89FD2F7F851A";
	
	String currentTail;
	int currentTailId;
	int numChainMembers = -1;
	
	public int getNumChainMembers() {
		return numChainMembers;
	}

	public void setNumChainMembers(int numChainMembers) {
		this.numChainMembers = numChainMembers;
	}

	ChainThreadStatus chainThreadStatus = ChainThreadStatus.OFF;
	ClientQueryThreadStatus clientQueryThreadStatus = ClientQueryThreadStatus.OFF;
	
	public ClientQueryThreadStatus getClientQueryThreadStatus() {
		return clientQueryThreadStatus;
	}

	public void setClientQueryThreadStatus(ClientQueryThreadStatus clientQueryThreadStatus) {
		this.clientQueryThreadStatus = clientQueryThreadStatus;
	}

	public ChainThreadStatus getChainThreadStatus () {
		return chainThreadStatus;
	}
	
	public void setChainThreadStatus (ChainThreadStatus chainThreadStatus) {
		this.chainThreadStatus = chainThreadStatus;
	}
	
	public int getCurrentTailId() {
		return currentTailId;
	}

	public void setCurrentTailId(int currentTailId) {
		this.currentTailId = currentTailId;
	}

	public String getCurrentTail() {
		return currentTail;
	}

	public void setCurrentTail(String currentTail) {
		this.currentTail = currentTail;
	}

	UUID getBT_UUID_CLIENTINFO () {
		return UUID.fromString(BT_UUID_CLIENTINFO);
	}
	
	UUID getBT_UUID_CHAIN () {
		return UUID.fromString(BT_UUID_CHAIN);
	}
	
}
