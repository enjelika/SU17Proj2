package systemDAO;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import eventDAO.EM;
import systemPD.User;

public class UserDAO { 

  public static void saveUser(User user) {
    EM.getEM().persist(user);
  }
  public static void addUser(User user) {
    EM.getEM().persist(user);
  }
  public static List<User> listUser()
  {
    TypedQuery<User> query = EM.getEM().createQuery("SELECT user FROM user user", User.class);
    return query.getResultList();
  }
  
  public static List<User> getAllUsers()
  {
    TypedQuery<User> query = EM.getEM().createQuery("SELECT user FROM user user", User.class);
    return query.getResultList();
  }
  public static User findUserById(long id)
  {
    User user = EM.getEM().find(User.class, id);
    return user;
  }
  
  public static User findUserByUserName(String username)
  {
    String qString = "SELECT user FROM user user  WHERE user.userName ='"+username+"'";
    Query query = EM.getEM().createQuery(qString);
    User user = (User)query.getSingleResult();
    return user;
  }

  public static List<User> getAllUsers(int page, int pageSize)
  {
    TypedQuery<User> query = EM.getEM().createQuery("SELECT user FROM user user", User.class);
    return query.setFirstResult(page)
            .setMaxResults(pageSize)
            .getResultList();

  }
  
  public static void removeUser(User user)
  {
    EM.getEM().remove(user);
  }
}

