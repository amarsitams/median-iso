package com.rumango.median.iso.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rumango.median.iso.entity.ServerDetails;

@Repository
public interface ServerDetailsRepository extends CrudRepository<ServerDetails, Long> {
	ServerDetails findById(Integer id);
}
