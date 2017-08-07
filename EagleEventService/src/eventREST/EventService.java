package eventREST;

import java.io.IOException;
import java.util.ArrayList;
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

import eventDAO.CompanyDAO;
import eventDAO.CustomerDAO;
import eventDAO.EM;
import eventDAO.GuestDAO;
import eventDAO.StaffDAO;
import eventPD.Company;
import eventPD.Customer;
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
		static ArrayList<List<Guest>> permutatedGuestLists = new ArrayList<List<Guest>>();	// For testing purpose will move to the correct place

		
		   @GET
		   @Path("/company/")
		   @Produces(MediaType.APPLICATION_JSON)
		   public Company getCompany(){
			  GenerateGuestsListWithSeatArrangement();	// for testing purpose
		      return company;
		   }
		 
		   // Generate a guests list with seat arrangement
		   public static void GenerateGuestsListWithSeatArrangement()
		   {				
			   // Obtain a guests list
			   List<Guest> guests = GuestDAO.listGuest();	//Need to update to check for getting the list depend on the selected event
			  
			   // Permute and store the result combination list 
			   permute(guests, 0);
			  
			   // Check each of the possible guests list
			   for(List<Guest> permutatedGuestList : permutatedGuestLists) 
			   {
				   // For example, if we have 3 seats per table
				   int tableSize = 3;	// Need to obtain somewhere in the db
				   int tableNumber = 1;	// First table number
				   int seatNumber = 1;	// First seat number
				  				
				   // Assign guest into each table depend on the size of the table
				   for(Guest guest : permutatedGuestList) 
				   {
					   if(seatNumber < tableSize + 1) {
						   guest.setTableNumber(tableNumber);	// Assign table number
						   seatNumber++;	// Increment seat number
					   }
					   else {
						   seatNumber = 1;	// Reset seat number
						   tableNumber++;	// Increment table number
						   guest.setTableNumber(tableNumber);	// Assign table number
						   seatNumber++;	// Increment seat number
					   }
				   	}

				   for(Guest guest : guests) {
					   System.out.println("---" + guest.getFirstName() + " " + guest.getTableNumber());
				   }
				   
				   // Validate the seat arrangement
				   boolean correctArrangement = ValidateSeatingArrangement(permutatedGuestList);
				  		
				   // If there is a correct seat arrangement , save it and stop checking other seat arrangements
				   if(correctArrangement) {
					   System.out.println("Complete seat arrangement");
					   for(Guest guest : permutatedGuestList) 
					   {
						   EntityTransaction guestsTransaction = EM.getEM().getTransaction();
						   guestsTransaction.begin();
						   GuestDAO.findGuestById(guest.getGuestId()).setTableNumber(guest.getTableNumber());
						   guestsTransaction.commit();
						   System.out.println(guest.getFirstName() + "(" + guest.getTableNumber() + ")");
					   }
					   break;
				   }
				   else {
					   System.out.println("This seat arrangement is fail the constraints. Move to the next seat arrangement");
				   }
			   }
			  
			   System.out.println("Done Process");
		   }
		   
		   // Find guest by id
		   public static Guest FindGuestById (int id, List<Guest> guests)
		   {
			   for (Guest guest : guests) {
				   if(guest.getGuestId() == id) {
					   return guest;
				   }
			   }
			   return null;
		   }
		   
		   // Validate the seating arrangement
		   public static boolean ValidateSeatingArrangement(List<Guest> permutatedGuestList) 
		   {
			  boolean correctArrangement = false;
			   
			  // Check each guest for the constraints
			  for(Guest guest : permutatedGuestList) 
			  {
				  // Check for same table request
				  if(guest.GetSameTable1() != 0)
				  {
					  if(FindGuestById(guest.GetSameTable1(), permutatedGuestList).getTableNumber() == guest.getTableNumber())
					  {
						  correctArrangement = true;
					  }
					  else {
						  correctArrangement = false;
						  break;
					  }
				  }
				  
				  if(guest.GetSameTable2() != 0)
					  if(FindGuestById(guest.GetSameTable2(), permutatedGuestList).getTableNumber() == guest.getTableNumber())
						  correctArrangement = true;
					  else
					  {
						  correctArrangement = false;
						  break;
					  }
				  if(guest.GetSameTable3() != 0)
					  if(FindGuestById(guest.GetSameTable3(), permutatedGuestList).getTableNumber() == guest.getTableNumber())
						  correctArrangement = true;
					  else
					  {
						  correctArrangement = false;
						  break;
					  }
				  
				  // Check for NOT same table request
				  if(guest.GetNotSameTable1() != 0)
					  if(FindGuestById(guest.GetNotSameTable1(), permutatedGuestList).getTableNumber() == guest.getTableNumber())
					  {
						  correctArrangement = false;
						  break;
					  }
					  else
						  correctArrangement = true;
				  if(guest.GetNotSameTable2() != 0)
					  if(FindGuestById(guest.GetNotSameTable2(), permutatedGuestList).getTableNumber() == guest.getTableNumber())
					  {
						  correctArrangement = false;
						  break;
					  }
					  else
						  correctArrangement = true;
			  }
			  
			  return correctArrangement;
		   }
		   
		   // Do permutation to find all guest combinations
		   public static void permute(List<Guest> guests, int startIndex) 
		   {
			   for(int index = startIndex; index < guests.size(); index++)
			   {
		            java.util.Collections.swap(guests, index, startIndex);
		            permute(guests, startIndex+1);
		            java.util.Collections.swap(guests, startIndex, index);
			   }
			   if (startIndex == guests.size() -1){
				   List result = new ArrayList<Guest>(guests);
				   permutatedGuestLists.add(result); // Store each result into a list
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
		   @Path("/{a:company|customer|staff}")
		   @Produces(MediaType.APPLICATION_JSON)
		   public String getSupportedOperations(){
		      return "{ {'POST' : { 'description' : 'update company'}} {'GET' : {'description' : 'get company'}} {'PUT' : {'description' : 'put'}} {'DELETE' : {'description' : 'delete'}}}";
		   }
		
}	
	   
