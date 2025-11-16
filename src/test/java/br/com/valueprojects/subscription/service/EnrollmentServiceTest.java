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
class EnrollmentServiceTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private EnrollmentService enrollmentService;

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
    void testEnrollWithVoucher() {
        when(studentService.findStudentEntityById(1L)).thenReturn(student);

        var result = enrollmentService.enroll(1L, "ML-101", true);

        assertTrue(result.getAccepted());
        assertEquals("ML-101", result.getCode());
        assertNull(result.getReason());
        verify(studentService, times(1)).findStudentEntityById(1L);
        verify(studentService, never()).updateStudent(any(), any());
    }

    @Test
    void testEnrollWithCredits() {
        when(studentService.findStudentEntityById(1L)).thenReturn(student);
        when(studentService.updateStudent(any(), any())).thenReturn(StudentDTO.fromEntity(student));

        var result = enrollmentService.enroll(1L, "ML-101", false);

        assertTrue(result.getAccepted());
        assertEquals("ML-101", result.getCode());
        verify(studentService, times(1)).updateStudent(any(), any());
    }

    @Test
    void testEnrollWithoutCredits() {
        student.setCredits(0);
        when(studentService.findStudentEntityById(1L)).thenReturn(student);

        var result = enrollmentService.enroll(1L, "ML-101", false);

        assertFalse(result.getAccepted());
        assertEquals("INSUFFICIENT_CREDIT_OR_VOUCHER", result.getReason());
        assertNull(result.getCode());
        verify(studentService, never()).updateStudent(any(), any());
    }
}



