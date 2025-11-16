package br.com.valueprojects.subscription.dto;

import br.com.valueprojects.subscription.entity.Student;
import br.com.valueprojects.subscription.vo.Plan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para representar um estudante")
public class StudentDTO {

    @Schema(description = "ID do estudante", example = "1")
    private Long id;

    @Schema(description = "Nome do estudante", example = "João Silva")
    private String name;

    @Schema(description = "Plano do estudante", example = "BASIC", allowableValues = {"BASIC", "PREMIUM"})
    private String plan;

    @Schema(description = "Quantidade de cursos concluídos", example = "10")
    private Integer completedCourses;

    @Schema(description = "Quantidade de créditos", example = "5")
    private Integer credits;

    @Schema(description = "Quantidade de moedas", example = "20")
    private Integer coins;

    public static StudentDTO fromEntity(Student student) {
        if (student == null) {
            return null;
        }
        return StudentDTO.builder()
                .id(student.getId())
                .name(student.getName())
                .plan(student.getPlan() != null ? student.getPlan().getType() : null)
                .completedCourses(student.getCompletedCourses())
                .credits(student.getCredits())
                .coins(student.getCoins())
                .build();
    }

    public Student toEntity() {
        Plan planObj = Plan.BASIC;
        if (this.plan != null && "PREMIUM".equalsIgnoreCase(this.plan)) {
            planObj = Plan.PREMIUM;
        }

        return Student.builder()
                .id(this.id)
                .name(this.name)
                .plan(planObj)
                .completedCourses(this.completedCourses != null ? this.completedCourses : 0)
                .credits(this.credits != null ? this.credits : 0)
                .coins(this.coins != null ? this.coins : 0)
                .build();
    }
}



