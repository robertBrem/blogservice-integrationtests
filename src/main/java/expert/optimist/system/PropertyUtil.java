package expert.optimist.system;

import org.json.simple.JSONObject;

public class PropertyUtil {

    private JSONObject service;

    public enum KNOWN_KEYS {
        REMOTE_USER("remote_user"),
        REMOTE_ADDRESS("remote_address"),
        REMOTE_PORT("remote_port"),
        REMOTE_MANAGEMENT_PORT("remote_management_port"),
        POSTGRES_USERNAME("postgres_username"),
        POSTGRES_PASSWORD("postgres_password"),
        DATABASE_NAME("database_name"),
        JNDI_NAME("jndi_name"),
        DATASOURCE_NAME("datasource_name"),
        DB_DRIVER_PATH("db_driver_path"),
        DB_DRIVER_NAME("db_driver_name"),
        ARTIFACT_URL("artifact_url"),
        BASE_URL("base_url");

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

    public PropertyUtil(JSONObject service) {
        this.service = service;
    }

    public String getRemoteUser() {
        return service.get(KNOWN_KEYS.REMOTE_USER.getKey()).toString();
    }

    public String getRemoteAddress() {
        return service.get(KNOWN_KEYS.REMOTE_ADDRESS.getKey()).toString();
    }

    public String getRemotePort() {
        return service.get(KNOWN_KEYS.REMOTE_PORT.getKey()).toString();
    }

    public String getRemoteManagementPort() {
        return service.get(KNOWN_KEYS.REMOTE_MANAGEMENT_PORT.getKey()).toString();
    }

    public String getArtifactUrl() {
        return service.get(KNOWN_KEYS.ARTIFACT_URL.getKey()).toString();
    }

    public String getBaseUrl() {
        return service.get(KNOWN_KEYS.BASE_URL.getKey()).toString();
    }

    public String getDatabaseName() {
        return service.get(KNOWN_KEYS.DATABASE_NAME.getKey()).toString();
    }

    public String getDatasourceName() {
        return service.get(KNOWN_KEYS.DATASOURCE_NAME.getKey()).toString();
    }

    public String getDbDriverName() {
        return service.get(KNOWN_KEYS.DB_DRIVER_NAME.getKey()).toString();
    }

    public String getDbDriverPath() {
        return service.get(KNOWN_KEYS.DB_DRIVER_PATH.getKey()).toString();
    }

    public String getJndiName() {
        return service.get(KNOWN_KEYS.JNDI_NAME.getKey()).toString();
    }

    public String getPostgresPassword() {
        return service.get(KNOWN_KEYS.POSTGRES_PASSWORD.getKey()).toString();
    }

    public String getPostgresUsername() {
        return service.get(KNOWN_KEYS.POSTGRES_USERNAME.getKey()).toString();
    }
}
