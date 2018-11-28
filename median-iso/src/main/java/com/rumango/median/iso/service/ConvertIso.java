package com.rumango.median.iso.service;

import org.jpos.iso.ISOMsg;

public interface ConvertIso {

	public ISOMsg genericTO93(ISOMsg isoMsg);
	
	public ISOMsg iso93TOGeneric(ISOMsg isoMsg);

	public ISOMsg iso93TO87(ISOMsg isoMsg);

	public ISOMsg iso87TO93(ISOMsg isoMsg);

	public ISOMsg doValidations(ISOMsg isoMsg);
}
