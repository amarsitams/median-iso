package com.rumango.median.iso.socket.server;

import java.nio.charset.StandardCharsets;

public class IsoMessage {
	private String str;

	public byte[] toByteArray() {
		return str.getBytes();
	}

	public String getStr() {
		return new String(toByteArray(), StandardCharsets.US_ASCII).trim();
	}

	public void setStr(String str) {
		this.str = str;
	}
}