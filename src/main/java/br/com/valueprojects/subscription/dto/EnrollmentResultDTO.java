package br.com.valueprojects.subscription.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para resultado de matrícula")
public class EnrollmentResultDTO {

    @Schema(description = "Indica se a matrícula foi aceita", example = "true")
    private Boolean accepted;

    @Schema(description = "Código do curso (quando aceito)", example = "ML-101")
    private String code;

    @Schema(description = "Razão da rejeição (quando não aceito)", example = "INSUFFICIENT_CREDIT_OR_VOUCHER")
    private String reason;

    public static EnrollmentResultDTO accepted(String code) {
        return EnrollmentResultDTO.builder()
                .accepted(true)
                .code(code)
                .reason(null)
                .build();
    }

    public static EnrollmentResultDTO rejected(String reason) {
        return EnrollmentResultDTO.builder()
                .accepted(false)
                .code(null)
                .reason(reason)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnrollmentResultDTO that = (EnrollmentResultDTO) o;
        return java.util.Objects.equals(accepted, that.accepted) &&
                java.util.Objects.equals(code, that.code) &&
                java.util.Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(accepted, code, reason);
    }
}



