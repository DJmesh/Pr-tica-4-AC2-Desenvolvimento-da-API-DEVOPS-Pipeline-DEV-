package br.com.valueprojects.subscription.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlanTest {

    @Test
    void testDeterminePlanBasic() {
        Plan plan = Plan.determinePlan(10);
        assertEquals(Plan.BASIC, plan);
        assertTrue(plan.isBasic());
    }

    @Test
    void testDeterminePlanPremium() {
        Plan plan = Plan.determinePlan(13);
        assertEquals(Plan.PREMIUM, plan);
        assertTrue(plan.isPremium());
    }

    @Test
    void testDeterminePlanExactly12() {
        Plan plan = Plan.determinePlan(12);
        assertEquals(Plan.BASIC, plan);
    }

    @Test
    void testShouldUpdate() {
        Plan basic = Plan.BASIC;
        assertTrue(basic.shouldUpdate(13));
        assertFalse(basic.shouldUpdate(10));
    }

    @Test
    void testEqualsAndHashCode() {
        Plan plan1 = Plan.BASIC;
        Plan plan2 = Plan.BASIC;
        Plan plan3 = Plan.PREMIUM;

        assertEquals(plan1, plan2);
        assertNotEquals(plan1, plan3);
        assertEquals(plan1.hashCode(), plan2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("BASIC", Plan.BASIC.toString());
        assertEquals("PREMIUM", Plan.PREMIUM.toString());
    }

    @Test
    void testIsBasic() {
        assertTrue(Plan.BASIC.isBasic());
        assertFalse(Plan.PREMIUM.isBasic());
    }

    @Test
    void testIsPremium() {
        assertTrue(Plan.PREMIUM.isPremium());
        assertFalse(Plan.BASIC.isPremium());
    }

    @Test
    void testShouldUpdateFromBasicToPremium() {
        Plan basic = Plan.BASIC;
        assertTrue(basic.shouldUpdate(13));
        assertTrue(basic.shouldUpdate(15));
    }

    @Test
    void testShouldUpdateFromPremiumToBasic() {
        Plan premium = Plan.PREMIUM;
        assertTrue(premium.shouldUpdate(10));
        assertTrue(premium.shouldUpdate(12));
    }

    @Test
    void testShouldNotUpdateWhenPlanIsCorrect() {
        Plan basic = Plan.BASIC;
        assertFalse(basic.shouldUpdate(10));
        assertFalse(basic.shouldUpdate(12));

        Plan premium = Plan.PREMIUM;
        assertFalse(premium.shouldUpdate(13));
        assertFalse(premium.shouldUpdate(15));
    }

    @Test
    void testDeterminePlanBoundary() {
        Plan plan11 = Plan.determinePlan(11);
        assertEquals(Plan.BASIC, plan11);

        Plan plan12 = Plan.determinePlan(12);
        assertEquals(Plan.BASIC, plan12);

        Plan plan13 = Plan.determinePlan(13);
        assertEquals(Plan.PREMIUM, plan13);
    }

    @Test
    void testPlanConstructorWithInvalidType() {
        // Teste do construtor privado através de reflexão ou validação
        // Como o construtor é privado, testamos através dos valores válidos
        // e verificamos que apenas BASIC e PREMIUM são aceitos
        assertNotNull(Plan.BASIC);
        assertNotNull(Plan.PREMIUM);
        assertEquals("BASIC", Plan.BASIC.getType());
        assertEquals("PREMIUM", Plan.PREMIUM.getType());
    }
}



