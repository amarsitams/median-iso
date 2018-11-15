package com.rumango.median.iso.socket.client;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rumango.median.iso.dao.ServerDetailsRepository;
import com.rumango.median.iso.entity.ServerDetails;

@Component
public class ClientSocketForSwitch {// implements DisposableBean implements InitializingBean

	@Value("${socket.maxResponseWaitingTime}")
	private int maxResponseWaitingTime;
	@Value("${socket.isAsciiHeader}")
	private boolean isAsciiHeader;
	@Value("${socket.host}")
	private String host;
	@Value("${socket.port}")
	private int port;
	private String message;
	private Socket socket;
	private ServerDetails serverDetails;
	
	@Autowired
	private ServerDetailsRepository  serverDetailsRepository;

	private final static Logger logger = Logger.getLogger(ClientSocketForSwitch.class);

	public void setValues(int waitingTime, boolean header, String host, int port) {
		this.maxResponseWaitingTime = waitingTime;
		this.isAsciiHeader = header;
		this.host = host;
		this.port = port;
	}
	
	public void setValues() {
		serverDetails = serverDetailsRepository.findById(1);
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
				String sendMessage = getTcpHeader(len) + message;
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
		} catch (Exception exception) {
			logger.info("exception in run of ClientSocketForSwitch" + exception.getMessage());
			exception.printStackTrace();
			return null;
		} 
//		finally {
//			close();
//		}
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

//	private void close() {
//		logger.info("closing socket ");
//		try {
//			if (socket != null)
//				socket.close();
//		} catch (Exception e) {
//			logger.info("Exception while closing socket  " + e.getMessage());
//		}
//	}

}