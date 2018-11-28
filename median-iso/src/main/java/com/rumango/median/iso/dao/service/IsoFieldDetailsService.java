package com.rumango.median.iso.dao.service;

import java.util.List;

import com.rumango.median.iso.entity.IsoFiledDetails;

public interface IsoFieldDetailsService {
	public IsoFiledDetails getRuleById(int id);

	public List<IsoFiledDetails> getAllFields();
	
	public void saveField(IsoFiledDetails field);

	public void saveAllFields(List<IsoFiledDetails> fields);

}
