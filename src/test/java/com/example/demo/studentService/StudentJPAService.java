package com.example.demo.studentService;

import com.example.demo.core.StudentRepository;
import com.example.demo.core.StudentService;
import com.example.demo.exception.StudentNotFoundException;
import com.example.demo.model.Gender;
import com.example.demo.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentJPAService {

    @Test
    public void returnStudentIfExist() {
        Long id = 1L;
        Student studentIvan = new Student(id, "ivan", "ivan@mail.ru", Gender.MALE, "joke", 0);
        StudentRepository studentRepository = mock(StudentRepository.class);
        when(studentRepository.findById(id))
                .thenReturn(Optional.of(studentIvan));

        StudentService studentService = new StudentService(studentRepository, null, null);

        assertEquals(studentService.getStudent(id), studentIvan);
    }

    @Test
    public void returnMsgIfStudentNotExist() {
        Long id = 1L;

        StudentRepository studentRepository = mock(StudentRepository.class);
        when(studentRepository.findById(id))
                .thenReturn(Optional.empty());

        StudentService studentService = new StudentService(studentRepository, null, null);

        StudentNotFoundException exception = assertThrows(StudentNotFoundException.class, () -> {
            studentService.getStudent(id);
        });
        assertEquals(exception.getMessage(), String.format("Студент с id = %d не был найден в базе данных", id));
    }
}
