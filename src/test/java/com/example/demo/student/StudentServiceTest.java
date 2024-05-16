package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentClient studentClient;
    private StudentService underTest;

    private Student student;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository, studentClient);
        student = new Student("Ivan", "someEmail@gmail.com", Gender.MALE);
    }

    @Test
    void getAllStudents() {
        underTest.getAllStudents();
        verify(studentRepository).findAll();
    }

    @Test
    void addStudent() {
        // arrange
        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);

        // act
        underTest.addStudent(student);
        // assert
        verify(studentRepository).save(captor.capture());
        Student capturedStudent = captor.getValue();
        Assertions.assertEquals(student, capturedStudent);
    }

    @Test
    void some() {
        String email = "someDifferentEmail@gmail.com";
        student.setEmail(email);
        when(studentRepository.selectExistsEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> {
            underTest.addStudent(student);
        }).isInstanceOf(BadRequestException.class).hasMessage("Email " + email + " taken");
    }

    @Test
    @Disabled
    void deleteStudent() {
    }
}