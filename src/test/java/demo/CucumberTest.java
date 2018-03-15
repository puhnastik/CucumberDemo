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
    public static WireMockClassRule fhirWireMock = FhirWiremockUtils.INSTANCE.getWiremock();

    @ClassRule
    public static WireMockClassRule nantomicsWireMock = NantomicsWiremockUtils.INSTANCE.getWiremock();

    @ClassRule
    public static WireMockClassRule nodeWireMock = new WireMockClassRule(WireMockSpring.options().port(32003));
}
