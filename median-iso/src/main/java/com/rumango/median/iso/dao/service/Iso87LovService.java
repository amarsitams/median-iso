package com.rumango.median.iso.dao.service;

import java.util.List;

import com.rumango.median.iso.entity.LovRule;

public interface Iso87LovService {
	public LovRule getRuleById(int id);

	public List<String> getAllRules(int fieldNumber);

	public List<LovRule> getAllRules();

	public void saveISO87Lov(LovRule rule);

	public void saveAllISO87Lovs(List<LovRule> rules);
}
