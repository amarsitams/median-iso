package com.rumango.median.iso.socket.client;

import java.io.IOException;
import java.io.InputStream;
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

public class BCPosting implements Callable<String> {
	ISOChannel channel;
	private String drAccount;
	private String crAccount;
	private int amount;
	private String narration;
	private int stan;
	private final static Logger logger = Logger.getLogger(BCPosting.class);

	public static void main() {
		ExecutorService service;
		Future<String> task;
		String responseCode = null;
		System.out.println("Starting...");
		service = Executors.newFixedThreadPool(1);
		task = service.submit(new BCPosting("xxx", "xxx", 62, 758756, "xxx"));
		try {
			responseCode = task.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		service.shutdownNow();
		System.out.println("Finacle response code is: " + responseCode);
	}

	/**
	 * This contructor, contains functionality for posting to Finacle.
	 * 
	 * @param String drAccount
	 * @param String crAccount
	 * @param        int amount
	 * @param        int stan
	 * @param String narration
	 */
	public BCPosting(String drAccount, String crAccount, int amount, int stan, String narration) {
		this.drAccount = drAccount;
		this.crAccount = crAccount;
		this.amount = amount;
		this.narration = narration;
		this.stan = stan;

		try {
			InputStream inputstream = ClassLoader.getSystemResourceAsStream("basic.xml");
			GenericPackager packager = new GenericPackager(inputstream);
			this.channel = new ASCIIChannel("172.16.2.225", 52000, packager);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BCPosting() {
		try {
			InputStream inputstream = ClassLoader.getSystemResourceAsStream("basic.xml");
			GenericPackager packager = new GenericPackager(inputstream);
			this.channel = new ASCIIChannel("172.16.2.225", 52000, packager);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String call() {
		System.out.println("Starting...");
		String responseCode = null;
		try {
			channel.connect();
			if (channel.isConnected()) {
				System.out.println("Channel connected. Sending...");
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

			m.setMTI("1804");
			m.set(11, "444444");
			m.set(12, "20180910112922");
			m.set(24, "831");

			channel.send(m);
			ISOMsg r = channel.receive();
			if (r != null) {
				System.out.println("Response received");
			}
			logISOMsg(m);

			byte[] data = m.pack();
			System.out.println("RESULT : " + new String(data));

			responseCode = r.getString(39);
			String responseString = "";
			for (int i = 1; i <= r.getMaxField(); i++) {
				if (r.hasField(i)) {
					responseString += "Field " + i + ": " + r.getString(i) + "|";
				}
			}

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

	private static void logISOMsg(ISOMsg msg) {
		System.out.println("----ISO MESSAGE-----");
		try {
			System.out.println(" MTI : " + msg.getMTI());
			for (int i = 1; i <= msg.getMaxField(); i++) {
				if (msg.hasField(i)) {
					System.out.println(" Field-" + i + " : " + msg.getString(i));
				}
			}
		} catch (ISOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("--------------------");
		}

	}

}