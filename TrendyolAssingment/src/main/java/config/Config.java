package config;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.apache.http.util.TextUtils.isBlank;

public class Config {
    public static final String DEFAULT_BROWSER_NAME;
    public static final String SELENIUM_GRID_HUB_URL;
    public static final Platform platform;
    public static final boolean ISREMOTE;
    public static final String SCREENSHOT_PATH;
    public static final String USER_DIR;
    public static final String API_ROOT;
    public static final String API_BASE_PATH;

    protected static final Properties properties;

    public static final String TRENDYOL_BASE_HTTPS_URL;

    static {
        DEFAULT_BROWSER_NAME = getBrowser();
        SELENIUM_GRID_HUB_URL = getSeleniumGridHub();
        platform = getPlatform();
        ISREMOTE = isRemote();

        properties = loadProperties();
        API_ROOT = properties.getProperty("api.root");
        API_BASE_PATH = properties.getProperty("api.base.path");
        TRENDYOL_BASE_HTTPS_URL = properties.getProperty("trendyol.prod");

        USER_DIR = System.getProperty("user.dir");
        SCREENSHOT_PATH = USER_DIR + "/screenshots/";
    }

    private static Properties loadProperties() {
        String configFileName = "properties/config.properties";
        InputStream in = ClassLoader.getSystemResourceAsStream(configFileName);
        Properties properties = new Properties();

        try {
            properties.load(in);
        } catch (IOException ioe) {
            throw new IllegalStateException("Exception on loading {" + configFileName + "} conf file from classpath", ioe);
        }
        return properties;
    }

    private static String getBrowser() {
        String browser = System.getProperties().getProperty("models/browsers");

        if (isBlank(browser)) {
//            throw new IllegalArgumentException("No models.browser option is set, please set -Dbrowser in java");
            browser = "chrome"; // Default
        }

        return browser;
    }

    private static String getSeleniumGridHub() {
        String gridIp = System.getProperties().getProperty("grid");

        if (isBlank(gridIp)) {
//            throw new IllegalArgumentException("No grid ip is set, please set -Dgrid in java");
            gridIp = "localhost";//
        }

        return gridIp;
    }

    private static Platform getPlatform() {
        String platformConf = System.getProperties().getProperty("platform");

        if (StringUtils.isBlank(platformConf)) {
            return null;
        }

        return Platform.valueOf(platformConf.toUpperCase());
    }

    private static boolean isRemote() {
        String remoteValue = System.getProperties().getProperty("remote");

        boolean remote = false;
        if (!StringUtils.isBlank(remoteValue)) {
            remote = Boolean.parseBoolean(remoteValue);
        }
        return remote;
    }
}
