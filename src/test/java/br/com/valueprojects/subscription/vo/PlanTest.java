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
}



