package com.example.demo.student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @Test
    void selectExistsEmail() {
        //arrange
        String email = "someEmail@gmail.com";
        Student student = new Student("Ivan", email, Gender.MALE);

        //act
        underTest.save(student);
        boolean result = underTest.selectExistsEmail(email);

        //assert
        assertTrue(result);
    }
}