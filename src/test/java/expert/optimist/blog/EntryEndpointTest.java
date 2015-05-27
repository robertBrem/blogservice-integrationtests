package expert.optimist.blog;

import expert.optimist.system.FileUtil;
import expert.optimist.system.PropertyUtil;
import expert.optimist.system.SystemUtil;
import expert.optimist.test.rest.RestClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.*;

import java.io.IOException;
import java.net.URL;

public class EntryEndpointTest {

    public static final String PROJECT_TO_TEST = "blogservice";
    public static final Boolean TEST_MODE = true;

    private static String privateKeyLocation;
    private static String propertyFileLocation;
    private static String remoteUser;
    private static String remoteAddress;
    private static String remotePort;
    private static String remoteManagementPort;
    private static String postgresUsername;
    private static String postgresPassword;
    private static String databaseName;
    private static String jndiName;
    private static String datasourceName;
    private static String dbDriverPath;
    private static String dbDriverName;
    private static String artifactUrl;
    private static String baseUrl;


    private static void initEnvironmentAndPropertyVariables() throws ParseException {
        privateKeyLocation = SystemUtil.INSTANCE.getEnvironmentVariableValue(SystemUtil.KNOWN_KEYS.PRIVATE_KEY_SSH_LOCATION.getKey());
        propertyFileLocation = SystemUtil.INSTANCE.getEnvironmentVariableValue(SystemUtil.KNOWN_KEYS.PROPERTY_FILE_LOCATION.getKey());

        Object servicesObject = new JSONParser().parse(FileUtil.INSTANCE.readFile(propertyFileLocation));
        JSONObject services = (JSONObject) servicesObject;

        JSONObject blogservice = (JSONObject) services.get(PROJECT_TO_TEST);
        remoteUser = blogservice.get(PropertyUtil.KNOWN_KEYS.REMOTE_USER.getKey()).toString();
        remoteAddress = blogservice.get(PropertyUtil.KNOWN_KEYS.REMOTE_ADDRESS.getKey()).toString();
        remotePort = blogservice.get(PropertyUtil.KNOWN_KEYS.REMOTE_PORT.getKey()).toString();
        remoteManagementPort = blogservice.get(PropertyUtil.KNOWN_KEYS.REMOTE_MANAGEMENT_PORT.getKey()).toString();
        postgresUsername = blogservice.get(PropertyUtil.KNOWN_KEYS.POSTGRES_USERNAME.getKey()).toString();
        postgresPassword = blogservice.get(PropertyUtil.KNOWN_KEYS.POSTGRES_PASSWORD.getKey()).toString();
        databaseName = blogservice.get(PropertyUtil.KNOWN_KEYS.DATABASE_NAME.getKey()).toString();
        jndiName = blogservice.get(PropertyUtil.KNOWN_KEYS.JNDI_NAME.getKey()).toString();
        datasourceName = blogservice.get(PropertyUtil.KNOWN_KEYS.DATASOURCE_NAME.getKey()).toString();
        dbDriverPath = blogservice.get(PropertyUtil.KNOWN_KEYS.DB_DRIVER_PATH.getKey()).toString();
        dbDriverName = blogservice.get(PropertyUtil.KNOWN_KEYS.DB_DRIVER_NAME.getKey()).toString();
        artifactUrl = blogservice.get(PropertyUtil.KNOWN_KEYS.ARTIFACT_URL.getKey()).toString();
        baseUrl = blogservice.get(PropertyUtil.KNOWN_KEYS.BASE_URL.getKey()).toString();
    }

    private static void runRemoteScript(String startScriptPath) throws IOException, InterruptedException {
        String startCommand = getRemotePreString() +
                "'bash -s' < " + startScriptPath;
        System.out.println("startCommand = " + startCommand);
        runCommand(startCommand);
    }

    private static void runRemoteCommand(String command) throws IOException, InterruptedException {
        String startCommand = getRemotePreString() + command;
        System.out.println("startCommand = " + startCommand);
        runCommand(startCommand);
    }

    private static String getRemotePreString() {
        return "ssh -i " + privateKeyLocation +
                " " + remoteUser + "@" + remoteAddress +
                " ";
    }

    private static void runCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "/bin/bash", "-c",
                command);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();
        p.waitFor();
    }

    private static String escpae(String toEscape) {
        return "'" + toEscape.replaceAll("/", "\\\\/") + "'";
    }

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
        initEnvironmentAndPropertyVariables();

        URL startScript = EntryEndpointTest.class.getResource("/start_container.sh");
        String startScriptPath = startScript.getPath();

        String remoteScript = startScriptPath;
        remoteScript += " " + escpae(PROJECT_TO_TEST); // $1
        remoteScript += " " + escpae(remoteAddress); // $2
        remoteScript += " " + escpae(remotePort); // $3
        remoteScript += " " + escpae(postgresUsername); // $4
        remoteScript += " " + escpae(postgresPassword); // $5
        remoteScript += " " + escpae(databaseName); // $6
        remoteScript += " " + escpae(jndiName); // $7
        remoteScript += " " + escpae(datasourceName); // $8
        remoteScript += " " + escpae(dbDriverPath); // $9
        remoteScript += " " + escpae(dbDriverName); // $10
        remoteScript += " " + escpae(artifactUrl); // $11
        remoteScript += " " + escpae(baseUrl); // $12
        if (TEST_MODE) {
            remoteScript += " " + escpae(remoteManagementPort); // $13
        }

        runRemoteScript(remoteScript);
    }

    @Before
    public void setUpTest() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        URL endScript = EntryEndpointTest.class.getResource("/stop_container.sh");
        String endScriptPath = endScript.getPath();

        runRemoteScript(endScriptPath + " " + escpae(PROJECT_TO_TEST));
    }

    @After
    public void tearDownTest() throws Exception {
    }

    @Test
    public void should_return_an_empty_json_array() throws Exception {
        String urlString = "http://" + remoteAddress + ":" + remotePort + baseUrl;
        Assert.assertEquals("[]", RestClient.INSTANCE.callRESTService(urlString));
    }

}
