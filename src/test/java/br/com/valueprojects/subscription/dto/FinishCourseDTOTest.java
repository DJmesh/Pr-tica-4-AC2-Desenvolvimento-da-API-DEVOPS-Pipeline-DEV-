package br.com.valueprojects.subscription.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FinishCourseDTOTest {

    static class FinishCourseDTOFake extends FinishCourseDTO {
        @Override
        protected boolean canEqual(Object other) {
            return false;
        }
    }

    @Test
    void builderDeveMontarObjetoCorretamente() {
        FinishCourseDTO dto = FinishCourseDTO.builder()
                .studentId(1L)
                .count(3)
                .average(9.5)
                .build();

        assertAll(
                () -> assertEquals(1L, dto.getStudentId()),
                () -> assertEquals(3, dto.getCount()),
                () -> assertEquals(9.5, dto.getAverage())
        );
    }

    @Test
    void construtorAllArgsEGettersSettersDevemFuncionar() {
        FinishCourseDTO dto = new FinishCourseDTO(2L, 5, 8.0);

        assertAll(
                () -> assertEquals(2L, dto.getStudentId()),
                () -> assertEquals(5, dto.getCount()),
                () -> assertEquals(8.0, dto.getAverage())
        );

        dto.setAverage(7.0);
        assertEquals(7.0, dto.getAverage());
    }

    @Test
    void equalsHashCodeToStringECanEqualDevemCobrirRamos() {
        FinishCourseDTO base = FinishCourseDTO.builder()
                .studentId(10L)
                .count(2)
                .average(7.5)
                .build();

        FinishCourseDTO igual = FinishCourseDTO.builder()
                .studentId(10L)
                .count(2)
                .average(7.5)
                .build();

        FinishCourseDTO diferente = FinishCourseDTO.builder()
                .studentId(11L)
                .count(3)
                .average(5.0)
                .build();

        // self
        assertEquals(base, base);

        // igual
        assertEquals(base, igual);
        assertEquals(base.hashCode(), igual.hashCode());

        // diferente
        assertNotEquals(base, diferente);

        // null e tipo diferente
        assertNotEquals(base, null);
        assertNotEquals(base, "x");

        // todos null
        FinishCourseDTO n1 = new FinishCourseDTO();
        FinishCourseDTO n2 = new FinishCourseDTO();
        assertEquals(n1, n2);
        n1.hashCode();

        // diferenças por campo: studentId, count, average
        FinishCourseDTO diffStudent1 = FinishCourseDTO.builder()
                .studentId(1L).count(1).average(1.0).build();
        FinishCourseDTO diffStudent2 = FinishCourseDTO.builder()
                .studentId(2L).count(1).average(1.0).build();
        assertNotEquals(diffStudent1, diffStudent2);

        FinishCourseDTO diffCount1 = FinishCourseDTO.builder()
                .studentId(1L).count(1).average(1.0).build();
        FinishCourseDTO diffCount2 = FinishCourseDTO.builder()
                .studentId(1L).count(2).average(1.0).build();
        assertNotEquals(diffCount1, diffCount2);

        FinishCourseDTO diffAverage1 = FinishCourseDTO.builder()
                .studentId(1L).count(1).average(1.0).build();
        FinishCourseDTO diffAverage2 = FinishCourseDTO.builder()
                .studentId(1L).count(1).average(2.0).build();
        assertNotEquals(diffAverage1, diffAverage2);

        // null vs não-null
        FinishCourseDTO thisCountNull = FinishCourseDTO.builder()
                .studentId(1L).count(null).average(1.0).build();
        FinishCourseDTO otherCountNaoNull = FinishCourseDTO.builder()
                .studentId(1L).count(1).average(1.0).build();
        assertNotEquals(thisCountNull, otherCountNaoNull);
        assertNotEquals(otherCountNaoNull, thisCountNull);

        // canEqual false
        FinishCourseDTOFake fake = new FinishCourseDTOFake();
        fake.setStudentId(base.getStudentId());
        fake.setCount(base.getCount());
        fake.setAverage(base.getAverage());
        assertFalse(base.equals(fake));

        // toString
        String texto = base.toString();
        assertNotNull(texto);
        assertTrue(texto.contains("studentId=10"));
    }

    @Test
    void builderToStringNaoDeveSerNull() {
        FinishCourseDTO.FinishCourseDTOBuilder builder = FinishCourseDTO.builder()
                .studentId(1L)
                .count(2)
                .average(9.0);

        String s = builder.toString();
        assertNotNull(s);

        FinishCourseDTO dto = builder.build();
        assertEquals(2, dto.getCount());
    }

    @Test
    void settersDevemFuncionarPerfeitamente() {
        FinishCourseDTO dto = new FinishCourseDTO();
        
        dto.setStudentId(5L);
        dto.setCount(3);
        dto.setAverage(8.5);
        
        assertEquals(5L, dto.getStudentId());
        assertEquals(3, dto.getCount());
        assertEquals(8.5, dto.getAverage());
    }

    @Test
    void toStringDeveConterValores() {
        FinishCourseDTO dto = new FinishCourseDTO(1L, 2, 7.5);
        String str = dto.toString();
        
        assertNotNull(str);
        assertTrue(str.contains("studentId=1"));
        assertTrue(str.contains("count=2"));
        assertTrue(str.contains("7.5"));
    }

    @Test
    void hashCodeDeveSerConsistente() {
        FinishCourseDTO dto1 = new FinishCourseDTO(1L, 2, 7.5);
        FinishCourseDTO dto2 = new FinishCourseDTO(1L, 2, 7.5);
        
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void dtoComTodosOsCamposNullDeveSerValido() {
        FinishCourseDTO dto = new FinishCourseDTO();
        
        assertNull(dto.getStudentId());
        assertNull(dto.getCount());
        assertNull(dto.getAverage());
    }

    @Test
    void nollVsNonNullAverageDeveFuncionar() {
        FinishCourseDTO thisAverageNull = FinishCourseDTO.builder()
                .studentId(1L).count(1).average(null).build();
        FinishCourseDTO otherAverageNaoNull = FinishCourseDTO.builder()
                .studentId(1L).count(1).average(1.0).build();
        assertNotEquals(thisAverageNull, otherAverageNaoNull);
        assertNotEquals(otherAverageNaoNull, thisAverageNull);
    }

    @Test
    void nollVsNonNullStudentIdDeveFuncionar() {
        FinishCourseDTO thisStudentNull = FinishCourseDTO.builder()
                .studentId(null).count(1).average(1.0).build();
        FinishCourseDTO otherStudentNaoNull = FinishCourseDTO.builder()
                .studentId(1L).count(1).average(1.0).build();
        assertNotEquals(thisStudentNull, otherStudentNaoNull);
        assertNotEquals(otherStudentNaoNull, thisStudentNull);
    }

    @Test
    void canEqualFalseDeveFazerEqualsRetornarFalso() {
        FinishCourseDTO real = new FinishCourseDTO(1L, 2, 7.5);
        FinishCourseDTOFake fake = new FinishCourseDTOFake();
        fake.setStudentId(1L);
        fake.setCount(2);
        fake.setAverage(7.5);
        
        assertFalse(real.equals(fake));
    }
}
