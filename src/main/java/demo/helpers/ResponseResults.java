package demo.helpers;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: jacobs
 * Date: 6/11/15
 * Time: 11:03 AM
 */
public class ResponseResults
{
    private final ClientHttpResponse theResponse;
    private final String body;

    public ResponseResults(final ClientHttpResponse response) throws IOException
    {
        this.theResponse = response;
        final InputStream bodyInputStream = response.getBody();
        if (null == bodyInputStream)
        {
            this.body = "{}";
        }
        else
        {
            final StringWriter stringWriter = new StringWriter();
            IOUtils.copy(bodyInputStream, stringWriter);
            this.body = stringWriter.toString();
        }
    }

    public ClientHttpResponse getTheResponse()
    {
        return theResponse;
    }

    public String getBody()
    {
        return body;
    }
}
