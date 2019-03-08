package com.sky.config.processor;


import com.sky.config.PropertyRefreshUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * 
 * @author ever4y 扩展文件加载处理器
 */
public class LocalServerPorpertySourcesProcessor {
	private static final Logger logger = LoggerFactory.getLogger(LocalServerPorpertySourcesProcessor.class);

	private static LocalServerPorpertySourcesProcessor processor;

	private String localCfgPath;
	private Properties lclserverProperties = new Properties();

	protected Set<String> localFiles = new HashSet<String>();

	/**
	 * 主要要使用的本地文件
	 * @param inLocalFiles
	 */
	public void addLocalFiles(List<String> inLocalFiles) {
		this.localFiles.addAll(inLocalFiles);
		initializeLocalConfig();
	}

	/**
	 * 
	 */
	private Map<String, Long> lclServerPropObsMap = new HashMap<String, Long>();

	/**
	 * read the local extend config file
	 * 
	 * @return
	 */
	public Properties getLclserverProperties() {
		return lclserverProperties;
	}

	/**
	 * 热更新本地扩展文件内容
	 */
	public void runtimeRefreshLclServerProperty() {
		// 只刷新系统监控的配置文件
		for (String fileName : localFiles) {
			File file = new File(fileName);
			if (!file.isFile()) {
				return;
			}
			// 检查文件是否有变化
			if (file.exists() && file.canRead()) {
				Long lastModified = file.lastModified();
				Long lastModifyInObserver = lclServerPropObsMap.get(file
						.getName());
				if (!lastModified.equals(lastModifyInObserver)) {
					logger.info("local file "+file.getName()+ " has been reloaded!");
					lclServerPropObsMap.put(file.getName(), lastModified);
					Properties property = PropertyRefreshUtils
							.readFileIntoProperty(file);
					lclserverProperties.putAll(property);
				}
			}
		}
	}

	/**
	 * return the local server property path
	 * 
	 * @return
	 */
	public String getLclserverPropDir() {
		return localCfgPath;
	}

	/**
	 * private constructor
	 */
	private LocalServerPorpertySourcesProcessor() {
		
	}

	/**
	 * Singleton initialize
	 * 
	 * @return
	 */
	public static LocalServerPorpertySourcesProcessor getInstance() {
		if (processor != null) {
			return processor;
		}
		synchronized (LocalServerPorpertySourcesProcessor.class) {
			if (processor == null) {
				processor = new LocalServerPorpertySourcesProcessor();
			}
		}
		return processor;
	}

	/**
	 * 初始化本地扩展文件
	 */
	private void initializeLocalConfig() {
		logger.info("begin to intilizing local file define in ada xsd into properties");
		for (String fileName : localFiles) {
			File file = new File(fileName);

			if (file.exists() && file.canRead()) {
				Long lastModified = file.lastModified();
				lclServerPropObsMap.put(file.getName(), lastModified);
				Properties property = PropertyRefreshUtils.readFileIntoProperty(file);
				logger.info("Initilizing .... local file "+file.getName()+ " in to processor!");
				lclserverProperties.putAll(property);
			}
		}

	}

}
