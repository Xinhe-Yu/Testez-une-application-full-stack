package com.openclassrooms.starterjwt.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

@Profile("!test")
public class EnvConfigTest {
  @BeforeAll
  static void setup() {
    System.setProperty("DB_PASSWORD", "test_password");
    System.setProperty("DB_USERNAME", "test_username");
  }

  @Test
  void environmentVariablesAreSet() {
    assertNotNull(System.getProperty("DB_PASSWORD"), "DB_PASSWORD should be set");
    assertNotNull(System.getProperty("DB_USERNAME"), "DB_USERNAME should be set");
    assertEquals("test_password", System.getProperty("DB_PASSWORD"));
    assertEquals("test_username", System.getProperty("DB_USERNAME"));
  }

}
