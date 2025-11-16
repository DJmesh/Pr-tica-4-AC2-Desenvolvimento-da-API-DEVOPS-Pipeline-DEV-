package br.com.valueprojects.subscription.repository;

import br.com.valueprojects.subscription.entity.Student;
import br.com.valueprojects.subscription.vo.Plan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    private Student student;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .name("Test Student")
                .plan(Plan.BASIC)
                .completedCourses(5)
                .credits(10)
                .coins(20)
                .build();
    }

    @Test
    void testSave() {
        Student saved = studentRepository.save(student);
        assertNotNull(saved.getId());
        assertEquals("Test Student", saved.getName());
    }

    @Test
    void testFindById() {
        Student saved = studentRepository.save(student);
        Optional<Student> found = studentRepository.findById(saved.getId());
        
        assertTrue(found.isPresent());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    void testFindAll() {
        studentRepository.save(student);
        Student student2 = Student.builder()
                .name("Another Student")
                .plan(Plan.PREMIUM)
                .completedCourses(15)
                .credits(5)
                .coins(10)
                .build();
        studentRepository.save(student2);

        List<Student> all = studentRepository.findAll();
        assertTrue(all.size() >= 2);
    }

    @Test
    void testFindByName() {
        studentRepository.save(student);
        Optional<Student> found = studentRepository.findByName("Test Student");
        
        assertTrue(found.isPresent());
        assertEquals(student.getName(), found.get().getName());
    }

    @Test
    void testFindByPlan() {
        studentRepository.save(student);
        Student premium = Student.builder()
                .name("Premium Student")
                .plan(Plan.PREMIUM)
                .completedCourses(15)
                .credits(5)
                .coins(10)
                .build();
        studentRepository.save(premium);

        List<Student> basicStudents = studentRepository.findByPlan("BASIC");
        List<Student> premiumStudents = studentRepository.findByPlan("PREMIUM");

        assertFalse(basicStudents.isEmpty());
        assertFalse(premiumStudents.isEmpty());
        assertTrue(basicStudents.stream().anyMatch(s -> s.getName().equals("Test Student")));
        assertTrue(premiumStudents.stream().anyMatch(s -> s.getName().equals("Premium Student")));
    }

    @Test
    void testFindByCreditsGreaterThanEqual() {
        studentRepository.save(student);
        Student lowCredits = Student.builder()
                .name("Low Credits")
                .plan(Plan.BASIC)
                .credits(2)
                .build();
        studentRepository.save(lowCredits);

        List<Student> result = studentRepository.findByCreditsGreaterThanEqual(10);
        assertTrue(result.stream().anyMatch(s -> s.getName().equals("Test Student")));
        assertFalse(result.stream().anyMatch(s -> s.getName().equals("Low Credits")));
    }

    @Test
    void testDelete() {
        Student saved = studentRepository.save(student);
        studentRepository.deleteById(saved.getId());

        Optional<Student> found = studentRepository.findById(saved.getId());
        assertFalse(found.isPresent());
    }
}


