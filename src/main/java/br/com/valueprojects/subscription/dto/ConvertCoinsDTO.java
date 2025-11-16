package br.com.valueprojects.subscription.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para converter moedas em créditos")
public class ConvertCoinsDTO {

    @Schema(description = "ID do estudante", example = "1", required = true)
    @NotNull(message = "ID do estudante é obrigatório")
    private Long studentId;

    @Schema(description = "Quantidade de moedas para converter", example = "10", required = true)
    @NotNull(message = "Quantidade de moedas é obrigatória")
    @Min(value = 1, message = "Quantidade de moedas deve ser pelo menos 1")
    private Integer coinsToConvert;
}



