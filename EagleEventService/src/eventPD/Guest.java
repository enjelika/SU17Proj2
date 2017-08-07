package eventPD;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eventDAO.GuestDAO;


@XmlRootElement(name = "guest")
@Entity(name = "guest")
public class Guest implements Serializable {
	   private static final long serialVersionUID = 1L;
	   @Id //signifies the primary key
	   @Column(name = "guest_id", nullable = false)
	   @GeneratedValue(strategy = GenerationType.AUTO)
	   private int guest_id;
	   
	   @Column(name = "firstname",nullable = false,length = 40)
	   private String firstname;
	   	   
	   @Column(name = "lastname",nullable = false,length = 40)
	   private String lastname;

		@Column(name = "sametable1", nullable = false,length = 10)
		private int sametable1;
		
		@Column(name = "sametable2", nullable = false,length = 10)
		private int sametable2;
		
		@Column(name = "sametable3", nullable = false,length = 10)
		private int sametable3;
		
		@Column(name = "notsametable1", nullable = false,length = 10)
		private int notsametable1;
		
		@Column(name = "notsametable2", nullable = false,length = 10)
		private int notsametable2;

		@Column(name = "tablenumber", nullable = true,length = 10)
		private int tablenumber;
		
	   public Guest(){

	   }
	   
	   public Guest(String firstname, String lastname, int sametable1, int sametable2, 
			   int sametable3, int notsametable1, int notsametable2, int tablenumber){
	     this();
	     this.firstname = firstname;
	     this.lastname = lastname;
	     this.sametable1 = sametable1;
	     this.sametable2 = sametable2;
	     this.sametable3 = sametable3;
	     this.notsametable1 = notsametable1;
	     this.notsametable2 = notsametable2;
	     this.tablenumber = tablenumber;
	   }


	   public int getGuestId() {
	      return guest_id;
	   }


	   public void setId(int guest_id) {
	      this.guest_id = guest_id;
	   }
	   
		public String getFirstName() {
			return firstname;
		}
		@XmlElement
		public void setFirstName(String firstname) {
		
			this.firstname = firstname;
		}
		
		
		public String getLastName() {
			return lastname;
		}
		@XmlElement
		public void setLastName(String lastname) {
		
			this.lastname = lastname;
		}
		
		public int GetSameTable1()
		{
			return this.sametable1;
		}
		@XmlElement
		public void SetSameTable1(int sametable1)
		{
			this.sametable1 = sametable1;
		}
		
		public int GetSameTable2()
		{
			return this.sametable2;
		}
		@XmlElement
		public void SetSameTable2(int sametable2)
		{
			this.sametable2 = sametable2;
		}
		
		public int GetSameTable3()
		{
			return this.sametable3;
		}
		@XmlElement
		public void SetSameTable3(int sametable3)
		{
			this.sametable3 = sametable3;
		}
		
		public int GetNotSameTable1()
		{
			return this.notsametable1;
		}
		@XmlElement
		public void SetNotSameTable1(int notsametable1)
		{
			this.notsametable1 = notsametable1;
		}
		
		public int GetNotSameTable2()
		{
			return this.notsametable2;
		}
		@XmlElement
		public void SetNotSameTable2(int notsametable2)
		{
			this.notsametable2 = notsametable2;
		}
		
		public boolean UpdateGuest(Guest guest)
		{
			GuestDAO.saveGuest(guest);
		    return true;
		}
		
		public int getTableNumber() {
		      return tablenumber;
	   }
	   
	   @XmlElement
	   public void setTableNumber(int tablenumber) {
		   this.tablenumber = tablenumber;
	   }
}
