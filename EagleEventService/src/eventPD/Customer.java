package eventPD;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eventDAO.CustomerDAO;

@XmlRootElement(name = "customer")
@Entity(name = "customer")
public class Customer implements Serializable {
	   private static final long serialVersionUID = 1L;
	   @Id //signifies the primary key
	   @Column(name = "customer_id", nullable = false)
	   @GeneratedValue(strategy = GenerationType.AUTO)
	   private int customer_id;
	   
	   @Column(name = "name",nullable = false,length = 40)
	   private String name;
	   	   
	   @Column(name = "email",nullable = false,length = 40)
	   private String email;


	   public Customer(){

	   }
	   
	   public Customer(String name, String email){
	     this();
	     this.name = name;
	     this.email = email;
	   }

	   public int getCustomerId() {
	      return customer_id;
	   }


	   public void setId(int customer_id) {
	      this.customer_id = customer_id;
	   }
	   
		public String getName() {
			return name;
		}
		@XmlElement
		public void setName(String name) {
		
			this.name = name;
		}
		
		
		public String getEmail() {
			return email;
		}
		@XmlElement
		public void setEmail(String email) {
		
			this.email = email;
		}
		
		public boolean UpdateCustomer(Customer customer)
		{
			CustomerDAO.saveCustomer(customer);
		    return true;
		}
}
