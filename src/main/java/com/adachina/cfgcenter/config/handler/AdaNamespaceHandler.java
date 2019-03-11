package com.adachina.cfgcenter.config.handler;

import com.adachina.cfgcenter.config.PropertyRefreshUtils;
import com.adachina.cfgcenter.config.ResourceConstant;
import com.adachina.cfgcenter.config.localize.LocalizeFileLoader;
import com.ctrip.framework.apollo.core.utils.StringUtils;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.adachina.cfgcenter.config.BaseGenericConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public class AdaNamespaceHandler extends NamespaceHandlerSupport {

	private static final Logger logger = LoggerFactory.getLogger(BaseGenericConfig.class);

	private static final Splitter NAMESPACE_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();
	
	private static final String FILE_NAME_PREFIX = "/";

	@Override
	public void init() {
		registerBeanDefinitionParser("config", new LocalizeBeanParser());
	}

	static class LocalizeBeanParser extends AbstractSingleBeanDefinitionParser {
		@Override
		protected Class<?> getBeanClass(Element element) {
			return LocalizeFileLoader.class;
		}

		@Override
		protected boolean shouldGenerateId() {
			return true;
		}

		@Override
		protected void doParse(Element element, BeanDefinitionBuilder builder) {
			String namespaces = element.getAttribute("files");
			
			processXSDLocalFile(namespaces);
			
			String resources = element.getAttribute("resources");
			
			processXSDResources(resources);
			
			
		}

		/**
		 * 处理xsd中的本地扩展文件
		 * @param namespaces
		 */
		public void processXSDLocalFile(String namespaces) {
			logger.info("Initilized ADA xsd namespace,readed for files:"+namespaces);
			// 没有文件则不加载
			if (Strings.isNullOrEmpty(namespaces)) {
				return;
			}
			List<String> files = new ArrayList<String>();

			boolean isWin = isOSWindows();

			for (String fileName : NAMESPACE_SPLITTER.splitToList(namespaces)) {
				if (StringUtils.isBlank(fileName)) {
					continue;
				}
				logger.info("read file name from ADA xsd:"+fileName);
				if(!fileName.startsWith(FILE_NAME_PREFIX)) {
					fileName = FILE_NAME_PREFIX + fileName;
				}
				fileName = PropertyRefreshUtils.getFileConfigPath() + fileName;
				//如果是windows，则需要替换文件 ；
				if(isWin){
					fileName = fileName.replace(FILE_NAME_PREFIX, File.separator);
					logger.info("系统为Windows：加载文件 ："+fileName);
				}

				files.add(fileName);
			}
			LocalizeFileLoader.addFileNames(files);
		}
		
		/**
		 * @param resources
		 */
		public void processXSDResources(String resources) {
			logger.info("Initilized ADA xsd ,readed for resources:"+resources);
			// 没有文件则不加载
			if (Strings.isNullOrEmpty(resources)) {
				resources = ResourceConstant.DEFAULT_RESOURCE;
			}
			List<String> resource = new ArrayList<String>();
			for (String fileName : NAMESPACE_SPLITTER.splitToList(resources)) {
				if (StringUtils.isBlank(fileName)) {
					continue;
				}
				logger.info("read file name from ADA xsd:"+fileName);
				resource.add(fileName);
			}
			LocalizeFileLoader.addResources(resource);
		}
		
		
	}


	/**
	 * 是否为windows系统
	 * @return
	 */
	public static boolean isOSWindows() {
		String osName = System.getProperty("os.name");
		if (Strings.isNullOrEmpty(osName)) {
			return false;
		}
		return osName.startsWith("Windows");

	}



}
