package com.openclassrooms.starterjwt.security.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthEntryPointJwtTest {

  private AuthEntryPointJwt authEntryPointJwt;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private AuthenticationException authException;
  private static final com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<Map<String, Object>>() {
  };

  @BeforeEach
  void setUp() {
    authEntryPointJwt = new AuthEntryPointJwt();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    authException = mock(AuthenticationException.class);
  }

  @Test
  void testCommence() throws IOException, ServletException {
    // Given
    String expectedPath = "/api/test";
    String expectedErrorMessage = "Unauthorized access";
    request.setServletPath(expectedPath);
    when(authException.getMessage()).thenReturn(expectedErrorMessage);

    // When
    authEntryPointJwt.commence(request, response, authException);

    // Then
    assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
    assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> responseBody = mapper.readValue(response.getContentAsString(), MAP_TYPE_REFERENCE);

    assertEquals(HttpServletResponse.SC_UNAUTHORIZED, responseBody.get("status"));
    assertEquals("Unauthorized", responseBody.get("error"));
    assertEquals(expectedErrorMessage, responseBody.get("message"));
    assertEquals(expectedPath, responseBody.get("path"));
  };

}
