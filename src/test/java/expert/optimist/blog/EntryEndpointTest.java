package expert.optimist.blog;

import expert.optimist.system.PropertyUtil;
import expert.optimist.test.DockerUtil;
import expert.optimist.test.rest.RestClient;
import org.junit.*;

import java.net.URL;

public class EntryEndpointTest {

    public static final String PROJECT_TO_TEST = "blogservice";
    public static final Boolean TEST_MODE = true;

    private static DockerUtil dockerUtil;

    /**
     * echo "export PRIVATE_KEY_SSH_LOCATION=/home/robert/.ssh/id_rsa" >> /etc/profile
     * echo "export PROPERTY_FILE_LOCATION=/home/robert/Schreibtisch/config/config.json" >> /etc/profile
     * <br/>
     * ssh -i /home/robert/.ssh/id_rsa \
     * user@104.167.115.228 'bash -s' < \
     * /home/robert/IdeaProjects/Blog/Blogservice-Integrationtest/src/test/resources/start_container.sh
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        dockerUtil = new DockerUtil(PROJECT_TO_TEST);

        URL startScript = EntryEndpointTest.class.getResource("/start_container.sh");
        String startScriptPath = startScript.getPath();

        String remoteScript = dockerUtil.getStartScript(startScriptPath, PROJECT_TO_TEST, TEST_MODE);
        dockerUtil.runRemoteScript(remoteScript);
    }

    @Before
    public void setUpTest() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        URL endScript = EntryEndpointTest.class.getResource("/stop_container.sh");
        String endScriptPath = endScript.getPath();

        dockerUtil.runRemoteScript(endScriptPath + " " + dockerUtil.escpae(PROJECT_TO_TEST));
    }

    @After
    public void tearDownTest() throws Exception {
    }

    @Test
    public void should_return_an_empty_json_array() throws Exception {
        PropertyUtil properties = dockerUtil.getProperties();
        String urlString =
                "http://" +
                        properties.getRemoteAddress() + ":" + properties.getRemotePort() +
                        properties.getBaseUrl();
        Assert.assertEquals("[]", RestClient.INSTANCE.callRESTService(urlString));
    }

}
