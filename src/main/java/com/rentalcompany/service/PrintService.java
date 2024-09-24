package com.rentalcompany.service;

import com.rentalcompany.model.RentalAgreement;
import com.rentalcompany.model.Tool;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class PrintService {

    public void printRentalAgreement(RentalAgreement rentalAgreement, Tool tool) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        System.out.println("Tool code: " + tool.getCode());
        System.out.println("Tool type: " + tool.getType());
        System.out.println("Tool brand: " + tool.getBrand());
        System.out.println("Rental days: " + rentalAgreement.getRentalDays() );
        System.out.println("Check out date: " + rentalAgreement.getCheckoutDate().format(dateFormatter));
        System.out.println("Due date: " + rentalAgreement.getDueDate().format(dateFormatter));
        System.out.printf("Daily rental charge: $%.2f%n", tool.getDailyCharge());
        System.out.println("Charge days: " + rentalAgreement.getChargeDays() );
        System.out.printf("Pre-discount charge: $%.2f%n", rentalAgreement.getPreDiscountCharge() );
        System.out.println("Discount percent: " + rentalAgreement.getDiscountPercent() + "%");
        System.out.printf("Discount amount: $%.2f%n", rentalAgreement.getDiscountAmount() );
        System.out.printf("Final charge: $%.2f%n", rentalAgreement.getFinalCharge() );
    }

}
