package expert.optimist.system;

public enum PropertyUtil {
    INSTANCE;

    public enum KNOWN_KEYS {
        REMOTE_USER("remote_user"),
        REMOTE_ADDRESS("remote_address");

        private String key;

        KNOWN_KEYS(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}
