package demo;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;

enum  FhirWiremockUtils {
    INSTANCE;

    private final WireMockClassRule wireMock = new WireMockClassRule(
            WireMockSpring.options().port(32001));

    WireMockClassRule getWiremock() {
        return wireMock;
    }
}
