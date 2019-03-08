package com.sky.config.processor;

import com.sky.config.ResourceConstant;
import com.sky.config.localize.LocalizeFileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;
import java.util.Properties;

/**
 * 
 * @author jibin 加载WAR包项目resource/config下配置文件，优先级最低
 *
 */
public class DefaultPropertyResourceProcessor {
	private static final Logger logger = LoggerFactory.getLogger(DefaultPropertyResourceProcessor.class);

	private static DefaultPropertyResourceProcessor processor = null;

	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	private Properties defaultProperties = new Properties();

	public Properties getDefaultProperties() {
		return defaultProperties;
	}

	private DefaultPropertyResourceProcessor() {
		loadDefaultResourceIntoContext();
	}

	/**
	 * 延迟加载单例
	 * 
	 * @return
	 */
	public static DefaultPropertyResourceProcessor getInstance() {
		if (processor != null) {
			return processor;
		}
		synchronized (DefaultPropertyResourceProcessor.class) {
			if (processor == null) {
				processor = new DefaultPropertyResourceProcessor();
			}
		}
		return processor;
	}

	/**
	 * 延迟加载单例
	 *
	 * @return
	 */
	public static void clear(){
		processor = null;
	}

	/**
	 * 
	 */
	public void loadDefaultResourceIntoContext() {
		for (String resourceName : LocalizeFileLoader.getResources()) {
			String rsName = ResourceConstant.DEFAULT_RESOUCE_PATH + resourceName;
			try {
				org.springframework.core.io.Resource resource = resourceLoader.getResource(rsName);
				if (!resource.exists()) {
					return;
				}
				ResourcePropertySource rps = new ResourcePropertySource(resource);
				defaultProperties.putAll(rps.getSource());
			} catch (IOException e) {
				logger.error("read default resouce error:" + rsName, e);
			}
		}

	}
}
