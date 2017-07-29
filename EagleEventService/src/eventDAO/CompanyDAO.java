package eventDAO;

import java.util.List;

import javax.persistence.TypedQuery;

import eventPD.Company;

public class CompanyDAO {

    public static void saveCompany(Company company) {
        EM.getEM().persist(company);
      }
      public static void addSchool(Company company) {
        EM.getEM().persist(company);
      }

      public static List<Company> listCompany()
      {
        TypedQuery<Company> query = EM.getEM().createQuery("SELECT company FROM company company", Company.class);
        return query.getResultList();
      }
}
