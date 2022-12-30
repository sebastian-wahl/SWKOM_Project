package at.fhtw.swen3.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    public static String readFromFile(String filename) throws IOException {
        return Files.readString(Paths.get("src/integration-test/resources/" + filename));
    }
}
