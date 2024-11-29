package com.openclassrooms.starterjwt.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
@Profile("!test")
public class EnvConfig {
  @PostConstruct
  public void init() {
    Dotenv dotenv = Dotenv.load();
    System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
    System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
  }
}
