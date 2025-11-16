package br.com.valueprojects.subscription.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value Object que representa o plano de assinatura do estudante.
 * Contém a regra de negócio para determinar se o aluno deve estar no plano Premium ou Básico.
 */
@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Plan {

    public static final Plan BASIC = new Plan("BASIC");
    public static final Plan PREMIUM = new Plan("PREMIUM");

    @Column(name = "plan_type", nullable = false)
    private String type;

    private static final int MINIMUM_COURSES_FOR_PREMIUM = 12;

    private Plan(String type) {
        if (type == null || (!type.equals("BASIC") && !type.equals("PREMIUM"))) {
            throw new IllegalArgumentException("Tipo de plano inválido: " + type);
        }
        this.type = type;
    }

    /**
     * Determina qual plano o estudante deve ter baseado na quantidade de cursos concluídos.
     * Regra: Premium quando completedCourses > 12, caso contrário Basic.
     *
     * @param completedCourses quantidade de cursos concluídos pelo estudante
     * @return Plan.PREMIUM se completedCourses > 12, caso contrário Plan.BASIC
     */
    public static Plan determinePlan(int completedCourses) {
        if (completedCourses > MINIMUM_COURSES_FOR_PREMIUM) {
            return PREMIUM;
        }
        return BASIC;
    }

    /**
     * Verifica se o plano atual deve ser atualizado baseado na quantidade de cursos concluídos.
     *
     * @param completedCourses quantidade de cursos concluídos pelo estudante
     * @return true se o plano deve ser alterado, false caso contrário
     */
    public boolean shouldUpdate(int completedCourses) {
        Plan expectedPlan = determinePlan(completedCourses);
        return !this.equals(expectedPlan);
    }

    public boolean isBasic() {
        return "BASIC".equals(this.type);
    }

    public boolean isPremium() {
        return "PREMIUM".equals(this.type);
    }

    @Override
    public String toString() {
        return type;
    }
}



