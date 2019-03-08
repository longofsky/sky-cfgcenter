package com.sky.utils;


import com.ctrip.framework.apollo.core.utils.ClassLoaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置加载类
 * @author fanglin
 *
 */
public class PropertisConfig {

	private static Logger logger = LoggerFactory.getLogger(PropertisConfig.class);

	private static final String PREFIX_FILE = "file:";

	/**
	 * 系统参数的根目录
	 */
	public static final String FILE_CONFIG_PATH = "file_config_path";

	/**
	 * 加载所有的配置文件的参数到系统properties中
	 */
	private static Map<String, String> properties = new ConcurrentHashMap<String, String>();
	
	public static synchronized String getParameterByKey(String fileName,String key){
		String path = getSystemPath(fileName);
		if(null == path){
			return null;
		}
		return getFileConfig(path,fileName, key);
	}

	/**
	 * 从-DFILE_CONFIG_PATH 头参数目录中取文件，删除-D 
     * 会检查文件目录有效性
	 * @param fileName
	 * @return
	 * @author 戴德荣
	 */
	private static String getSystemPath(String fileName) {
		String fileConfigPath = getSystemFolderPath();
		File filePath = new File(fileConfigPath,fileName);
		if( ! filePath.exists()){
            return null;
        }
		return fileConfigPath;
	}


    /**
     * 从-DFILE_CONFIG_PATH 头参数目录中取文件，删除-D
     * 会检查文件目录有效性
     * @return
     * @author 戴德荣
     */
    public static String getSystemFolderPath() {
        String fileConfigPath = System.getProperty(FILE_CONFIG_PATH);
        if (fileConfigPath != null && ! fileConfigPath.isEmpty()) {
        		//删除前缀file:
        		if(fileConfigPath.startsWith(PREFIX_FILE)) {
        			fileConfigPath = fileConfigPath.replaceAll(PREFIX_FILE, "");
        			logger.debug("删除file:前缀以后的路径为："+fileConfigPath);
        		}
            logger.info("指定的系统参数路径为："+FILE_CONFIG_PATH+"="+fileConfigPath);
            File fileConfigPathFile = new File(fileConfigPath);
            if(! fileConfigPathFile.exists()){
            	//todo
            }
            if(! fileConfigPathFile.isDirectory()){
            	//todo
            }
            fileConfigPath = fileConfigPathFile.getAbsolutePath();
        }else {
            logger.warn("未设置系统参数"+FILE_CONFIG_PATH+"文件目录,生产及测试环境中推荐设置启动参数，使用-D"+FILE_CONFIG_PATH+"=/dir1/aaa,必须为目录");
            //读取classPath
            URL resource = PropertisConfig.class.getResource("/");
            fileConfigPath = resource.getFile();
            logger.warn("使用classpath为系统目录:"+fileConfigPath);

        }
        return fileConfigPath;
    }


	private static String getFileConfig(String configPath,String fileName, String key) {
		String path = null;
		try(InputStream inputStream = new FileInputStream(new File(configPath, fileName))){
			Properties p = new Properties();
			p.load(inputStream);
			path = p.getProperty(key);
		} catch (Exception e) {
			logger.error("加载配置文件时出现异常：", e);
		}
		return path;
	}

	public static synchronized void loadPropertisConfig(String fileDir, String fileName) {
		File file = new File(fileDir, fileName);
		logger.info("heartbeat-加载" + fileName + "配置文件，路径为：" + file.getAbsolutePath());
		if (!file.exists()) {
			logger.warn("not found " + fileDir + "/" + fileName + " from external");
		}
		try (InputStream in = new FileInputStream(file)) {
			Properties p = new Properties();
			p.load(in);
			put(fileName, p);
		} catch (Exception e) {
			logger.error("加载配置文件时出现异常：", e);
		}
	}

	private static void put(String fileName,Map<? extends Object, ? extends Object> map) {
		for (Entry<? extends Object, ? extends Object> entry : map.entrySet()) {
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (key == null) {
				continue;
			}
			properties.put(fileName+"_"+key.toString().trim(), value == null ? null : value.toString().trim());
		}
	}

	public static String get(String fileName,String key) {
		return properties.get(fileName+"_"+key);
	}

	public static Integer getInt(String fileName,String key) {
		String value = get(fileName,key);
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			logger.error("获取配置文件数据类型转换出现异常，key = " + key + "：", e);
		}
		return null;
	}

	public static Integer getInt(String fileName,String key, Integer defaultValue) {
		Integer value = getInt(fileName,key);
		return value == null ? defaultValue : value;
	}

	public static Properties readConfigFile(String configPath, Properties defaults) {
		Properties props = new Properties();
		if (defaults != null) {
			props.putAll(defaults);
		}

		InputStream in = loadConfigFileFromDefaultSearchLocations(configPath);

		try {
			if (in != null) {
				props.load(in);
			}
		} catch (IOException ex) {
			logger.warn("Reading config failed: {}", ex.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException ex) {
					logger.warn("Close config failed: {}", ex.getMessage());
				}
			}
		}

		if (logger.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			for (String propertyName : props.stringPropertyNames()) {
				sb.append(propertyName).append('=').append(props.getProperty(propertyName)).append('\n');

			}
			if (sb.length() > 0) {
				logger.debug("Reading properties: \n" + sb.toString());
			} else {
				logger.warn("No available properties: {}", configPath);
			}
		}
		return props;
	}

	private static InputStream loadConfigFileFromDefaultSearchLocations(String configPath) {
		try {

			String fileConfigPath = System.getProperty(FILE_CONFIG_PATH);

			if (fileConfigPath != null && ! fileConfigPath.isEmpty()) {

				//删除前缀file:
				if(fileConfigPath.startsWith(PREFIX_FILE)) {
					fileConfigPath = fileConfigPath.replaceAll(PREFIX_FILE, "");
					logger.debug("删除file:前缀以后的路径为："+fileConfigPath);
				}

				File candidate = Paths.get(fileConfigPath, configPath).toFile();
				if (candidate.exists() && candidate.isFile() && candidate.canRead()) {
					logger.debug("Reading config from resource {}", candidate.getAbsolutePath());
					return new FileInputStream(candidate);
				}
			}

			// load from classpath
			URL url = ClassLoaderUtil.getLoader().getResource(configPath);

			if (url != null) {
				InputStream in = getResourceAsStream(url);

				if (in != null) {
					logger.debug("Reading config from resource {}", url.getPath());
					return in;
				}
			}

			// load outside resource under current user path
			File candidate = new File(System.getProperty("user.dir"), configPath);
			if (candidate.exists() && candidate.isFile() && candidate.canRead()) {
				logger.debug("Reading config from resource {}", candidate.getAbsolutePath());
				return new FileInputStream(candidate);
			}
		} catch (FileNotFoundException e) {
			//ignore
		}
		return null;
	}

	private PropertisConfig() {
		throw new IllegalStateException("Utility class");
	}

	private static InputStream getResourceAsStream(URL url) {
		try {
			return url != null ? url.openStream() : null;
		} catch (IOException e) {
			return null;
		}
	}
}
