package com.rumango.median.iso.dao;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rumango.median.iso.entity.LovRule;

@Repository
public interface IsoLovRepository extends CrudRepository<LovRule, Integer> {

	List<LovRule> findByFieldNumber(int fieldNumber);

	default List<String> getStringValues(int fieldNumber) {
		List<LovRule> lovRules = findByFieldNumber(fieldNumber);
		List<String> stringList = new LinkedList<>();
		for (LovRule rule : lovRules) {
			stringList.add(rule.getLovValue());
		}
		return stringList;
	}
}