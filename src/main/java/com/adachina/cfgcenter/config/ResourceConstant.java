package com.adachina.cfgcenter.config;

/**
 * 
 * @author
 * 配置中心常量类
 */
public class ResourceConstant {

	public static final String APOLLO_PROPERTY_SOURCE_NAME = "ApolloPropertySources";
	
	public static final String DEFAULT_RESOUCE_PATH = "classpath:config/";
	
	public static final String DEFAULT_RESOURCE = "systemconfig.properties";
	
	public static final String HIGHEST_PIRORITY_RESOURCE_PREFIX = "_highest_resource_";
	public static final String LOWEST_PIRORITY_RESOURCE_PREFIX = "_lowest_resource_";
	public static final String MIDDLE_PRIORITY_RESOURCE_PREFIX = "_middle_resource_";
	
	public static final String MULTI_VERSION_PROPERTY = "_multi_ver";
	public static final String CLEAR_PROP_TAG = "_clear_prop";

	private ResourceConstant() {
		throw new IllegalStateException("Constant class");
	}
}
