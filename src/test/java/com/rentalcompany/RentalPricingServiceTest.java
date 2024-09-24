package com.rentalcompany;

import com.rentalcompany.model.RentalAgreement;
import com.rentalcompany.model.Tool;
import com.rentalcompany.service.RentalPricingService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RentalPricingServiceTest {

    private RentalPricingService rentalPricingService;

    @BeforeEach
    void setUp() {
        rentalPricingService = new RentalPricingService();
    }

    @Test
    public void calculateCharges_WeekdayRental_NoDiscount() {
        Tool tool = new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false);
        RentalAgreement agreement = new RentalAgreement();
        agreement.setCheckoutDate(LocalDate.of(2023, 9, 11)); // Monday
        agreement.setRentalDays(3);
        agreement.setDiscountPercent(0);
        agreement.setDueDate(agreement.getCheckoutDate().plusDays(agreement.getRentalDays()));

        rentalPricingService.calculateCharges(agreement, tool);

        assertEquals(2, agreement.getChargeDays());
        assertEquals(5.98, agreement.getPreDiscountCharge());
        assertEquals(0.0, agreement.getDiscountAmount());
        assertEquals(5.98, agreement.getFinalCharge());
    }

    @Test
    public void calculateCharges_WeekendRental_WithDiscount() {
        Tool tool = new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, true, false);
        RentalAgreement agreement = new RentalAgreement();
        agreement.setCheckoutDate(LocalDate.of(2023, 9, 15)); // Friday
        agreement.setRentalDays(4);
        agreement.setDiscountPercent(20);
        agreement.setDueDate(agreement.getCheckoutDate().plusDays(agreement.getRentalDays()));

        rentalPricingService.calculateCharges(agreement, tool);

        assertEquals(3, agreement.getChargeDays());
        assertEquals(4.47, agreement.getPreDiscountCharge());
        assertEquals(0.89, agreement.getDiscountAmount());
        assertEquals(3.58, agreement.getFinalCharge());
    }

    @Test
    public void calculateCharges_HolidayRental_NoCharge() {
        Tool tool = new Tool("LADW", "Ladder", "Werner", 1.99, true, true, true);
        RentalAgreement agreement = new RentalAgreement();
        agreement.setCheckoutDate(LocalDate.of(2023, 7, 3)); // Monday before July 4th
        agreement.setRentalDays(2);
        agreement.setDiscountPercent(10);
        agreement.setDueDate(agreement.getCheckoutDate().plusDays(agreement.getRentalDays()));

        rentalPricingService.calculateCharges(agreement, tool);

        assertEquals(1, agreement.getChargeDays());
        assertEquals(1.99, agreement.getPreDiscountCharge());
        assertEquals(0.20, agreement.getDiscountAmount());
        assertEquals(1.79, agreement.getFinalCharge());
    }

    @ParameterizedTest
    @CsvSource({
            "2023-07-03, 2023-07-04, 1", // Day before July 4th
            "2023-07-04, 2023-07-05, 0", // July 4th
            "2023-07-05, 2023-07-06, 1", // Day after July 4th
            "2023-09-03, 2023-09-04, 0", // Day before Labor Day
            "2023-09-04, 2023-09-05, 0", // Labor Day
            "2023-09-05, 2023-09-06, 1"  // Day after Labor Day
    })
    void isHoliday_VariousDates(LocalDate checkoutDate, LocalDate dueDate, int expectedChargeDays) {
        Tool tool = new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false);
        RentalAgreement agreement = new RentalAgreement();
        agreement.setCheckoutDate(checkoutDate);
        agreement.setRentalDays((int) checkoutDate.until(dueDate).getDays());
        agreement.setDiscountPercent(0);
        agreement.setDueDate(dueDate);

        rentalPricingService.calculateCharges(agreement, tool);

        System.out.println("Checkout Date: " + checkoutDate);
        System.out.println("Due Date: " + dueDate);
        System.out.println("Expected Charge Days: " + expectedChargeDays);
        System.out.println("Actual Charge Days: " + agreement.getChargeDays());
        System.out.println("Is Holiday: " + rentalPricingService.isHoliday(checkoutDate));
        System.out.println("Is Chargeable: " + rentalPricingService.isChargeable(checkoutDate, tool));

        assertEquals(expectedChargeDays, agreement.getChargeDays(),
                "Failed for checkout date: " + checkoutDate + ", due date: " + dueDate);
    }

    @Test
    public void calculateCharges_LongTermRental_WithWeekends() {
        Tool tool = new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false);
        RentalAgreement agreement = new RentalAgreement();
        agreement.setCheckoutDate( LocalDate.of(2023, 9, 1) ); // Friday
        agreement.setRentalDays(10);
        agreement.setDiscountPercent(15);
        agreement.setDueDate( agreement.getCheckoutDate().plusDays( agreement.getRentalDays()) );

        rentalPricingService.calculateCharges(agreement, tool);

        assertEquals(6, agreement.getChargeDays());
        assertEquals(17.94, agreement.getPreDiscountCharge());
        assertEquals(2.69, agreement.getDiscountAmount());
        assertEquals(15.25, agreement.getFinalCharge());
    }
}