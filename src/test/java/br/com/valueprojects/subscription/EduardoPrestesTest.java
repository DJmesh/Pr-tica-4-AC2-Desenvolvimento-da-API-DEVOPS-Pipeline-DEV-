package br.com.valueprojects.subscription;

import br.com.valueprojects.subscription.entity.Student;
import br.com.valueprojects.subscription.vo.Plan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class EduardoPrestesTest {
    @Test
    void voucherDoesNotConsumeCredit() {
        Student s = Student.builder()
                .name("Aluno")
                .plan(Plan.BASIC)
                .credits(2)
                .coins(0)
                .completedCourses(0)
                .build();
        EnrollmentService svc = new EnrollmentService();
        EnrollmentService.Result r = svc.enroll(s, "ML-101", true);
        assertEquals(true, r.accepted);
        assertEquals(2, s.getCredits());
    }
}
