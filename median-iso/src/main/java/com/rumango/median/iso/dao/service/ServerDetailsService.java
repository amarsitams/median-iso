package com.rumango.median.iso.dao.service;

import java.util.List;

import com.rumango.median.iso.entity.ServerDetails;

public interface ServerDetailsService {
	
	public ServerDetails getDetailsById(int id);

	public List<ServerDetails> getAllDetails();

	public void saveServerDetails(ServerDetails serverDetails);

	public void saveAllServerDetails(List<ServerDetails> serverDetailsList);
}
