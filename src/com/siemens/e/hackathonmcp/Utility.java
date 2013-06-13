package com.siemens.e.hackathonmcp;

import java.nio.charset.Charset;

public class Utility {

	// looks for \0 (termination symbol) in the provided byte array
	public static String extractMAC(byte[] byteArray, int index){
		
		Charset charset = Charset.forName("US-ASCII");
		int i;
		for (i = 0; i < byteArray.length && byteArray[i] != 0; i++) { }
		return new String(byteArray, index, i-1, charset);
		
	}
	
}