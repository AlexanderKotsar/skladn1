package xyz.kots.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.kots.repository.FileSystemRepository;
import xyz.kots.repository.StorageRepository;

/**
 * Created by kots on 23.01.2018.
 */

@Configuration
public class ApplicationConfiguration {

    @Bean
    public StorageRepository storageRepository(){
        return new FileSystemRepository();
    }
}
