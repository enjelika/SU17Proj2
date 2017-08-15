package eventPD;

import eventDAO.EM;
import eventDAO.EventDAO;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

@SuppressWarnings("serial")
@Named
@RequestScoped
public class FileBean implements Serializable 
{    
    private static Part part;

    // Method for downloading the selected file from the DB
    public static String downloadFile(int eventID) throws SQLException, IOException 
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext context = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        ServletOutputStream outStream = response.getOutputStream();

        String eventId = String.valueOf(eventID);
        Event event = EventDAO.findEventByIdNumber(eventId);
        
        if (event != null) 
        {
            facesContext.addMessage("downloadForm:download",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "file download failed for id = " + eventID, null));
        }

        byte[] fileBytes = event.getGuestList();
        
        response.setContentType(part.getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + part.getName() + "\"");

        int length = 0;

        while ((fileBytes != null) && ((length = fileBytes.length) != -1)) 
        {
            outStream.write(fileBytes, 0, length);
        }

        outStream.close();
        EM.getEM().close();

        return null;
    }

    // Method for uploading the guestList file into the DB
    public static void uploadFile(int eventID) throws IOException, SQLException 
    {
    	System.out.println("uploading file... \n eventID = " + eventID);
    	
        String eventId = String.valueOf(eventID);
        Event event = EventDAO.findEventByIdNumber(eventId);

        InputStream inputStream;
        inputStream = null;
        
        try 
        {
            inputStream = part.getInputStream();
            byte[] buff = IOUtils.toByteArray(inputStream);
            event.setGuestList(buff);
            
            EventDAO.saveEvent(event);
        } 
        catch (IOException e) 
        {
        	e.printStackTrace();
        } 
        finally 
        {
            if (inputStream != null) 
            {
                inputStream.close();
            }
            
            if (event != null) 
            {
                EM.getEM().close();
            }
        }
    }

    public Part getPart() 
    {
        return part;
    }

    public void setPart(Part part) 
    {
        FileBean.part = part;
    }
}