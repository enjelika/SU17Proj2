package eventREST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import eventDAO.CompanyDAO;
import eventDAO.CustomerDAO;
import eventDAO.EM;
import eventDAO.EventDAO;
import eventDAO.GuestDAO;
import eventDAO.StaffDAO;
import eventPD.Company;
import eventPD.Customer;
import eventPD.Event;
import eventPD.Guest;
import eventPD.Staff;
import eventUT.Log;
import eventUT.Message;


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
			// Example how to call the GA
				int tableSize = 4;
				int numberOfTour = 9999;
				GeneticAlgorithm.GA.runGA(GuestDAO.listGuest(), tableSize, numberOfTour);
				GeneticAlgorithm.GA.printResult();
		      return company;
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
		   @Path("/guest")
		   @Produces(MediaType.APPLICATION_JSON)
		   public List<Guest> getGuests(){
			   return (GuestDAO.listGuest());
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
		   
		   @DELETE
		   @Path("/customer/{id}")
		   @Produces(MediaType.APPLICATION_JSON)
		   @Consumes(MediaType.APPLICATION_JSON)
		   public ArrayList<Message> DeleteCustomer(Customer deleteCustomer,@PathParam("id") String id,@Context final HttpServletResponse response) throws IOException{

			  if (deleteCustomer == null) {

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
				  CustomerDAO.removeCustomer(existingCustomer);
				  userTransaction.commit();
				  messages.add(new Message("op001","Success Operation",""));
				  return messages;
			  }
		}
		   
		   @GET
		   @Path("/event")
		   @Produces(MediaType.APPLICATION_JSON)
		   public List<Event> getEvents(){
			   return (EventDAO.listEvent());
		   }	
		   
		   @POST
		   @Path("/event")
		   @Produces(MediaType.APPLICATION_JSON)
		   public ArrayList<Message> addEvent(String event,@Context final HttpServletResponse response) throws IOException{
			   
			   Object obj = JSONValue.parse(event);
			   HashMap<String, String> map = (HashMap<String, String>) obj;
			   if (event == null) {

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
					  Event addEvent = new Event();
					  addEvent.setName(map.get("name"));
					  addEvent.setCustomerId(Integer.parseInt(map.get("customer_id")));
					  addEvent.setStaffId(Integer.parseInt(map.get("staff_id")));
					  addEvent.setVenue(map.get("venue"));
					  addEvent.setMaxguests(Integer.parseInt(map.get("maxguests")));
					  addEvent.setNumtables(Integer.parseInt(map.get("numtables")));
					  EventDAO.addEvent(addEvent);
					  userTransaction.commit();
					  messages.add(new Message("op001","Success Operation",""));
					  return messages;
				  }
		   }
		   
		   @PUT
		   @Path("/event/{id}")
		   @Produces(MediaType.APPLICATION_JSON)
		   @Consumes(MediaType.APPLICATION_JSON)
		   public ArrayList<Message> UpdateEvent(String updatedEvent,@PathParam("id") String id,@Context final HttpServletResponse response) throws IOException{

			   Object obj = JSONValue.parse(updatedEvent);
			   HashMap<String, String> map = (HashMap<String, String>) obj;
			  if (updatedEvent == null) {

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
				  Event existingEvent = EventDAO.findEventByIdNumber(id);
				  existingEvent.setCustomerId(Integer.parseInt(map.get("customer_id")));
				  existingEvent.setStaffId(Integer.parseInt(map.get("staff_id")));
				  EventDAO.saveEvent(existingEvent);
				  userTransaction.commit();
				  messages.add(new Message("op001","Success Operation",""));
				  return messages;
			  }
		}
		   
		   @DELETE
		   @Path("/event/{id}")
		   @Produces(MediaType.APPLICATION_JSON)
		   @Consumes(MediaType.APPLICATION_JSON)
		   public ArrayList<Message> DeleteEvent(Event deleteEvent,@PathParam("id") String id,@Context final HttpServletResponse response) throws IOException{

			  if (deleteEvent == null) {

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
				  Event existingEvent = EventDAO.findEventByIdNumber(id);
				  EventDAO.removeEvent(existingEvent);
				  userTransaction.commit();
				  messages.add(new Message("op001","Success Operation",""));
				  return messages;
			  }
		}
		   
		   @GET
		   @Path("/staff")
		   @Produces(MediaType.APPLICATION_JSON)
		   public List<Staff> getStaff(){
			   return (StaffDAO.listStaff());
		   }	
		   
		   @POST
		   @Path("/staff")
		   @Produces(MediaType.APPLICATION_JSON)
		   public ArrayList<Message> addStaff(Staff staff,@Context final HttpServletResponse response) throws IOException{
			   
			   if (staff == null) {

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
					  StaffDAO.addStaff(staff);
					  userTransaction.commit();
					  messages.add(new Message("op001","Success Operation",""));
					  return messages;
				  }
		   }
		   
		   @PUT
		   @Path("/staff/{id}")
		   @Produces(MediaType.APPLICATION_JSON)
		   @Consumes(MediaType.APPLICATION_JSON)
		   public ArrayList<Message> UpdateStaff(Staff updatedStaff,@PathParam("id") String id,@Context final HttpServletResponse response) throws IOException{

			  if (updatedStaff == null) {

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
				  Staff existingStaff = StaffDAO.findStaffByIdNumber(id);
				  existingStaff.setEmail(updatedStaff.getEmail());
				  StaffDAO.saveStaff(existingStaff);
				  userTransaction.commit();
				  messages.add(new Message("op001","Success Operation",""));
				  return messages;
			  }
		}
		   
		   @DELETE
		   @Path("/staff/{id}")
		   @Produces(MediaType.APPLICATION_JSON)
		   @Consumes(MediaType.APPLICATION_JSON)
		   public ArrayList<Message> DeleteStaff(Staff deleteStaff,@PathParam("id") String id,@Context final HttpServletResponse response) throws IOException{

			  if (deleteStaff == null) {

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
				  Staff existingStaff = StaffDAO.findStaffByIdNumber(id);
				  StaffDAO.removeStaff(existingStaff);
				  userTransaction.commit();
				  messages.add(new Message("op001","Success Operation",""));
				  return messages;
			  }
		}
		   
		   @OPTIONS
		   @Path("/{a:company|customer|staff|guest|event}")
		   @Produces(MediaType.APPLICATION_JSON)
		   public String getSupportedOperations(){
		      return "{ {'POST' : { 'description' : 'update company'}} {'GET' : {'description' : 'get company'}} {'PUT' : {'description' : 'put'}} {'DELETE' : {'description' : 'delete'}}}";
		   }
		
}	
	   
