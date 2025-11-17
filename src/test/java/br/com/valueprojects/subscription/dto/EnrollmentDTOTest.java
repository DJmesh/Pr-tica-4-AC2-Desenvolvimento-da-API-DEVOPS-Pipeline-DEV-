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

    static class EnrollmentDTOFake extends EnrollmentDTO {
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

        assertEquals(base, base);
        assertEquals(base, igual);
        assertEquals(base.hashCode(), igual.hashCode());
        assertNotEquals(base, diferente);
        assertNotEquals(base, null);
        assertNotEquals(base, "string");

        EnrollmentDTO studentNull = EnrollmentDTO.builder().courseCode("A").build();
        EnrollmentDTO studentValue = EnrollmentDTO.builder().studentId(1L).courseCode("A").build();
        assertNotEquals(studentNull, studentValue);

        EnrollmentDTO codeNull = EnrollmentDTO.builder().studentId(1L).courseCode(null).build();
        EnrollmentDTO codeValue = EnrollmentDTO.builder().studentId(1L).courseCode("A").build();
        assertNotEquals(codeNull, codeValue);

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

    @Test
    void settersDevemFuncionarPerfeitamente() {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setStudentId(2L);
        dto.setCourseCode("DS-101");
        dto.setUsingVoucher(true);
        assertEquals(2L, dto.getStudentId());
        assertEquals("DS-101", dto.getCourseCode());
        assertTrue(dto.getUsingVoucher());
    }

    @Test
    void toStringDeveConterValores() {
        EnrollmentDTO dto = new EnrollmentDTO(1L, "ML-101", false);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.contains("studentId=1"));
        assertTrue(str.contains("ML-101"));
    }

    @Test
    void hashCodeDeveSerConsistente() {
        EnrollmentDTO dto1 = EnrollmentDTO.builder().studentId(1L).courseCode("ML-101").usingVoucher(true).build();
        EnrollmentDTO dto2 = EnrollmentDTO.builder().studentId(1L).courseCode("ML-101").usingVoucher(true).build();
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void nullVsNonNullVoucherDeveFuncionar() {
        EnrollmentDTO thisVoucherNull = EnrollmentDTO.builder().studentId(1L).courseCode("A").usingVoucher(null).build();
        EnrollmentDTO otherVoucherNaoNull = EnrollmentDTO.builder().studentId(1L).courseCode("A").usingVoucher(false).build();
        assertNotEquals(thisVoucherNull, otherVoucherNaoNull);
    }

    @Test
    void dtoComTodosOsCamposNullDeveSerValido() {
        EnrollmentDTO dto = new EnrollmentDTO();
        assertNull(dto.getStudentId());
        assertNull(dto.getCourseCode());
        assertFalse(dto.getUsingVoucher());
    }

    @Test
    void builderToStringNaoDeveSerNull() {
        EnrollmentDTO.EnrollmentDTOBuilder builder = EnrollmentDTO.builder()
                .studentId(5L)
                .courseCode("AI-202");
        String s = builder.toString();
        assertNotNull(s);
        EnrollmentDTO dto = builder.build();
        assertEquals("AI-202", dto.getCourseCode());
    }

    @Test
    void nullVsNonNullCourseCodeDeveFuncionar() {
        EnrollmentDTO thisCodeNull = EnrollmentDTO.builder().studentId(1L).courseCode(null).usingVoucher(false).build();
        EnrollmentDTO otherCodeNaoNull = EnrollmentDTO.builder().studentId(1L).courseCode("A").usingVoucher(false).build();
        assertNotEquals(thisCodeNull, otherCodeNaoNull);
        assertNotEquals(otherCodeNaoNull, thisCodeNull);
    }

    @Test
    void nullVsNonNullStudentIdDeveFuncionar() {
        EnrollmentDTO thisStudentNull = EnrollmentDTO.builder().studentId(null).courseCode("A").usingVoucher(false).build();
        EnrollmentDTO otherStudentNaoNull = EnrollmentDTO.builder().studentId(1L).courseCode("A").usingVoucher(false).build();
        assertNotEquals(thisStudentNull, otherStudentNaoNull);
        assertNotEquals(otherStudentNaoNull, thisStudentNull);
    }
    @Test
    void equalsComUsingVoucherTrueEFalseMostrarDiferenca() {
        EnrollmentDTO voucherTrue = EnrollmentDTO.builder().studentId(1L).courseCode("ABC").usingVoucher(true).build();
        EnrollmentDTO voucherFalse = EnrollmentDTO.builder().studentId(1L).courseCode("ABC").usingVoucher(false).build();
        assertNotEquals(voucherTrue, voucherFalse);
        assertNotEquals(voucherFalse, voucherTrue);
    }

    @Test
    void hashCodeComUsingVoucherVariandoMostraBranch() {
        EnrollmentDTO dto1 = EnrollmentDTO.builder().studentId(1L).courseCode("ABC").usingVoucher(true).build();
        EnrollmentDTO dto2 = EnrollmentDTO.builder().studentId(1L).courseCode("ABC").usingVoucher(false).build();
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void equalsComStudentIdDiferentesMostraBranch() {
        EnrollmentDTO dto1 = EnrollmentDTO.builder().studentId(1L).courseCode("ABC").usingVoucher(false).build();
        EnrollmentDTO dto2 = EnrollmentDTO.builder().studentId(2L).courseCode("ABC").usingVoucher(false).build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    void equalsComCourseCodeDiferentesMostraBranch() {
        EnrollmentDTO dto1 = EnrollmentDTO.builder().studentId(1L).courseCode("ABC").usingVoucher(false).build();
        EnrollmentDTO dto2 = EnrollmentDTO.builder().studentId(1L).courseCode("XYZ").usingVoucher(false).build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCodeComCourseCodeVariandoMostraBranch() {
        EnrollmentDTO dto1 = EnrollmentDTO.builder().studentId(1L).courseCode("ABC").usingVoucher(false).build();
        EnrollmentDTO dto2 = EnrollmentDTO.builder().studentId(1L).courseCode("XYZ").usingVoucher(false).build();
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void hashCodeComStudentIdVariandoMostraBranch() {
        EnrollmentDTO dto1 = EnrollmentDTO.builder().studentId(1L).courseCode("ABC").usingVoucher(false).build();
        EnrollmentDTO dto2 = EnrollmentDTO.builder().studentId(2L).courseCode("ABC").usingVoucher(false).build();
        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }
}
