package com.rumango.median.iso.socket.client;

import java.io.IOException;
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
import org.jpos.iso.packager.ISO93APackager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IsoPosting implements Callable<String> {
	@Value("${socket.host}")
	private String host;
	@Value("${socket.port}")
	private int port;
	ISOChannel channel;
	private ISOMsg isoMsg;
	private String drAccount;
	private String crAccount;
	private int amount;
	private String narration;
	private int stan;
	private final static Logger logger = Logger.getLogger(IsoPosting.class);

	public IsoPosting(String drAccount, String crAccount, int amount, int stan, String narration) {
		this.drAccount = drAccount;
		this.crAccount = crAccount;
		this.amount = amount;
		this.narration = narration;
		this.stan = stan;

		try {
			// InputStream xmlFile = this.getClass().getResourceAsStream("basic.xml");
			// GenericPackager packager = new GenericPackager(xmlFile);
			this.channel = new ASCIIChannel(host, port, new ISO93APackager());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public IsoPosting() {
	}

	public String start(IsoPosting posting, ISOMsg isoMessage) {
		try {
			this.channel = new ASCIIChannel(host, port, new ISO93APackager());
		} catch (Exception e) {
			logger.error(e);
		}
		this.isoMsg = isoMessage;
		ExecutorService service;
		Future<String> task;
		String responseCode = null;
		service = Executors.newFixedThreadPool(1);
		posting.setValues("xxx", "xxx", 62, 758756, "xxx");
		// task = service.submit(new BCPosting2("xxx", "xxx", 62, 758756, "xxx"));
		task = service.submit(posting);
		try {
			responseCode = task.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		service.shutdownNow();
		logger.info("Finacle response code is: " + responseCode);
		return responseCode;
	}

	public void start2(IsoPosting posting, ISOMsg isoMessage) {
		try {
			this.channel = new ASCIIChannel(host, port, new ISO93APackager());
		} catch (Exception e) {
			logger.error(e);
		}
		this.isoMsg = isoMessage;
		ExecutorService service;
		Future<String> task;
		String responseCode = null;
		service = Executors.newFixedThreadPool(1);
		task = service.submit(posting);
		try {
			responseCode = task.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		service.shutdownNow();
		logger.info("Finacle response code is: " + responseCode);
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
	private void setValues(String drAccount, String crAccount, int amount, int stan, String narration) {
		this.drAccount = drAccount;
		this.crAccount = crAccount;
		this.amount = amount;
		this.narration = narration;
		this.stan = stan;
	}

	public String call() {
		//String responseCode = null;
		String message = null;
		try {
			channel.connect();
			if (channel.isConnected()) {
				logger.info("Channel connected. Sending...");
			}

			channel.send(this.isoMsg);
			ISOMsg r = channel.receive();
			if (r != null) {
				System.out.println("Response received");
			}
			logISOMsg(this.isoMsg);

			byte[] data = this.isoMsg.pack();
			message = new String(data);
			logger.info("RESULT : " + message);

//			responseCode = r.getString(39);
//			String responseString = "";
//			for (int i = 1; i <= r.getMaxField(); i++) {
//				if (r.hasField(i)) {
//					responseString += "Field " + i + ": " + r.getString(i) + "|";
//				}
//			}
//
//			if (r.getString(39).equals("000")) {
//				logger.info("Finacle successfully posted from: " + drAccount + " to " + crAccount + " => Response "
//						+ responseString);
//			} else {
//				logger.error("Finacle posting from: " + drAccount + " to " + crAccount + " failed => Response "
//						+ responseString);
//			}
			channel.disconnect();
		} catch (IOException e) {
			logger.error(e);
			return null;
		} catch (ISOException e) {
			logger.error(e);
			return null;
		}
		return message;
	}

	private void logISOMsg(ISOMsg msg) {
		System.out.println("----ISO MESSAGE-----");
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
			System.out.println("--------------------");
		}

	}

}