package com.rumango.median.iso.dao.service;

import java.util.List;
import java.util.Map;

import org.jpos.iso.ISOMsg;

import com.rumango.median.iso.entity.AuditLog;

public interface AuditLogService {
	public void saveData(Map<String, ISOMsg> isoMap, Map<String, String> statusMap);

	public void saveData(Map<String, String> statusMap);
	
	public AuditLog findById(int id);

	public List<AuditLog> getAllLogs();
}
