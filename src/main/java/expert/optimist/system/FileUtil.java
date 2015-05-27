package expert.optimist.system;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public enum FileUtil {
    INSTANCE;

    public String readFile(String path) {
        return readFile(path, Charset.forName("UTF-8"));
    }

    public String readFile(String path, Charset encoding) {
        byte[] encoded = null;
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read file: " + path + " with encoding: " + encoding);
        }
        return new String(encoded, encoding);
    }

}
