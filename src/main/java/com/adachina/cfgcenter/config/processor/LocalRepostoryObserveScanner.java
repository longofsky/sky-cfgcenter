package com.adachina.cfgcenter.config.processor;

import com.adachina.cfgcenter.config.ConfigCenterProcessor;
import com.adachina.cfgcenter.config.PropertyRefreshUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * 对于本地扩展文件做热更新
 **/
public class LocalRepostoryObserveScanner implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(LocalRepostoryObserveScanner.class);

	private ConfigCenterProcessor configProcessor;
	
	/**
	 * 本地文件变更扫描时间
	 */
	private final static Long OBSERVE_SLEEP_TIME = 5000L;

	/**
	 * 配置文件内容变更检查
	 */
	private final Map<String, String> fileObserverMap = new HashMap<>();

	public LocalRepostoryObserveScanner(ConfigCenterProcessor configProcessor){
		this.configProcessor = configProcessor;
	}

	@Override
	public void run() {
		logger.info("Here start to observe the local config file");
		String repositoryDir = PropertyRefreshUtils.getLocalCacheRemoteRepositoryDir();
		int failCount = 0;
		for (;;) {
			try {
				Thread.currentThread().setName("配置中心自动加载线程");
				Thread.sleep(OBSERVE_SLEEP_TIME);
				logger.debug("In observe remote and local config change");
				observerLocalExtendFile();
			} catch (Exception e) {
				++failCount;
				logger.error("Configure center observe error", e);
			}

			// change this for sonar rules: Add an end condition to this loop
			if(failCount == Integer.MAX_VALUE) {
				logger.error("Configure center error reach max count and exit:{}", failCount);
				break;
			}
		}
	}


	/**
	 * 监控本地扩展文件
	 */
	public void observerLocalExtendFile() {
		LocalServerPorpertySourcesProcessor.getInstance().runtimeRefreshLclServerProperty();
	}

}
