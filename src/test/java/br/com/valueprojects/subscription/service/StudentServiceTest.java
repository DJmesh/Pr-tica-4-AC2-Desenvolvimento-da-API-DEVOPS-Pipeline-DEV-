package br.com.valueprojects.subscription.service;

import br.com.valueprojects.subscription.dto.StudentDTO;
import br.com.valueprojects.subscription.entity.Student;
import br.com.valueprojects.subscription.repository.StudentRepository;
import br.com.valueprojects.subscription.vo.Plan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentDTO studentDTO;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .id(1L)
                .name("Test Student")
                .plan(Plan.BASIC)
                .completedCourses(5)
                .credits(10)
                .coins(20)
                .build();

        studentDTO = StudentDTO.builder()
                .id(1L)
                .name("Test Student")
                .plan("BASIC")
                .completedCourses(5)
                .credits(10)
                .coins(20)
                .build();
    }

    @Test
    void testGetAllStudents() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student));

        List<StudentDTO> result = studentService.getAllStudents();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Student", result.get(0).getName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentDTO result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals("Test Student", result.getName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStudentByIdNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            studentService.getStudentById(1L);
        });
    }

    @Test
    void testCreateStudent() {
        StudentDTO newDTO = StudentDTO.builder()
                .name("New Student")
                .plan("BASIC")
                .completedCourses(0)
                .credits(0)
                .coins(0)
                .build();

        Student newStudent = newDTO.toEntity();
        newStudent.setId(2L);

        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        StudentDTO result = studentService.createStudent(newDTO);

        assertNotNull(result);
        assertEquals("New Student", result.getName());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testUpdateStudent() {
        StudentDTO updateDTO = StudentDTO.builder()
                .name("Updated Student")
                .plan("PREMIUM")
                .completedCourses(10)
                .credits(20)
                .coins(30)
                .build();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentDTO result = studentService.updateStudent(1L, updateDTO);

        assertNotNull(result);
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testDeleteStudent() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1L);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).existsById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteStudentNotFound() {
        when(studentRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            studentService.deleteStudent(1L);
        });
    }
}



