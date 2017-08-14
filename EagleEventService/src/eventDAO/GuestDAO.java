package eventDAO;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import eventPD.Customer;
import eventPD.Guest;

@SuppressWarnings("unused")
public class GuestDAO {
	public static void saveGuest(Guest guest) {
        EM.getEM().persist(guest);
      }
      public static void addGuest(Guest guest) {
        EM.getEM().persist(guest);
      }

      // Find guest by id
      public static Guest findGuestById(int id) 
      {
    	  return EM.getEM().find(Guest.class, id);
      }

      // Get guests by table
	  	public static List<Guest> listGuestsByTable(int tablenumber) 
	  	{
	  		Query query = EM.getEM().createQuery("SELECT g FROM guest g WHERE g.tablenumber = " + tablenumber);
	  		@SuppressWarnings("unchecked")
	  		List<Guest> listGuestsByTable= (List<Guest>) query.getResultList();
	
	  		return listGuestsByTable;
	  	}
      
      public static List<Guest> listGuest()
      {
        TypedQuery<Guest> query = EM.getEM().createQuery("SELECT guest FROM guest guest", Guest.class);
        return query.getResultList();
      }
      
      public static List<Guest> listGuestByEventId(int event) 
      {
    	  Query query = EM.getEM().createQuery("SELECT g FROM guest g WHERE g.event = " + event);
	  		@SuppressWarnings("unchecked")
	  		List<Guest> listGuestByEventId= (List<Guest>) query.getResultList();
	
	  		return listGuestByEventId;
      }
}
