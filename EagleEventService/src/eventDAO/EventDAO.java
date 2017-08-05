package eventDAO;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import eventPD.Event;


public class EventDAO {
	
	public static void saveEvent(Event event) {
        EM.getEM().persist(event);
      }
      public static void addEvent(Event event) {
        EM.getEM().persist(event);
      }
      
      public static Event findEventByIdNumber(String idNumber)
      {
        String qString = "SELECT event FROM event event  WHERE event.event_id ="+idNumber;
        Query query = EM.getEM().createQuery(qString);
        Event event = (Event)query.getSingleResult();
        return event;
      }

      public static List<Event> listEvent()
      {
        TypedQuery<Event> query = EM.getEM().createQuery("SELECT event FROM event event", Event.class);
        return query.getResultList();
      }
      
      public static void removeEvent(Event event)
      {
        EM.getEM().remove(event);
      }
}
