package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilsTest {

  @InjectMocks
  private JwtUtils jwtUtils;

  @Mock
  private Authentication authentication;

  @Mock
  private UserDetailsImpl userDetails;

  private String jwtSecret = "testSecret";
  private int jwtExpirationMs = 60000;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);
    ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
  }

  @Test
  void testGenerateJwtToken() {
    // Given
    String username = "testuser";
    when(authentication.getPrincipal()).thenReturn(userDetails);
    when(userDetails.getUsername()).thenReturn(username);

    // When
    String token = jwtUtils.generateJwtToken(authentication);

    // Then
    assertNotNull(token);
    assertTrue(token.startsWith("eyJhbGciOiJIUzUxMiJ9")); // JWTs with HS512 start with this
    assertEquals(username, jwtUtils.getUserNameFromJwtToken(token));
  }

  @Test
  void testGetUserNameFromJwtToken() {
    // Given
    String username = "testuser";
    String token = Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();

    // When
    String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

    // Then
    assertEquals(username, extractedUsername);
  }

  @Test
  void testValidateJwtTokenWithValidToken() {
    // Given
    String token = Jwts.builder()
        .setSubject("testuser")
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();

    // When
    boolean isValid = jwtUtils.validateJwtToken(token);

    // Then
    assertTrue(isValid);
  }

  @Test
  void testValidateJwtTokenWithExpiredToken() {
    // Given
    String token = Jwts.builder()
        .setSubject("testuser")
        .setIssuedAt(new Date(System.currentTimeMillis() - 2 * jwtExpirationMs))
        .setExpiration(new Date(System.currentTimeMillis() - jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();

    // When
    boolean isValid = jwtUtils.validateJwtToken(token);

    // Then
    assertFalse(isValid);
  }

  @Test
  void testValidateJwtTokenWithInvalidSignature() {
    // Given
    String token = Jwts.builder()
        .setSubject("testuser")
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, "wrongSecret")
        .compact();

    // When
    boolean isValid = jwtUtils.validateJwtToken(token);

    // Then
    assertFalse(isValid);
  }

  @Test
  void testValidateJwtTokenWithMalformedToken() {
    // Given
    String token = "malformed.jwt.token";

    // When
    boolean isValid = jwtUtils.validateJwtToken(token);

    // Then
    assertFalse(isValid);
  }

  @Test
  void testValidateJwtTokenWithEmptyToken() {
    // Given
    String token = "";

    // When
    boolean isValid = jwtUtils.validateJwtToken(token);

    // Then
    assertFalse(isValid);
  }

  @Test
  void testValidateJwtTokenWithNullToken() {
    // Given
    String token = null;

    // When
    boolean isValid = jwtUtils.validateJwtToken(token);

    // Then
    assertFalse(isValid);
  }

  @Test
  void testValidateJwtTokenWithSignatureException() {
    // Given
    String token = Jwts.builder()
        .setSubject("testuser")
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, "wrongSecret")
        .compact();

    // When
    boolean isValid = jwtUtils.validateJwtToken(token);

    // Then
    assertFalse(isValid);
  }

  @Test
  void testValidateJwtTokenWithMalformedJwtException() {
    // Given
    String token = "malformed.jwt.token";

    // When
    boolean isValid = jwtUtils.validateJwtToken(token);

    // Then
    assertFalse(isValid);
  }

  @Test
  void testValidateJwtTokenWithExpiredJwtException() {
    // Given
    String token = Jwts.builder()
        .setSubject("testuser")
        .setIssuedAt(new Date(System.currentTimeMillis() - 2 * jwtExpirationMs))
        .setExpiration(new Date(System.currentTimeMillis() - jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();

    // When
    boolean isValid = jwtUtils.validateJwtToken(token);

    // Then
    assertFalse(isValid);
  }

  @Test
  void testValidateJwtTokenWithUnsupportedJwtException() {
    // Given
    String token = Jwts.builder()
        .setPayload("unsupported payload")
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();

    // When
    boolean isValid = jwtUtils.validateJwtToken(token);

    // Then
    assertFalse(isValid);
  }

  @Test
  void testValidateJwtTokenWithIllegalArgumentException() {
    // Given
    String token = "";

    // When
    boolean isValid = jwtUtils.validateJwtToken(token);

    // Then
    assertFalse(isValid);
  }
}
