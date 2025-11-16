package br.com.valueprojects.subscription.entity;

import br.com.valueprojects.subscription.vo.Plan;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void testCreateStudentWithBuilder() {
        Student student = Student.builder()
                .name("João Silva")
                .plan(Plan.BASIC)
                .completedCourses(5)
                .credits(10)
                .coins(20)
                .build();

        assertNotNull(student);
        assertEquals("João Silva", student.getName());
        assertEquals(Plan.BASIC, student.getPlan());
        assertEquals(5, student.getCompletedCourses());
        assertEquals(10, student.getCredits());
        assertEquals(20, student.getCoins());
    }

    @Test
    void testCreateStudentWithConstructor() {
        Student student = new Student("Maria Santos");

        assertNotNull(student);
        assertEquals("Maria Santos", student.getName());
        assertEquals(Plan.BASIC, student.getPlan());
        assertEquals(0, student.getCompletedCourses());
        assertEquals(0, student.getCredits());
        assertEquals(0, student.getCoins());
    }

    @Test
    void testAddCredits() {
        Student student = new Student("Test");
        student.setCredits(5);
        student.addCredits(10);

        assertEquals(15, student.getCredits());
    }

    @Test
    void testConsumeCredit() {
        Student student = new Student("Test");
        student.setCredits(5);
        student.consumeCredit();

        assertEquals(4, student.getCredits());
    }

    @Test
    void testConsumeCreditWhenZero() {
        Student student = new Student("Test");
        student.setCredits(0);
        student.consumeCredit();

        assertEquals(0, student.getCredits());
    }

    @Test
    void testAddCompletedCourses() {
        Student student = new Student("Test");
        student.setCompletedCourses(5);
        student.addCompletedCourses(3);

        assertEquals(8, student.getCompletedCourses());
    }

    @Test
    void testSetPlanPremium() {
        Student student = new Student("Test");
        student.setPlan(Plan.PREMIUM);

        assertEquals(Plan.PREMIUM, student.getPlan());
        assertTrue(student.getPlan().isPremium());
    }
}



