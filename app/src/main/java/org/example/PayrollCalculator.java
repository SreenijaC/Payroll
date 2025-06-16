package org.example;

public class PayrollCalculator {

    private double hoursWorked;
    private int numChildren;
    private double payRate;
    private int lifeInsurancePlan;

    // Constants
    private static final double STANDARD_HOURS = 40.0;
    private static final double OVERTIME_MULTIPLIER = 1.5;
    private static final double SOCIAL_SECURITY_RATE = 0.06;
    private static final double FEDERAL_INCOME_TAX_RATE = 0.14;
    private static final double STATE_INCOME_TAX_RATE = 0.05;
    private static final double UNION_DUES = 10.00;

    // Insurance costs based on number of children
    private static final double INSURANCE_COST_FEW_CHILDREN = 15.00;
    private static final double INSURANCE_COST_MANY_CHILDREN = 35.00;

    // Life insurance plan costs
    // 1 = no plan, 2 = single, 3 = married, 4 = married with children
    private static final double LIFE_INSURANCE_COST_NONE = 0.0;
    private static final double LIFE_INSURANCE_COST_SINGLE = 5.0;
    private static final double LIFE_INSURANCE_COST_MARRIED = 10.0;
    private static final double LIFE_INSURANCE_COST_MARRIED_CHILDREN = 15.0;

    // Calculated fields
    private double grossPay;
    private double socialSecurityTax;
    private double federalIncomeTax;
    private double stateIncomeTax;
    private double unionDues;
    private double insuranceCost;
    private double lifeInsuranceCost;
    private double netPay;

    private boolean unionDuePaid = true;
    private boolean insurancePaid = true;
    private boolean lifeInsurancePaid = true;

    public PayrollCalculator(double hoursWorked, int numChildren, double payRate, int lifeInsurancePlan) {
        this.hoursWorked = hoursWorked;
        this.numChildren = numChildren < 0 ? 0 : numChildren;
        this.payRate = payRate;
        this.lifeInsurancePlan = lifeInsurancePlan;
    }

    public void calculate() {
        calculateGrossPay();
        calculateTaxes();
        calculateDeductions();
        calculateNetPay();
    }

    private void calculateGrossPay() {
        if (hoursWorked <= STANDARD_HOURS) {
            this.grossPay = hoursWorked * payRate;
        } else {
            double overtimeHours = hoursWorked - STANDARD_HOURS;
            this.grossPay = STANDARD_HOURS * payRate + overtimeHours * payRate * OVERTIME_MULTIPLIER;
        }
    }

    private void calculateTaxes() {
        socialSecurityTax = grossPay * SOCIAL_SECURITY_RATE;
        federalIncomeTax = grossPay * FEDERAL_INCOME_TAX_RATE;
        stateIncomeTax = grossPay * STATE_INCOME_TAX_RATE;
    }

    private void calculateDeductions() {
        unionDues = UNION_DUES;
        insuranceCost = (numChildren >= 2) ? INSURANCE_COST_MANY_CHILDREN : INSURANCE_COST_FEW_CHILDREN; // 2 children +
                                                                                                         // wife = 3
                                                                                                         // dependants
                                                                                                         // (that's why
                                                                                                         // considering
                                                                                                         // 2 children
                                                                                                         // as the
                                                                                                         // cutoff)

        switch (lifeInsurancePlan) {
            case 1:
                lifeInsuranceCost = LIFE_INSURANCE_COST_NONE;
                break;
            case 2:
                lifeInsuranceCost = LIFE_INSURANCE_COST_SINGLE;
                break;
            case 3:
                lifeInsuranceCost = LIFE_INSURANCE_COST_MARRIED;
                break;
            case 4:
                lifeInsuranceCost = LIFE_INSURANCE_COST_MARRIED_CHILDREN;
                break;
            default:
                lifeInsuranceCost = 0.0; // should never happen because validated earlier
                break;
        }
    }

    private void calculateNetPay() {
        // Calculate net pay after taxes only
        double netAfterTaxes = grossPay - socialSecurityTax - federalIncomeTax - stateIncomeTax;

        // Check if enough to pay union dues
        if (netAfterTaxes >= unionDues) {
            netAfterTaxes -= unionDues;
            this.unionDuePaid = true;
        } else {
            this.unionDuePaid = false;
        }

        // Check if enough to pay insurance
        if (netAfterTaxes >= insuranceCost) {
            netAfterTaxes -= insuranceCost;
            insurancePaid = true;
        } else {
            insurancePaid = false;
        }

        if (lifeInsuranceCost <= netAfterTaxes) {
            netAfterTaxes -= lifeInsuranceCost;
            lifeInsurancePaid = true;
        } else {
            lifeInsurancePaid = false;
        }

        netPay = netAfterTaxes;
    }

    // Getters for report output
    public double getHoursWorked() {
        return hoursWorked;
    }

    public int getNumChildren() {
        return numChildren;
    }

    public double getPayRate() {
        return payRate;
    }

    public double getGrossPay() {
        return grossPay;
    }

    public double getSocialSecurityTax() {
        return socialSecurityTax;
    }

    public double getFederalIncomeTax() {
        return federalIncomeTax;
    }

    public double getStateTax() {
        return stateIncomeTax;
    }

    public double getUnionDues() {
        return unionDues;
    }

    public double getInsuranceCost() {
        return insuranceCost;
    }

    public double getLifeInsuranceCost() {
        return lifeInsuranceCost;
    }

    public double getNetPay() {
        return netPay;
    }

    public boolean isUnionDuePaid() {
        return unionDuePaid;
    }

    public boolean isInsurancePaid() {
        return insurancePaid;
    }

    public boolean isLifeInsurancePaid() {
        return lifeInsurancePaid;
    }
}
