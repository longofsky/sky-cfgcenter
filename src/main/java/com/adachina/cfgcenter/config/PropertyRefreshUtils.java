package com.adachina.cfgcenter.config;

import com.adachina.cfgcenter.utils.PropertisConfig;
import com.ctrip.framework.apollo.build.ApolloInjector;
import com.ctrip.framework.apollo.util.ConfigUtil;
import com.ctrip.framework.foundation.Foundation;
import com.ctrip.framework.foundation.internals.io.BOMInputStream;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author jibin
 */
public class PropertyRefreshUtils {

    private static final Logger logger = LoggerFactory.getLogger(PropertyRefreshUtils.class);
    /**
     * 远程配置中心本地缓存文件地址(和apollo源码中地址同步
     */
    private static final String PATH_WINDOWS = "c:\\opt\\data\\%s\\%s";
    private static final String PATH_LINUX = "/opt/data/%s/%s";
    private static final String PATH_SUFFIX = "config-cache";

    /**
     * 本地缓存文件对应命名空间解析
     */
    public static final String NAMESPACE_SPLIT_TAG = "+";
    public static final String NAMESPACE_FILE_SUFFIX = ".properties";

    public static final String EMPTY_STR = "";

    private PropertyRefreshUtils() {
    }

    /**
     *
     */
    private static Map<String, String> apolloFileNameConvert = new HashMap<>();


    private static final String ENV_PROD = "PRO";
    private static final String ENV_GRY = "GRY";
    private static final String ENV_APB = "APB";

    private static String filePath = null;

    private static boolean filePathInitilized = false;

    /**
     * 获取配置中心缓存文件前缀
     */
    private static volatile String cacheFilePrefix = null;

    /**
     * 判断是否线上环境
     *
     * @return
     */
    public static boolean isOnlineEnv() {
        String env = Foundation.server().getEnvType();
        return env == null || ENV_PROD.equals(env) || ENV_GRY.equals(env) || ENV_APB.equals(env);
    }

    public static String getEnv() {
        return Foundation.server().getEnvType();
    }


    /**
     * @param file
     * @return
     */
    public static Properties readFileIntoProperty(File file) {
        Properties property = new Properties();
        if (file.exists() && file.canRead()) {
            try {
                logger.info("Loading {}", file.getAbsolutePath());
                property.load(new InputStreamReader(new BOMInputStream(new FileInputStream(file)),
                        StandardCharsets.UTF_8));
            } catch (Exception ex) {
                logger.error("Initialize DefaultServerProvider failed.", ex);
            }

        }
        return property;
    }

    /**
     * @param fileName
     * @return
     */
    public static String readNamespaceFromFile(String fileName) {
        if (!checkFileNameLegal(fileName)) {
            return null;
        }
        String namespace = apolloFileNameConvert.get(fileName);
        if (namespace != null) {
            return namespace;
        }
        int lastIndex = fileName.lastIndexOf(NAMESPACE_SPLIT_TAG);
        int offset = 1;
        String fullNamespace = fileName.substring(lastIndex + offset);
        int lastSuffixIndex = fullNamespace.lastIndexOf(NAMESPACE_FILE_SUFFIX);
        int startPos = 0;
        namespace = fullNamespace.substring(startPos, lastSuffixIndex);
        apolloFileNameConvert.put(fileName, namespace);
        return namespace;
    }

    /**
     * 处理多个项目下相同命名空间问题
     *
     * @param fileName
     * @return
     */
    private static boolean checkFileNameLegal(String fileName) {
        if (cacheFilePrefix == null) {
            ConfigUtil configUtil = ApolloInjector.getInstance(ConfigUtil.class);
            String appId = configUtil.getAppId();
            String cluster = configUtil.getCluster();
            cacheFilePrefix = appId + NAMESPACE_SPLIT_TAG + cluster;
        }
        return fileName.startsWith(cacheFilePrefix);
    }

    /**
     * @return
     */
    public static String getLocalCacheRemoteRepositoryDir() {
        String cacheDir = isOSWindows() ? PATH_WINDOWS : PATH_LINUX;
        return String.format(cacheDir, Foundation.server().getEnvType(),
                PATH_SUFFIX);
    }

    /**
     * @return
     */
    private static boolean isOSWindows() {
        String osName = System.getProperty("os.name");
        if (Strings.isNullOrEmpty(osName)) {
            return false;
        }
        return osName.startsWith("Windows");

    }


    /**
     * 获取配置文件路径
     *
     * @return
     */
    public static String getFileConfigPath() {
        if (!filePathInitilized) {
            initFileConfigType();
            filePathInitilized = true;
        }
        return filePath;
    }

    private static void initFileConfigType() {
        filePath = PropertisConfig.getSystemFolderPath();
    }

}
