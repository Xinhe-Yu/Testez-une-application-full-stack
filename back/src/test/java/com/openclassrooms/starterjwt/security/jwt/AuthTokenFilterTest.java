package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthTokenFilterTest {

  @InjectMocks
  private AuthTokenFilter authTokenFilter;

  @Mock
  private JwtUtils jwtUtils;

  @Mock
  private UserDetailsServiceImpl userDetailsService;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain filterChain;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    SecurityContextHolder.clearContext();
  }

  @Test
  void testDoFilterInternal_WithValidJwt() throws Exception {
    // Given
    String token = "valid.jwt.token";
    String username = "testuser";
    UserDetails userDetails = mock(UserDetails.class);

    when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
    when(jwtUtils.validateJwtToken(token)).thenReturn(true);
    when(jwtUtils.getUserNameFromJwtToken(token)).thenReturn(username);
    when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

    // When
    authTokenFilter.doFilterInternal(request, response, filterChain);

    // Then
    verify(jwtUtils).validateJwtToken(token);
    verify(jwtUtils).getUserNameFromJwtToken(token);
    verify(userDetailsService).loadUserByUsername(username);
    assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void testDoFilterInternal_WithInvalidJwt() throws Exception {
    // Given
    String token = "invalid.jwt.token";

    when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
    when(jwtUtils.validateJwtToken(token)).thenReturn(false);

    // When
    authTokenFilter.doFilterInternal(request, response, filterChain);

    // Then
    verify(jwtUtils).validateJwtToken(token);
    verifyNoInteractions(userDetailsService);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void testDoFilterInternal_WithNoJwt() throws Exception {
    // Given
    when(request.getHeader("Authorization")).thenReturn(null);

    // When
    authTokenFilter.doFilterInternal(request, response, filterChain);

    // Then
    verifyNoInteractions(jwtUtils);
    verifyNoInteractions(userDetailsService);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void testDoFilterInternal_WithException() throws Exception {
    // Given
    String token = "valid.jwt.token";
    when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
    when(jwtUtils.validateJwtToken(token)).thenThrow(new RuntimeException("Test exception"));

    // When
    authTokenFilter.doFilterInternal(request, response, filterChain);

    // Then
    verify(jwtUtils).validateJwtToken(token);
    verifyNoInteractions(userDetailsService);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void testDoFilterInternal_WithNonBearerToken() throws Exception {
    // Given
    String token = "Basic sometoken";
    when(request.getHeader("Authorization")).thenReturn(token);

    // When
    authTokenFilter.doFilterInternal(request, response, filterChain);

    // Then
    verifyNoInteractions(jwtUtils);
    verifyNoInteractions(userDetailsService);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void testDoFilterInternal_WithEmptyAuthorizationHeader() throws Exception {
    // Given
    when(request.getHeader("Authorization")).thenReturn("");

    // When
    authTokenFilter.doFilterInternal(request, response, filterChain);

    // Then
    verifyNoInteractions(jwtUtils);
    verifyNoInteractions(userDetailsService);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }
}
