package expert.optimist.test;

import expert.optimist.system.FileUtil;
import expert.optimist.system.PropertyUtil;
import expert.optimist.system.SystemUtil;
import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class DockerUtil {

    @Getter
    private PropertyUtil properties;

    public DockerUtil(String projectName) {
        JSONObject service = getService(projectName);
        properties = new PropertyUtil(service);
    }

    public JSONObject getService(String projectName) {
        String propertyFileContent = FileUtil.INSTANCE.readFile(SystemUtil.INSTANCE.getPropertyFileLocation());
        JSONObject services = getJSONObject(propertyFileContent);
        return (JSONObject) services.get(projectName);
    }

    public JSONObject getJSONObject(String toParse) {
        try {
            return (JSONObject) new JSONParser().parse(toParse);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Could not parse: " + toParse);
        }
    }

    public void runRemoteScript(String startScriptPath) throws IOException, InterruptedException {
        String startCommand = getRemotePreString() +
                "'bash -s' < " + startScriptPath;
        System.out.println("startCommand = " + startCommand);
        runCommand(startCommand);
    }

    public String getRemotePreString() {
        return "ssh -i " + SystemUtil.INSTANCE.getPrivateKeyLocation() +
                " " + properties.getRemoteUser() + "@" + properties.getRemoteAddress() +
                " ";
    }

    public void runCommand(String command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "/bin/bash", "-c",
                command);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();
        p.waitFor();
    }


    public String getStartScript(String startScriptPath, String projectName, boolean testMode) {
        String startScript = startScriptPath;
        startScript += " " + escpae(projectName); // $1
        startScript += " " + escpae(properties.getRemoteAddress()); // $2
        startScript += " " + escpae(properties.getRemotePort()); // $3
        startScript += " " + escpae(properties.getPostgresUsername()); // $4
        startScript += " " + escpae(properties.getPostgresPassword()); // $5
        startScript += " " + escpae(properties.getDatabaseName()); // $6
        startScript += " " + escpae(properties.getJndiName()); // $7
        startScript += " " + escpae(properties.getDatasourceName()); // $8
        startScript += " " + escpae(properties.getDbDriverPath()); // $9
        startScript += " " + escpae(properties.getDbDriverName()); // $10
        startScript += " " + escpae(properties.getArtifactUrl()); // $11
        startScript += " " + escpae(properties.getBaseUrl()); // $12
        if (testMode) {
            startScript += " " + escpae(properties.getRemoteManagementPort()); // $13
        }
        return startScript;
    }

    public String escpae(String toEscape) {
        return "'" + toEscape.replaceAll("/", "\\\\/") + "'";
    }

}
