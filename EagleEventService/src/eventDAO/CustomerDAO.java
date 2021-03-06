package eventDAO;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import eventPD.Customer;

public class CustomerDAO {
	
	public static void saveCustomer(Customer customer) {
        EM.getEM().persist(customer);
      }
      public static void addCustomer(Customer customer) {
        EM.getEM().persist(customer);
      }
      
      public static Customer findCustomerByIdNumber(String idNumber)
      {
        String qString = "SELECT customer FROM customer customer  WHERE customer.customer_id ="+idNumber;
        Query query = EM.getEM().createQuery(qString);
        Customer customer = (Customer)query.getSingleResult();
        return customer;
      }

      public static List<Customer> listCustomer()
      {
        TypedQuery<Customer> query = EM.getEM().createQuery("SELECT customer FROM customer customer", Customer.class);
        return query.getResultList();
      }
      
      public static void removeCustomer(Customer customer)
      {
        EM.getEM().remove(customer);
      }
}
