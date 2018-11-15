package com.rumango.median.iso.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rumango.median.iso.dao.Iso87RulesRepository;
import com.rumango.median.iso.dao.IsoLovRepository;
import com.rumango.median.iso.entity.IsoRule;
import com.rumango.median.iso.service.ValidationUtil;

@Service
public class ValidationUtilImpl implements ValidationUtil {

	@Autowired
	private Iso87RulesRepository rulesrepo;

	@Autowired
	private IsoLovRepository lovRepo;

	private IsoRule rule;

	@Override
	public String validate(int fieldNo, String currentValue) {
		rule = rulesrepo.findByFieldNumber(fieldNo);
		String ruleType = rule.getRule();

		if (ruleType.equalsIgnoreCase("default") || ruleType.equalsIgnoreCase("DB"))
			// return currentValue.length() > 0 ? currentValue : rule.getDefaultValue();
			return rule.getDefaultValue();

		else if (ruleType.equalsIgnoreCase("pass") || ruleType.equalsIgnoreCase("echo"))
			return currentValue;

		else if (ruleType.equalsIgnoreCase("wildcard")) {
			return null;
		} else if (ruleType.equalsIgnoreCase("lov")) {
			List<String> lovRules = lovRepo.getStringValues(fieldNo);
			if (lovRules.contains(currentValue)) {
				return currentValue;
			} else
				return "000000";
		} else if (ruleType.equalsIgnoreCase("query"))
			return null;
		else
			return null;
	}
}
