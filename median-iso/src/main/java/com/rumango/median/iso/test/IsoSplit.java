package com.rumango.median.iso.test;

public class IsoSplit {
	public static void main(String[] args) {
		String s = "90980009612301000000000000000000";
		StringBuilder sb = new StringBuilder();
		char[] ary = s.toCharArray();

		for (int i = 0; i < s.length(); i++) {
			// System.out.println(Integer.toBinaryString(ary[i]).substring(2));
			sb.append(Integer.toBinaryString(ary[i]).substring(2));
		}
		System.out.println(sb.length());
		System.out.println(sb);
	}
}
