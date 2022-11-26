package com.yrgo.dataaccess;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;

public class CustomerDaoJdbcImpl implements CustomerDao {

    @Autowired
    private JdbcTemplate template;

    public CustomerDaoJdbcImpl(JdbcTemplate template) {
        this.template = template;
        createTables();
    }

    private static final String INSERT_CALL_SQL = "INSERT INTO TBL_CALL(NOTES, TIME_AND_DATE, CUSTOMER_ID) VALUES (?, ?, ?)";
	private static final String SELECT_ALL_CUSTOMERS_SQL = "SELECT * FROM CUSTOMER";
	private static final String UPDATE_CUSTOMER_SQL = "UPDATE CUSTOMER SET COMPANY_NAME=?, EMAIL=?, TELEPHONE=?, NOTES=? WHERE CUSTOMER_ID = ?";
	private static final String SELECT_CUSTOMER_BY_ID_SQL = "SELECT * FROM CUSTOMER WHERE CUSTOMER_ID = ?";
	private static final String SELECT_CUSTOMERS_BY_NAME_SQL = "SELECT * FROM CUSTOMER WHERE COMPANY_NAME = ?";
	private static final String INSERT_CUSTOMER_SQL = "INSERT INTO CUSTOMER (CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES) VALUES (?,?,?,?,?)";
    private static final String DELETE_CUSTOMER_SQL = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID = ?";

    private void createTables() {
        try {
            this.template.update(
                    "CREATE TABLE CUSTOMER (CUSTOMER_ID VARCHAR(20), COMPANY_NAME VARCHAR(50), EMAIL VARCHAR(50), TELEPHONE VARCHAR(20), NOTES VARCHAR(255))");
            this.template
                    .update("CREATE TABLE TBL_CALL(NOTES VARCHAR(255), TIME_AND_DATE DATE, CUSTOMER_ID VARCHAR(20))");
        } catch (org.springframework.jdbc.BadSqlGrammarException e) {
            System.out.println("Assuming the Action table exists");
        }
    }

    @Override
    public void create(Customer customer) {
        template.update(INSERT_CUSTOMER_SQL, customer.getCustomerId(), customer.getCompanyName(),
         customer.getEmail(), customer.getTelephone(), customer.getNotes());
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        return template.queryForObject(SELECT_CUSTOMER_BY_ID_SQL, new CustomerRowMapper(), customerId);
    }

    @Override
    public List<Customer> getByName(String name) {
        return template.query(SELECT_CUSTOMERS_BY_NAME_SQL, new CustomerRowMapper(), name);
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        template.update(UPDATE_CUSTOMER_SQL, customerToUpdate.getCompanyName(), customerToUpdate.getEmail(), customerToUpdate.getTelephone(), 
        customerToUpdate.getNotes(), customerToUpdate.getCustomerId());
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        template.update(DELETE_CUSTOMER_SQL, oldCustomer.getCustomerId());
    }

    @Override
    public List<Customer> getAllCustomers() {
        return template.query(SELECT_ALL_CUSTOMERS_SQL, new CustomerRowMapper());
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        Customer customer = this.getById(customerId);
		
        List<Call> allCallsForTheCustomer = template.query("SELECT * FROM TBL_CALL WHERE CUSTOMER_ID=?", new CallRowMapper(), customerId);
   
        customer.setCalls(allCallsForTheCustomer);
        return customer;
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        template.update(INSERT_CALL_SQL, newCall.getNotes(), newCall.getTimeAndDate(), customerId);
    }

    class CustomerRowMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            String customerId = rs.getString(1);
            String companyName = rs.getString(2);
            String email = rs.getString(3);
            String telephone = rs.getString(4);
            String notes = rs.getString(5);

            return new Customer(customerId, companyName, email, telephone, notes);
        }
    }

    class CallRowMapper implements RowMapper<Call> {
        @Override
        public Call mapRow(ResultSet rs, int rowNum) throws SQLException {
            String notes = rs.getString(1);
            Date time_and_date = rs.getDate(2);
            return new Call(notes, time_and_date);
        }
    }
}
