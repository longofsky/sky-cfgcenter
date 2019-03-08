package com.sky.config.localize;

import com.ctrip.framework.apollo.spring.util.BeanRegistrationUtil;
import com.sky.config.ResourceConstant;
import com.sky.config.processor.LocalServerPorpertySourcesProcessor;
import com.sky.config.processor.context.EnvironmentContextProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePropertySource;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jibin
 * 本地文件扩展
 */
public class LocalizeFileLoader implements BeanFactoryPostProcessor, EnvironmentAware, BeanDefinitionRegistryPostProcessor {


    private static final Logger logger = LoggerFactory.getLogger(LocalizeFileLoader.class);

    private ConfigurableEnvironment environment;

    private ResourceLoader fileResourceLoader = new FileSystemResourceLoader();

    private static final String LINUX_FILE_PFEFIX_COMPENSION = "/";

    /**
     * 要使用的本地扩展文件
     */
    private static Set<String> fileNames = new HashSet<>();

    /**
     * 加载WAR包项目resource/config下配置文件
     */
    private static Set<String> resources = new HashSet<>();

    public static void addResources(List<String> inResource) {
        LocalizeFileLoader.resources.addAll(inResource);
    }


    public static void addFileNames(List<String> inFileNames) {
        LocalizeFileLoader.fileNames.addAll(inFileNames);
        LocalServerPorpertySourcesProcessor.getInstance().addLocalFiles(inFileNames);
    }

    public static Set<String> getFileNames() {
        return fileNames;
    }

    public static Set<String> getResources() {
        return resources;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        logger.info(getClass().getName() + "Here loading class into container");
        //注册后将调用LocalizeFileLoader 实现的BeanFactoryPostProcessor接口
        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry,
                LocalizeFileLoader.class.getName(), LocalizeFileLoader.class);


    }

    /**
     *
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        loadExtRsourceIntoContext();
        //加载默认空间的内容
        EnvironmentContextProcessor processor = new EnvironmentContextProcessor();
        processor.setEnvironment(environment);
        processor.postProcess();

    }

    /**
     * 本地扩展文件读进SpringContext
     */
    private void loadExtRsourceIntoContext() {
        for (String fileName : fileNames) {
            try {
                logger.info("load..........local ext config file:" + fileName);
                if (fileName.startsWith(LINUX_FILE_PFEFIX_COMPENSION)) {
                    fileName = LINUX_FILE_PFEFIX_COMPENSION + fileName;
                }
                org.springframework.core.io.Resource resource = fileResourceLoader.getResource(fileName);
                if (!resource.exists()) {
                    logger.error("==============file :" + fileName + " illeagle");
                    continue;
                }
                ResourcePropertySource rps = new ResourcePropertySource(ResourceConstant.MIDDLE_PRIORITY_RESOURCE_PREFIX + resource.getFile().getAbsolutePath(), resource);

                environment.getPropertySources().addAfter(ResourceConstant.APOLLO_PROPERTY_SOURCE_NAME, rps);
            } catch (IOException e) {
                logger.error("Initialize local ext resource into environment error:" + fileName, e);
            }
        }
    }


}
