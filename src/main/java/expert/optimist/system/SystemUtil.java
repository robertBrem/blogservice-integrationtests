package expert.optimist.system;

import lombok.Getter;

import java.util.Map;

@Getter
public enum SystemUtil {

    INSTANCE;

    private String privateKeyLocation;
    private String propertyFileLocation;

    SystemUtil() {
        privateKeyLocation = getEnvironmentVariableValue(KNOWN_KEYS.PRIVATE_KEY_SSH_LOCATION);
        propertyFileLocation = getEnvironmentVariableValue(KNOWN_KEYS.PROPERTY_FILE_LOCATION);
    }

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

        @Override
        public String toString() {
            return getKey();
        }
    }

    public String getEnvironmentVariableValue(KNOWN_KEYS environmentVariableKey) {
        Map<String, String> env = System.getenv();
        return env.get(environmentVariableKey.getKey());
    }

}
