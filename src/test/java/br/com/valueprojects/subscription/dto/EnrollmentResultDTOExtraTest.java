package br.com.valueprojects.subscription.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentResultDTOExtraTest {

    @Test
    void staticFactoriesAndEquality() {
        EnrollmentResultDTO accepted = EnrollmentResultDTO.accepted("ML-101");
        EnrollmentResultDTO accepted2 = EnrollmentResultDTO.builder()
                .accepted(true)
                .code("ML-101")
                .reason(null)
                .build();

        EnrollmentResultDTO rejected = EnrollmentResultDTO.rejected("INSUFFICIENT");

        assertTrue(accepted.getAccepted());
        assertEquals("ML-101", accepted.getCode());
        assertNull(accepted.getReason());

        assertEquals(accepted, accepted2);
        assertNotEquals(accepted, rejected);
        assertNotEquals(accepted, null);
        assertNotEquals(accepted, "string");
    }

}
