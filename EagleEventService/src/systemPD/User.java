package systemPD;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import eventPD.Student;
import eventUT.Message;
import systemDAO.RoleAssignmentDAO;
@XmlRootElement(name = "user")
@Entity(name = "user")
public class User implements Serializable {

private static final long serialVersionUID = 1L;

@Id //signifies the primary key
@Column(name = "user_id", nullable = false)
@GeneratedValue(strategy = GenerationType.AUTO)
private long userID;

@OneToMany(mappedBy="user",targetEntity=RoleAssignment.class,fetch=FetchType.EAGER)
  private Collection<RoleAssignment> roleAssignments;
  @Column(name = "user_name",nullable = false,length = 25)
  private String userName;
  @Column(name = "password",nullable = false,length = 25)
  private String password;
  @Column(name = "first_name",nullable = false,length = 25)
  private String firstName;
  @Column(name = "last_name",nullable = false,length = 25)
  private String lastName;
  @Column(name = "email",nullable = false,length = 50)
  private String email;

  @OneToMany 
  @JoinColumn(name="user_id",referencedColumnName="user_id")
  private Collection<Token> tokens;

  public long getUserID() {
    return this.userID;
  }
  @XmlElement
  public void setUserID(long userID) {
    this.userID = userID;
  }

  public String getUserName() {
    return this.userName;
  }
  @XmlElement
  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return this.password;
  }
  @XmlElement
  public void setPassword(String password) {
    this.password = password;
  }

  @XmlTransient
  public Collection<Token> getTokens() {
    return this.tokens;
  }
 
  public Collection<RoleAssignment> getRoleAssignments() {
    return this.roleAssignments;
  }
    
  @XmlElement
  public void setRoleAssignments(Collection<RoleAssignment> roleAssignments) {
      this.roleAssignments=roleAssignments;
  }

  /**
   * 
   * @param password
   */
  public boolean authenticate(String password) {
    return getPassword().equals(password);
  }

  public String getFirstName() {
    return this.firstName;
  }
  @XmlElement
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }
  @XmlElement
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return this.email;
  }
  @XmlElement
  public void setEmail(String email) {
    this.email = email;
  }
  /**
   * 
   * @param roleName
   */
  public boolean isAuthorize(Role role) {
    for (RoleAssignment ra : getRoleAssignments()) {
      if (ra.getRole().equals(role) ) return true;
    }
    return false;
  }
  
  public Boolean update(User user) {

    setFirstName(user.getFirstName());
    setLastName(user.getLastName());
    setEmail(user.getEmail());
    setUserName(user.getUserName());
    setPassword(user.getPassword());
    if (user.getRoleAssignments() != null) {
      for (RoleAssignment ra : this.getRoleAssignments()) {
        removeRoleAssignment(ra);
      }
      for (RoleAssignment ra : user.getRoleAssignments()) {
        addRoleAssignment(ra);
      }
    }
  

    return true;
  }
  public ArrayList<Message> validate() {
    return null;
  }
  
  public void removeRoleAssignment(RoleAssignment ra) {
    RoleAssignmentDAO.removeRoleAssignment(ra);
  }
  
  public void addRoleAssignment(RoleAssignment ra) {
    ra.setUser(this);
    RoleAssignmentDAO.saveRoleAssignment(ra);
  }

}