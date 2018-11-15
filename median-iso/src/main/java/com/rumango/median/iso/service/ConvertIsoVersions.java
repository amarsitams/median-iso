package com.rumango.median.iso.service;

import org.jpos.iso.ISOMsg;

public interface ConvertIsoVersions {
	public ISOMsg iso93TO87(ISOMsg isoMsg);

	public ISOMsg iso87TO93(ISOMsg isoMsg);
}
