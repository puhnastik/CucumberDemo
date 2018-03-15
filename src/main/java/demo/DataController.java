package demo;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import demo.helpers.HeaderSettingRequestCallback;
import demo.helpers.ResponseResultErrorHandler;
import demo.helpers.ResponseResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jacobs
 * Date: 6/11/15
 * Time: 10:32 AM
 */
@RestController
public class DataController
{

    public static Logger log = LoggerFactory.getLogger(DataController.class);

    protected RestTemplate restTemplate = null;

    @Value("${nantomicsHost}")
    String nantomicsUrl;

    @Value("${fhirHost}")
    String fhirUrl;


    @RequestMapping(method={RequestMethod.GET},value={"/nantomics/data"})
    public String getNantomicsData(HttpServletResponse response) throws IOException, InterruptedException {
        ResponseResults mockResponse = executeGet(nantomicsUrl);
        log.info("Called to Nantomics. Response: " + mockResponse.getBody());
        return getData(mockResponse);
    }



    @RequestMapping(method={RequestMethod.GET},value={"/fhir/data"})
    public String getFhirData(HttpServletResponse response) throws IOException, InterruptedException {
        ResponseResults mockResponse = executeGet(fhirUrl);
        log.info("Called to FHIR. Response: " + mockResponse.getBody());
        return getData(mockResponse);
    }


    private String getData(ResponseResults mockResponse) throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser  = factory.createParser(mockResponse.getBody());
        String data = null;

        while(!parser.isClosed())
        {
            JsonToken jsonToken = parser.nextToken();
            if(JsonToken.FIELD_NAME.equals(jsonToken)){
                String fieldName = parser.getCurrentName();
                System.out.println(fieldName);

                jsonToken = parser.nextToken();

                if("data".equals(fieldName)){
                    data = parser.getValueAsString();
                }
            }
        }
        return data;
    }


    protected ResponseResults executeGet(String url) throws IOException
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
        return restTemplate.execute(url,
                HttpMethod.GET,
                requestCallback,
                new ResponseExtractor<ResponseResults>()
                {
                    @Override
                    public ResponseResults extractData(ClientHttpResponse response) throws IOException
                    {
                        if (errorHandler.hasError(response))
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
}
