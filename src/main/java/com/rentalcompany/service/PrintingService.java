package com.rentalcompany.service;

import com.rentalcompany.model.RentalAgreement;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class PrintingService {
    private static final DecimalFormat currencyFormat = new DecimalFormat("$#,##0.00");
    private static final DecimalFormat percentageFormat = new DecimalFormat("#%");

    // Formatting dates as "MM/dd/yy"
    private static String formatDate(String inputDate) throws ParseException {
        // Create a SimpleDateFormat object for parsing the input string
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = inputFormat.parse(inputDate);

        // Create another SimpleDateFormat object for the desired output format
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yy");
        return outputFormat.format(date);
    }

    // Formatting currency as "$9,999.99"
    private static String formatCurrency(double amount) {
        return currencyFormat.format(amount);
    }

    // Formatting percentage as "99%"
    private static String formatPercentage(int discountPercent) {
        return discountPercent + "%";
    }

    // Print rental agreement details
    public void printRentalAgreement(RentalAgreement rentalAgreement) throws ParseException {
        System.out.println("Tool code: " + rentalAgreement.getTool().getCode());
        System.out.println("Tool type: " + rentalAgreement.getTool().getType());
        System.out.println("Tool brand: " + rentalAgreement.getTool().getBrand());
        System.out.println("Rental days: " + rentalAgreement.getRentalDays());
        System.out.println("Check out date: " + formatDate(String.valueOf(rentalAgreement.getCheckoutDate())));
        System.out.println("Due date: " + formatDate(String.valueOf(rentalAgreement.getDueDate())));
        System.out.println("Daily rental charge: " + formatCurrency(rentalAgreement.getTool().getDailyCharge()));
        System.out.println("Charge days: " + rentalAgreement.getChargeDays());
        System.out.println("Pre-discount charge: " + formatCurrency(rentalAgreement.getPreDiscountCharge()));
        System.out.println("Discount percent: " + formatPercentage(rentalAgreement.getDiscountPercent()));
        System.out.println("Discount amount: " + formatCurrency(rentalAgreement.getDiscountAmount()));
        System.out.println("Final charge: " + formatCurrency(rentalAgreement.getFinalCharge()));
    }
}

