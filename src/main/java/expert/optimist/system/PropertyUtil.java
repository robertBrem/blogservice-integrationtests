package expert.optimist.system;

public enum PropertyUtil {
    INSTANCE;

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
    }
}
