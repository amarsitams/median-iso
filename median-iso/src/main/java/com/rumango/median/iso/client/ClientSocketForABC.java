package com.rumango.median.iso.client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Calendar;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import com.rumango.median.iso.entity.ServerDetails;

public class ClientSocketForABC {// implements DisposableBean implements InitializingBean

	private int maxResponseWaitingTime;
	private boolean isAsciiHeader = true;
	private String host;
	private int port;
	private String message;
	private Socket socket;
	private ServerDetails serverDetails;

	private final static Logger logger = Logger.getLogger(ClientSocketForABC.class);

	public static void main(String[] args) {

		ClientSocketForABC cs = new ClientSocketForABC();
		String iso = cs.logISOMsg(cs.test());
		cs.run(iso);
	}

	ClientSocketForABC() {
		setValues(4000, "172.16.10.232", 10090);
	}

	private void setValues(int waitingTime, String host, int port) {
		this.maxResponseWaitingTime = waitingTime;
		this.isAsciiHeader = true;
		this.host = host;
		this.port = port;
	}

	public void setValues() {
		serverDetails = null;
		this.maxResponseWaitingTime = 40000;
		this.isAsciiHeader = true;
		this.host = serverDetails.getHostIp();
		this.port = serverDetails.getHostPort();
	}

	public String run(String string) {
		try {
			logger.info("inside run of client IsoSocket");
			InetAddress address = InetAddress.getByName(host);
			logger.info("waitingTime ::: address ::  port " + "  ::: " + maxResponseWaitingTime + "  ::: " + address
					+ "  ::: " + port);
			socket = new Socket(address, port);
			logger.info(socket.toString());
			// Send the message to the server
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			message = string.trim();
			int len = message.length();
			if (len > 0) {
				String sendMessage = message;// getTcpHeader(len) + message;
				bw.write(sendMessage);
				bw.flush();
				logger.info("Message sent to the server : " + sendMessage);
				// Get the return message from the server
				InputStream is = socket.getInputStream();
				int msgLength = 0;
				byte[] responseMsg = null;
				byte[] b = new byte[4];
				for (int i = 0; (i < maxResponseWaitingTime) && (is.available() <= 0); i += 50) {
					System.out.print(".");
					Thread.sleep(50L);
				}
				if (is.available() <= 0) {
					logger.info("Response Not Availiable with in the " + maxResponseWaitingTime + " time");
					return "";
				}
				if (isAsciiHeader) {
					msgLength = is.read(b);
					msgLength = Integer.parseInt(new String(b, 0, msgLength));
				} else {
					for (int i = 0; i < 2; i++) {
						int ret;
						if ((ret = is.read()) != -1) {
							msgLength = msgLength << i * 8 | ret;
						}
					}
				}
				logger.info("Received Message Length is:" + msgLength);
				responseMsg = new byte[msgLength];
				is.read(responseMsg, 0, msgLength);
				String recievedMessage = new String(responseMsg);
				logger.info("Received Message is:" + recievedMessage);
				return recievedMessage;
			}
		} catch (Exception e) {
			logger.info("exception in run of ClientSocketForSwitch", e);
			return "";
		} finally {
			try {
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	private String getTcpHeader(int length) {
		String tcpHeader = "";
		if (isAsciiHeader) {
			tcpHeader = (length < 9 ? "000" : length < 99 ? "00" : length < 999 ? "0" : "") + length;
			logger.info("tcpHeader :" + tcpHeader);
			return tcpHeader;
		} else {
			tcpHeader = (char) (length - (length / 256) * 256) + tcpHeader;
			length = length / 256;
			tcpHeader = (char) (length) + tcpHeader;
			logger.info("tcpHeader :" + tcpHeader);
			return tcpHeader;
		}
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

	private String logISOMsg(@NotEmpty @NotNull ISOMsg msg) {
		StringBuilder responseString = new StringBuilder();
		try {
			System.out.println("-----------------" + "ISO" + "-----------------------");
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