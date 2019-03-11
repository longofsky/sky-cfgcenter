package com.adachina.cfgcenter.config.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 
 * @author ever4y
 * 
 */
public class LocalPropertySourcesDelegator {
	
	private static final Logger logger = LoggerFactory.getLogger(LocalPropertySourcesDelegator.class);
		
	private static LocalPropertySourcesDelegator processor = null;

	private LocalPropertySourcesDelegator() {
		
	}
	/**
	 * 延迟加载单例
	 * @return
	 */
	public static LocalPropertySourcesDelegator getInstance(){
		if (processor != null) {
			return processor;
		}
		synchronized(LocalPropertySourcesDelegator.class) {
			if (processor == null) {
				processor = new LocalPropertySourcesDelegator();
			}
		}
		return processor;
	}
	

	
	/**
	 * 
	 * @param propName
	 * @return
	 */
	public String getLocalPropertyVal(String propName) {
		logger.debug("--Middle Priority-- read property value from local extension :"+propName);
		Properties props =  LocalServerPorpertySourcesProcessor.getInstance().getLclserverProperties();
		if (props == null){
			return null;
		}
		return props.getProperty(propName);
	}
	

	/**
	 * 
	 * @param propName
	 * @return
	 */
	public String getDefaltropertyVal(String propName) {
		logger.debug("--Lowest Priority-- read property value from default extension :"+propName);
		Properties props =  DefaultPropertyResourceProcessor.getInstance().getDefaultProperties();
		if (props == null){
			return null;
		}
		return props.getProperty(propName);
	}
	
}
