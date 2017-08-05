package eventPD;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eventDAO.StaffDAO;

@XmlRootElement(name = "staff")
@Entity(name = "staff")
public class Staff implements Serializable {
	   private static final long serialVersionUID = 1L;
	   @Id //signifies the primary key
	   @Column(name = "staff_id", nullable = false)
	   @GeneratedValue(strategy = GenerationType.AUTO)
	   private int staff_id;
	   
	   @Column(name = "name",nullable = false,length = 40)
	   private String name;
	   	   
	   @Column(name = "email",nullable = false,length = 40)
	   private String email;


	   public Staff(){

	   }
	   
	   public Staff(String name, String email){
	     this();
	     this.name = name;
	     this.email = email;
	   }

	   public int getStaffId() {
	      return staff_id;
	   }


	   public void setId(int staff_id) {
	      this.staff_id = staff_id;
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
		
		public boolean UpdateStaff(Staff staff)
		{
			StaffDAO.saveStaff(staff);
		    return true;
		}
}
