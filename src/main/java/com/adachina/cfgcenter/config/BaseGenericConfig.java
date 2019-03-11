package com.adachina.cfgcenter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * @author
 */
@Component
public final class BaseGenericConfig{


	private static final Logger logger = LoggerFactory.getLogger(BaseGenericConfig.class);

	@Autowired
	private ConfigCenterProcessor configProcessor;
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return configProcessor.getProperty(key, null);
	}

	/**
	 * 
	 * @param key
	 * @param dftVal
	 * @return
	 */
	public String getPropertyWithDft(String key, String dftVal) {
		return configProcessor.getProperty(key, dftVal);
	}


}
