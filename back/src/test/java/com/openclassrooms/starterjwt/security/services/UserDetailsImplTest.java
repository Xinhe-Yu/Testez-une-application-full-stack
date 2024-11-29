package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailsImplTest {

  @Test
  void testBuilder() {
    UserDetailsImpl userDetails = UserDetailsImpl.builder()
        .id(1L)
        .username("testuser")
        .firstName("John")
        .lastName("Doe")
        .admin(true)
        .password("password")
        .build();

    assertThat(userDetails.getId()).isEqualTo(1L);
    assertThat(userDetails.getUsername()).isEqualTo("testuser");
    assertThat(userDetails.getFirstName()).isEqualTo("John");
    assertThat(userDetails.getLastName()).isEqualTo("Doe");
    assertThat(userDetails.getAdmin()).isTrue();
    assertThat(userDetails.getPassword()).isEqualTo("password");
  }

  @Test
  void testGetAuthorities() {
    UserDetailsImpl userDetails = UserDetailsImpl.builder().build();
    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

    assertThat(authorities).isEmpty();
  }

  @Test
  void testIsAccountNonExpired() {
    UserDetailsImpl userDetails = UserDetailsImpl.builder().build();
    assertThat(userDetails.isAccountNonExpired()).isTrue();
  }

  @Test
  void testIsAccountNonLocked() {
    UserDetailsImpl userDetails = UserDetailsImpl.builder().build();
    assertThat(userDetails.isAccountNonLocked()).isTrue();
  }

  @Test
  void testIsCredentialsNonExpired() {
    UserDetailsImpl userDetails = UserDetailsImpl.builder().build();
    assertThat(userDetails.isCredentialsNonExpired()).isTrue();
  }

  @Test
  void testIsEnabled() {
    UserDetailsImpl userDetails = UserDetailsImpl.builder().build();
    assertThat(userDetails.isEnabled()).isTrue();
  }

  @Test
  void testEquals() {
    UserDetailsImpl user1 = UserDetailsImpl.builder().id(1L).build();
    UserDetailsImpl user2 = UserDetailsImpl.builder().id(1L).build();
    UserDetailsImpl user3 = UserDetailsImpl.builder().id(2L).build();

    assertThat(user1).isEqualTo(user1);
    assertThat(user1).isEqualTo(user2);
    assertThat(user1).isNotEqualTo(user3);
    assertThat(user1).isNotEqualTo(null);
    assertThat(user1).isNotEqualTo(new Object());
  }

  @Test
  void testBuilderToString() {
    UserDetailsImpl.UserDetailsImplBuilder builder = UserDetailsImpl.builder()
        .id(1L)
        .username("testuser")
        .firstName("John")
        .lastName("Doe")
        .admin(true)
        .password("password");

    String builderString = builder.toString();

    assertThat(builderString)
        .contains("UserDetailsImpl.UserDetailsImplBuilder")
        .contains("id=1")
        .contains("username=testuser")
        .contains("firstName=John")
        .contains("lastName=Doe")
        .contains("admin=true")
        .contains("password=password");
  }
}
