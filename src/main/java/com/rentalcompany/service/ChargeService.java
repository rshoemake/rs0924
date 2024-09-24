package com.rentalcompany.service;

import com.rentalcompany.model.RentalAgreement;
import com.rentalcompany.model.Tool;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class ChargeService {

    public void calculateCharges(RentalAgreement rentalAgreement, Tool tool) {
        rentalAgreement.setChargeDays(calculateChargeDays(rentalAgreement, tool));
        rentalAgreement.setPreDiscountCharge(Math.round(rentalAgreement.getChargeDays() * tool.getDailyCharge() * 100.0) / 100.0);
        rentalAgreement.setDiscountAmount(Math.round(rentalAgreement.getPreDiscountCharge() * rentalAgreement.getDiscountPercent() / 100.0 * 100.0) / 100.0);
        rentalAgreement.setFinalCharge(rentalAgreement.getPreDiscountCharge() - rentalAgreement.getDiscountAmount());
    }

    private int calculateChargeDays(RentalAgreement rentalAgreement, Tool tool) {
        int count = 0;
        LocalDate currentDate = rentalAgreement.getCheckoutDate();
        //LocalDate currentDate = rentalAgreement.getCheckoutDate().plusDays(1);// Start from the day after checkout
        LocalDate dueDate = rentalAgreement.getDueDate();

        while (!currentDate.isAfter(dueDate) && !currentDate.isEqual(dueDate)) {
            if (isChargeable(currentDate, tool)) {
                count++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return count;
    }

    public boolean isChargeable(LocalDate date, Tool tool) {
        boolean isWeekend = date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
        boolean isHoliday = isHoliday(date);

        if (isHoliday && !tool.isHolidayCharge()) return false;
        if (isWeekend && !tool.isWeekendCharge()) return false;
        if (!isWeekend && !isHoliday && !tool.isWeekdayCharge()) return false;

        return true;
    }

    public boolean isHoliday(LocalDate date) {
        // Check for Independence Day (July 4th)
        if (date.getMonthValue() == 7 && date.getDayOfMonth() == 4) {
            return true;
        }

        // Check for Labor Day (First Monday in September)
        if (date.getMonthValue() == 9 && date.getDayOfWeek() == DayOfWeek.MONDAY && date.getDayOfMonth() <= 7) {
            return true;
        }

        return false;
    }
}