package br.com.valueprojects.subscription.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlanTest {

    @Test
    void determinePlan_basicAndPremium() {
        assertEquals(Plan.PREMIUM, Plan.determinePlan(13));
        assertEquals(Plan.BASIC, Plan.determinePlan(12));
        assertEquals(Plan.BASIC, Plan.determinePlan(0));
    }

    @Test
    void shouldUpdate_behavior_and_equals() {
        // reflexive
        assertTrue(Plan.BASIC.equals(Plan.BASIC));

        // upgrade path
        assertTrue(Plan.BASIC.shouldUpdate(13));
        // no change
        assertFalse(Plan.PREMIUM.shouldUpdate(13));

        // null and different type
        assertFalse(Plan.BASIC.equals(null));
        assertFalse(Plan.BASIC.equals("BASIC"));
    }

    @Test
    void isBasicAndIsPremiumWork() {
        assertTrue(Plan.BASIC.isBasic());
        assertFalse(Plan.BASIC.isPremium());
        assertTrue(Plan.PREMIUM.isPremium());
        assertFalse(Plan.PREMIUM.isBasic());
    }

    @Test
    void hashCodeAndPrivateConstructorException() throws Exception {
        // ensure hashCode works and is consistent
        assertEquals(Plan.BASIC.hashCode(), Plan.determinePlan(0).hashCode());

        // invoke private constructor to trigger invalid type exception branch
        java.lang.reflect.Constructor<Plan> ctor = Plan.class.getDeclaredConstructor(String.class);
        ctor.setAccessible(true);
        java.lang.reflect.InvocationTargetException ex1 = assertThrows(java.lang.reflect.InvocationTargetException.class, () -> ctor.newInstance("INVALID"));
        assertTrue(ex1.getCause() instanceof IllegalArgumentException);
        java.lang.reflect.InvocationTargetException ex2 = assertThrows(java.lang.reflect.InvocationTargetException.class, () -> ctor.newInstance((Object) null));
        assertTrue(ex2.getCause() instanceof IllegalArgumentException);
    }
}



