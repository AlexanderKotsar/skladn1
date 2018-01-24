package xyz.kots.repository;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by kots on 23.01.2018.
 */

public interface StorageRepository {
    String save(String dir, String name, MultipartFile file) throws IOException;
}
