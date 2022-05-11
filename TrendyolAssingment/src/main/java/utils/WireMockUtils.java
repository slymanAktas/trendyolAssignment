package utils;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class WireMockUtils {
    public static final int WIREMOCK_PORT_NUMBER = 8090;
    public static final int WIREMOCK_SECURE_PORT_NUMBER = 8043;
    public static final String WIREMOCK_HOST = "localhost";
    private static WireMockServer wireMockServer = null;


    private WireMockUtils() {
    }

    private static WireMockServer getInstance() {
        if (wireMockServer == null) {
            wireMockServer = new WireMockServer(wireMockConfig()
                    .httpsPort(WIREMOCK_SECURE_PORT_NUMBER)
                    .port(WIREMOCK_PORT_NUMBER));
            configureFor(WIREMOCK_HOST, WIREMOCK_PORT_NUMBER);
        }

        return wireMockServer;
    }

    public static void start() { getInstance().start(); }

    public static void stop() {
        getInstance().stop();
    }
}
