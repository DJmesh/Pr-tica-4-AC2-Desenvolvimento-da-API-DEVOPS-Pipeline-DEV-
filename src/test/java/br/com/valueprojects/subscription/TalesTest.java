package br.com.valueprojects.subscription;

import br.com.valueprojects.subscription.entity.Student;
import br.com.valueprojects.subscription.vo.Plan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TalesTest {
    @Test
    void gainFiveCreditsWhenAvgAtLeastNine() {
        Student s = Student.builder()
                .name("Tales")
                .plan(Plan.BASIC)
                .completedCourses(0)
                .credits(0)
                .coins(0)
                .build();
        ProgressService p = new ProgressService();
        p.finishCourse(s, 1, 9.1);
        assertEquals(1, s.getCompletedCourses());
        assertEquals(5, s.getCredits());
        assertEquals(Plan.BASIC, s.getPlan());
    }

    @Test
    void gainThreeCreditsWhenAvgBetweenSevenAndNine() {
        Student s = Student.builder()
                .name("Tales")
                .plan(Plan.BASIC)
                .completedCourses(0)
                .credits(0)
                .coins(0)
                .build();
        ProgressService p = new ProgressService();
        p.finishCourse(s, 1, 7.3);
        assertEquals(3, s.getCredits());
    }
}
