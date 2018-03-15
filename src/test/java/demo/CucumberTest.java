package demo;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

/**
 * Created with IntelliJ IDEA.
 * User: jacobs
 * Date: 6/11/15
 * Time: 11:09 AM
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources")
public class CucumberTest{

    @ClassRule
    public static WireMockClassRule fhirWireMock = new WireMockClassRule(
            WireMockSpring.options().port(32001));

    @ClassRule
    public static WireMockClassRule nantomicsWireMock = new WireMockClassRule(
            WireMockSpring.options().port(32002));


    @ClassRule
    public static WireMockClassRule nodeWireMock = new WireMockClassRule(
            WireMockSpring.options().port(32003));


    @BeforeClass
    public static void setupMocks(){
        setupNantomicsMock();
        setupFhirMock();
    }

    private static void setupNantomicsMock() {
        nantomicsWireMock.stubFor(get(urlEqualTo("/mock/nantomics/data"))
                .willReturn(okJson("{\"data\" :\"nantomicsdata\"}")));
        nantomicsWireMock.stubFor(get(urlEqualTo("/mock/nantomics/version"))
                .willReturn(ok("nantomics-1.0")));
    }

    private static void setupFhirMock() {
        fhirWireMock.stubFor(get(urlEqualTo("/mock/fhir/data"))
                .willReturn(okJson("{\"data\" :\"fhirdata\"}")));
        fhirWireMock.stubFor(get(urlEqualTo("/mock/fhir/version"))
                .willReturn(ok("fhir-1.0")));
    }
}
