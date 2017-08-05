package systemREST;

import systemPD.Role;
import systemPD.RoleAssignment;
import systemPD.System;
import systemPD.Token;
import systemPD.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import eventDAO.CompanyDAO;
import eventDAO.EM;
import eventPD.Company;
import eventUT.Message;


@Path("/systemservices")



public class SystemService  {
  
  @Context
  SecurityContext securityContext;
  
  ArrayList<Message> messages = new ArrayList<Message>();
  
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(User user)   {

      try {
            // Authenticate the user using the credentials provided
            authenticate(user.getUserName(), user.getPassword());

            // Issue a token for the user
            String token = issueToken(user.getUserName());

            // Return the token on the response
            return Response.ok(token).build();

       } catch (Exception e) {
           return Response.status(Response.Status.UNAUTHORIZED).build();
        }      
    }

    private void authenticate(String username, String password) throws Exception {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
        User user = System.findUserByUserName(username);
        if (user == null) throw new Exception();
        if (!user.authenticate(password)) throw new Exception();
    }

    private String issueToken(String username) {
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
      EntityTransaction userTransaction = EM.getEM().getTransaction();
      userTransaction.begin();
      Token token = new Token(System.findUserByUserName(username));
      token.save();
      userTransaction.commit();
      return token.getToken();
    }
    
	   @GET
	   @Path("/company/")
	   @Produces(MediaType.APPLICATION_JSON)
	   public Company getCompany(){
	      return (Company) (CompanyDAO.listCompany().get(0));
	   }
	 
    @Secured({Role.ADMIN})
    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUser(
       @DefaultValue("0") @QueryParam("page") String page,
       @DefaultValue("5") @QueryParam("per_page") String perPage){
      return System.findAllUsers(Integer.parseInt(page),Integer.parseInt(perPage));
    }  
    @Secured
    @GET
    @Path("/users/current")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(){
      String username = securityContext.getUserPrincipal().getName();
      User user = System.findUserByUserName(username);
      return user;
    }
    
    @Secured({Role.ADMIN})
    @GET
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("id") String id){
       User user = System.findUserById(id);
       EM.getEM().refresh(user);
       return user;
    }
    
    @Secured({Role.ADMIN})
    @POST
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<Message> addUser(User user,@Context final HttpServletResponse response) throws IOException{

     if (user == null) {

       response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
       try {
             response.flushBuffer();
       }catch(Exception e){}
       messages.add(new Message("op002","Fail Operation",""));
       return messages;
     }
     else  {
       
       ArrayList<Message> errMessages = user.validate();
       if (errMessages != null) {
         
         response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
         try {
           response.flushBuffer();
         } 
         catch(Exception e){
           }
         return errMessages;
       }
       EntityTransaction userTransaction = EM.getEM().getTransaction();
       userTransaction.begin();
       Boolean result = System.addUser(user);
       if (user.getRoleAssignments() != null){
        for (RoleAssignment ra : user.getRoleAssignments()) {
          user.addRoleAssignment(ra); 
         }
       }
       userTransaction.commit();
       if(result){
         messages.add(new Message("op001","Success Operation",""));
         return messages;
       }
       response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
       try {
         response.flushBuffer();
       } 
       catch(Exception e){
         }
       messages.add(new Message("op002","Fail Operation",""));
       return messages;
     }
 }
    @Secured({Role.ADMIN})
    @PUT
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ArrayList<Message> udpatedStudent(User user,@PathParam("id") String id, @Context final HttpServletResponse response) throws IOException{
      User oldUser = System.findUserById(id);
      if (oldUser == null)
       {
           response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
           try {
             response.flushBuffer();
           }catch(Exception e){}
         messages.add(new Message("op002","Fail Operation",""));
         return messages;
       }
     else
       {
         ArrayList<Message> errMessages = user.validate();
         if (errMessages != null) {
           response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
           try {
             response.flushBuffer();
           } 
           catch(Exception e){
             }
           return errMessages;
         }
       }
       EntityTransaction userTransaction = EM.getEM().getTransaction();
       userTransaction.begin();
       Boolean result = oldUser.update(user);
       userTransaction.commit();
       if(result){
       messages.add(new Message("op001","Success Operation",""));
       return messages;
       }
       response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
       try {
           response.flushBuffer();
       }catch(Exception e){}
       messages.add(new Message("op002","Fail Operation",""));
       return messages;
    }
    
    @Secured({Role.ADMIN})
    @DELETE
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Message> deleteUser(@PathParam("id") String id, @Context final HttpServletResponse response){
     User user = System.findUserById(id);
     if (user == null) {
       response.setStatus(HttpServletResponse.SC_NOT_FOUND);
         try {
             response.flushBuffer();
         }catch(Exception e){}
       messages.add(new Message("op002","Fail Operation",""));
       return messages;
     }
       EntityTransaction userTransaction = EM.getEM().getTransaction();
       userTransaction.begin();
       if (user.getRoleAssignments() != null){
         for (RoleAssignment ra : user.getRoleAssignments()) {
           user.removeRoleAssignment(ra);
         }
       }
         
       Boolean result = System.removeUser(user);
       userTransaction.commit();
       if(result){
       messages.add(new Message("op001","Success Operation",""));
       return messages;
       }
       else {
       messages.add(new Message("op002","Fail Operation",""));
       return messages;
       }
    }

}