package com.rumango.median.iso.dao.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rumango.median.iso.dao.IsoLovRepository;
import com.rumango.median.iso.dao.service.Iso87LovService;
import com.rumango.median.iso.entity.LovRule;

@Service
public class Iso87LovServiceImpl implements Iso87LovService {

	@Autowired
	IsoLovRepository repo;

	@Override
	public LovRule getRuleById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllRules(int fieldNumber) {
		return repo.getStringValues(fieldNumber);
	}

	@Override
	public void saveISO87Lov(LovRule rule) {
		repo.save(rule);
	}

	@Override
	public void saveAllISO87Lovs(List<LovRule> rules) {
		repo.saveAll(rules);
	}

	@Override
	public List<LovRule> getAllRules() {
		return (List<LovRule>) repo.findAll();
	}

}
