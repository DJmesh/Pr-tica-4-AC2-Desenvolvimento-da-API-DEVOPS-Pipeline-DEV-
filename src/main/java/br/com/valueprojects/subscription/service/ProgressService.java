package br.com.valueprojects.subscription.service;

import br.com.valueprojects.subscription.dto.StudentDTO;
import br.com.valueprojects.subscription.entity.Student;
import br.com.valueprojects.subscription.vo.Plan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgressService {

    private final StudentService studentService;

    @Autowired
    public ProgressService(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Finish courses with the given average.
     * >= 9.0 -> +5 credits
     * 7.0 <= avg < 9.0 -> +3 credits
     * < 7.0 -> +0 credits
     * Promotion to PREMIUM only when completedCourses > 12.
     */
    @Transactional
    public StudentDTO finishCourse(Long studentId, int count, double average) {
        Student student = studentService.findStudentEntityById(studentId);

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

        return studentService.updateStudent(studentId, StudentDTO.fromEntity(student));
    }

    /**
     * Convert coins to credits at a 2:1 rate.
     */
    @Transactional
    public StudentDTO convertCoins(Long studentId, int coinsToConvert) {
        Student student = studentService.findStudentEntityById(studentId);

        int available = Math.min(coinsToConvert, student.getCoins());
        int credits = available / 2;
        student.setCoins(student.getCoins() - available + (available - credits * 2));
        student.addCredits(credits);

        return studentService.updateStudent(studentId, StudentDTO.fromEntity(student));
    }
}



