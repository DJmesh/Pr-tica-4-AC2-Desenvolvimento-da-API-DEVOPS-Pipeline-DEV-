package br.com.valueprojects.subscription.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para realizar matrícula em um curso")
public class EnrollmentDTO {

    @Schema(description = "ID do estudante", example = "1", required = true)
    @NotNull(message = "ID do estudante é obrigatório")
    private Long studentId;

    @Schema(description = "Código do curso", example = "ML-101", required = true)
    @NotBlank(message = "Código do curso é obrigatório")
    private String courseCode;

    @Schema(description = "Indica se está usando voucher", example = "false")
    @Builder.Default
    private Boolean usingVoucher = false;
}



