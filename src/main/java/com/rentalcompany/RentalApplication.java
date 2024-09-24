package com.rentalcompany.model;

import com.rentalcompany.runner.ToolRentalRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.rentalcompany.runner")
public class RentalApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run( RentalApplication.class, args);
        ToolRentalRunner runner = context.getBean( ToolRentalRunner.class);
        runner.run(args);
    }

}