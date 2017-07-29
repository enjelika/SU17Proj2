package eventPD;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "company")
@Entity(name = "company")
public class Company implements Serializable {

   private static final long serialVersionUID = 1L;
   @Id //signifies the primary key
   @Column(name = "company_id", nullable = false)
   @GeneratedValue(strategy = GenerationType.AUTO)
   private int company_id;
   
   @Column(name = "name",nullable = false,length = 40)
   private String name;
   
   @Column(name = "address",nullable = false,length = 40)
   private String address;
   
   @Column(name = "email",nullable = false,length = 40)
   private String email;

   @Column(name = "phonenumber",nullable = false,length = 40)
   private String phonenumber;


   public Company(){

   }
   
   public Company(String name, String address, String email, String phonenumber){
     this();
     this.name = name;
     this.address = address;
     this.email = email;
     this.phonenumber = phonenumber;
   }

   public int getCompanyId() {
      return company_id;
   }


   public void setId(int companyId) {
      this.company_id = companyId;
   }
   
	public String getName() {
		return name;
	}
	@XmlElement
	public void setName(String name) {
	
		this.name = name;
	}
	
	public String getAddress() {
		return name;
	}
	@XmlElement
	public void setAddress(String address) {
	
		this.address = address;
	}
	
	public String getEmail() {
		return name;
	}
	@XmlElement
	public void setEmail(String email) {
	
		this.email = email;
	}
	
	public String getPhoneNumber() {
		return phonenumber;
	}
	@XmlElement
	public void setPhoneNumber(String phonenumber) {
	
		this.phonenumber = phonenumber;
	}
}
	


