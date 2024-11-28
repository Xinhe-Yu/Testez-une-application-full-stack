package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherMapperTest {

  private TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

  @Test
  void testToEntity() {
    TeacherDto dto = new TeacherDto();
    dto.setId(1L);
    dto.setLastName("Doe");
    dto.setFirstName("John");
    dto.setCreatedAt(LocalDateTime.now());
    dto.setUpdatedAt(LocalDateTime.now());

    Teacher entity = teacherMapper.toEntity(dto);

    assertNotNull(entity);
    assertEquals(dto.getId(), entity.getId());
    assertEquals(dto.getLastName(), entity.getLastName());
    assertEquals(dto.getFirstName(), entity.getFirstName());
    assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
    assertEquals(dto.getUpdatedAt(), entity.getUpdatedAt());
  }

  @Test
  void testToDto() {
    Teacher entity = new Teacher();
    entity.setId(1L);
    entity.setLastName("Doe");
    entity.setFirstName("John");
    entity.setCreatedAt(LocalDateTime.now());
    entity.setUpdatedAt(LocalDateTime.now());

    TeacherDto dto = teacherMapper.toDto(entity);

    assertNotNull(dto);
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getLastName(), dto.getLastName());
    assertEquals(entity.getFirstName(), dto.getFirstName());
    assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
    assertEquals(entity.getUpdatedAt(), dto.getUpdatedAt());
  }

  @Test
  void testToEntityList() {
    TeacherDto dto1 = new TeacherDto(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
    TeacherDto dto2 = new TeacherDto(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now());
    List<TeacherDto> dtoList = Arrays.asList(dto1, dto2);

    List<Teacher> entityList = teacherMapper.toEntity(dtoList);

    assertNotNull(entityList);
    assertEquals(2, entityList.size());
    assertEquals(dto1.getId(), entityList.get(0).getId());
    assertEquals(dto2.getId(), entityList.get(1).getId());
  }

  @Test
  void testToDtoList() {
    Teacher entity1 = new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());
    Teacher entity2 = new Teacher(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now());
    List<Teacher> entityList = Arrays.asList(entity1, entity2);

    List<TeacherDto> dtoList = teacherMapper.toDto(entityList);

    assertNotNull(dtoList);
    assertEquals(2, dtoList.size());
    assertEquals(entity1.getId(), dtoList.get(0).getId());
    assertEquals(entity2.getId(), dtoList.get(1).getId());
  }

  @Test
  void testToEntityWithNull() {
    TeacherDto dto = null;
    Teacher entity = teacherMapper.toEntity(dto);
    assertNull(entity);
  }

  @Test
  void testToDtoWithNull() {
    Teacher entity = null;
    TeacherDto dto = teacherMapper.toDto(entity);
    assertNull(dto);
  }

  @Test
  void testToEntityListWithNull() {
    List<TeacherDto> dtoList = null;
    List<Teacher> entityList = teacherMapper.toEntity(dtoList);
    assertNull(entityList);
  }

  @Test
  void testToDtoListWithNull() {
    List<Teacher> entityList = null;
    List<TeacherDto> dtoList = teacherMapper.toDto(entityList);
    assertNull(dtoList);
  }
}
