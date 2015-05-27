package expert.optimist.system;

import java.util.Map;

public enum SystemUtil {

    INSTANCE;

    public enum KNOWN_KEYS {
        PRIVATE_KEY_SSH_LOCATION("PRIVATE_KEY_SSH_LOCATION"),
        PROPERTY_FILE_LOCATION("PROPERTY_FILE_LOCATION");

        private String key;

        KNOWN_KEYS(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public String getEnvironmentVariableValue(String environmentVariableKey) {
        Map<String, String> env = System.getenv();
        return env.get(environmentVariableKey);
    }

}
