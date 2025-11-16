package br.com.valueprojects.subscription.bdd;

import static org.junit.jupiter.api.Assertions.*;
import io.cucumber.java.pt.Entao;
import br.com.valueprojects.subscription.vo.Plan;
import br.com.valueprojects.subscription.entity.Student;

public class Steps {
    private final StepContext ctx;
    public Steps(StepContext ctx) { this.ctx = ctx; } // injeção

    @Entao("o aluno deve possuir {int} crédito(s)")
    public void studentShouldHaveCredits(int credits) {
        assertEquals(credits, ctx.student.getCredits(), "Quantidade de créditos divergente.");
    }

    @Entao("o aluno deve possuir {int} moedas")
    public void studentShouldHaveCoins(int coins) {
        assertEquals(coins, ctx.student.getCoins(), "Quantidade de moedas divergente.");
    }

    @Entao("o aluno deve possuir {int} curso concluído")
    public void studentShouldHaveCompletedCourses(int completed) {
        assertEquals(completed, ctx.student.getCompletedCourses(), "Cursos concluídos divergente.");
    }

    @Entao("o plano deve ser BASIC")
    public void planShouldBeBasic() { assertEquals(Plan.BASIC, ctx.student.getPlan()); }

    @Entao("o plano deve ser PREMIUM")
    public void planShouldBePremium() { assertEquals(Plan.PREMIUM, ctx.student.getPlan()); }
}
