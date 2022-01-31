package delta.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils implements Wrapper{
    public static String getContentFromFile(File file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
    }
}
