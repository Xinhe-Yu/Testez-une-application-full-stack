package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SpringBootSecurityJwtApplicationTests {

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
}
