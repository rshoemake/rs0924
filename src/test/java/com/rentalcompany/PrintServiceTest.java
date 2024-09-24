package com.rentalcompany;

import com.rentalcompany.model.RentalAgreement;
import com.rentalcompany.model.Tool;
import com.rentalcompany.service.PrintingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PrintServiceTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private PrintingService printingService;

    @BeforeEach
    public void setUp() {
        printingService = new PrintingService();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void printAgreement_ShouldPrintCorrectInformation() {
        // Arrange
        Tool tool = new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false);
        RentalAgreement agreement = new RentalAgreement();
        agreement.setRentalDays(5);
        agreement.setCheckoutDate(LocalDate.of(2023, 9, 3));
        agreement.setDueDate(LocalDate.of(2023, 9, 8));
        agreement.setChargeDays(4);
        agreement.setPreDiscountCharge(11.96);
        agreement.setDiscountPercent(10);
        agreement.setDiscountAmount(1.20);
        agreement.setFinalCharge(10.76);

        printingService.printRentalAgreement(agreement);

        // Assert
        String printedOutput = outputStreamCaptor.toString().trim();
        assertTrue(printedOutput.contains("Tool code: JAKR"));
        assertTrue(printedOutput.contains("Tool type: Jackhammer"));
        assertTrue(printedOutput.contains("Tool brand: Ridgid"));
        assertTrue(printedOutput.contains("Rental days: 5"));
        assertTrue(printedOutput.contains("Check out date: 09/03/23"));
        assertTrue(printedOutput.contains("Due date: 09/08/23"));
        assertTrue(printedOutput.contains("Daily rental charge: $2.99"));
        assertTrue(printedOutput.contains("Charge days: 4"));
        assertTrue(printedOutput.contains("Pre-discount charge: $11.96"));
        assertTrue(printedOutput.contains("Discount percent: 10%"));
        assertTrue(printedOutput.contains("Discount amount: $1.20"));
        assertTrue(printedOutput.contains("Final charge: $10.76"));
    }

    @Test
    void printAgreement_ShouldHandleZeroDiscount() {
        // Arrange
        Tool tool = new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false);
        RentalAgreement agreement = new RentalAgreement();
        agreement.setRentalDays(3);
        agreement.setCheckoutDate(LocalDate.of(2023, 7, 2));
        agreement.setDueDate(LocalDate.of(2023, 7, 5));
        agreement.setChargeDays(3);
        agreement.setPreDiscountCharge(5.97);
        agreement.setDiscountPercent(0);
        agreement.setDiscountAmount(0.00);
        agreement.setFinalCharge(5.97);

        printingService.printRentalAgreement(agreement);

        // Assert
        String printedOutput = outputStreamCaptor.toString().trim();
        assertTrue(printedOutput.contains("Tool code: LADW"));
        assertTrue(printedOutput.contains("Discount percent: 0%"));
        assertTrue(printedOutput.contains("Discount amount: $0.00"));
        assertTrue(printedOutput.contains("Final charge: $5.97"));
    }

    @Test
    void printAgreement_ShouldHandleLongRentalPeriod() {
        // Arrange
        Tool tool = new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, true, false);
        RentalAgreement agreement = new RentalAgreement();
        agreement.setRentalDays(30);
        agreement.setCheckoutDate(LocalDate.of(2023, 7, 15));
        agreement.setDueDate(LocalDate.of(2023, 8, 14));
        agreement.setChargeDays(30);
        agreement.setPreDiscountCharge(44.70);
        agreement.setDiscountPercent(25);
        agreement.setDiscountAmount(11.18);
        agreement.setFinalCharge(33.52);

        printingService.printRentalAgreement(agreement);

        // Assert
        String printedOutput = outputStreamCaptor.toString().trim();
        assertTrue(printedOutput.contains("Tool code: CHNS"));
        assertTrue(printedOutput.contains("Rental days: 30"));
        assertTrue(printedOutput.contains("Check out date: 07/15/23"));
        assertTrue(printedOutput.contains("Due date: 08/14/23"));
        assertTrue(printedOutput.contains("Pre-discount charge: $44.70"));
        assertTrue(printedOutput.contains("Discount percent: 25%"));
        assertTrue(printedOutput.contains("Discount amount: $11.18"));
        assertTrue(printedOutput.contains("Final charge: $33.52"));
    }
}
