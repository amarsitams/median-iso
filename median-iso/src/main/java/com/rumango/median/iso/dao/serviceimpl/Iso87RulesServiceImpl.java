package com.rumango.median.iso.dao.serviceimpl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rumango.median.iso.dao.Iso87RulesRepository;
import com.rumango.median.iso.dao.service.Iso87RulesService;
import com.rumango.median.iso.entity.IsoRule;

@Service
public class Iso87RulesServiceImpl implements Iso87RulesService {

	@Autowired
	Iso87RulesRepository iso87RulesRepository;

	@Override
	public IsoRule getRuleById(int id) {
		return iso87RulesRepository.findById(id);
	}

	@Override
	public List<IsoRule> getAllRules() {
		return (List<IsoRule>) iso87RulesRepository.findAll();
	}

	@Override
	public void saveISO87Rule(IsoRule rule) {
		if (rule.getMakerTimestamp() == null)
			rule.setMakerTimestamp(new Timestamp(System.currentTimeMillis()));
		iso87RulesRepository.save(rule);
	}

	@Override
	public void saveAllISO87Rules(List<IsoRule> rules) {
		iso87RulesRepository.saveAll(rules);
	}

	public IsoRule addRule() {
		IsoRule rule = new IsoRule();
		rule.setExternlSystem("Sample external System");
		rule.setTargetSystem("Sample target System");
		rule.setFieldNumber(49);
		rule.setFieldDescription("Field Description");
		rule.setRule("PASS");
		rule.setDefaultValue("10");
		rule.setRecordStatus("R");
		rule.setAuthStatus("A");
		rule.setMaker("A");
		rule.setChecker("C");
		rule.setCheckerTimestamp(new Timestamp(System.currentTimeMillis()));
		rule.setMakerTimestamp(new Timestamp(System.currentTimeMillis()));
		rule.setModNo(12);
		return iso87RulesRepository.save(rule);
	}

	@Override
	public IsoRule findByFieldNumber(int id) {
		// return iso87RulesRepository.findByFieldNumber(id);
		Optional<IsoRule> rule = iso87RulesRepository.findById((long) id);
		return rule.get();
	}

}
