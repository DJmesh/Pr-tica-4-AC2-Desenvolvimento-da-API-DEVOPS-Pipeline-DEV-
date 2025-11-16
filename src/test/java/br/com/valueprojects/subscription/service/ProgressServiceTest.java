package br.com.valueprojects.subscription.service;

import br.com.valueprojects.subscription.dto.StudentDTO;
import br.com.valueprojects.subscription.entity.Student;
import br.com.valueprojects.subscription.vo.Plan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressServiceTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private ProgressService progressService;

    private Student student;

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
    }

    @Test
    void testFinishCourseWithHighAverage() {
        when(studentService.findStudentEntityById(1L)).thenReturn(student);
        when(studentService.updateStudent(any(), any())).thenAnswer(invocation -> {
            Student updated = invocation.getArgument(1, StudentDTO.class).toEntity();
            return StudentDTO.fromEntity(updated);
        });

        StudentDTO result = progressService.finishCourse(1L, 1, 9.5);

        assertNotNull(result);
        assertEquals(6, result.getCompletedCourses());
        assertEquals(15, result.getCredits());
        verify(studentService, times(1)).updateStudent(any(), any());
    }

    @Test
    void testFinishCourseWithMediumAverage() {
        when(studentService.findStudentEntityById(1L)).thenReturn(student);
        when(studentService.updateStudent(any(), any())).thenAnswer(invocation -> {
            Student updated = invocation.getArgument(1, StudentDTO.class).toEntity();
            return StudentDTO.fromEntity(updated);
        });

        StudentDTO result = progressService.finishCourse(1L, 1, 7.5);

        assertNotNull(result);
        assertEquals(6, result.getCompletedCourses());
        assertEquals(13, result.getCredits());
        verify(studentService, times(1)).updateStudent(any(), any());
    }

    @Test
    void testFinishCoursePromotesToPremium() {
        student.setCompletedCourses(12);
        when(studentService.findStudentEntityById(1L)).thenReturn(student);
        when(studentService.updateStudent(any(), any())).thenAnswer(invocation -> {
            StudentDTO dto = invocation.getArgument(1);
            return dto;
        });

        StudentDTO result = progressService.finishCourse(1L, 1, 8.0);

        assertNotNull(result);
        assertEquals(13, result.getCompletedCourses());
        assertEquals("PREMIUM", result.getPlan());
    }

    @Test
    void testConvertCoins() {
        when(studentService.findStudentEntityById(1L)).thenReturn(student);
        when(studentService.updateStudent(any(), any())).thenAnswer(invocation -> {
            StudentDTO dto = invocation.getArgument(1);
            return dto;
        });

        StudentDTO result = progressService.convertCoins(1L, 10);

        assertNotNull(result);
        assertEquals(15, result.getCredits());
        assertEquals(10, result.getCoins());
        verify(studentService, times(1)).updateStudent(any(), any());
    }
}



