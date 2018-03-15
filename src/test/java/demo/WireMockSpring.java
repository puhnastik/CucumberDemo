package demo;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContexts;
import org.junit.Assert;
import org.springframework.util.ClassUtils;

import javax.net.ssl.HttpsURLConnection;

public abstract class WireMockSpring {

    private static boolean initialized = false;

    public static WireMockConfiguration options() {
        if (!initialized) {
            if (ClassUtils.isPresent("org.apache.http.conn.ssl.NoopHostnameVerifier",
                    null)) {
                HttpsURLConnection
                        .setDefaultHostnameVerifier(NoopHostnameVerifier.INSTANCE);
                try {
                    HttpsURLConnection
                            .setDefaultSSLSocketFactory(SSLContexts.custom()
                                    .loadTrustMaterial(null,
                                            TrustSelfSignedStrategy.INSTANCE)
                                    .build().getSocketFactory());
                }
                catch (Exception e) {
                    Assert.fail("Cannot install custom socket factory: [" + e.getMessage()
                            + "]");
                }
            }
            initialized = true;
        }
        WireMockConfiguration config = new WireMockConfiguration();
        return config;
    }

}