package eventREST;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import eventDAO.ZipDAO;
import eventPD.Zip;

	@Path("/zipservice")
	public class ZipService {
		
	   @GET
	   @Path("/zip/{zipCode}")
	   @Produces(MediaType.APPLICATION_JSON)
	   public Zip getZip(@PathParam("zipCode") String zipCode) throws Exception{
		   return ZipDAO.findZipByNumber(zipCode);
	   }	
	   
	   
	   @OPTIONS
	   @Path("/zip")
	   @Produces(MediaType.APPLICATION_JSON)
	   public String getSupportedOperations(){
	      return "<operations>GET</operations>";
	   }
	 }	
	   
