package eventPD;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
		
		@Lob
		@Column(name = "guestlist", nullable = true,length = 2147483647)
		private byte[] guestlist;
		
		@Column(name = "customer_id", nullable = false,length = 10)
		private int customer_id;
		
		@Column(name = "staff_id", nullable = false,length = 10)
		private int staff_id;
		
	   	   
	   @Column(name = "name",nullable = false,length = 40)
	   private String name;


	   public Event(){}
	   
	   public Event(String venue, int maxguests, int numtables, byte[] guestlist,
			   int customer_id, int staff_id, String name){
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
		
		public byte[] getGuestList() {
			return guestlist;
		}
		@XmlElement
		public void setGuestList(byte[] guestlist) {
		
			this.guestlist = guestlist;
		}
		
		public int getCustomerId() {
			return customer_id;
		}
		@XmlElement
		public void setCustomerId(int customer_id) {
		
			this.customer_id = customer_id;
		}
		
		public int getStaffId() {
			return staff_id;
		}
		@XmlElement
		public void setStaffId(int staff_id) {
		
			this.staff_id = staff_id;
		}
	     
		public boolean UpdateEvent(Event event)
		{
			EventDAO.saveEvent(event);
		    return true;
		}
}
