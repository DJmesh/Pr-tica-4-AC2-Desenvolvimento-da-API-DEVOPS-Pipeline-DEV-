package br.com.valueprojects.subscription.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentDTOExtraTest {

    @Test
    void equalsAndHashCodeAndBuilderDefault() {
        EnrollmentDTO a = EnrollmentDTO.builder()
                .studentId(1L)
                .courseCode("ML-101")
                .build();

        EnrollmentDTO b = EnrollmentDTO.builder()
                .studentId(1L)
                .courseCode("ML-101")
                .usingVoucher(false)
                .build();

        EnrollmentDTO c = EnrollmentDTO.builder()
                .studentId(2L)
                .courseCode("ML-101")
                .usingVoucher(true)
                .build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "string");
    }

}
