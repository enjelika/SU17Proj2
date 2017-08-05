package eventPD;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import eventDAO.EventDAO;

@XmlRootElement(name = "event")
@Entity(name = "event")
public class Event implements Serializable {
	   private static final long serialVersionUID = 1L;
	   @Id //signifies the primary key
	   @Column(name = "event_id", nullable = false)
	   @GeneratedValue(strategy = GenerationType.AUTO)
	   private int event_id;
	   
	   @Column(name = "venue",nullable = false,length = 40)
	   private String venue;
	   
		@Column(name = "maxguests", nullable = false,length = 10)
		private int maxguests;
		
		
		@Column(name = "numtables", nullable = false,length = 10)
		private int numtables;
		
		
		@Column(name = "guestlist", nullable = false,length = 10)
		private int guestlist;
		
		@ManyToOne(optional = false, cascade = CascadeType.PERSIST)
		@JoinColumn(name = "customer_id",referencedColumnName="customer_id") 
		private Customer customer_id;
		
		@ManyToOne(optional = false, cascade = CascadeType.PERSIST)
		@JoinColumn(name = "staff_id",referencedColumnName="staff_id") 
		private Staff staff_id;
		
	   	   
	   @Column(name = "name",nullable = false,length = 40)
	   private String name;


	   public Event(){

	   }
	   
	   public Event(String venue, int maxguests, int numtables, int guestlist,
			   Customer customer_id, Staff staff_id, String name){
	     this();
	     this.venue = venue;
	     this.maxguests = maxguests;
	     this.numtables = numtables;
	     this.guestlist = guestlist;
	     this.customer_id = customer_id;
	     this.staff_id = staff_id;
	     this.name = name;
	   }

	   public int getEventId() {
	      return event_id;
	   }


	   public void setId(int event_id) {
	      this.event_id = event_id;
	   }
	   
		public String getName() {
			return name;
		}
		@XmlElement
		public void setName(String name) {
		
			this.name = name;
		}
		
		
		public String getVenue() {
			return venue;
		}
		@XmlElement
		public void setVenue(String venue) {
		
			this.venue = venue;
		}
	     
		public int getMaxguests() {
			return maxguests;
		}
		@XmlElement
		public void setMaxguests(int maxguests) {
		
			this.maxguests = maxguests;
		}
		
		public int getNumtables() {
			return numtables;
		}
		@XmlElement
		public void setNumtables(int numtables) {
		
			this.numtables = numtables;
		}
		
		public int getGuestList() {
			return guestlist;
		}
		@XmlElement
		public void setGuestList(int guestlist) {
		
			this.guestlist = guestlist;
		}
		
		public Customer getCustomerId() {
			return customer_id;
		}
		@XmlElement
		public void setCustomerId(Customer customer_id) {
		
			this.customer_id = customer_id;
		}
		
		public Staff getStaffId() {
			return staff_id;
		}
		@XmlElement
		public void setStaffId(Staff staff_id) {
		
			this.staff_id = staff_id;
		}
	     
		public boolean UpdateEvent(Event event)
		{
			EventDAO.saveEvent(event);
		    return true;
		}
}
