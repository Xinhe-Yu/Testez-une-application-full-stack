package com.openclassrooms.starterjwt.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class SessionMapperprivateTest {
  private SessionMapper sessionMapper;
  private Method sessionTeacherIdMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    sessionMapper = Mappers.getMapper(SessionMapper.class);
    sessionTeacherIdMethod = sessionMapper.getClass().getDeclaredMethod("sessionTeacherId", Session.class);
    sessionTeacherIdMethod.setAccessible(true);
  }

  @Test
  void testSessionTeacherIdWithValidTeacher() throws Exception {
    Teacher teacher = new Teacher();
    teacher.setId(1L);
    Session session = new Session();
    session.setTeacher(teacher);

    Long result = (Long) sessionTeacherIdMethod.invoke(sessionMapper, session);
    assertEquals(1L, result);
  }

  @Test
  void testSessionTeacherIdWithNullSession() throws Exception {
    Long result = (Long) sessionTeacherIdMethod.invoke(sessionMapper, (Session) null);
    assertNull(result);
  }

  @Test
  void testSessionTeacherIdWithNullTeacher() throws Exception {
    Session session = new Session();
    session.setTeacher(null);

    Long result = (Long) sessionTeacherIdMethod.invoke(sessionMapper, session);
    assertNull(result);
  }

  @Test
  void testSessionTeacherIdWithNullTeacherId() throws Exception {
    Teacher teacher = new Teacher();
    teacher.setId(null);
    Session session = new Session();
    session.setTeacher(teacher);

    Long result = (Long) sessionTeacherIdMethod.invoke(sessionMapper, session);
    assertNull(result);
  }

}
