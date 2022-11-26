package com.yrgo.client;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.yrgo.domain.Customer;
import com.yrgo.services.calls.CallHandlingService;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;
import com.yrgo.services.diary.DiaryManagementService;


public class SimpleClient {
    
    public static void main(String[] args) {
        
        ClassPathXmlApplicationContext container = new ClassPathXmlApplicationContext("application.xml");

		CustomerManagementService customerService = container.getBean(CustomerManagementService.class);
		CallHandlingService callService = container.getBean(CallHandlingService.class);
		DiaryManagementService diaryService = container.getBean(DiaryManagementService.class);

        customerService.newCustomer(new Customer("82491A", "Cool Comp", "Pro"));
        customerService.newCustomer(new Customer("92813B", "Cooler Comp", "Proer"));

        try {            
            System.out.println(customerService.findCustomerById("82491A"));
        } catch (Exception e){System.out.println("Not found!");}

        try {
            System.out.println(customerService.getAllCustomers());
        } catch (Exception e){System.out.println("Not found!");}

        container.close();
    }
}
