package com.rumango.median.iso.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rumango.median.iso.entity.AuditLog;

@Repository
public interface AuditLogRepository extends CrudRepository<AuditLog, Long> {
//	default AuditLog findByIntId(Integer id) {
//		Optional<AuditLog> al = findById(id);
//		return al.get();
//	}
//
//	Optional<AuditLog> findById(Integer id);

	//@Query(value = "select * from median_audit_log a where a.request_ip = ?1", nativeQuery = true)
	public List<AuditLog> findById(int id);

	// @Query(value = "select * from median_audit_log a where a.id = ?1",
	// nativeQuery = true)
	List<AuditLog> findByIpAddress(String id);

}
