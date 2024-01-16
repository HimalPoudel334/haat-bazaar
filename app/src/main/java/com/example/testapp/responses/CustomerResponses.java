package com.example.testapp.responses;

import com.example.testapp.models.Customer;

import java.util.List;

public class CustomerResponses {
    public class SingleCustomerResponse {
        private Customer customer;

        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            this.customer = customer;
        }
    }

    public class MultiCustomerResponse {
        private List<Customer> customers;

        public List<Customer> getCustomers() {
            return customers;
        }

        public void setCustomers(List<Customer> customers) {
            this.customers = customers;
        }
    }
}
