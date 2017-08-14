package eventPD;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@SuppressWarnings("serial")
@WebServlet(name = "FileServlet", urlPatterns = {"/FileServlet"})
public class FileServlet extends HttpServlet 
{
	@Resource(name = "jdbc/fileDB")
	private DataSource ds;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException 
	{
		int eventID = Integer.parseInt(request.getParameter("event_id"));

		try 
		{
			Connection conn = ds.getConnection();
			PreparedStatement selectQuery = conn.prepareStatement("SELECT * FROM EVENT EVENT WHERE EVENT_ID=" + eventID);

			ResultSet result = selectQuery.executeQuery();
			
			if (!result.next()) 
			{
				System.out.println("***** SELECT query failed for FileServlet");
			}

			Blob fileBlob = result.getBlob("GUESTLIST");

			response.setContentType(".csv");
			response.setHeader("Content-Disposition", "inline; filename=\""
					+ "guestList file\"");

			final int BYTES = 1024;
			int length = 0;
			InputStream in = fileBlob.getBinaryStream();
			OutputStream out = response.getOutputStream();
			byte[] bbuf = new byte[BYTES];

			while ((in != null) && ((length = in.read(bbuf)) != -1)) 
			{
				out.write(bbuf, 0, length);
			}

			out.close();
			conn.close();
		} 
		catch (SQLException e) 
		{
			System.out.println(e.getMessage());
		}
	}
}