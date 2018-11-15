package com.rumango.median.iso.dao.serviceimpl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.iso.packager.ISO93APackager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rumango.median.iso.dao.AuditLogRepository;
import com.rumango.median.iso.dao.service.AuditLogService;
import com.rumango.median.iso.entity.AuditLog;

@Service
public class AuditLogServiceImpl implements AuditLogService {

	private ISO87APackager packager87;
	private ISO93APackager packager93;
	private final static Logger logger = Logger.getLogger(AuditLogServiceImpl.class);
	private boolean isPCIMask = true;

	@Autowired
	private AuditLogRepository auditLogRepository;

	// Modified for postgre sql
	@Transactional
	public void saveData(ISOMsg originalRequestISOMsg, ISOMsg modifiedRequestISOMsg, ISOMsg originalResponseISOMsg,
			ISOMsg modifiedResponseISOMsg, Map<String, String> statusMap) {
		logger.info(" Inside save data ");
		try {
			AuditLog log = new AuditLog();
			log.setTimeStamp(new Timestamp(System.currentTimeMillis()));
			log.setIpAddress(statusMap.containsKey("IP") ? statusMap.get("IP") : null);
			log.setMedianUuid(statusMap.containsKey("uuid") ? statusMap.get("uuid") : null);
			log.setRequestStatus(
					statusMap.containsKey("receivedMsgStatus") ? statusMap.get("receivedMsgStatus") : "FAIL");
			log.setResponseStatus(statusMap.containsKey("sentMsgStatus") ? statusMap.get("sentMsgStatus") : "FAIL");
			log.setOriginal_request(originalRequestISOMsg == null ? statusMap.get("originalRequestISOMsg")
					: isoToString(originalRequestISOMsg, "93"));
			log.setModified_request_isomsg(
					modifiedRequestISOMsg == null ? null : isoToString(modifiedRequestISOMsg, "87"));
			log.setOriginal_response_isomsg(
					originalResponseISOMsg == null ? null : isoToString(originalResponseISOMsg, "87"));
			log.setModified_response_isomsg(
					modifiedResponseISOMsg == null ? null : isoToString(modifiedResponseISOMsg, "93"));
			log.setOriginal_request_splitted(originalRequestISOMsg == null ? null
					: isoSplittedString(originalRequestISOMsg, "originalRequestISOMsg"));
			log.setModified_request_splitted(modifiedRequestISOMsg == null ? null
					: isoSplittedString(modifiedRequestISOMsg, "modifiedRequestISOMsg"));
			log.setOriginal_response_splitted(originalResponseISOMsg == null ? null
					: isoSplittedString(originalResponseISOMsg, "originalResponseISOMsg"));
			log.setModified_response_splitted(modifiedResponseISOMsg == null ? null
					: isoSplittedString(modifiedResponseISOMsg, "modifiedResponseISOMsg"));
			auditLogRepository.save(log);
			logger.info(log.toString());
		} catch (Exception e) {
			logger.info("Exception while saving  data ", e);
		}
		// logger.info(" saved data successfully");
	}

	private String isoToString(ISOMsg isoMessage, String version) {
		try {
//			if (version.equalsIgnoreCase("87")) {
//				packager87 = new ISO87APackager();
//				isoMessage.setPackager(packager87);
//				return new String(isoMessage.pack());
//			} else {
//				packager93 = new ISO93APackager();
//				isoMessage.setPackager(packager93);
//				return new String(isoMessage.pack());
//			}
			return new String(isoMessage.pack());
		} catch (ISOException e) {
			logger.info("isoMessage.getString(12); " + isoMessage.getString(12));
			logger.warn(" Exception in isoToString ", e);
			return null;
		}
	}

	private String isoSplittedString(ISOMsg isoMessage, String message) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append(" MTI : " + isoMessage.getMTI());
			for (int i = 1; i <= isoMessage.getMaxField(); i++) {
				if (isoMessage.hasField(i)) {
					if (i == 2 && isPCIMask) {
						sb.append(",  " + i + ": " + mask(isoMessage.getString(i)));
					} else
						sb.append(",  " + i + ": " + isoMessage.getString(i));
				}
			}
			logger.info(message + sb);
			String response = sb.toString();
			logger.info("response in byte array " + response);
			return response;
		} catch (ISOException e) {
			logger.error("Exception occured" + e.getMessage());
			return null;
		}
	}

	private ISOMsg unpackMessage(String stringMessage, String isoVersion) {
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

	private String splittedString(String message, String version) {
		StringBuilder sb = new StringBuilder();
		ISOMsg isoMessage = unpackMessage(message, version);
		try {
			sb.append(" MTI : " + isoMessage.getMTI());
			for (int i = 1; i <= isoMessage.getMaxField(); i++) {
				if (isoMessage.hasField(i)) {
					if (i == 2 && isPCIMask) {
						sb.append(",  " + i + ": " + mask(isoMessage.getString(i)));
					} else
						sb.append(",  " + i + ": " + isoMessage.getString(i));
				}
			}
			logger.info(message + sb);
			String response = sb.toString();
			return response;
		} catch (ISOException e) {
			logger.error("Exception occured" + e.getMessage());
			return null;
		}
	}

	public void saveData(Map<String, ISOMsg> isoMap, Map<String, String> statusMap) {
		ISOMsg originalRequestISOMsg = null, modifiedRequestISOMsg = null, originalResponseISOMsg = null,
				modifiedResponseISOMsg = null;
		logger.info(" isoMap size " + isoMap.size());
		try {
			originalRequestISOMsg = isoMap.containsKey("originalRequestISOMsg") ? isoMap.get("originalRequestISOMsg")
					: null;
			modifiedRequestISOMsg = isoMap.containsKey("modifiedRequestISOMsg") ? isoMap.get("modifiedRequestISOMsg")
					: null;
			originalResponseISOMsg = isoMap.containsKey("originalResponseISOMsg") ? isoMap.get("originalResponseISOMsg")
					: null;
			modifiedResponseISOMsg = isoMap.containsKey("modifiedResponseISOMsg") ? isoMap.get("modifiedResponseISOMsg")
					: null;
			logger.info(" originalResponseISOMsg " + originalResponseISOMsg);

		} catch (Exception e) {
			logger.info(" Exception while saving data ");
		} finally {
			saveData(originalRequestISOMsg, modifiedRequestISOMsg, originalResponseISOMsg, modifiedResponseISOMsg,
					statusMap);
		}
	}

	private String mask(String accNo) {
		StringBuilder sb = new StringBuilder(accNo);
		if (sb.length() > 6) {
			sb.replace(6, accNo.length(), "X");
			return sb.toString();
		} else
			return sb.toString();

	}

	@Override
	@Transactional
	public void saveData(Map<String, String> statusMap) {
		logger.info(" Inside save data ");
		try {
			AuditLog auditLog = new AuditLog();
			auditLog.setTimeStamp(new Timestamp(System.currentTimeMillis()));
			auditLog.setIpAddress(statusMap.containsKey("IP") ? statusMap.get("IP") : null);
			auditLog.setExternalSystemName(
					statusMap.containsKey("externalSystemName") ? statusMap.get("externalSystemName") : null);
			auditLog.setMedianUuid(statusMap.containsKey("uuid") ? statusMap.get("uuid") : null);
			auditLog.setRequestStatus(
					statusMap.containsKey("receivedMsgStatus") ? statusMap.get("receivedMsgStatus") : "FAIL");
			auditLog.setResponseStatus(
					statusMap.containsKey("sentMsgStatus") ? statusMap.get("sentMsgStatus") : "FAIL");
			auditLog.setOriginal_request(
					statusMap.containsKey("originalRequestString") ? statusMap.get("originalRequestString") : null);
			auditLog.setModified_request_isomsg(
					statusMap.containsKey("modifiedRequestString") ? statusMap.get("modifiedRequestString") : null);
			auditLog.setOriginal_response_isomsg(
					statusMap.containsKey("originalResponseString") ? statusMap.get("originalResponseString") : null);
			auditLog.setModified_response_isomsg(
					statusMap.containsKey("modifiedResponseString") ? statusMap.get("modifiedResponseString") : null);

			auditLog.setOriginal_request_splitted(statusMap.containsKey("originalRequestString")
					? splittedString(statusMap.get("originalRequestString"), "93")
					: null);
			auditLog.setModified_request_splitted(statusMap.containsKey("modifiedRequestString")
					? splittedString(statusMap.get("modifiedRequestString"), "87")
					: null);
			auditLog.setOriginal_response_splitted(statusMap.containsKey("originalResponseString")
					? splittedString(statusMap.get("originalResponseString"), "87")
					: null);
			auditLog.setModified_response_splitted(statusMap.containsKey("modifiedResponseString")
					? splittedString(statusMap.get("modifiedResponseString"), "93")
					: null);
			auditLogRepository.save(auditLog);
			logger.info(auditLog.toString());
		} catch (Exception e) {
			logger.info("Exception while saving  data ", e);
		}
	}

	@Override
	public AuditLog findById(int id) {
		return auditLogRepository.findFromId(id);
	}

	@Override
	public List<AuditLog> getAllLogs() {
		return (List<AuditLog>) auditLogRepository.findAll();
	}

//	private byte[] isoToByteArray(ISOMsg isoMessage, String version) {
//	try {
//		if (version.equalsIgnoreCase("87")) {
//			packager87 = new ISO87APackager();
//			isoMessage.setPackager(packager87);
//			return isoMessage.pack();
//		} else {
//			packager93 = new ISO93APackager();
//			isoMessage.setPackager(packager93);
//			return isoMessage.pack();
//		}
//	} catch (ISOException e) {
//		return null;
//	}
//}
//
//private byte[] isoSplitted(ISOMsg isoMessage, String message) {
//	StringBuilder sb = new StringBuilder();
//	try {
//		sb.append("MTI : " + isoMessage.getMTI());
//		for (int i = 1; i <= isoMessage.getMaxField(); i++) {
//			if (isoMessage.hasField(i)) {
//				if (i == 2 && isPCIMask) {
//					sb.append(",  " + i + ": " + mask(isoMessage.getString(i)));
//				} else
//					sb.append(",  " + i + ": " + isoMessage.getString(i));
//			}
//		}
//		logger.info(message + sb);
//		byte[] response = sb.toString().getBytes();
//		logger.info("response in byte array " + new String(response));
//		return response;
//	} catch (ISOException e) {
//		logger.error("Exception occured" + e.getMessage());
//		return null;
//	}
//}
}
