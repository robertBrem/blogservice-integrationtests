package expert.optimist.blog;

import expert.optimist.test.rest.RestClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EntryEndpointTest {

    @Before
    public void setUp() throws Exception {
        ProcessBuilder pb = new ProcessBuilder("src/test/resources/start_container.sh");
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();
        p.waitFor();
    }

    @After
    public void tearDown() throws Exception {
        ProcessBuilder pb = new ProcessBuilder("src/test/resources/stop_container.sh");
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();
        p.waitFor();
    }

    @Test
    public void should_return_an_empty_json_array() throws Exception {
        String urlString = "http://localhost:8081/blogservice/resources/entries";
        Assert.assertEquals("[]", RestClient.INSTANCE.callRESTService(urlString));
    }

}
