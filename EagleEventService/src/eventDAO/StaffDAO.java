package eventDAO;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import eventPD.Staff;


public class StaffDAO {
	
	public static void saveStaff(Staff staff) {
        EM.getEM().persist(staff);
      }
      public static void addStaff(Staff staff) {
        EM.getEM().persist(staff);
      }
      
      public static Staff findStaffByIdNumber(String idNumber)
      {
        String qString = "SELECT staff FROM staff staff  WHERE staff.staff_id ="+idNumber;
        Query query = EM.getEM().createQuery(qString);
        Staff student = (Staff)query.getSingleResult();
        return student;
      }

      public static List<Staff> listStaff()
      {
        TypedQuery<Staff> query = EM.getEM().createQuery("SELECT staff FROM staff staff", Staff.class);
        return query.getResultList();
      }
      
      public static void removeStaff(Staff staff)
      {
        EM.getEM().remove(staff);
      }
}
