package br.com.valueprojects.subscription.bdd;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Entao;

/** Somente passos de matrícula (When/Then). */
public class EnrollmentSteps {
    private final StepContext ctx;
    public EnrollmentSteps(StepContext ctx) { this.ctx = ctx; } // injeção

    @Quando("o aluno se matricula no curso {string} sem voucher")
    public void enrollWithoutVoucher(String code) {
        ctx.lastEnrollment = ctx.enrollmentSvc.enroll(ctx.student, code, false);
    }

    @Quando("o aluno se matricula no curso {string} usando voucher")
    public void enrollUsingVoucher(String code) {
        ctx.lastEnrollment = ctx.enrollmentSvc.enroll(ctx.student, code, true);
    }

    @Quando("o aluno tenta se matricular no curso {string} sem voucher")
    public void triesEnroll(String code) {
        ctx.lastEnrollment = ctx.enrollmentSvc.enroll(ctx.student, code, false);
    }

    @Entao("a matrícula deve ser aceita com o código {string}")
    public void enrollmentAccepted(String code) {
        assertEquals(true, ctx.lastEnrollment.accepted, "Matrícula deveria ser aceita");
        assertEquals(code, ctx.lastEnrollment.code);
    }

    @Entao("a matrícula deve ser rejeitada com o motivo {string}")
    public void enrollmentRejectedWith(String reason) {
        assertEquals(false, ctx.lastEnrollment.accepted, "Matrícula deveria ser rejeitada");
        assertEquals(reason, ctx.lastEnrollment.reason);
    }
}
