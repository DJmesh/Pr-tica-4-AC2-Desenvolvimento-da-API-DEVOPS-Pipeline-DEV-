package br.com.valueprojects.subscription.service;

import br.com.valueprojects.subscription.dto.StudentDTO;
import br.com.valueprojects.subscription.entity.Student;
import br.com.valueprojects.subscription.vo.Plan;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProgressServiceExtraTest {

    @Test
    void finishCourse_withAverageBelow7_noCreditsAwarded() {
        StudentService studentService = mock(StudentService.class);
        ProgressService progressService = new ProgressService(studentService);

        Student student = Student.builder()
                .id(1L)
                .name("Student")
                .plan(Plan.BASIC)
                .completedCourses(0)
                .credits(0)
                .coins(0)
                .build();

        when(studentService.findStudentEntityById(1L)).thenReturn(student);
        // forward updateStudent to simply return the DTO argument
        when(studentService.updateStudent(eq(1L), any(StudentDTO.class))).thenAnswer(invocation -> invocation.getArgument(1));

        StudentDTO result = progressService.finishCourse(1L, 1, 6.5);

        assertEquals(1, student.getCompletedCourses());
        assertEquals(0, student.getCredits());
        assertEquals(Plan.BASIC, student.getPlan());
        assertNotNull(result);
    }
}
