package com.rumango.median.iso.test;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

public class GenericTest {

	public static Map<String, String> arrayToMap(String[] arrayOfString) {
		return Arrays.asList(arrayOfString).stream().map(str -> str.split(":"))
				.collect(Collectors.toMap(str -> str[0], str -> str[1]));
	}

	public static void main(String[] args) throws UnsupportedEncodingException, ISOException {
		GenericTest gt = new GenericTest();
		// ISOMsg origional = gt.test();
		// gt.logISOMsg(origional, "origional");

//		ISOMsg origional = gt.test();
//		String IsoString = gt.logISOMsg(origional, "origional");
//		System.out.println(IsoString);

		String str = "0:1200;2:ITAX;3:400000;4:0000000000003939;11:00000;12:20181128114951;17:20181128;24:200;32:000000;49:840;102:0090000001003;103:001190001000309;123:SWT;124:ITA;125:ABC0000019;";

		String[] splitted = str.split(";");

		Map<String, String> map = arrayToMap(splitted);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}

//		
//		for (String string : splitted) {
//			System.out.println(string);
//		}

//		System.out.println("---- Split by comma '|' ------");
//		StringTokenizer st1 = new StringTokenizer(str, ":");
//		StringTokenizer st2 = new StringTokenizer(str, "|");
//
//		String temp;
//		while (st2.hasMoreElements()) {
//			temp = (String) st2.nextElement();
//			while (temp.hasMoreElements()) {
//				System.out.println(temp.nextElement());
//			}
//			System.out.println(st2.nextElement());
//		}
	}

	private ISOMsg test() {
		ISOMsg m = new ISOMsg();
		try {
			Calendar cal = Calendar.getInstance();
			String MM = "" + (1 + cal.get(Calendar.MONTH));
			String DD = "" + cal.get(Calendar.DAY_OF_MONTH);
			String hh = "" + cal.get(Calendar.HOUR_OF_DAY);
			String mm = "" + cal.get(Calendar.MINUTE);
			String ss = "" + cal.get(Calendar.SECOND);
			String YY = "" + cal.get(Calendar.YEAR);
			if (MM.length() < 2) {
				MM = "0" + MM;
			}
			if (DD.length() < 2) {
				DD = "0" + DD;
			}
			if (hh.length() < 2) {
				hh = "0" + hh;
			}
			if (mm.length() < 2) {
				mm = "0" + mm;
			}
			if (ss.length() < 2) {
				ss = "0" + ss;
			}
			if (YY.length() < 2) {
				YY = "0" + YY;
			}

			m.setMTI("1200");
			m.set(2, "ITAX");
			m.set(3, "400000");
			m.set(4, new DecimalFormat("0000000000000000").format(3939));
			m.set(11, "00000");
			m.set(12, YY + MM + DD + hh + mm + ss);
			m.set(17, YY + MM + DD);
			m.set(24, "200");
			m.set(32, "000000");
			m.set(49, "840");
			// m.set(62,"00000000101");
			m.set(102, "0090000001003");
			m.set(103, "001190001000309");
			m.set(123, "SWT");
			m.set(124, "ITA");
			m.set(125, "ABC0000019");
		} catch (ISOException e) {
			e.printStackTrace();
			return null;
		}
		return m;
	}

	private GenericPackager getPackager() throws ISOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputstream = classLoader.getResourceAsStream("basic.xml");
		GenericPackager packager = new GenericPackager(inputstream);
		return packager;
	}

	private ISOMsg unpackMessage(String stringMessage) throws UnsupportedEncodingException, ISOException {
		try {
			ISOMsg isoMsg = new ISOMsg();
			isoMsg.setPackager(getPackager());
			isoMsg.unpack(stringMessage.getBytes("US-ASCII"));// "US-ASCII"
			return isoMsg;
		} catch (Exception e) {
			System.out.println("Exception while unpacking ");
			return null;
		}
	}

	private String logISOMsg(@NotEmpty @NotNull ISOMsg msg, String stringMessage) {
		StringBuilder responseString = new StringBuilder();
		try {
			System.out.println("-----------------" + stringMessage + "-----------------------");
			for (int i = 0; i <= msg.getMaxField(); i++) {
				if (msg.hasField(i)) {
					responseString = responseString.append(i).append(":").append(msg.getString(i)).append(";");
					System.out.println(i + " " + ":" + msg.getString(i));
				}
			}
		} catch (Exception e) {
			System.out.println("Exception occured" + e.getMessage());
		}
		return responseString.toString();
	}
}
