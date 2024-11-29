package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserMapperIT {

  @Autowired
  private UserMapper userMapper;

  @Test
  public void testUserMapperAutowired() {
    assertNotNull(userMapper, "UserMapper should be autowired");
  }

  @Test
  public void testToEntityIntegration() {
    UserDto dto = new UserDto(1L, "test@example.com", "Doe", "John", false, "password", LocalDateTime.now(),
        LocalDateTime.now());

    User entity = userMapper.toEntity(dto);

    assertNotNull(entity);
    assertEquals(dto.getId(), entity.getId());
    assertEquals(dto.getEmail(), entity.getEmail());
    assertEquals(dto.getLastName(), entity.getLastName());
    assertEquals(dto.getFirstName(), entity.getFirstName());
    assertEquals(dto.isAdmin(), entity.isAdmin());
    assertEquals(dto.getPassword(), entity.getPassword());
    assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
    assertEquals(dto.getUpdatedAt(), entity.getUpdatedAt());
  }

  @Test
  public void testToDtoIntegration() {
    User entity = new User("test@example.com", "Doe", "John", "password", false);
    entity.setId(1L);
    entity.setCreatedAt(LocalDateTime.now());
    entity.setUpdatedAt(LocalDateTime.now());

    UserDto dto = userMapper.toDto(entity);

    assertNotNull(dto);
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getEmail(), dto.getEmail());
    assertEquals(entity.getLastName(), dto.getLastName());
    assertEquals(entity.getFirstName(), dto.getFirstName());
    assertEquals(entity.isAdmin(), dto.isAdmin());
    assertEquals(entity.getPassword(), dto.getPassword());
    assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
    assertEquals(entity.getUpdatedAt(), dto.getUpdatedAt());
  }

  @Test
  public void testToEntityListIntegration() {
    UserDto dto1 = new UserDto(1L, "test1@example.com", "Doe", "John", false, "password", LocalDateTime.now(),
        LocalDateTime.now());
    UserDto dto2 = new UserDto(2L, "test2@example.com", "Smith", "Jane", true, "password", LocalDateTime.now(),
        LocalDateTime.now());
    List<UserDto> dtoList = Arrays.asList(dto1, dto2);

    List<User> entityList = userMapper.toEntity(dtoList);

    assertNotNull(entityList);
    assertEquals(2, entityList.size());
    assertEquals(dto1.getId(), entityList.get(0).getId());
    assertEquals(dto1.getEmail(), entityList.get(0).getEmail());
    assertEquals(dto2.getId(), entityList.get(1).getId());
    assertEquals(dto2.getEmail(), entityList.get(1).getEmail());
  }

  @Test
  public void testToDtoListIntegration() {
    User entity1 = new User("test1@example.com", "Doe", "John", "password", false);
    User entity2 = new User("test2@example.com", "Smith", "Jane", "password", true);
    entity1.setId(1L);
    entity2.setId(2L);
    List<User> entityList = Arrays.asList(entity1, entity2);

    List<UserDto> dtoList = userMapper.toDto(entityList);

    assertNotNull(dtoList);
    assertEquals(2, dtoList.size());
    assertEquals(entity1.getId(), dtoList.get(0).getId());
    assertEquals(entity1.getEmail(), dtoList.get(0).getEmail());
    assertEquals(entity2.getId(), dtoList.get(1).getId());
    assertEquals(entity2.getEmail(), dtoList.get(1).getEmail());
  }

  @Test
  public void testToEntityWithNullIntegration() {
    UserDto dto = null;
    User entity = userMapper.toEntity(dto);
    assertNull(entity);
  }

  @Test
  public void testToDtoWithNullIntegration() {
    User entity = null;
    UserDto dto = userMapper.toDto(entity);
    assertNull(dto);
  }

  @Test
  public void testToEntityListWithNullIntegration() {
    List<UserDto> dtoList = null;
    List<User> entityList = userMapper.toEntity(dtoList);
    assertNull(entityList);
  }

  @Test
  public void testToDtoListWithNullIntegration() {
    List<User> entityList = null;
    List<UserDto> dtoList = userMapper.toDto(entityList);
    assertNull(dtoList);
  }
}
