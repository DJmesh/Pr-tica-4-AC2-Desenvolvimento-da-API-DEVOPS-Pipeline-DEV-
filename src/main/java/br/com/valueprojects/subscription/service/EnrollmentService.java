package br.com.valueprojects.subscription.service;

import br.com.valueprojects.subscription.dto.EnrollmentResultDTO;
import br.com.valueprojects.subscription.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnrollmentService {

    public static final String REJECT_REASON_INSUFFICIENT = "INSUFFICIENT_CREDIT_OR_VOUCHER";

    private final StudentService studentService;

    @Autowired
    public EnrollmentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Transactional
    public EnrollmentResultDTO enroll(Long studentId, String courseCode, boolean usingVoucher) {
        Student student = studentService.findStudentEntityById(studentId);

        if (usingVoucher) {
            // Voucher: does not consume credit
            return EnrollmentResultDTO.accepted(courseCode);
        }

        if (student.getCredits() < 1) {
            return EnrollmentResultDTO.rejected(REJECT_REASON_INSUFFICIENT);
        }

        student.consumeCredit();
        studentService.updateStudent(studentId, 
                br.com.valueprojects.subscription.dto.StudentDTO.fromEntity(student));

        return EnrollmentResultDTO.accepted(courseCode);
    }
}



