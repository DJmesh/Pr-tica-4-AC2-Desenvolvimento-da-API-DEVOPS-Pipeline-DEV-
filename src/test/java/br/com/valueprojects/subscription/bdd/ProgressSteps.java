package br.com.valueprojects.subscription.bdd;

import io.cucumber.java.pt.Quando;
import io.cucumber.java.en.When;

/** Somente passos de progresso (When). */
public class ProgressSteps {
    private final StepContext ctx;
    public ProgressSteps(StepContext ctx) { this.ctx = ctx; } // injeção

    // Escapando "(s)" com REGEX para casar exatamente seu texto da feature
    @Quando("^o aluno conclui (\\d+) curso\\(s\\) com média ([0-9]+\\.?[0-9]*)$")
    @When("the student finishes {int} course(s) with average {double}")
    public void finishesCourse(int count, double average) {
        ctx.progressSvc.finishCourse(ctx.student, count, average);
    }

    @Quando("o aluno converte {int} moedas em créditos")
    public void convertCoins(int coins) {
        ctx.progressSvc.convertCoins(ctx.student, coins);
    }
}
