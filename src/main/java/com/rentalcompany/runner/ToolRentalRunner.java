package com.rentalcompany.runner;

import com.rentalcompany.model.RentalAgreement;
import com.rentalcompany.model.Tool;
import com.rentalcompany.service.ChargeService;
import com.rentalcompany.service.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
@ComponentScan({"com.rentalcompany.service","com.rentalcompany.model"})
public class ToolRentalRunner implements CommandLineRunner {

    private final Map<String, Tool> tools = new HashMap<>();

    @Autowired
    PrintService printService;

    @Autowired
    ChargeService chargeService;

    public ToolRentalRunner() {
        tools.put("CHNS", new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, true, false));
        tools.put("LADW", new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false));
        tools.put("JAKD", new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false));
        tools.put("JAKR", new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false));
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nTool Rental Checkout System");
            System.out.println("1. Checkout Tool");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                checkout(scanner);
            } else if (choice == 2) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void checkout(Scanner scanner) {
        System.out.print("Enter Tool Code (CHNS, LADW, JAKD, JAKR): ");
        String toolCode = scanner.nextLine().toUpperCase();

        System.out.print("Enter Rental Day Count: ");
        int rentalDayCount = scanner.nextInt();

        System.out.print("Enter Discount Percent (0-100): ");
        int discountPercent = scanner.nextInt();

        scanner.nextLine(); // Consume newline

        System.out.print("Enter Checkout Date (MM/dd/yy): ");
        String checkoutDateStr = scanner.nextLine();

        try {
            Tool tool = tools.get(toolCode);
            if (tool == null) {
                throw new IllegalArgumentException("Invalid tool code.");
            }

            if (rentalDayCount < 1) {
                throw new IllegalArgumentException("Rental day count must be 1 or greater.");
            }

            if (discountPercent < 0 || discountPercent > 100) {
                throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
            }

            LocalDate checkoutDate = LocalDate.parse(checkoutDateStr, DateTimeFormatter.ofPattern("MM/dd/yy"));
            RentalAgreement agreement = new RentalAgreement(tool, rentalDayCount, discountPercent, checkoutDate);
            chargeService.calculateCharges(agreement, tool);
            printService.printRentalAgreement(agreement, tool);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
