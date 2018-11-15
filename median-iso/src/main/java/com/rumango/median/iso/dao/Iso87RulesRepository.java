package com.rumango.median.iso.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rumango.median.iso.entity.IsoRule;

@Repository
public interface Iso87RulesRepository extends CrudRepository<IsoRule, Long> {
	IsoRule findById(Integer id);

	IsoRule findByFieldNumber(Integer fieldNumber);
}
