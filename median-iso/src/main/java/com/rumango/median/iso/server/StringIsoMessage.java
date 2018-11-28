package com.rumango.median.iso.server;

import java.nio.charset.StandardCharsets;

public class StringIsoMessage {
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