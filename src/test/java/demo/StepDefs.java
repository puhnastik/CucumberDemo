package demo;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import demo.helpers.HeaderSettingRequestCallback;
import demo.helpers.ResponseResults;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DemoApplication.class, loader = SpringApplicationContextLoader.class)
@WebAppConfiguration
@IntegrationTest
public class StepDefs
{
    private final WireMockClassRule fhirMock = FhirWiremockUtils.INSTANCE.getWiremock();
    private final WireMockClassRule nantomicsMock = NantomicsWiremockUtils.INSTANCE.getWiremock();

    protected static ResponseResults latestResponse = null;

    protected RestTemplate restTemplate = null;

    @Given("data exists in fhir for version (.+)$")
    public void data_exists_in_fhir(String versionId) throws Throwable {
        fhirMock.stubFor(get(urlEqualTo("/mock/fhir/data"))
                .willReturn(okJson("{\"data\" :\"fhirdata" + versionId +"\"}")));
    }

    @Given("data exists in nantomics")
    public void data_exists_in_nantomics() throws Throwable {
        nantomicsMock.stubFor(get(urlEqualTo("/mock/nantomics/data"))
                .willReturn(okJson("{\"data\" :\"nantomicsdata\"}")));
    }

    @When("^the client calls (.+)$")
    public void the_client_issues_GET_endpoint(String endpoint) throws Throwable
    {
        executeGet("http://localhost:8080" + endpoint);
    }

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable
    {
        final HttpStatus currentStatusCode = latestResponse.getTheResponse().getStatusCode();
        assertThat("status code is incorrect : "+ latestResponse.getBody(), currentStatusCode.value(), is(statusCode) );
    }

    @And("^the client receives server version (.+)$")
    public void the_client_receives_server_version_body(String version) throws Throwable
    {
        assertThat(latestResponse.getBody(), is(version)) ;
    }

//    private  void executeGet(String url) {
//        entity = this.testRestTemplate.getForEntity(url, Map.class);
//    }

    protected void executeGet(String url) throws IOException
    {
        final Map<String,String> headers = new HashMap<>();
        headers.put("Accept","application/json");
        final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
        final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();
        if (restTemplate == null)
        {
            restTemplate = new RestTemplate();
        }
        restTemplate.setErrorHandler(errorHandler);
        latestResponse = restTemplate.execute(url,
                HttpMethod.GET,
                requestCallback,
                new ResponseExtractor<ResponseResults>()
                {
                    @Override
                    public ResponseResults extractData(ClientHttpResponse response) throws IOException
                    {
                        if (errorHandler.hadError)
                        {
                            return (errorHandler.getResults());
                        }
                        else
                        {
                            return (new ResponseResults(response));
                        }
                    }
                });

    }

    private class ResponseResultErrorHandler implements ResponseErrorHandler
    {
        private ResponseResults results = null;
        private Boolean hadError = false;

        private ResponseResults getResults()
        {
            return results;
        }

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException
        {
            hadError = response.getRawStatusCode() >= 400;
            return hadError;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException
        {
            results =  new ResponseResults(response);
        }
    }
}
