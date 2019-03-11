package com.adachina.cfgcenter.config.processor.context;

import com.adachina.cfgcenter.config.ResourceConstant;
import com.adachina.cfgcenter.config.localize.LocalizeFileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;

/**
 * 
 * @author 读取本地文件到 environment，优先级低于远程中心
 */
public class EnvironmentContextProcessor {

	private static final Logger logger = LoggerFactory
			.getLogger(EnvironmentContextProcessor.class);


	/**
	 * Spring配置容器
	 */
	private ConfigurableEnvironment environment;

	private String appResourceFile = "classpath:META-INF/app.properties";

	private ResourceLoader resourceLoader = new DefaultResourceLoader();

	public void setEnvironment(Environment environment) {
		this.environment = (ConfigurableEnvironment) environment;
	}

	/**
	 * MetaInfo中的app.property读入SpringContext
	 */
	private void loadMetaInfoIntoContext() {
		try {
			logger.info("Here start to initialize meta-inf into enviroment!");
			org.springframework.core.io.Resource resource = resourceLoader
					.getResource(appResourceFile);
			if (!resource.exists()){
				return;
			}
			ResourcePropertySource rps = new ResourcePropertySource(resource);
			environment.getPropertySources().addLast(rps);
			logger.info("Here end initialize meta-inf into enviroment!");
		} catch (IOException e) {
			logger.error("Initialize meta app resource into environment error:"
					+ appResourceFile, e);
		}
	}

	/**
	 * 加载系统默认配置文件
	 */
	private void loadDefaultResourceIntoContext() {
		for(String rs: LocalizeFileLoader.getResources()){
			String rsName = ResourceConstant.DEFAULT_RESOUCE_PATH + rs;
			try {
				org.springframework.core.io.Resource resource = resourceLoader.getResource(rsName);
				if (!resource.exists()){
					return;
				}
				ResourcePropertySource rps = new ResourcePropertySource(
						ResourceConstant.LOWEST_PIRORITY_RESOURCE_PREFIX
								+ resource.getFilename(), resource);
				for (String propName : rps.getPropertyNames()) {
					logger.info("in resourece for default file:"
							+ ResourceConstant.DEFAULT_RESOUCE_PATH + "  property:"
							+ propName + " value is :" + rps.getProperty(propName));
				}
				environment.getPropertySources().addLast(rps);
			} catch (IOException e) {
				logger.error("Initialize default resource into environment error:"
						+ ResourceConstant.DEFAULT_RESOUCE_PATH, e);
			}
		}
	}

	/**
	 * 启动本地文件扫描线程
	 */
	public void postProcess() throws BeansException{
		logger.info("In "+this.getClass().getName()+" refresh context ...");
		
		// 再加载MetaInfo
		loadMetaInfoIntoContext();
		// 加载系统默认资源 -- 优先级最低 最后加载
		loadDefaultResourceIntoContext();

		
	}
	
}
