package models.browsers.chrome;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.junit.Assert;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

//    https://www.swtestacademy.com/browsermobproxy-in-selenium/
public class ChromeWithProxy extends Chrome {

    private static BrowserMobProxy proxy;

    public static BrowserMobProxy getProxy() {
        return proxy;
    }

    public ChromeOptions getOptions(boolean isHeadless) {
        BrowserMobProxy proxy = startProxy();
        Proxy seleniumProxy = convertProxyToSelenium(proxy);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        //3. Add Capability for PROXY in DesiredCapabilities
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

        capabilities.acceptInsecureCerts();
        capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

//        //4. Add all the different capture type which you want in our HAR file
        setHarCaptureTypes(proxy);

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);

        ChromeOptions options = new ChromeOptions();
        options.merge(capabilities);
        options.addArguments("window-position=1620,0");
        options.addArguments("--ignore-certificate-errors");

        return options;
    }

    private static BrowserMobProxy startProxy() {
        //1. Start the proxy on some port
        proxy = new BrowserMobProxyServer();
        proxy.setTrustAllServers(true);
        proxy.start();
        return proxy;
    }

    private static Proxy convertProxyToSelenium(BrowserMobProxy proxy) {
        //2. Set SSL and HTTP proxy in SeleniumProxy
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        try {
            String hostIp = Inet4Address.getLocalHost().getHostAddress();
            seleniumProxy.setHttpProxy(hostIp + ":" + proxy.getPort());
            seleniumProxy.setSslProxy(hostIp + ":" + proxy.getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Assert.fail("invalid Host Address");
        }
        return seleniumProxy;
    }

    private void setHarCaptureTypes(BrowserMobProxy proxy) {
        EnumSet<CaptureType> captureTypes = CaptureType.getAllContentCaptureTypes();
        captureTypes.addAll(CaptureType.getCookieCaptureTypes());
        captureTypes.addAll(CaptureType.getHeaderCaptureTypes());
        captureTypes.addAll(CaptureType.getRequestCaptureTypes());
        captureTypes.addAll(CaptureType.getResponseCaptureTypes());

        proxy.setHarCaptureTypes(captureTypes);
        //5. give a name to HAR file
        proxy.newHar("BoutiquesHAR");
    }
}
