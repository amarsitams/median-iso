package com.rumango.median.iso.dao.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rumango.median.iso.dao.ServerDetailsRepository;
import com.rumango.median.iso.dao.service.ServerDetailsService;
import com.rumango.median.iso.entity.ServerDetails;

@Service
public class ServerDetailsServiceImpl implements ServerDetailsService {

	@Autowired
	private ServerDetailsRepository repo;

	@Override
	public ServerDetails getDetailsById(int id) {
		return repo.findById(id);
	}

	@Override
	public List<ServerDetails> getAllDetails() {
		return (List<ServerDetails>) repo.findAll();
	}

	@Override
	public void saveServerDetails(ServerDetails serverDetails) {
		repo.save(serverDetails);
	}

	@Override
	public void saveAllServerDetails(List<ServerDetails> serverDetailsList) {
		repo.saveAll(serverDetailsList);
	}

}
