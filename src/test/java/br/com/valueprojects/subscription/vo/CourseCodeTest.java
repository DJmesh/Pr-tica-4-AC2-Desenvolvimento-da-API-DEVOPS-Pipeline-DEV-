package br.com.valueprojects.subscription.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseCodeTest {

    @Test
    void shouldCreateCourseCodeWithValidCode() {
        CourseCode courseCode = new CourseCode("CS101");
        assertNotNull(courseCode);
        assertEquals("CS101", courseCode.getCode());
    }

    @Test
    void shouldTrimAndUpperCaseCode() {
        CourseCode courseCode = new CourseCode("  cs101  ");
        assertEquals("CS101", courseCode.getCode());
    }

    @Test
    void shouldThrowExceptionWhenCodeIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CourseCode(null);
        });
    }

    @Test
    void shouldThrowExceptionWhenCodeIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CourseCode("");
        });
    }

    @Test
    void shouldThrowExceptionWhenCodeIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new CourseCode("   ");
        });
    }

    @Test
    void shouldThrowExceptionWhenCodeExceedsMaxLength() {
        String longCode = "A".repeat(51);
        assertThrows(IllegalArgumentException.class, () -> {
            new CourseCode(longCode);
        });
    }

    @Test
    void shouldAcceptCodeWithMaxLength() {
        String maxLengthCode = "A".repeat(50);
        CourseCode courseCode = new CourseCode(maxLengthCode);
        assertEquals(maxLengthCode, courseCode.getCode());
    }

    @Test
    void shouldHaveEqualsAndHashCode() {
        CourseCode code1 = new CourseCode("CS101");
        CourseCode code2 = new CourseCode("CS101");
        CourseCode code3 = new CourseCode("CS102");

        assertEquals(code1, code2);
        assertEquals(code1.hashCode(), code2.hashCode());
        assertNotEquals(code1, code3);
    }

    @Test
    void shouldReturnCodeInToString() {
        CourseCode courseCode = new CourseCode("CS101");
        assertEquals("CS101", courseCode.toString());
    }

    @Test
    void shouldHandleSpecialCharacters() {
        CourseCode courseCode = new CourseCode("CS-101");
        assertEquals("CS-101", courseCode.getCode());
    }

    @Test
    void shouldHandleNumbers() {
        CourseCode courseCode = new CourseCode("101");
        assertEquals("101", courseCode.getCode());
    }

    @Test
    void shouldHandleMixedCase() {
        CourseCode courseCode = new CourseCode("cs101");
        assertEquals("CS101", courseCode.getCode());
    }
}

