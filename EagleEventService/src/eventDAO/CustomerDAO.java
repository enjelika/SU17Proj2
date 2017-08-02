package eventDAO;

import java.util.List;

import javax.persistence.TypedQuery;

import eventPD.Customer;

public class CustomerDAO {
	
	public static void saveCustomer(Customer customer) {
        EM.getEM().persist(customer);
      }
      public static void addCustomer(Customer customer) {
        EM.getEM().persist(customer);
      }

      public static List<Customer> listCustomer()
      {
        TypedQuery<Customer> query = EM.getEM().createQuery("SELECT customer FROM customer customer", Customer.class);
        return query.getResultList();
      }
}
