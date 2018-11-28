package com.rumango.median.iso.serviceimpl;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.iso.packager.ISO93APackager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rumango.median.iso.client.IsoJposResponse;
import com.rumango.median.iso.client.RestCall;
import com.rumango.median.iso.dao.service.AuditLogService;
import com.rumango.median.iso.model.ValidateChannel;
import com.rumango.median.iso.service.ConvertIso;
import com.rumango.median.iso.service.GetResponse;

@Service
public class GetResponseImpl implements GetResponse {

	private String originalRequestString, modifiedRequestString, originalResponseString, modifiedResponseString, reason;
	private ISOMsg originalRequestISOMsg, modifiedRequestISOMsg, originalResponseISOMsg, modifiedResponseISOMsg,
			response = null;
	private String receivedMsgStatus, sentMsgStatus;
	private ISO93APackager packager93;
	private ISO87APackager packager87;
	private GenericPackager genericPackager = null;
	@Autowired
	private ConvertIso convertIsoVersions;

	@Autowired
	private AuditLogService auditLogService;

	private final static Logger logger = Logger.getLogger(GetResponseImpl.class);

	private Map<String, String> arrayToMap(String[] arrayOfString) {
		return Arrays.asList(arrayOfString).stream().map(str -> str.split(":"))
				.collect(Collectors.toMap(str -> str[0], str -> str[1]));
	}

	private boolean validate(ISOMsg isoMsg) {
		boolean response = false;
		ValidateChannel vc = null;
		try {
			logger.info("Calling rest call to validate ");
			String channel = isoMsg == null ? "Channel Id Invalid" : isoMsg.getString(2);
			String tXiD = isoMsg == null ? "Transaction Id Invalid" : isoMsg.getString(125);
			logger.info("Channel Id :" + channel + " Transaction Id : " + tXiD);
			if (!channel.equalsIgnoreCase("Channel Id Invalid") && channel != null
					&& !tXiD.equalsIgnoreCase("Transaction Id Invalid") && tXiD != null)
				vc = RestCall.callRestApi(channel, tXiD);
			logger.info("ValidateChannel " + vc);
			logger.info("Amount " + isoMsg.getString(4) + "  Account Number" + isoMsg.getString(102));
			if (vc.getStatus().startsWith("00")) {
				if (vc.getAmount() == Long.parseLong(isoMsg.getString(4))) {
					if (vc.getAccountNumber().equalsIgnoreCase(isoMsg.getString(102)))
						response = true;
					else {
						response = false;
						reason = "Account number do not match";
					}
				} else {
					response = false;
					reason = "Amount do not match";
				}
			} else {
				response = false;
				reason = "Status not 00";
			}
		} catch (Exception e) {
			logger.error("Exception in validate", e);
			response = false;
		}
		return response;
	}

	private ISOMsg getIsoFromString(String message) {
		ISOMsg msg = new ISOMsg();
		try {
			String[] splitted = message.split(";");
			for (Map.Entry<String, String> entry : arrayToMap(splitted).entrySet()) {
				msg.set(Integer.parseInt(entry.getKey()), entry.getValue());
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return msg;
	}

	public String convertAndRespond(String stringMessage, Map<String, String> map) {
		ISOMsg msg = null;
		ISOMsg isoMsg = null;
		String response = null;
		try {
			map.put("originalRequestString", stringMessage);
			isoMsg = getIsoFromString(stringMessage);
			logISOMsg(isoMsg, "REQUEST");
			receivedMsgStatus = "SUCCESS";
		} catch (Exception e) {
			receivedMsgStatus = "FAIL";
			logger.error("Exception while unpacking", e);
		}
		logger.info("inside convertAndRespond of GetResponseImpl ");
		try {
			if (validate(isoMsg)) {
				isoMsg.setPackager(getPackager());
				Object[] objArray = IsoJposResponse.main(isoMsg);
				logger.info("objArray size ::" + objArray.length);
				response = (String) objArray[0];
				map.put("originalResponseString", response);
				logger.info("response ::" + response);
				msg = (ISOMsg) objArray[1];
				if (msg != null) {
					logISOMsg(msg, "RESPONSE");
					sentMsgStatus = "SUCCESS";
				}
			} else {
				logger.info("Validation failed with rest Api");
				sentMsgStatus = "FAIL";
				map.put("reason", reason);
			}
		} catch (Exception e) {
			logger.error("Exception inside convertAndRespond of GetResponseImpl ", e);
		} finally {
			map.put("receivedMsgStatus", receivedMsgStatus);
			map.put("sentMsgStatus", sentMsgStatus);
			logger.info("receivedMsgStatus " + receivedMsgStatus + " received Response Status   " + sentMsgStatus);
			try {
				logger.info("Map Size" + map.size());
				for (Map.Entry<String, String> set : map.entrySet()) {
					logger.info(set.getKey() + ":" + set.getValue());
				}
				auditLogService.saveData(map);
			} catch (Exception e) {
				logger.warn("Exception while saving log information ", e);
			}
		}
		return msg == null ? reason : msg.getString(39);
	}

	private ISOMsg unpackMessage(String stringMessage, String isoVersion)
			throws UnsupportedEncodingException, ISOException {
		try {
			ISOMsg isoMsg = new ISOMsg();
			if (isoVersion.equalsIgnoreCase("87")) {
				packager87 = new ISO87APackager();
				isoMsg.setPackager(packager87);
			}
			if (isoVersion.equalsIgnoreCase("93")) {
				packager93 = new ISO93APackager();
				isoMsg.setPackager(packager93);
			} else {
				genericPackager = getPackager();
				isoMsg.setPackager(genericPackager);
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
		}
		if (isoVersion.equalsIgnoreCase("93")) {
			packager93 = new ISO93APackager();
			isoMessage.setPackager(packager93);
		} else {
			genericPackager = getPackager();
			isoMessage.setPackager(genericPackager);
		}
		byte[] binaryImage = isoMessage.pack();
		return new String(binaryImage);
	}

	private ISOMsg validateRequest(ISOMsg isoMessage) {
		try {
			// isoMessage.set(49, "840");
			// return convertIsoVersions.iso93TO87(isoMessage);
			return isoMessage;
		} catch (Exception e) {
			logger.warn("Exception while updateRequestIso ", e);
		}
		return null;
	}

	private ISOMsg validateResponse(ISOMsg isoMessage) {
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
			logISOMsg(originalRequestISOMsg, "original Request message");

			// read and convert
			if (originalRequestISOMsg != null)
				modifiedRequestISOMsg = validateRequest(originalRequestISOMsg);

			logISOMsg(modifiedRequestISOMsg, "modified Request message");

		} catch (Exception e) {
			stringMessage = "";
			receivedMsgStatus = "FAIL";
			logger.error(" Exception while converting request iso message ", e);
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
			originalResponseISOMsg = unpackMessage(responseMsg, "gp");
			// logger.info("original_response_isomsg.toString()" + new
			// String(originalResponseISOMsg.pack()));
			logISOMsg(originalResponseISOMsg, "original response iso message");
			// isoMap.put("originalResponseISOMsg", originalResponseISOMsg);
			// read and convert
			modifiedResponseISOMsg = validateResponse(originalResponseISOMsg);
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

	private ISOMsg getResponse(ISOMsg isoMessage) throws Exception {
		logger.info("inside getResponse of IsoMessageConvertionImpl");
		try {
			response = null;// new IsoJposResponse().call(isoMessage);
		} catch (Exception e) {
			response = null;
			sentMsgStatus = "Exception while getResponse of IsoMessageConvertionImpl";
			logger.warn(sentMsgStatus);
			throw e;
		}
		if (response.toString() != "" && response != null)
			sentMsgStatus = "SUCCESS";
		return response;
	}

	private void logISOMsg(@NotEmpty @NotNull ISOMsg msg, String stringMessage) {
		// StringBuilder responseString = new StringBuilder();
		try {
			logger.info("-----------------" + stringMessage + "-----------------------");
			for (int i = 0; i <= msg.getMaxField(); i++) {
				if (msg.hasField(i)) {
					// responseString =
					// responseString.append(i).append(":").append(msg.getString(i)).append(";");
					logger.info(i + " " + ":" + msg.getString(i));
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured", e);
		}
		// return responseString.toString();
	}

	private String mask(String accNo) {
		StringBuilder sb = new StringBuilder(accNo);
		if (sb.length() > 6)
			sb.replace(6, accNo.length(), "X");
		return sb.toString();
	}

	private GenericPackager getPackager() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream inputstream = classLoader.getResourceAsStream("basic.xml");
			genericPackager = new GenericPackager(inputstream);
			return genericPackager;
		} catch (ISOException e) {
			logger.error("Exception while loading Generic Packager", e);
			return null;
		}
	}

//	public String convertAndRespond(ISOMsg input, Map<String, String> map) {
//		ISOMsg msg = null;
//		ISOMsg isoMsg = null;
//		String response = null;
//		try {
//			isoMsg = input;
//			logISOMsg(isoMsg, "ORIGINAL MESSAGE");
//
//			isoMsg.setPackager(getPackager());
//			isoMsg.pack();
//
//			logISOMsg(isoMsg, "GENERIC MESSAGE");
//		} catch (ISOException e) {
//			logger.error("Exception while unpacking", e);
//		}
//		logger.info("inside convertAndRespond of GetResponseImpl ");
//		try {
//			if (validate(isoMsg)) {
//				isoMsg.setPackager(getPackager());
//				Object[] objArray = IsoJposResponse.main(isoMsg);
//				logger.info("objArray size ::" + objArray.length);
//				response = (String) objArray[0];
//				logger.info("response ::" + response);
//				msg = (ISOMsg) objArray[1];
//				if (msg != null)
//					logISOMsg(msg, "RESPONSE MESSAGE");
//			} else
//				logger.info("Validation failed with rest Api");
//		} catch (Exception e) {
//			logger.error("Exception inside convertAndRespond of GetResponseImpl ", e);
//		} finally {
//			map.put("receivedMsgStatus", receivedMsgStatus);
//			map.put("sentMsgStatus", sentMsgStatus);
//			logger.info("receivedMsgStatus " + receivedMsgStatus + " received Response Status   " + sentMsgStatus);
//			try {
//				auditLogService.saveData(map);
//			} catch (Exception e) {
//				logger.warn("Exception while saving log information ", e);
//			}
//		}
//		return msg == null ? "ERROR" : msg.getString(39);
//	}
}
