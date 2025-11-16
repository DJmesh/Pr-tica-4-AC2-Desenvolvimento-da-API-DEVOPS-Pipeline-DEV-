package br.com.valueprojects.subscription;

import br.com.valueprojects.subscription.entity.Student;
import br.com.valueprojects.subscription.vo.Plan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class LucasTest {
    @Test
    void promoteOnlyWhenMoreThanTwelve() {
        Student s = Student.builder()
                .name("Lucas")
                .plan(Plan.BASIC)
                .completedCourses(12)
                .credits(0)
                .coins(0)
                .build();
        ProgressService p = new ProgressService();
        p.finishCourse(s, 1, 8.0);
        assertEquals(Plan.PREMIUM, s.getPlan());
    }
}
