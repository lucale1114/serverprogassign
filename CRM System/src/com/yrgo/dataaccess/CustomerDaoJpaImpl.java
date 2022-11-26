package com.yrgo.dataaccess;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;

@Repository
public class CustomerDaoJpaImpl implements CustomerDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Customer customer) {
        em.persist(customer);   
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        return(Customer)em.createQuery("select customer from Customer as customer where customer.customerId=:id")
        .setParameter("id", customerId).getSingleResult();
    }

    @Override
    public List<Customer> getByName(String name) {
        return em.createQuery("select customer from Customer as customer where customer.companyName=:name")
        .setParameter("name", name).getResultList();
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        em.merge(customerToUpdate);        
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        em.remove(oldCustomer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return em.createQuery("select customer from Customer as customer").getResultList();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        List<Call> calls = em.createQuery("select call from Call as call where call.customerId = :id").setParameter("id", customerId).getResultList();
        Customer customer = getById(customerId);
        customer.setCalls(calls);
        return customer;
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        Customer customer = getById(customerId);
        customer.addCall(newCall);
    }

    
}
