package br.com.valueprojects.subscription.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Subclasse para testar o ramo !other.canEqual(this)
     */
    static class EnrollmentDTOFake extends EnrollmentDTO {
        @Override
        protected boolean canEqual(Object other) {
            return false;
        }
    }

    @Test
    void deveCriarComAllArgsEGettersFuncionando(){
        EnrollmentDTO dto = new EnrollmentDTO(1L, "ML-101", true);

        assertEquals(1L, dto.getStudentId());
        assertEquals("ML-101", dto.getCourseCode());
        assertTrue(dto.getUsingVoucher());

        dto.setCourseCode("NEW");
        assertEquals("NEW", dto.getCourseCode());
    }

    @Test
    void builderDeveFuncionarEToStringNaoPodeSerNull(){
        EnrollmentDTO dto = EnrollmentDTO.builder()
                .studentId(10L)
                .courseCode("ABC-1")
                .usingVoucher(true)
                .build();

        assertEquals(10L, dto.getStudentId());
        assertEquals("ABC-1", dto.getCourseCode());
        assertTrue(dto.getUsingVoucher());

        assertNotNull(dto.toString());
    }

    @Test
    void deveUsarDefaultUsingVoucherFalse(){
        EnrollmentDTO dto = EnrollmentDTO.builder()
                .studentId(5L)
                .courseCode("ZZ-99")
                .build();

        assertFalse(dto.getUsingVoucher());
    }

    @Test
    void equalsHashCodeECanEqualDevemCobrirTodosOsRamos(){
        EnrollmentDTO base = EnrollmentDTO.builder()
                .studentId(1L)
                .courseCode("ABC")
                .usingVoucher(false)
                .build();

        EnrollmentDTO igual = EnrollmentDTO.builder()
                .studentId(1L)
                .courseCode("ABC")
                .usingVoucher(false)
                .build();

        EnrollmentDTO diferente = EnrollmentDTO.builder()
                .studentId(2L)
                .courseCode("XYZ")
                .usingVoucher(true)
                .build();

        assertEquals(base, base);            // self
        assertEquals(base, igual);           // igual
        assertEquals(base.hashCode(), igual.hashCode());
        assertNotEquals(base, diferente);    // valores diferentes
        assertNotEquals(base, null);         // null
        assertNotEquals(base, "string");     // outro tipo

        // null vs non-null fields
        EnrollmentDTO studentNull = EnrollmentDTO.builder().courseCode("A").build();
        EnrollmentDTO studentValue = EnrollmentDTO.builder().studentId(1L).courseCode("A").build();
        assertNotEquals(studentNull, studentValue);

        EnrollmentDTO codeNull = EnrollmentDTO.builder().studentId(1L).courseCode(null).build();
        EnrollmentDTO codeValue = EnrollmentDTO.builder().studentId(1L).courseCode("A").build();
        assertNotEquals(codeNull, codeValue);

        // canEqual = false
        EnrollmentDTOFake fake = new EnrollmentDTOFake();
        fake.setStudentId(base.getStudentId());
        fake.setCourseCode(base.getCourseCode());
        fake.setUsingVoucher(base.getUsingVoucher());

        assertFalse(base.equals(fake));
    }

    @Test
    void validacoesDevemDetectarErros(){
        EnrollmentDTO dto = new EnrollmentDTO(null, "", false);

        Set<ConstraintViolation<EnrollmentDTO>> violations = validator.validate(dto);

        assertEquals(2, violations.size());
    }
}
