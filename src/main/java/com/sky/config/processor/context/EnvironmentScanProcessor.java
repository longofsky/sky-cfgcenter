package com.sky.config.processor.context;

import com.sky.config.ConfigCenterProcessor;
import com.sky.config.processor.LocalRepostoryObserveScanner;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * 
 * @author
 * 启动本地配置文件热更新
 */
@Component
public class EnvironmentScanProcessor implements InitializingBean{

	@Autowired
	private ConfigCenterProcessor configProcessor;
	
	
	/**
	 * 启动本地文件扫描线程
	 */
	@Override
	public void afterPropertiesSet() throws Exception{
		// 启动热更新
		LocalRepostoryObserveScanner scanner = new LocalRepostoryObserveScanner(configProcessor);
		ExecutorService executorService = newFixedThreadPool(1);
		executorService.submit(scanner);
	}
}
