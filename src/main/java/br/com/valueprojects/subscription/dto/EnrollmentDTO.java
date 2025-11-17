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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnrollmentDTO that = (EnrollmentDTO) o;
        return java.util.Objects.equals(studentId, that.studentId) &&
                java.util.Objects.equals(courseCode, that.courseCode) &&
                java.util.Objects.equals(usingVoucher, that.usingVoucher);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(studentId, courseCode, usingVoucher);
    }
}



