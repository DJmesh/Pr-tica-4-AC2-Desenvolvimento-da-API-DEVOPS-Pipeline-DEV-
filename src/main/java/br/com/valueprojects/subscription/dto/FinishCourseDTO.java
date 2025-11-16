package br.com.valueprojects.subscription.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para finalizar cursos e atualizar progresso")
public class FinishCourseDTO {

    @Schema(description = "ID do estudante", example = "1", required = true)
    @NotNull(message = "ID do estudante é obrigatório")
    private Long studentId;

    @Schema(description = "Quantidade de cursos concluídos", example = "1", required = true)
    @NotNull(message = "Quantidade de cursos é obrigatória")
    @Min(value = 1, message = "Quantidade de cursos deve ser pelo menos 1")
    @Max(value = 10, message = "Quantidade de cursos não pode ser maior que 10")
    private Integer count;

    @Schema(description = "Média alcançada", example = "8.5", required = true)
    @NotNull(message = "Média é obrigatória")
    @DecimalMin(value = "0.0", message = "Média deve ser maior ou igual a 0")
    @DecimalMax(value = "10.0", message = "Média deve ser menor ou igual a 10")
    private Double average;
}

