package br.com.valueprojects.subscription;

import br.com.valueprojects.subscription.entity.Student;
import br.com.valueprojects.subscription.vo.Plan;

/**
 * Versão simplificada do ProgressService para testes BDD.
 * Esta versão trabalha diretamente com a entidade Student sem depender de Spring.
 */
public class ProgressService {

    /**
     * Finish {count} courses with the given average.
     * >= 9.0 -> +5 credits
     * 7.0 <= avg < 9.0 -> +3 credits
     * < 7.0 -> +0 credits
     * Promotion to PREMIUM only when completedCourses > 12.
     */
    public void finishCourse(Student student, int count, double average) {
        // update courses
        student.addCompletedCourses(count);

        // award credits
        if (average >= 9.0) {
            student.addCredits(5);
        } else if (average >= 7.0) {
            student.addCredits(3);
        }

        // promotion rule: strictly greater than 12
        Plan determinedPlan = Plan.determinePlan(student.getCompletedCourses());
        student.setPlan(determinedPlan);
    }

    /** Convert coins to credits at a 2:1 rate. */
    public void convertCoins(Student student, int coinsToConvert) {
        int available = Math.min(coinsToConvert, student.getCoins());
        int credits = available / 2;
        student.setCoins(available - credits * 2 + (student.getCoins() - available));
        student.addCredits(credits);
    }
}
