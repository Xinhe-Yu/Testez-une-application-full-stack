package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionMapperTest {

  @InjectMocks
  private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

  @Mock
  private TeacherService teacherService;

  @Mock
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testToEntity() {
    SessionDto dto = new SessionDto();
    dto.setId(1L);
    dto.setName("Yoga Session");
    dto.setDate(new Date());
    dto.setTeacher_id(1L);
    dto.setDescription("A relaxing yoga session");
    dto.setUsers(Arrays.asList(1L, 2L));
    dto.setCreatedAt(LocalDateTime.now());
    dto.setUpdatedAt(LocalDateTime.now());

    Teacher teacher = new Teacher();
    teacher.setId(1L);
    when(teacherService.findById(1L)).thenReturn(teacher);

    User user1 = new User();
    user1.setId(1L);
    User user2 = new User();
    user2.setId(2L);
    when(userService.findById(1L)).thenReturn(user1);
    when(userService.findById(2L)).thenReturn(user2);

    Session entity = sessionMapper.toEntity(dto);

    assertNotNull(entity);
    assertEquals(dto.getId(), entity.getId());
    assertEquals(dto.getName(), entity.getName());
    assertEquals(dto.getDate(), entity.getDate());
    assertEquals(dto.getDescription(), entity.getDescription());
    assertEquals(dto.getCreatedAt(), entity.getCreatedAt());
    assertEquals(dto.getUpdatedAt(), entity.getUpdatedAt());
    assertEquals(teacher, entity.getTeacher());
    assertEquals(2, entity.getUsers().size());
    assertTrue(entity.getUsers().contains(user1));
    assertTrue(entity.getUsers().contains(user2));
  }

  @Test
  void testToDto() {
    Session entity = new Session();
    entity.setId(1L);
    entity.setName("Yoga Session");
    entity.setDate(new Date());
    entity.setDescription("A relaxing yoga session");
    entity.setCreatedAt(LocalDateTime.now());
    entity.setUpdatedAt(LocalDateTime.now());

    Teacher teacher = new Teacher();
    teacher.setId(1L);
    entity.setTeacher(teacher);

    User user1 = new User();
    user1.setId(1L);
    User user2 = new User();
    user2.setId(2L);
    entity.setUsers(Arrays.asList(user1, user2));

    SessionDto dto = sessionMapper.toDto(entity);

    assertNotNull(dto);
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getName(), dto.getName());
    assertEquals(entity.getDate(), dto.getDate());
    assertEquals(entity.getDescription(), dto.getDescription());
    assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
    assertEquals(entity.getUpdatedAt(), dto.getUpdatedAt());
    assertEquals(entity.getTeacher().getId(), dto.getTeacher_id());
    assertEquals(2, dto.getUsers().size());
    assertTrue(dto.getUsers().contains(1L));
    assertTrue(dto.getUsers().contains(2L));
  }

  @Test
  void testToEntityList() {
    SessionDto dto1 = new SessionDto(1L, "Session 1", new Date(), 1L, "Description 1", Arrays.asList(1L),
        LocalDateTime.now(), LocalDateTime.now());
    SessionDto dto2 = new SessionDto(2L, "Session 2", new Date(), 2L, "Description 2", Arrays.asList(2L),
        LocalDateTime.now(), LocalDateTime.now());
    List<SessionDto> dtoList = Arrays.asList(dto1, dto2);

    Teacher teacher1 = new Teacher();
    teacher1.setId(1L);
    Teacher teacher2 = new Teacher();
    teacher2.setId(2L);
    when(teacherService.findById(1L)).thenReturn(teacher1);
    when(teacherService.findById(2L)).thenReturn(teacher2);

    User user1 = new User();
    user1.setId(1L);
    User user2 = new User();
    user2.setId(2L);
    when(userService.findById(1L)).thenReturn(user1);
    when(userService.findById(2L)).thenReturn(user2);

    List<Session> entityList = sessionMapper.toEntity(dtoList);

    assertNotNull(entityList);
    assertEquals(2, entityList.size());
    assertEquals(dto1.getId(), entityList.get(0).getId());
    assertEquals(dto2.getId(), entityList.get(1).getId());
  }

  @Test
  void testToDtoList() {
    Teacher teacher1 = new Teacher();
    teacher1.setId(1L);
    Teacher teacher2 = new Teacher();
    teacher2.setId(2L);

    User user1 = new User();
    user1.setId(1L);
    User user2 = new User();
    user2.setId(2L);

    Session entity1 = new Session(1L, "Session 1", new Date(), "Description 1", teacher1, Arrays.asList(user1),
        LocalDateTime.now(), LocalDateTime.now());
    Session entity2 = new Session(2L, "Session 2", new Date(), "Description 2", teacher2, Arrays.asList(user2),
        LocalDateTime.now(), LocalDateTime.now());
    List<Session> entityList = Arrays.asList(entity1, entity2);

    List<SessionDto> dtoList = sessionMapper.toDto(entityList);

    assertNotNull(dtoList);
    assertEquals(2, dtoList.size());
    assertEquals(entity1.getId(), dtoList.get(0).getId());
    assertEquals(entity2.getId(), dtoList.get(1).getId());
  }

  @Test
  void testToEntityWithNull() {
    SessionDto dto = null;
    Session entity = sessionMapper.toEntity(dto);
    assertNull(entity);
  }

  @Test
  void testToDtoWithNull() {
    Session entity = null;
    SessionDto dto = sessionMapper.toDto(entity);
    assertNull(dto);
  }

  @Test
  void testToEntityListWithNull() {
    List<SessionDto> dtoList = null;
    List<Session> entityList = sessionMapper.toEntity(dtoList);
    assertNull(entityList);
  }

  @Test
  void testToDtoListWithNull() {
    List<Session> entityList = null;
    List<SessionDto> dtoList = sessionMapper.toDto(entityList);
    assertNull(dtoList);
  }

  @Test
  void testUsersMappingWithValidUserIds() {
    SessionDto dto = new SessionDto();
    dto.setUsers(Arrays.asList(1L, 2L));
    User user1 = new User();
    user1.setId(1L);
    User user2 = new User();
    user2.setId(2L);

    when(userService.findById(1L)).thenReturn(user1);
    when(userService.findById(2L)).thenReturn(user2);

    Session result = sessionMapper.toEntity(dto);
    assertEquals(2, result.getUsers().size());
    assertTrue(result.getUsers().contains(user1));
    assertTrue(result.getUsers().contains(user2));
  }

  @Test
  void testUsersMappingWithNullUserIds() {
    SessionDto dto = new SessionDto();
    dto.setUsers(null);

    Session result = sessionMapper.toEntity(dto);
    assertTrue(result.getUsers().isEmpty());
  }

  @Test
  void testUsersMappingWithEmptyUserIds() {
    SessionDto dto = new SessionDto();
    dto.setUsers(Collections.emptyList());

    Session result = sessionMapper.toEntity(dto);
    assertTrue(result.getUsers().isEmpty());
  }

  @Test
  void testUsersMappingWithNullUser() {
    SessionDto dto = new SessionDto();
    dto.setUsers(Arrays.asList(1L, null, 2L));
    User user1 = new User();
    user1.setId(1L);
    User user2 = new User();
    user2.setId(2L);

    when(userService.findById(1L)).thenReturn(user1);
    when(userService.findById(2L)).thenReturn(user2);
    when(userService.findById(null)).thenReturn(null);

    Session result = sessionMapper.toEntity(dto);
    assertEquals(3, result.getUsers().size());
    assertTrue(result.getUsers().contains(user1));
    assertTrue(result.getUsers().contains(user2));
  }

  @Test
  void testTeacherMappingWithValidTeacherId() {
    SessionDto dto = new SessionDto();
    dto.setTeacher_id(1L);
    Teacher teacher = new Teacher();
    teacher.setId(1L);

    when(teacherService.findById(1L)).thenReturn(teacher);

    Session result = sessionMapper.toEntity(dto);
    assertEquals(teacher, result.getTeacher());
  }

  @Test
  void testTeacherMappingWithNullTeacherId() {
    SessionDto dto = new SessionDto();
    dto.setTeacher_id(null);

    Session result = sessionMapper.toEntity(dto);
    assertNull(result.getTeacher());
  }
}
