package eventDAO;

import java.util.List;

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

      public static List<Guest> listGuest()
      {
        TypedQuery<Guest> query = EM.getEM().createQuery("SELECT guest FROM guest guest", Guest.class);
        return query.getResultList();
      }
}
