package tr.technicalserviceapp.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public class StorageFile {

    private static final Logger logger = Logger.getLogger(StorageFile.class.getName());

    public static void createFile(Path file_path) throws IOException {

        if (!Files.exists(file_path)) {
            Files.createDirectories(file_path.getParent());
            Files.createFile(file_path);
            logger.info("[Storage Files] INFO | Dosya oluşturuldu: " + file_path);
        } else {
            logger.warning("[Storage Files] WARN | Dosya zaten mevcut: " + file_path);
        }
    }

    public static BufferedWriter getWriter(Path file_path) throws IOException {
        //Path path = Paths.get(file_path);
        return Files.newBufferedWriter(file_path);
    }

    public static BufferedReader getReader(Path file_path) throws IOException {
        //Path path = Paths.get(file_path);
        return Files.newBufferedReader(file_path);
    }


}
