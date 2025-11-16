package br.com.valueprojects.subscription.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value Object para código do curso.
 * Garante que o código do curso siga um padrão válido.
 */
@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class CourseCode {

    @Column(name = "course_code", nullable = false, length = 50)
    private String code;

    public CourseCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Código do curso não pode ser vazio");
        }
        if (code.length() > 50) {
            throw new IllegalArgumentException("Código do curso não pode ter mais de 50 caracteres");
        }
        this.code = code.trim().toUpperCase();
    }

    @Override
    public String toString() {
        return code;
    }
}



