package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

  private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @Test
  void testToEntity() {
    UserDto dto = new UserDto();
    dto.setId(1L);
    dto.setEmail("test@example.com");
    dto.setLastName("Doe");
    dto.setFirstName("John");
    dto.setAdmin(false);
    dto.setPassword("password");
    dto.setCreatedAt(LocalDateTime.now());
    dto.setUpdatedAt(LocalDateTime.now());

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
  void testToDto() {
    User entity = new User();
    entity.setId(1L);
    entity.setEmail("test@example.com");
    entity.setLastName("Doe");
    entity.setFirstName("John");
    entity.setAdmin(false);
    entity.setPassword("password");
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
  void testToEntityList() {
    UserDto dto1 = new UserDto(1L, "test1@example.com", "Doe", "John", false, "password", LocalDateTime.now(),
        LocalDateTime.now());
    UserDto dto2 = new UserDto(2L, "test2@example.com", "Smith", "Jane", true, "password", LocalDateTime.now(),
        LocalDateTime.now());
    List<UserDto> dtoList = Arrays.asList(dto1, dto2);

    List<User> entityList = userMapper.toEntity(dtoList);

    assertNotNull(entityList);
    assertEquals(2, entityList.size());
    assertEquals(dto1.getId(), entityList.get(0).getId());
    assertEquals(dto2.getId(), entityList.get(1).getId());
  }

  @Test
  void testToDtoList() {
    User entity1 = new User("test1@example.com", "Doe", "John", "password", false);
    User entity2 = new User("test2@example.com", "Smith", "Jane", "password", true);
    List<User> entityList = Arrays.asList(entity1, entity2);

    List<UserDto> dtoList = userMapper.toDto(entityList);

    assertNotNull(dtoList);
    assertEquals(2, dtoList.size());
    assertEquals(entity1.getEmail(), dtoList.get(0).getEmail());
    assertEquals(entity2.getEmail(), dtoList.get(1).getEmail());
  }

  @Test
  void testToEntityWithNull() {
    UserDto dto = null;
    User entity = userMapper.toEntity(dto);
    assertNull(entity);
  }

  @Test
  void testToDtoWithNull() {
    User entity = null;
    UserDto dto = userMapper.toDto(entity);
    assertNull(dto);
  }

  @Test
  void testToEntityListWithNull() {
    List<UserDto> dtoList = null;
    List<User> entityList = userMapper.toEntity(dtoList);
    assertNull(entityList);
  }

  @Test
  void testToDtoListWithNull() {
    List<User> entityList = null;
    List<UserDto> dtoList = userMapper.toDto(entityList);
    assertNull(dtoList);
  }
}
