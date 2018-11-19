package com.rumango.median.iso.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.iso.packager.ISO93APackager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rumango.median.iso.dao.service.AuditLogService;
import com.rumango.median.iso.service.ConvertIsoVersions;
import com.rumango.median.iso.service.GetResponse;
import com.rumango.median.iso.socket.client.BCPosting;
import com.rumango.median.iso.socket.client.ClientSocketForSwitch;

@Service
public class GetResponseImpl implements GetResponse {

	private String modifiedRequestString, originalResponseString, modifiedResponseString, response;
	private ISOMsg originalRequestISOMsg, modifiedRequestISOMsg, originalResponseISOMsg, modifiedResponseISOMsg;
	private String receivedMsgStatus, sentMsgStatus;
	private ISO93APackager packager93;
	private ISO87APackager packager87;
	@Autowired
	private ConvertIsoVersions convertIsoVersions;

	@Autowired
	private ClientSocketForSwitch clientSocket;

//	@Autowired
//	private IsoPosting isoPosting;

	@Autowired
	private AuditLogService auditLogService;

	private final static Logger logger = Logger.getLogger(GetResponseImpl.class);

	public String convertAndRespond(String stringMessage, Map<String, String> map) {
		logger.info("inside convertAndRespond of IsoMessageConvertor ");
		map.put("originalRequestString", stringMessage);
		// logger.info("originalRequestString in byte []" + stringMessage.getBytes());
		try {
			// originalRequestString = stringMessage;
//			modifiedRequestString = convertRequest(stringMessage);
//			logger.info(" modifiedRequestString " + modifiedRequestString);
//			map.put("modifiedRequestString", modifiedRequestString);
//
//			originalResponseString = getResponse(modifiedRequestString);
//			logger.info("originalResponseString  " + originalResponseString);
//			map.put("originalResponseString", originalResponseString);
//
//			modifiedResponseString = convertResponse(originalResponseString);
			// modifiedResponseString = getResponse(stringMessage);
			//modifiedResponseString = isoPosting.start(isoPosting, unpackMessage(stringMessage, "93"));
			BCPosting.main();

//			logger.info(" modifiedResponseString " + modifiedResponseString);
//			map.put("modifiedResponseString", modifiedResponseString);
		} catch (Exception e) {
			modifiedResponseString = "";
			logger.warn("Exception inside convertAndRespond of IsoMessageConvertor ", e);
		} finally {
			map.put("receivedMsgStatus", receivedMsgStatus);
			map.put("sentMsgStatus", sentMsgStatus);
			logger.info("receivedMsgStatus " + receivedMsgStatus + " received Response Status   " + sentMsgStatus);
			try {
				// auditLogService.saveData(isoMap, statusMap);
				auditLogService.saveData(map);
			} catch (Exception e) {
				logger.warn("Exception while saving log information ", e);
			}
		}
		return modifiedResponseString;

	}

	private ISOMsg unpackMessage(String stringMessage, String isoVersion)
			throws UnsupportedEncodingException, ISOException {
		try {
			ISOMsg isoMsg = new ISOMsg();
			if (isoVersion.equalsIgnoreCase("87")) {
				packager87 = new ISO87APackager();
				isoMsg.setPackager(packager87);
			} else if (isoVersion.equalsIgnoreCase("93")) {
				packager93 = new ISO93APackager();
				isoMsg.setPackager(packager93);
			}
			isoMsg.unpack(stringMessage.getBytes("US-ASCII"));// "US-ASCII"
			return isoMsg;
		} catch (Exception e) {
			logger.warn("Exception while unpacking ", e);
			return null;
		}
	}

	private String packMessage(ISOMsg isoMessage, String isoVersion) throws ISOException {
		if (isoVersion.equalsIgnoreCase("87")) {
			packager87 = new ISO87APackager();
			isoMessage.setPackager(packager87);
		} else {
			packager93 = new ISO93APackager();
			isoMessage.setPackager(packager93);
		}
		byte[] binaryImage = isoMessage.pack();
		return new String(binaryImage);
	}

	private ISOMsg updateRequestIso(ISOMsg isoMessage) {
		try {
			// isoMessage.set(49, "840");
			return convertIsoVersions.iso93TO87(isoMessage);
		} catch (Exception e) {
			logger.warn("Exception while updateRequestIso ", e);
		}
		return null;
	}

	private ISOMsg updateResponseIso(ISOMsg isoMessage) {
		try {
			// isoMessage.set(49, "826");
			return convertIsoVersions.iso87TO93(isoMessage);
		} catch (Exception e) {
			logger.warn("Exception while updateResponseIso ", e);
		}
		return null;
	}

	private String convertRequest(String requestMsg) throws Exception {
		logger.info("inside convertRequest of IsoMessageConvertor ");
		String stringMessage = null;
		// unpack
		try {
			if (requestMsg == null | requestMsg == "")
				throw new Exception("Request message invalid");
			originalRequestISOMsg = unpackMessage(requestMsg, "93");
			// isoMap.put("originalRequestISOMsg", originalRequestISOMsg);
			logISOMsg(originalRequestISOMsg, "original Request message");
			// read and convert
			if (originalRequestISOMsg != null)
				modifiedRequestISOMsg = updateRequestIso(originalRequestISOMsg);
			// isoMap.put("modifiedRequestISOMsg", modifiedRequestISOMsg);
			logISOMsg(modifiedRequestISOMsg, "modified Request message");
			// pack
			if (modifiedRequestISOMsg != null)
				stringMessage = packMessage(modifiedRequestISOMsg, "87");
		} catch (Exception e) {
			stringMessage = "";
			receivedMsgStatus = "FAIL";
			logger.warn(" Exception while converting request iso message ");
			throw e;
		}
		if (stringMessage != "" && stringMessage != null)
			receivedMsgStatus = "SUCCESS";
		return stringMessage; // clientSocket.run(stringMessage);
	}

	private String convertResponse(String responseMsg) throws Exception {
		String stringMessage;
		try {
			// unpack
			if (responseMsg == null | responseMsg == "")
				throw new Exception("Response message invalid");
			originalResponseISOMsg = unpackMessage(responseMsg, "87");
			// logger.info("original_response_isomsg.toString()" + new
			// String(originalResponseISOMsg.pack()));
			logISOMsg(originalResponseISOMsg, "original response iso message");
			// isoMap.put("originalResponseISOMsg", originalResponseISOMsg);
			// read and convert
			modifiedResponseISOMsg = updateResponseIso(originalResponseISOMsg);
			// isoMap.put("modifiedResponseISOMsg", modifiedResponseISOMsg);
			logISOMsg(modifiedResponseISOMsg, "modified response message");
			// pack
			stringMessage = packMessage(modifiedResponseISOMsg, "93");
		} catch (Exception e) {
			stringMessage = "";
			sentMsgStatus = "FAIL";
			logger.warn("Exception while converting response iso message");
			throw e;
		}
		if (stringMessage != "" && stringMessage != null)
			sentMsgStatus = "SUCCESS";
		return stringMessage;
	}

	private String getResponse(String isoMessage) throws Exception {
		logger.info("inside getResponse of IsoMessageConvertionImpl");
		try {
			response = clientSocket.run(isoMessage);
			if (response == null)
				response = "0200F23A801F08A08010000000000400000014940400502010010100000000000020001024154319000001154319102410241024 00000000 00000000 00000000 0000000006940400768365278912CAN00001test                                    84001620182018201820180850201001";
			// clientSocket.setValues();
			// clientSocket.setValues(10000, true, "192.0.0.0", 2112);
			//
		} catch (Exception e) {
			response = "";
			sentMsgStatus = "Exception while getResponse of IsoMessageConvertionImpl";
			logger.warn(sentMsgStatus);
			throw e;
		}
		if (response != "" && response != null)
			sentMsgStatus = "SUCCESS";
		return response;
	}

	private void logISOMsg(@NotEmpty @NotNull ISOMsg msg, String stringMessage) {
		try {
			logger.info("-----------------" + stringMessage + "-----------------------");
			logger.info("  MTI : " + msg.getMTI());
			for (int i = 1; i <= msg.getMaxField(); i++) {
				if (msg.hasField(i)) {
					if (i == 2)
						logger.info(i + " " + " : " + mask(msg.getString(i)));
					else
						logger.info(i + " " + " : " + msg.getString(i));
				}
			}
		} catch (ISOException e) {
			logger.error("Exception occured" + e.getMessage());
		}
	}

	private String mask(String accNo) {
		StringBuilder sb = new StringBuilder(accNo);
		if (sb.length() > 6)
			sb.replace(6, accNo.length(), "X");
		return sb.toString();
	}

}
