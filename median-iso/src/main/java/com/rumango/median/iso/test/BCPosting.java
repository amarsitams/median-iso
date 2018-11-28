package com.rumango.median.iso.test;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.iso.packager.ISO93APackager;

public class BCPosting implements Callable<String> {
	private ISOChannel channel;
	private String drAccount;
	private String crAccount;
	private int amount;
	private String narration;
	private int stan;
	private final static Logger logger = Logger.getLogger(BCPosting.class);

	public static void main(String[] args) {
		// main();
		new BCPosting().test();
	}

	public static void main() {
		ExecutorService service = Executors.newFixedThreadPool(1);
		Future<String> task = service.submit(new BCPosting("xxx", "xxx", 62, 758756, "xxx"));
		String responseCode = null;
		System.out.println("Constructor...");
		try {
			responseCode = task.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		service.shutdownNow();
		System.out.println("Finacle response code is: " + responseCode);
	}

	public BCPosting() {
	}

	public BCPosting(String drAccount, String crAccount, int amount, int stan, String narration) {
		this.drAccount = drAccount;
		this.crAccount = crAccount;
		this.amount = amount;
		this.narration = narration;
		this.stan = stan;

		try {
			this.channel = new ASCIIChannel("172.16.2.225", 52000, getPackager());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String call() {
		logger.info("Call ...");
		String responseCode = null;
		try {
			channel.connect();
			if (channel.isConnected()) {
				logger.info("Channel connected. Sending...");
			}
			ISOMsg m = new ISOMsg();
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

//			m.setMTI("1804");
//			m.set(11, "444444");
//			m.set(12, "201809101129");
//			m.set(24, "831");

//			m.setMTI("1200");
//			m.set(2, "INTB");
//			m.set(3, "400000");
//			m.set(4, new DecimalFormat("0000000000000000").format(100));
//			m.set(11, "ABC0000019");
//			m.set(12, YY + MM + DD + hh + mm + ss);
//			m.set(17, YY + MM + DD);
//			m.set(24, "200");
//			m.set(32, "000000");
//			m.set(49, "840");
//			// m.set(62,"00000000101");
//			m.set(102, drAccount);
//			m.set(103, crAccount);
//			m.set(123, "SWT");
//			m.set(124, "ITAX");
//			m.set(125, narration);

			m.setMTI("1200");
			m.set(2, "ITAX");
			m.set(3, "400000");
			m.set(4, new DecimalFormat("000000000000").format(100));
			m.set(12, YY + MM + DD + hh);
			m.set(17, YY);
			m.set(24, "200");
			m.set(25, "ABC0000019");
			m.set(32, "000000");
			m.set(49, "840");
			// m.set(62,"00000000101");
			m.set(102, drAccount);
			m.set(103, crAccount);
			m.set(123, "SWT");
			m.set(124, "ITAX");
			m.set(125, narration);

			channel.send(m);
			ISOMsg r = channel.receive();
			if (r != null) {
				logger.info("Response received");
			}
			logISOMsg(m);

			byte[] data = m.pack();
			System.out.println("RESULT : " + new String(data));

			responseCode = r.getString(39);
			String responseString = "";
			ISOMsg respMessage = new ISOMsg();
			for (int i = 0; i <= r.getMaxField(); i++) {
				if (r.hasField(i)) {
					respMessage.set(i, r.getString(i));
					responseString += "Field " + i + ": " + r.getString(i) + "|";
				}
			}
			respMessage.setPackager(getPackager());
			respMessage.pack();
			System.out.println("new String(respMessage.pack())" + new String(respMessage.pack()));

			if (r.getString(39).equals("000")) {
				logger.info("Finacle successfully posted from: " + drAccount + " to " + crAccount + " => Response "
						+ responseString);
			} else {
				logger.error("Finacle posting from: " + drAccount + " to " + crAccount + " failed => Response "
						+ responseString);
			}
			channel.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ISOException e) {
			e.printStackTrace();
			return null;
		}
		return responseCode;
	}

	private GenericPackager getPackager() throws ISOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputstream = classLoader.getResourceAsStream("basic.xml");
		GenericPackager packager = new GenericPackager(inputstream);
		return packager;
	}

	private static void logISOMsg(ISOMsg msg) {
		logger.info("----ISO MESSAGE-----");
		try {
			logger.info(" MTI : " + msg.getMTI());
			for (int i = 1; i <= msg.getMaxField(); i++) {
				if (msg.hasField(i)) {
					logger.info(" Field-" + i + " : " + msg.getString(i));
				}
			}
		} catch (ISOException e) {
			e.printStackTrace();
		} finally {
			logger.info("--------------------");
		}

	}

	public String test() {
		logger.info("Call ...");
		String responseCode = null;
		try {
			ISOMsg m = new ISOMsg();
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

//			m.setMTI("1804");
//			m.set(11, "444444");
//			m.set(12, "201809101129");
//			m.set(24, "831");

//			m.setMTI("1200");
//			m.set(2, "INTB");
//			m.set(3, "400000");
//			m.set(4, new DecimalFormat("000000000000").format(100));
//			m.set(11, "000019");
//			m.set(12, YY + MM + DD + hh);
//			m.set(17, YY);
//			m.set(24, "200");
//			m.set(46, "ABC0000019");
//			m.set(49, "840");
//			// m.set(62,"00000000101");
//			m.set(102, drAccount);
//			m.set(103, crAccount);
//			m.set(123, "SWT");
//			m.set(124, "ITAX");
//			m.set(125, narration);
			
//			m.set(4,new DecimalFormat("0000000000000000").format(amount * 100));
//			m.set(11, "00000" + stan);
//			m.set(12,YY + MM + DD + hh + mm + ss);
//			m.set(17,YY + MM + DD);
			
//			m.setMTI("1200");
//			m.set(2, "ITAX");
//			m.set(3, "400000");
//			m.set(4, new DecimalFormat("000000000000").format(100));
//			m.set(6, "ABC0000019");
//			m.set(12, YY + MM + DD + hh);
//			m.set(17, YY);
//			m.set(24, "200");
//			m.set(32, "000000");
//			m.set(49, "840");
//			// m.set(62,"00000000101");
//			m.set(102, drAccount);
//			m.set(103, crAccount);
//			m.set(123, "SWT");
//			m.set(124, "ITAX");
//			m.set(125, narration);
			
			
			m.setMTI("1200");
			m.set(2, "ITAX");
			m.set(3, "400000");
			m.set(4, new DecimalFormat("000000000000").format(100));
			m.set(12, YY + MM + DD + hh);
			m.set(17, YY);
			m.set(24, "200");
			m.set(32, "000000");
			m.set(49, "840");
			// m.set(62,"00000000101");
			m.set(102, "001190001000275");
			m.set(103, "001190001000309");
			m.set(123, "SWT");
			m.set(124, "ITA");
			m.set(125, "ABC0000019");

			m.setPackager(new ISO93APackager());
			byte[] data = m.pack();
			System.out.println("RESULT : " + new String(data));
		} catch (ISOException e) {
			e.printStackTrace();
			return null;
		}
		return responseCode;
	}
}