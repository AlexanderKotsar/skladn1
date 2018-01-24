package xyz.kots.repository;

import com.google.common.io.Files;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by kots on 23.01.2018.
 */

public class FileSystemRepository implements StorageRepository{

   @Override
    public String save(String dir, String name, MultipartFile file) throws IOException {
        File fileToSave = new File(dir, name);
        Files.createParentDirs(fileToSave);
        file.transferTo(fileToSave);

        return fileToSave.getAbsolutePath();
    }
}
