package br.com.valueprojects.subscription;

import br.com.valueprojects.subscription.entity.Student;
import br.com.valueprojects.subscription.vo.Plan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class EduardoWeberTest {
    @Test
    void convertCoinsRateTwoToOne() {
        Student s = Student.builder()
                .name("Weber")
                .plan(Plan.BASIC)
                .coins(4)
                .credits(0)
                .completedCourses(0)
                .build();
        ProgressService p = new ProgressService();
        p.convertCoins(s, 4);
        assertEquals(2, s.getCredits());
        assertEquals(0, s.getCoins());
    }
}
