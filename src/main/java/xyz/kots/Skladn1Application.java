package xyz.kots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import xyz.kots.config.JpaConfig;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableJpaAuditing
@EnableConfigurationProperties
public class Skladn1Application {

	public static void main(String[] args) {
//		SpringApplication.run(Skladn1Application.class, args);
		SpringApplication.run(new Class<?>[] {Skladn1Application.class, JpaConfig.class}, args);
	}
}
