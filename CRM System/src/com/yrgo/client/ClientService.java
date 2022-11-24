package com.yrgo.client;

import java.io.Console;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yrgo.domain.Customer;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;

public class ClientService {
    
    public static void main(String[] args) {
		
		ClassPathXmlApplicationContext container = new 
		ClassPathXmlApplicationContext("application.xml");

		CustomerManagementService purchasing = container.getBean(CustomerManagementService.class);

        try{
            List<Customer> guy = purchasing.getAllCustomers();
            for (Customer v: guy) {
                System.out.println(v);
            }
        }
        catch(Exception e){
            System.out.println("Customer not found!");
        }
        container.close();
    }
}
