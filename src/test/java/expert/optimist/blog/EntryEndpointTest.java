package expert.optimist.blog;

import expert.optimist.system.FileUtil;
import expert.optimist.system.PropertyUtil;
import expert.optimist.system.SystemUtil;
import expert.optimist.test.rest.RestClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

public class EntryEndpointTest {

    public static final String PROJECT_TO_TEST = "blogservice";

    /**
     * echo "export PRIVATE_KEY_SSH_LOCATION=/home/robert/.ssh/id_rsa" >> /etc/profile
     * echo "export PROPERTY_FILE_LOCATION=/home/robert/Schreibtisch/config/config.json" >> /etc/profile
     * <br/>
     * ssh -i /home/robert/.ssh/id_rsa \
     * user@104.167.115.228 'bash -s' < \
     * /home/robert/IdeaProjects/Blog/Blogservice-Integrationtest/src/test/resources/start_container.sh
     */
    @Before
    public void setUp() throws Exception {
        String privateKeyLocation = SystemUtil.INSTANCE.getEnvironmentVariableValue(SystemUtil.KNOWN_KEYS.PRIVATE_KEY_SSH_LOCATION.getKey());
        String propertyFileLocation = SystemUtil.INSTANCE.getEnvironmentVariableValue(SystemUtil.KNOWN_KEYS.PROPERTY_FILE_LOCATION.getKey());

        Object servicesObject = new JSONParser().parse(FileUtil.INSTANCE.readFile(propertyFileLocation));
        JSONObject services = (JSONObject) servicesObject;

        JSONObject blogservice = (JSONObject) services.get(PROJECT_TO_TEST);
        String remoteUser = blogservice.get(PropertyUtil.KNOWN_KEYS.REMOTE_USER.getKey()).toString();
        String remoteAddress = blogservice.get(PropertyUtil.KNOWN_KEYS.REMOTE_ADDRESS.getKey()).toString();

        URL startScript = getClass().getResource("/start_container.sh");
        String startScriptPath = startScript.getPath();

        ProcessBuilder pb = new ProcessBuilder(
                "/bin/bash", "-c",
                "ssh -i " + privateKeyLocation +
                        " " + remoteUser + "@" + remoteAddress +
                        " 'bash -s' < " + startScriptPath);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();
        p.waitFor();
    }

    @After
    public void tearDown() throws Exception {
        ProcessBuilder pb = new ProcessBuilder(
                "/bin/bash", "-c",
                "ssh -i /home/robert/.ssh/id_rsa user@104.167.115.228 bash -s < /home/robert/IdeaProjects/Blog/Blogservice-Integrationtest/src/test/resources/stop_container.sh");
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();
        p.waitFor();
    }

    @Test
    public void should_return_an_empty_json_array() throws Exception {
        String urlString = "http://104.167.115.228:8081/blogservice/resources/entries";
        Assert.assertEquals("[]", RestClient.INSTANCE.callRESTService(urlString));
    }

}
