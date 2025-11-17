package br.com.valueprojects.subscription.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseCodeTest {

    @Test
    void constructor_nullOrEmpty_throws() {
        assertThrows(IllegalArgumentException.class, () -> new CourseCode(null));
        assertThrows(IllegalArgumentException.class, () -> new CourseCode("   "));
    }

    @Test
    void equals_sameAndDifferentCases_and_hashCode() {
        CourseCode a = new CourseCode("abc-101");
        CourseCode b = new CourseCode("ABC-101");

        // same value (constructor normalizes to upper case)
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        // reflexive (this == o)
        assertTrue(a.equals(a));

        // null and different type
        assertFalse(a.equals(null));
        assertFalse(a.equals("ABC-101"));
    }

    @Test
    void shouldTrimAndUpperCaseCode() {
        CourseCode courseCode = new CourseCode("  cs101  ");
        assertEquals("CS101", courseCode.getCode());
    }

    @Test
    void shouldThrowExceptionWhenCodeExceedsMaxLength() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 51; i++) sb.append('A');
        String longCode = sb.toString();
        assertThrows(IllegalArgumentException.class, () -> new CourseCode(longCode));
    }

    @Test
    void shouldAcceptCodeWithMaxLength() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 50; i++) sb.append('A');
        String maxLengthCode = sb.toString();
        CourseCode courseCode = new CourseCode(maxLengthCode);
        assertEquals(maxLengthCode, courseCode.getCode());
    }

    @Test
    void noArgConstructorAndToStringBehavior() {
        // no-arg constructor is used by frameworks (JPA) and should be covered
        CourseCode empty = new CourseCode();
        assertNull(empty.getCode());
        // toString should return null when code is null
        assertNull(empty.toString());
        assertNotEquals(empty, new CourseCode("CS101"));

        // toString for non-null
        CourseCode cc = new CourseCode("CS101");
        assertEquals("CS101", cc.toString());
    }
}

