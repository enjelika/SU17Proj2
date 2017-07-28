package systemPD;

import java.util.Collection;
import java.util.List;

import eventDAO.StudentDAO;
import systemDAO.TokenDAO;
import systemDAO.UserDAO;

public class System {

  public static System system;
  private Collection<Token> tokens;
  private Collection<Role> roles;
  private Collection<User> users;

  public System getSystem() {
    return this.system;
  }

  public Collection<Token> getTokens() {
    return this.tokens;
  }

  public void setTokens(Collection<Token> tokens) {
    this.tokens = tokens;
  }

  public Collection<User> getUsers() {
    return this.users;
  }

  /**
   * 
   * @param token
   */
  public static Token findToken(String token) {
    return TokenDAO.findTokenByToken(token);
  }

  /**
   * 
   * @param userName
   */
  public static User findUserByUserName(String userName) {
    return UserDAO.findUserByUserName(userName);
  }
  
  public static User findUserById(String id) {
    User user = UserDAO.findUserById(Long.parseLong(id));
    return user;
  }
  
  public static Boolean addUser(User user) {
    UserDAO.addUser(user);
    return true;
  }
  
  public static Boolean removeUser(User user) {
    UserDAO.removeUser(user);
    return true;
  }
  
  
  public static List<User> findAllUsers(int page, int pageSize) {
    List<User> list = UserDAO.getAllUsers(page, pageSize);
    return list;
  }

}