package com.yrgo.services.customers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yrgo.dataaccess.CustomerDao;
import com.yrgo.dataaccess.RecordNotFoundException;
import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;

@Transactional
@Service
public class CustomerManagementServiceProductionImpl implements CustomerManagementService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public void newCustomer(Customer newCustomer) {
        customerDao.create(newCustomer);
    }

    @Override
    public void updateCustomer(Customer changedCustomer) {
        try {
            customerDao.update(changedCustomer);
        } catch (RecordNotFoundException e) {
            try {
                throw new CustomerNotFoundException();
            } catch (CustomerNotFoundException e1) {

            }
        }
    }

    @Override
    public void deleteCustomer(Customer oldCustomer) {
        try {
            customerDao.delete(oldCustomer);
        } catch (RecordNotFoundException e) {
            try {
                throw new CustomerNotFoundException();
            } catch (CustomerNotFoundException e1) {

            }
        }
    }

    @Override
    public Customer findCustomerById(String customerId) throws CustomerNotFoundException {
        Customer customer = new Customer();
        try {
            customer = customerDao.getById(customerId);
        } catch (RecordNotFoundException e) {
            throw new CustomerNotFoundException();
        }
        return customer;
    }

    @Override
    public List<Customer> findCustomersByName(String name) {
        return customerDao.getByName(name);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws CustomerNotFoundException {
        Customer customer = new Customer();
        try {
            customer = customerDao.getFullCustomerDetail(customerId);
        } catch (RecordNotFoundException e) {
            throw new CustomerNotFoundException();
        }
        return customer;
    }

    @Override
    public void recordCall(String customerId, Call callDetails) throws CustomerNotFoundException {
        try {
            customerDao.addCall(callDetails, customerId);
        } catch (RecordNotFoundException e) {
            throw new CustomerNotFoundException();
        }
    }
}
