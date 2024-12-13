package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();
  }

  @Test
  public void testAuthenticateUser() throws Exception {
    // Create a test user
    User user = new User("test@example.com", "Doe", "John", passwordEncoder.encode("password"), false);
    userRepository.save(user);

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail("test@example.com");
    loginRequest.setPassword("password");

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.username").value("test@example.com"))
        .andExpect(jsonPath("$.firstName").value("John"))
        .andExpect(jsonPath("$.lastName").value("Doe"))
        .andExpect(jsonPath("$.admin").value(false));
  }

  @Test
  public void testAuthenticateUser_InvalidCredentials() throws Exception {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail("test@example.com");
    loginRequest.setPassword("wrongpassword");

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testRegisterUser() throws Exception {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setEmail("newuser@example.com");
    signupRequest.setPassword("password");
    signupRequest.setFirstName("Jane");
    signupRequest.setLastName("Doe");

    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signupRequest)))
        .andExpect(status().isOk())
        .andExpect(content().string("{\"message\":\"User registered successfully!\"}"));

    // Verify user was actually saved in the database
    assertTrue(userRepository.existsByEmail("newuser@example.com"));
  }

  @Test
  public void testRegisterUserEmailTaken() throws Exception {
    // Create a user with the email we'll try to register
    User existingUser = new User("existing@example.com", "Existing", "User", passwordEncoder.encode("password"), false);
    userRepository.save(existingUser);

    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setEmail("existing@example.com");
    signupRequest.setPassword("password");
    signupRequest.setFirstName("Jane");
    signupRequest.setLastName("Doe");

    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signupRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("{\"message\":\"Error: Email is already taken!\"}"));
  }
}
