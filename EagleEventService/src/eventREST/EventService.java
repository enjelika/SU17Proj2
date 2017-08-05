package eventREST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import eventDAO.CompanyDAO;
import eventDAO.CustomerDAO;
import eventDAO.EM;
import eventDAO.SchoolDAO;
import eventPD.Company;
import eventPD.Customer;
import eventPD.School;
import eventPD.Student;
import eventUT.Log;
import eventUT.Message;
import systemREST.Secured;


	@Path("/eventservice")
	public class EventService {
	  @Context
	  SecurityContext securityContext;
	  
		ArrayList<Message> messages = new ArrayList<Message>();
		
		Company company = (Company) (CompanyDAO.listCompany().get(0));
		Log log = new Log();
		
		
		   @GET
		   @Path("/company/")
		   @Produces(MediaType.APPLICATION_JSON)
		   public Company getCompany(){
			   
			  // I put some codes here just for testing purpose
			  // TODO: Obtain a guests list
			   
			  // Store the result combination list 
			  permute(java.util.Arrays.asList("Blake","Thinh","Debra","Trung","Shane"), 0);
			  
			  // TODO: Add logic to assign guest into each table depend on the size of the table
			  // TODO: Add logic to check the constrains for each table
			  // TODO: Save the correct arrangement and break the loop
			  
		      return company;
		   }
		 
		   // Do permutation to find all guest combinations
		   public static void permute(List<String> arr, int startIndex) 
		   {
			   for(int index = startIndex; index < arr.size(); index++)
			   {
		            java.util.Collections.swap(arr, index, startIndex);
		            permute(arr, startIndex+1);
		            java.util.Collections.swap(arr, startIndex, index);
			   }
			   if (startIndex == arr.size() -1){
				   // TODO: Store each result into a list
		            System.out.println(java.util.Arrays.toString(arr.toArray()));
			   }
		   }
		   
		   @POST
		   @Path("/company")
		   @Produces(MediaType.APPLICATION_JSON)
		   @Consumes(MediaType.APPLICATION_JSON)
		   public ArrayList<Message> UpdateCompany(Company updatedCompany,@Context final HttpServletResponse response) throws IOException{

			  if (updatedCompany == null) {

				  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
				  try {
				        response.flushBuffer();
				  }catch(Exception e){}
				  messages.add(new Message("op002","Fail Operation",""));
				  return messages;
			  }
			  else  {
				  
				  EntityTransaction userTransaction = EM.getEM().getTransaction();
				  userTransaction.begin();
				  company.setAddress(updatedCompany.getAddress());
				  company.setEmail(updatedCompany.getEmail());
				  company.setName(updatedCompany.getName());
				  company.setPhoneNumber(updatedCompany.getPhoneNumber());
				  Boolean result = company.UpdateCompany(company);
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
		   
		   @GET
		   @Path("/customer")
		   @Produces(MediaType.APPLICATION_JSON)
		   public List<Customer> getCustomers(){
			   return (CustomerDAO.listCustomer());
		   }	
		   
		   @POST
		   @Path("/customer")
		   @Produces(MediaType.APPLICATION_JSON)
		   public ArrayList<Message> addCustomer(Customer customer,@Context final HttpServletResponse response) throws IOException{
			   
			   if (customer == null) {

					  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
					  try {
					        response.flushBuffer();
					  }catch(Exception e){}
					  messages.add(new Message("op002","Fail Operation",""));
					  return messages;
				  }
				  else  {
					  
					  EntityTransaction userTransaction = EM.getEM().getTransaction();
					  userTransaction.begin();
					  CustomerDAO.addCustomer(customer);
					  userTransaction.commit();
					  messages.add(new Message("op001","Success Operation",""));
					  return messages;
				  }
		   }
		   
		   @PUT
		   @Path("/customer/{id}")
		   @Produces(MediaType.APPLICATION_JSON)
		   @Consumes(MediaType.APPLICATION_JSON)
		   public ArrayList<Message> UpdateCustomer(Customer updatedCustomer,@PathParam("id") String id,@Context final HttpServletResponse response) throws IOException{

			  if (updatedCustomer == null) {

				  response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
				  try {
				        response.flushBuffer();
				  }catch(Exception e){}
				  messages.add(new Message("op002","Fail Operation",""));
				  return messages;
			  }
			  else  {
				  
				  EntityTransaction userTransaction = EM.getEM().getTransaction();
				  userTransaction.begin();
				  Customer existingCustomer = CustomerDAO.findCustomerByIdNumber(id);
				  existingCustomer.setEmail(updatedCustomer.getEmail());
				  CustomerDAO.saveCustomer(existingCustomer);
				  userTransaction.commit();
				  messages.add(new Message("op001","Success Operation",""));
				  return messages;
			  }
		}
		   
		   @OPTIONS
		   @Path("/{a:company|customer}")
		   @Produces(MediaType.APPLICATION_JSON)
		   public String getSupportedOperations(){
		      return "{ {'POST' : { 'description' : 'update company'}} {'GET' : {'description' : 'get company'}} {'PUT' : {'description' : 'put'}}";
		   }
		
}	
	   
