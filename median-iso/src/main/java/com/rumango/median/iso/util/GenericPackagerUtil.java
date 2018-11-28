/*package com.rumango.median.iso.util;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.stereotype.Component;

@Component
public class GenericPackagerUtil {

	private GenericPackager genericPackager = null;
	private final static Logger logger = Logger.getLogger(GenericPackagerUtil.class);

	public GenericPackager getPackager() {
		try {
			if (genericPackager == null) {
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				InputStream inputstream = classLoader.getResourceAsStream("basic.xml");
				genericPackager = new GenericPackager(inputstream);
				return genericPackager;
			} else
				return genericPackager;
		} catch (ISOException e) {
			logger.error(e);
			return null;
		}
	}
}
*/