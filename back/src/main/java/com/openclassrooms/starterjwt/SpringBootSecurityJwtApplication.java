package com.openclassrooms.starterjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableJpaAuditing
public class SpringBootSecurityJwtApplication {
  public static void main(String[] args) {
    Dotenv dotenv = Dotenv.load();
    System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
    System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
    SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
  }
}
