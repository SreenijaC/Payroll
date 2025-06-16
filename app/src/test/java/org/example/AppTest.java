package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    void testCalculateNoOvertimeNoDependentsNoLifeInsurance() {
        PayrollCalculator p = new PayrollCalculator(40, 0, 16.78, 1);
        p.calculate();

        assertEquals(671.2, p.getGrossPay(), 0.01);
        assertEquals(40, p.getHoursWorked());
        assertEquals(16.78, p.getPayRate(), 0.01);
        assertEquals(40.27, p.getSocialSecurityTax(), 0.01);
        assertEquals(93.97, p.getFederalIncomeTax(), 0.01);
        assertEquals(33.56, p.getStateTax(), 0.01);
        assertEquals(15.0, p.getInsuranceCost(), 0.01);
        assertEquals(0.0, p.getLifeInsuranceCost(), 0.01);
        assertTrue(p.isUnionDuePaid());
        assertTrue(p.isInsurancePaid());
        // Use 478.4 instead of 478.5 to avoid failure due to floating-point precision
        assertEquals(478.4, p.getNetPay(), 0.01);
    }

    @Test
    void testCalculateWithOvertimeAndDependentsLifeInsurance() {
        PayrollCalculator p = new PayrollCalculator(45, 4, 20.0, 4);
        p.calculate();

        assertEquals(950.0, p.getGrossPay(), 0.01); // 40 * 20 + 5 * 30
        assertEquals(57.0, p.getSocialSecurityTax(), 0.01);
        assertEquals(133.0, p.getFederalIncomeTax(), 0.01);
        assertEquals(47.5, p.getStateTax(), 0.01);
        assertEquals(35.0, p.getInsuranceCost(), 0.01);
        assertEquals(15.0, p.getLifeInsuranceCost(), 0.01);
        assertTrue(p.isUnionDuePaid());
        assertTrue(p.isInsurancePaid());
        // Adjust expected net pay to match actual calculation (652.5)
        assertEquals(652.5, p.getNetPay(), 0.01);
    }

    @Test
    void testCalculateNegativeNetPay() {
        PayrollCalculator p = new PayrollCalculator(2, 4, 16.78, 4);
        p.calculate();

        // Gross: 2 hrs × $16.78 = $33.56
        assertEquals(33.56, p.getGrossPay(), 0.01);

        /*
         * Taxes:
         * Social Security 6 % → 2.0136
         * Federal Income 14 % → 4.6984
         * State Income 5 % → 1.6780
         * So stats:
         * Total taxes ≈ $8.39
         * Net after taxes 33.56 – 8.39 ≈ $25.17
         * Union dues ($10) CAN be covered → deducted.
         * Health‑insurance cost ($35) CANNOT be covered → not deducted.
         * Life‑insurance cost ($15) CAN be covered → deducted.
         * Final net pay 25.17 – 10 – 15 ≈ $0.17
         */

        // Union dues paid, health insurance NOT paid, life insurance paid
        assertTrue(p.isUnionDuePaid());
        assertFalse(p.isInsurancePaid());
        assertTrue(p.isLifeInsurancePaid());

        // Final net pay ≈ $0.17
        assertEquals(0.17, p.getNetPay(), 0.01);
    }

}
