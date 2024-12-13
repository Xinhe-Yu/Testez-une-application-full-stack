package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SpringBootSecurityJwtApplicationTests {
  @BeforeAll
  static void setup() {
    System.setProperty("DB_PASSWORD", "test_password");
    System.setProperty("DB_USERNAME", "test_username");
  }

  @Test
  void contextLoads() {
    // This test will fail if the application context cannot start
  }

  @Test
  void mainMethodStartsApplication() {
    assertDoesNotThrow(() -> {
      SpringBootSecurityJwtApplication.main(new String[] {});
    });
  }

  @Test
  void environmentVariablesAreSet() {
    assertNotNull(System.getProperty("DB_PASSWORD"), "DB_PASSWORD should be set");
    assertNotNull(System.getProperty("DB_USERNAME"), "DB_USERNAME should be set");
    assertEquals("test_password", System.getProperty("DB_PASSWORD"));
    assertEquals("test_username", System.getProperty("DB_USERNAME"));
  }
}
