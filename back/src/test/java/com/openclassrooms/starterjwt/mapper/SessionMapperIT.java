package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class SessionMapperIT {

  @Autowired
  private SessionMapper sessionMapper;

  @Autowired
  private TeacherService teacherService;

  @Autowired
  private TeacherRepository teacherRepository;

  @Autowired
  private UserService userService;

  @Test
  public void testTeacherFetch() {
    List<Teacher> teachers = teacherRepository.findAll();
    assertFalse(teachers.isEmpty());
    Teacher teacher = teachers.get(0);
    assertNotNull(teacher);
    assertEquals("Margot", teacher.getFirstName());
    assertEquals("DELAHAYE", teacher.getLastName());
  }

  @Test
  public void testToEntity() {
    SessionDto sessionDto = new SessionDto();
    sessionDto.setName("Test Session");
    sessionDto.setDescription("Test Description");
    sessionDto.setDate(new Date());
    sessionDto.setTeacher_id(1L);
    sessionDto.setUsers(Arrays.asList(1L, 2L));

    Session session = sessionMapper.toEntity(sessionDto);

    assertNotNull(session);
    assertEquals(sessionDto.getName(), session.getName());
    assertEquals(sessionDto.getDescription(), session.getDescription());
    assertEquals(sessionDto.getDate(), session.getDate());
    assertNotNull(session.getTeacher());
    assertEquals(sessionDto.getTeacher_id(), session.getTeacher().getId());
    assertEquals(2, session.getUsers().size());
  }

  @Test
  public void testToDto() {
    Session session = new Session();
    session.setName("Test Session");
    session.setDescription("Test Description");
    session.setDate(new Date());

    Teacher teacher = teacherService.findById(1L);
    assertNotNull(teacher, "Teacher should not be null");
    session.setTeacher(teacher);

    User user1 = userService.findById(1L);
    assertNotNull(user1, "User 1 should not be null");
    session.setUsers(Arrays.asList(user1));

    // Ensure users are not null before mapping
    assertNotNull(session.getUsers());
    assertFalse(session.getUsers().isEmpty());

    SessionDto sessionDto = sessionMapper.toDto(session);

    assertNotNull(sessionDto);
    assertEquals(session.getName(), sessionDto.getName());
    assertEquals(session.getDescription(), sessionDto.getDescription());
    assertEquals(session.getDate(), sessionDto.getDate());
    assertEquals(session.getTeacher().getId(), sessionDto.getTeacher_id());
    assertNotNull(sessionDto.getUsers());
    assertEquals(1, sessionDto.getUsers().size());
    assertTrue(sessionDto.getUsers().contains(user1.getId()));
  }
}
