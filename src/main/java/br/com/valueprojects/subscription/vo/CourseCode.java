package br.com.valueprojects.subscription.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Locale;

/**
 * Value Object para código do curso.
 * Garante que o código do curso siga um padrão válido.
 */
@Embeddable
@Getter
@NoArgsConstructor
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
        this.code = code.trim().toUpperCase(Locale.ROOT);
    }

    @Override
    public String toString() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CourseCode that = (CourseCode) o;
        return java.util.Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(code);
    }
}



