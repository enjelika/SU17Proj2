package GeneticAlgorithm;

import java.util.List;

import GeneticAlgorithm.IndividualGuest;
import eventDAO.GuestDAO;
import eventPD.Guest;

public class GuestsList
{
	public IndividualGuest[] guestLists_Genes = new IndividualGuest[GuestDAO.listGuest().size()];
	
	// Create a random GuestsListIndividual
    public void generateIndividual() {
    	List<Guest> guestsListInTheDB = GuestDAO.listGuest();
    	
    	/*/
    	 * Clone a list of guest in the db
    	 */
    	for(int i = 0; i < size(); i ++) {
    		guestLists_Genes[i] = new IndividualGuest();
    	}
    	for(Guest guest : guestsListInTheDB) 
    	{
    		guestLists_Genes[guestsListInTheDB.indexOf(guest)].guest_id = guest.getGuestId();
    		guestLists_Genes[guestsListInTheDB.indexOf(guest)].firstname = guest.getFirstName();
    		guestLists_Genes[guestsListInTheDB.indexOf(guest)].lastname = guest.getLastName();
    		guestLists_Genes[guestsListInTheDB.indexOf(guest)].sametable1 = guest.GetSameTable1();
    		guestLists_Genes[guestsListInTheDB.indexOf(guest)].sametable2 = guest.GetSameTable2();
    		guestLists_Genes[guestsListInTheDB.indexOf(guest)].sametable3 = guest.GetSameTable3();
    		guestLists_Genes[guestsListInTheDB.indexOf(guest)].notsametable1 = guest.GetNotSameTable1();
    		guestLists_Genes[guestsListInTheDB.indexOf(guest)].notsametable2 = guest.GetNotSameTable2();
    		guestLists_Genes[guestsListInTheDB.indexOf(guest)].tablenumber = guest.getTableNumber();
    	}
    	
    	/*/
    	 * Shuffle to get a random organization
    	 */
		Utility.shuffleArray(guestLists_Genes);	
		
    	/*/
    	 * Assign table number depend on the size of the table to each guest
    	 */
		int tableSize = 3; // Need to obtain somewhere in the db
		int tableNumber = 1; // First table number
		int seatNumber = 1; // First seat number
		for (int i = 0; i < guestLists_Genes.length; i++)
		{
			if (seatNumber < tableSize + 1)
			{
				guestLists_Genes[i].tablenumber = tableNumber; // Assign table number
				seatNumber++; // Increment seat number
			} 
			else
			{
				seatNumber = 1; // Reset seat number
				tableNumber++; // Increment table number
				guestLists_Genes[i].tablenumber = tableNumber; // Assign table number
				seatNumber++; // Increment seat number
			}
		}
    }
    
    public int size() {
    	return guestLists_Genes.length;
    } 
}