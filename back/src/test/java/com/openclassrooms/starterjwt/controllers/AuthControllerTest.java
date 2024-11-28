package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthenticationManager authenticationManager;

  @MockBean
  private JwtUtils jwtUtils;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @Mock
  private Authentication authentication;

  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  public void setup() {
    when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
  }

  @Test
  public void testAuthenticateUser() throws Exception {
    // Test case 1: Successful login
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail("test@example.com");
    loginRequest.setPassword("password");

    UserDetailsImpl userDetails = UserDetailsImpl.builder()
        .id(1L)
        .username("test@example.com")
        .firstName("John")
        .lastName("Doe")
        .password("password")
        .build();

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(authenticationManager.authenticate(any())).thenReturn(authentication);
    when(jwtUtils.generateJwtToken(any())).thenReturn("testJwtToken");

    User user = new User("test@example.com", "Doe", "John", "password", false);
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("testJwtToken"))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.username").value("test@example.com"))
        .andExpect(jsonPath("$.firstName").value("John"))
        .andExpect(jsonPath("$.lastName").value("Doe"))
        .andExpect(jsonPath("$.admin").value(false));

    // Test case 2: Admin user login
    User adminUser = new User("admin@example.com", "Admin", "Super", "adminpass", true);
    UserDetailsImpl adminDetails = UserDetailsImpl.builder()
        .id(2L)
        .username("admin@example.com")
        .firstName("Super")
        .lastName("Admin")
        .password("adminpass")
        .build();

    loginRequest.setEmail("admin@example.com");
    loginRequest.setPassword("adminpass");

    when(authentication.getPrincipal()).thenReturn(adminDetails);
    when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(adminUser));

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.admin").value(true));

    // Test case 3: User not found in repository
    LoginRequest nonExistentUserRequest = new LoginRequest();
    nonExistentUserRequest.setEmail("nonexistent@example.com");
    nonExistentUserRequest.setPassword("password");

    when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
    when(authenticationManager.authenticate(any()))
        .thenThrow(new BadCredentialsException("Invalid credentials"));

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(nonExistentUserRequest)))
        .andExpect(status().isUnauthorized());

    // Test case 4: Empty email
    LoginRequest emptyEmailRequest = new LoginRequest();
    emptyEmailRequest.setEmail("");
    emptyEmailRequest.setPassword("password");

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(emptyEmailRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
        .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains("email")));

    // Test case 5: Empty password
    LoginRequest emptyPasswordRequest = new LoginRequest();
    emptyPasswordRequest.setEmail("test@example.com");
    emptyPasswordRequest.setPassword("");

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(emptyPasswordRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
        .andExpect(result -> assertTrue(result.getResolvedException().getMessage().contains("password")));
  }

  @Test
  public void testAuthenticateUser_InvalidCredentials() throws Exception {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail("test@example.com");
    loginRequest.setPassword("wrongpassword");

    when(authenticationManager.authenticate(any()))
        .thenThrow(new BadCredentialsException("Invalid credentials"));

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testAuthenticateUser_AccountDisabled() throws Exception {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail("disabled@example.com");
    loginRequest.setPassword("password");

    when(authenticationManager.authenticate(any()))
        .thenThrow(new DisabledException("Account is disabled"));

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testAuthenticateUser_AccountLocked() throws Exception {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail("locked@example.com");
    loginRequest.setPassword("password");

    when(authenticationManager.authenticate(any()))
        .thenThrow(new LockedException("Account is locked"));

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testAuthenticateUser_UserNotFoundInRepository() throws Exception {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail("notfound@example.com");
    loginRequest.setPassword("password");

    UserDetailsImpl userDetails = UserDetailsImpl.builder()
        .id(3L)
        .username("notfound@example.com")
        .firstName("Not")
        .lastName("Found")
        .password("password")
        .build();

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(authenticationManager.authenticate(any())).thenReturn(authentication);
    when(jwtUtils.generateJwtToken(any())).thenReturn("testJwtToken");
    when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.admin").value(false));
  }

  @Test
  public void testRegisterUser() throws Exception {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setEmail("newuser@example.com");
    signupRequest.setPassword("password");
    signupRequest.setFirstName("Jane");
    signupRequest.setLastName("Doe");

    when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);

    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signupRequest)))
        .andExpect(status().isOk())
        .andExpect(content().string("{\"message\":\"User registered successfully!\"}"));
  }

  @Test
  public void testRegisterUserEmailTaken() throws Exception {
    SignupRequest signupRequest = new SignupRequest();
    signupRequest.setEmail("existing@example.com");
    signupRequest.setPassword("password");
    signupRequest.setFirstName("Jane");
    signupRequest.setLastName("Doe");

    when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

    mockMvc.perform(post("/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signupRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("{\"message\":\"Error: Email is already taken!\"}"));
  }
}
