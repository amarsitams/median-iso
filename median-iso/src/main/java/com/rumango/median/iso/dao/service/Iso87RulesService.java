package com.rumango.median.iso.dao.service;

import java.util.List;

import com.rumango.median.iso.entity.IsoRule;

public interface Iso87RulesService {

	public IsoRule getRuleById(int id);

	public List<IsoRule> getAllRules();

	public void saveISO87Rule(IsoRule rule);

	public void saveAllISO87Rules(List<IsoRule> rules);

	public IsoRule findByFieldNumber(int id);
}
