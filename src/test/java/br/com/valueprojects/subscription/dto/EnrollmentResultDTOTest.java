package br.com.valueprojects.subscription.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentResultDTOTest {

    static class EnrollmentResultDTOFake extends EnrollmentResultDTO {
        @Override
        protected boolean canEqual(Object other) {
            return false;
        }
    }

    @Test
    void acceptedFactoryDevePreencherAcceptedTrueECode() {
        EnrollmentResultDTO dto = EnrollmentResultDTO.accepted("ML-101");

        assertTrue(dto.getAccepted());
        assertEquals("ML-101", dto.getCode());
        assertNull(dto.getReason());
    }

    @Test
    void rejectedFactoryDevePreencherAcceptedFalseEReason() {
        EnrollmentResultDTO dto = EnrollmentResultDTO.rejected("INSUFFICIENT_CREDIT");

        assertFalse(dto.getAccepted());
        assertNull(dto.getCode());
        assertEquals("INSUFFICIENT_CREDIT", dto.getReason());
    }

    @Test
    void construtorAllArgsEGettersSettersDevemFuncionar() {
        EnrollmentResultDTO dto = new EnrollmentResultDTO(true, "ML-101", null);

        assertTrue(dto.getAccepted());
        assertEquals("ML-101", dto.getCode());
        assertNull(dto.getReason());

        dto.setReason("MOTIVO");
        assertEquals("MOTIVO", dto.getReason());
    }

    @Test
    void equalsHashCodeToStringECanEqualDevemCobrirRamos() {
        EnrollmentResultDTO base = EnrollmentResultDTO.builder()
                .accepted(true)
                .code("ML-101")
                .reason(null)
                .build();

        EnrollmentResultDTO igual = EnrollmentResultDTO.builder()
                .accepted(true)
                .code("ML-101")
                .reason(null)
                .build();

        EnrollmentResultDTO diferente = EnrollmentResultDTO.builder()
                .accepted(false)
                .code("ML-102")
                .reason("X")
                .build();

        // self
        assertEquals(base, base);

        // igual
        assertEquals(base, igual);
        assertEquals(base.hashCode(), igual.hashCode());

        // diferente
        assertNotEquals(base, diferente);

        // null
        assertNotEquals(base, null);

        // tipo diferente
        assertNotEquals(base, "outro tipo");

        // todos campos null
        EnrollmentResultDTO n1 = new EnrollmentResultDTO();
        EnrollmentResultDTO n2 = new EnrollmentResultDTO();
        assertEquals(n1, n2);
        n1.hashCode();

        // diferenças por campo na ordem: accepted, code, reason
        EnrollmentResultDTO diffAccepted1 = EnrollmentResultDTO.builder()
                .accepted(true).code("X").reason("R").build();
        EnrollmentResultDTO diffAccepted2 = EnrollmentResultDTO.builder()
                .accepted(false).code("X").reason("R").build();
        assertNotEquals(diffAccepted1, diffAccepted2);

        EnrollmentResultDTO diffCode1 = EnrollmentResultDTO.builder()
                .accepted(true).code("A").reason("R").build();
        EnrollmentResultDTO diffCode2 = EnrollmentResultDTO.builder()
                .accepted(true).code("B").reason("R").build();
        assertNotEquals(diffCode1, diffCode2);

        EnrollmentResultDTO diffReason1 = EnrollmentResultDTO.builder()
                .accepted(true).code("A").reason("R1").build();
        EnrollmentResultDTO diffReason2 = EnrollmentResultDTO.builder()
                .accepted(true).code("A").reason("R2").build();
        assertNotEquals(diffReason1, diffReason2);

        // this null vs other não-null
        EnrollmentResultDTO thisCodeNull = EnrollmentResultDTO.builder()
                .accepted(true).code(null).reason("R").build();
        EnrollmentResultDTO otherCodeNaoNull = EnrollmentResultDTO.builder()
                .accepted(true).code("X").reason("R").build();
        assertNotEquals(thisCodeNull, otherCodeNaoNull);
        assertNotEquals(otherCodeNaoNull, thisCodeNull);

        // canEqual false
        EnrollmentResultDTOFake fake = new EnrollmentResultDTOFake();
        fake.setAccepted(base.getAccepted());
        fake.setCode(base.getCode());
        fake.setReason(base.getReason());
        assertFalse(base.equals(fake));

        // toString
        String texto = base.toString();
        assertNotNull(texto);
        assertTrue(texto.contains("accepted=true"));
    }

    @Test
    void builderToStringNaoDeveSerNull() {
        EnrollmentResultDTO.EnrollmentResultDTOBuilder builder = EnrollmentResultDTO.builder()
                .accepted(true)
                .code("ML-201")
                .reason("OK");

        String s = builder.toString();
        assertNotNull(s);

        EnrollmentResultDTO dto = builder.build();
        assertEquals("ML-201", dto.getCode());
    }

    @Test
    void settersDevemFuncionarPerfeitamente() {
        EnrollmentResultDTO dto = new EnrollmentResultDTO();
        
        dto.setAccepted(true);
        dto.setCode("TEST");
        dto.setReason("REASON");
        
        assertTrue(dto.getAccepted());
        assertEquals("TEST", dto.getCode());
        assertEquals("REASON", dto.getReason());
    }

    @Test
    void toStringDeveConterValores() {
        EnrollmentResultDTO dto = EnrollmentResultDTO.accepted("ML-101");
        String str = dto.toString();
        
        assertNotNull(str);
        assertTrue(str.contains("accepted=true"));
        assertTrue(str.contains("ML-101"));
    }

    @Test
    void hashCodeDeveSerConsistente() {
        EnrollmentResultDTO dto1 = EnrollmentResultDTO.accepted("ML-101");
        EnrollmentResultDTO dto2 = EnrollmentResultDTO.accepted("ML-101");
        
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void dtoComTodosOsCamposNullDeveSerValido() {
        EnrollmentResultDTO dto = new EnrollmentResultDTO();
        dto.setAccepted(null);
        dto.setCode(null);
        dto.setReason(null);
        
        assertNull(dto.getAccepted());
        assertNull(dto.getCode());
        assertNull(dto.getReason());
    }

    @Test
    void rejectedFactoryComNullDeveRetornarNulls() {
        EnrollmentResultDTO dto = EnrollmentResultDTO.rejected(null);
        
        assertFalse(dto.getAccepted());
        assertNull(dto.getCode());
        assertNull(dto.getReason());
    }

    @Test
    void acceptedFactoryComNullDeveRetornarNulls() {
        EnrollmentResultDTO dto = EnrollmentResultDTO.accepted(null);
        
        assertTrue(dto.getAccepted());
        assertNull(dto.getCode());
        assertNull(dto.getReason());
    }

    @Test
    void canEqualFalseDeveFazerEqualsRetornarFalso() {
        EnrollmentResultDTO real = EnrollmentResultDTO.accepted("ML-101");
        EnrollmentResultDTOFake fake = new EnrollmentResultDTOFake();
        fake.setAccepted(true);
        fake.setCode("ML-101");
        fake.setReason(null);
        
        assertFalse(real.equals(fake));
    }

    @Test
    void nullVsNonNullReasonDeveFuncionar() {
        EnrollmentResultDTO thisReasonNull = EnrollmentResultDTO.builder()
                .accepted(false).code(null).reason(null).build();
        EnrollmentResultDTO otherReasonNaoNull = EnrollmentResultDTO.builder()
                .accepted(false).code(null).reason("X").build();
        assertNotEquals(thisReasonNull, otherReasonNaoNull);
        assertNotEquals(otherReasonNaoNull, thisReasonNull);
    }

    @Test
    void nullVsNonNullAcceptedDeveFuncionar() {
        EnrollmentResultDTO thisAcceptedNull = EnrollmentResultDTO.builder()
                .accepted(null).code("A").reason("R").build();
        EnrollmentResultDTO otherAcceptedNaoNull = EnrollmentResultDTO.builder()
                .accepted(true).code("A").reason("R").build();
        assertNotEquals(thisAcceptedNull, otherAcceptedNaoNull);
        assertNotEquals(otherAcceptedNaoNull, thisAcceptedNull);
    }
}
