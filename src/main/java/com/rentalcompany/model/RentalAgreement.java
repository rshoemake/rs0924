package com.rentalcompany.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Data
@NoArgsConstructor
public class RentalAgreement {
    private Tool tool;
    private int rentalDays;
    private int discountPercent;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private int chargeDays;
    private double preDiscountCharge;
    private double discountAmount;
    private double finalCharge;

    public RentalAgreement( Tool tool, int rentalDays, int discountPercent, LocalDate checkoutDate) {
        this.tool = tool;
        this.rentalDays = rentalDays;
        this.discountPercent = discountPercent;
        this.checkoutDate = checkoutDate;
        this.dueDate = checkoutDate.plusDays(rentalDays);
    }

}
