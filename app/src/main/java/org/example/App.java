package org.example;

import java.util.Scanner;

public class App {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Welcome to the Payroll Program!\n");

    // Input: hours worked
    System.out.print("How many hours did you work this week? ");
    double hoursWorked = scanner.nextDouble();

    // Input: number of children/dependents (handle negative by forcing zero)
    System.out.print("How many children do you have? ");
    int numChildren = scanner.nextInt();
    if (numChildren < 0) {
      System.out.println("Invalid number of children entered. Setting to 0.");
      numChildren = 0;
    }

    // Input: pay rate with validation for non-negative
    double payRate = -1;
    do {
      System.out.print("Enter your hourly pay rate: ");
      payRate = scanner.nextDouble();
      if (payRate < 0) {
        System.out.println("Pay rate cannot be negative. Please enter again.");
      }
    } while (payRate < 0);

    // Life insurance plan menu and validation
    int lifeInsurancePlan = 0;
    while (true) {
      System.out.println("\nWhich life insurance plan do you want to select?\n");
      System.out.println("  (1) no plan");
      System.out.println("  (2) single plan");
      System.out.println("  (3) married plan");
      System.out.println("  (4) married with children plan");
      System.out.print("\nYour choice: ");
      lifeInsurancePlan = scanner.nextInt();

      if (lifeInsurancePlan < 1 || lifeInsurancePlan > 4) {
        System.out.println("Invalid selection. Please choose again.");
        continue;
      }
      if (lifeInsurancePlan == 4 && numChildren < 1) {
        System.out.println("Sorry! You need at least one child to select that plan.");
        continue;
      }
      break;
    }

    // Create PayrollCalculator object and calculate payroll
    PayrollCalculator payroll = new PayrollCalculator(hoursWorked, numChildren, payRate, lifeInsurancePlan);
    payroll.calculate();

    // Print report
    System.out.println("\nPayroll Stub:\n");
    System.out.printf("   Hours:   %.1f\n", payroll.getHoursWorked());
    System.out.printf("    Rate:   %.2f $/hr\n", payroll.getPayRate());
    System.out.printf("   Gross:   $%7.2f\n\n", payroll.getGrossPay());

    System.out.printf("  SocSec:   $%7.2f\n", payroll.getSocialSecurityTax());
    System.out.printf("  FedTax:   $%7.2f\n", payroll.getFederalIncomeTax());
    System.out.printf("   StTax:   $%7.2f\n", payroll.getStateTax());

    // Check if union dues and insurance dues were deducted or owed
    if (payroll.isUnionDuePaid()) {
      System.out.printf("   Union:   $%7.2f\n", payroll.getUnionDues());
    }
    if (payroll.isInsurancePaid()) {
      System.out.printf("     Ins:   $%7.2f\n", payroll.getInsuranceCost());
    }
    if (payroll.isLifeInsurancePaid()) {
      System.out.printf(" LifeIns:   $%7.2f\n\n", payroll.getLifeInsuranceCost());
    }

    System.out.printf("     Net:   $%7.2f\n\n", payroll.getNetPay());

    if (!payroll.isUnionDuePaid() || !payroll.isInsurancePaid() || !payroll.isLifeInsurancePaid()) {
      System.out.println("The employee still owes:\n");
      if (!payroll.isUnionDuePaid()) {
        System.out.printf("   Union:   $%7.2f\n", payroll.getUnionDues());
      }
      if (!payroll.isInsurancePaid()) {
        System.out.printf("     Ins:   $%7.2f\n", payroll.getInsuranceCost());
      }
      if (!payroll.isLifeInsurancePaid()) {
        System.out.printf(" Life  Ins:   $%7.2f\n", payroll.getLifeInsuranceCost());
      }
      System.out.println();
    }

    System.out.println("Thank you for using the Payroll Program!");
    scanner.close();
  }
}
