package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
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
public class TeacherMapperIT {

  @Autowired
  private TeacherMapper teacherMapper;

  @Test
  public void testTeacherMapperAutowired() {
    assertNotNull(teacherMapper, "TeacherMapper should be autowired");
  }

  @Test
  public void testToEntityIntegration() {
    TeacherDto dto = new TeacherDto(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now());

    Teacher entity = teacherMapper.toEntity(dto);

    assertNotNull(entity);
    assertEquals(dto.getId(), entity.getId());
    assertEquals(dto.getLastName(), entity.getLastName());
    assertEquals(dto.getFirstName(), entity.getFirstName());
    assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
    assertEquals(dto.getUpdatedAt(), entity.getUpdatedAt());
  }

  @Test
  public void testToDtoIntegration() {
    Teacher entity = new Teacher(1L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now());

    TeacherDto dto = teacherMapper.toDto(entity);

    assertNotNull(dto);
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getLastName(), dto.getLastName());
    assertEquals(entity.getFirstName(), dto.getFirstName());
    assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
    assertEquals(entity.getUpdatedAt(), dto.getUpdatedAt());
  }

  @Test
  public void testToEntityListIntegration() {
    List<TeacherDto> dtoList = Arrays.asList(
        new TeacherDto(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now()),
        new TeacherDto(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now()));

    List<Teacher> entityList = teacherMapper.toEntity(dtoList);

    assertNotNull(entityList);
    assertEquals(2, entityList.size());
    assertEquals(dtoList.get(0).getId(), entityList.get(0).getId());
    assertEquals(dtoList.get(1).getId(), entityList.get(1).getId());
  }

  @Test
  public void testToDtoListIntegration() {
    List<Teacher> entityList = Arrays.asList(
        new Teacher(1L, "Doe", "John", LocalDateTime.now(), LocalDateTime.now()),
        new Teacher(2L, "Smith", "Jane", LocalDateTime.now(), LocalDateTime.now()));

    List<TeacherDto> dtoList = teacherMapper.toDto(entityList);

    assertNotNull(dtoList);
    assertEquals(2, dtoList.size());
    assertEquals(entityList.get(0).getId(), dtoList.get(0).getId());
    assertEquals(entityList.get(1).getId(), dtoList.get(1).getId());
  }
}
