package demo;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;

enum NantomicsWiremockUtils {
    INSTANCE;

    private final WireMockClassRule wiremock =  new WireMockClassRule(WireMockSpring.options().port(32002));

    WireMockClassRule getWiremock() {
        return wiremock;
    }
}
