package com.adachina.cfgcenter.config;

import com.adachina.cfgcenter.config.processor.LocalPropertySourcesDelegator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 
 * @author
 */
public class ConfigCenterProcessor {

	private static final Logger logger = LoggerFactory
			.getLogger(ConfigCenterProcessor.class);

	private final static Object INI_CONFIG_LOCKER = new Object();

	private final static Object RELOAD_CONFIG_LOCKER = new Object();

	private static final String EMPTY_DEFAULT = null;

	private static final String READ_FROM_DEFAULT = "read from default:";
	private static final String READ_FROM_APOLLO = "read from apollo:";
	private static final String READ_FROM_LOCALIZE = "read from localize file:";
	private static final String READ_FROM_RESOURCE = "read from resource:";

	@Autowired
	private Environment environment;

	private Map<String, String> paramsInUse = new HashMap<>(16);


	/**
	 * Read property value from remote repository
	 * 
	 * @param propKey
	 * @return
	 */
	private String getPropertyFromRemoteCFG(String propKey) {
		String val = environment.getProperty(propKey);
		logger.debug("read property " + propKey
				+ " from  config center JVM redis  value is:" + val);
		return val;
	}

	/**
	 * 
	 * @param name
	 * @param dftVal
	 * @return
	 */
	public String getProperty(String name, String dftVal) {
		logger.debug("Read property from repository key:" + name
				+ " default value:" + dftVal);
		String val = getPropertyFromRemoteCFG(name);
		logger.debug("Property value readed from repository:" + val);
		// 如果键值被清除，则直接返回默认值
		if (ResourceConstant.CLEAR_PROP_TAG.equals(val)) {
			logger.debug("Property value has been settered to cleared!");
			paramsInUse.put(name, READ_FROM_DEFAULT + dftVal);
			return dftVal;
		}
		// 如果是从APOLLO文件读取
		if (val != null) {
			paramsInUse.put(name, READ_FROM_APOLLO + val);
			return val;
		}
		// 如果配置中心未定义，则读取本地文件

		val = LocalPropertySourcesDelegator.getInstance().getLocalPropertyVal(name);
		if (val != null) {
			paramsInUse.put(name, READ_FROM_LOCALIZE + val);
			return val;
		}

		// 如果本地文件不存在，从默认文件读取
		val = LocalPropertySourcesDelegator.getInstance().getDefaltropertyVal(name);
		if (val != null) {
			paramsInUse.put(name, READ_FROM_RESOURCE + val);
			return val;
		}
		logger.debug("Final Property value readed from repository:" + val);
		paramsInUse.put(name, READ_FROM_DEFAULT + dftVal);
		return dftVal;

	}

}
