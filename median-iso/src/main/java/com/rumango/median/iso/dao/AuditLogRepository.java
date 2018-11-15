package com.rumango.median.iso.dao;

import org.springframework.data.jpa.repository.Query;
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

	@Query(value = "select a from AuditLog a where a.id = ?1", nativeQuery = true)
	public AuditLog findFromId(Integer id);

}
