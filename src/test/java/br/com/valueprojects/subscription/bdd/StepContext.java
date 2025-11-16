package br.com.valueprojects.subscription.bdd;

import br.com.valueprojects.subscription.EnrollmentService;
import br.com.valueprojects.subscription.ProgressService;
import br.com.valueprojects.subscription.entity.Student;

/** Contexto compartilhado entre os passos. */
public class StepContext {
    public Student student;
    public final EnrollmentService enrollmentSvc = new EnrollmentService();
    public final ProgressService progressSvc = new ProgressService();
    public EnrollmentService.Result lastEnrollment;
}
