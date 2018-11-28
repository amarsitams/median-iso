package com.rumango.median.iso.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rumango.median.iso.service.ValidationUtil;

@Service
public class ValidationUtilImpl implements ValidationUtil {

//	@Autowired
//	private GenericRepository genericRepo;

	private int external_system_id, node_map_id, tag_map_id, from_system_id = 3, to_system_id = 2;
	private String ruleType = "default";

	private String ext_sys_code = "NAV789";

	@Override
	public String validate(int fieldNo, String currentValue) {
		// rule = genericRepo.findByFieldNumber(fieldNo);
		if (ruleType.equalsIgnoreCase("default") || ruleType.equalsIgnoreCase("DB"))
			// return currentValue.length() > 0 ? currentValue : rule.getDefaultValue();
			return null;// genericRepo.getDefaultValue();

		else if (ruleType.equalsIgnoreCase("pass") || ruleType.equalsIgnoreCase("echo"))
			return currentValue;

		else if (ruleType.equalsIgnoreCase("wildcard")) {
			return null;
		} else if (ruleType.equalsIgnoreCase("lov")) {
			List<String> lovRules = null;// genericRepo.getStringValues(fieldNo);
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
