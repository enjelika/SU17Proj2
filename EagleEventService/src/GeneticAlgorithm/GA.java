package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityTransaction;

import GeneticAlgorithm.Utility;
import eventDAO.EM;
import eventDAO.GuestDAO;
import eventPD.Guest;

public class GA
{
	// population size
	private static int popSize = 30;

	// chromosome/solution/geneotype
	private static int chromLength = GuestDAO.listGuest().size();

	// mutation rate, change it have a play
	private static double mutRate = 0.1;

	// recomBination rate
	private static double xoRate = 0.5;

	private static int tableSize = 4; // Default value
	public static int totalNumberOfTable = 1; // Default value
	
	// Create an initial input guests list
	private static IndividualGuest[] guestsList;

	// Create a genes array/population
	private static int[][] population;
	
	// Create initial chromosome
	static int[] initChromosome;
	
	// Best chromosome so far
	static int[] bestChromosomeSoFar;
	
	// Boolean solution is found
	public static boolean solutionFound = false;

	// Guests List from the db
	public static List<Guest> guestsListInTheDB;

	// Print result
	public static void printResult()
	{
		if (!solutionFound)
		{
			System.out.println("Fitness of best solution so far is: " + evalSolution(bestChromosomeSoFar) + " / " + expectedFitnessValue(guestsListInTheDB));
			for (int i = 0; i < chromLength; i++)
			{
				System.out.println(guestsList[i].firstname + " (" + guestsList[i].tablenumber + ") ");
			}
			storeResultToTheDB();
		}
	}

	// Initialize a population
	private static void initPopulation()
	{
		// Clone a list of guest in the db
		for (int i = 0; i < chromLength; i++)
		{
			guestsList[i] = new IndividualGuest();
		}
		for (Guest guest : guestsListInTheDB)
		{
			guestsList[guestsListInTheDB.indexOf(guest)].guest_id = guest.getGuestId();
			guestsList[guestsListInTheDB.indexOf(guest)].firstname = guest.getFirstName();
			guestsList[guestsListInTheDB.indexOf(guest)].lastname = guest.getLastName();
			guestsList[guestsListInTheDB.indexOf(guest)].sametable1 = guest.GetSameTable1();
			guestsList[guestsListInTheDB.indexOf(guest)].sametable2 = guest.GetSameTable2();
			guestsList[guestsListInTheDB.indexOf(guest)].sametable3 = guest.GetSameTable3();
			guestsList[guestsListInTheDB.indexOf(guest)].notsametable1 = guest.GetNotSameTable1();
			guestsList[guestsListInTheDB.indexOf(guest)].notsametable2 = guest.GetNotSameTable2();
			guestsList[guestsListInTheDB.indexOf(guest)].tablenumber = guest.getTableNumber();
		}

		// Shuffle to get a random organization
		Utility.shuffleArray(guestsList);

		// Assign table number depend on the size of the table to each guest
		AssignTableNumber(guestsList);

		// Create an initial chromosome
		for (int i = 0; i < initChromosome.length; i++)
			initChromosome[i] = i;

		// Create an initial best chromosome
		for (int i = 0; i < bestChromosomeSoFar.length; i++)
			bestChromosomeSoFar[i] = i;
		Utility.shuffleArray(bestChromosomeSoFar);

		// Add and shuffle entire population
		for (int i = 0; i < popSize; i++)
			population[i] = Utility.shuffleArray(initChromosome);
	}

	// Assign table number depend on the size of the table to each guest
	public static void AssignTableNumber(IndividualGuest[] guestsList)
	{
		totalNumberOfTable = 1; // First table number
		int seatNumber = 1; // First seat number
		for (int i = 0; i < guestsList.length; i++)
		{
			if (seatNumber < tableSize + 1)
			{
				guestsList[i].tablenumber = totalNumberOfTable; // Assign table number
				seatNumber++; // Increment seat number
			} else
			{
				seatNumber = 1; // Reset seat number
				totalNumberOfTable++; // Increment table number
				guestsList[i].tablenumber = totalNumberOfTable; // Assign table number
				seatNumber++; // Increment seat number
			}
		}
	}

	// Assign table number depend on the size of the table to each guest
	public static void AssignTableNumber(List<IndividualGuest> guestsList)
	{
		int tableNumber = 1; // First table number
		int seatNumber = 1; // First seat number
		for (IndividualGuest guest : guestsList)
		{
			if (seatNumber < tableSize + 1)
			{
				guest.tablenumber = tableNumber; // Assign table number
				seatNumber++; // Increment seat number
			} else
			{
				seatNumber = 1; // Reset seat number
				tableNumber++; // Increment table number
				guest.tablenumber = tableNumber; // Assign table number
				seatNumber++; // Increment seat number
			}
		}
	}

	// Get fitness
	private static int getFittestMember()
	{
		// let's assume the 1st element in the population is the fittest
		int fittest = 0;
		// Loop through individuals to find fittest
		for (int i = 1; i < popSize; i++)
		{
			// compare fitness of current member with fitness
			// of fittest member so far
			if (evalSolution(population[i]) > evalSolution(population[fittest]))
			{
				// current member becomes fittest so far if condition holds
				fittest = i;
			}
		}
		return fittest;// return index of the fittest
	}

	// Evolve population
	public static void evolvePopulation()
	{
		int[][] newPopulation = new int[popSize][chromLength];
		int a = -1, b = -1;
		int winner, loser;
		// Crossover population
		// Loop over the new population's size and create individuals from
		// current population
		for (int i = 0; i < popSize; i++)
		{
			// Select parents
			// pull 2 population members at random
			// a and b must be different
			do
			{
				a = selectMemberUsingRouletteWheel();
				b = selectMemberUsingRouletteWheel();
			} while (a == b);
			// have a fight, see who has best genes
			if (evalSolution(population[a]) > evalSolution(population[b]))
			{
				winner = a;
				loser = b;
			} else
			{
				winner = b;
				loser = a;
			}
			// Keep track of chromosome when it's crossed over and mutated
			int[] tempChromo = population[winner];

			// Possibly do some crossover depends on randomness
			if (Utility.randomDouble() < xoRate)
			{
				tempChromo = orderOneCrossover(population[winner], population[loser]);
			}

			// Add new tempChromo to new population
			newPopulation[i] = tempChromo;
		}

		// Mutate the new population a bit to add some new genetic material
		for (int i = 0; i < popSize; i++)
		{
			if (Utility.randomDouble() < mutRate)
			{
				newPopulation[i] = Mutation.insertMutation(newPopulation[i]);
			}
		}
		// replace population
		population = newPopulation;
	}

	// Select random member
	public static int selectMemberUsingRouletteWheel()
	{
		int totalSum = 0;
		for (int x = population.length - 1; x >= 0; x--)
		{
			totalSum += evalSolution(population[x]);
		}
		int rand = Utility.randomNumber(0, totalSum);
		int partialSum = 0;
		for (int x = population.length - 1; x >= 0; x--)
		{
			partialSum += evalSolution(population[x]);
			if (partialSum > rand)
			{
				return x;
			}
		}
		return -1;
	}

	// Run Algorithm
	public static void runGA(List<Guest> guests, int size, int maxNoTour)
	{
		guestsListInTheDB = guests;
		chromLength = guestsListInTheDB.size();
		tableSize = size;
		guestsList = new IndividualGuest[chromLength];
		population = new int[popSize][chromLength];
		initChromosome = new int[chromLength];
		bestChromosomeSoFar = new int[chromLength];

		// Initialize the population (randomly)
		initPopulation();
		System.out.println("Max fitness = " + expectedFitnessValue(guestsListInTheDB));

		// Start a tournament
		for (int tournamentNo = 0; tournamentNo < maxNoTour; tournamentNo++)
		{
			System.out.print("Current Generation: " + tournamentNo);
			int fID = getFittestMember();// index of fittest member in this population
			// Test to see if the fittest population member is the solution
			if (evalSolution(population[fID]) == expectedFitnessValue(guestsListInTheDB))
			{
				System.out.println(" --- fitness = " + evalSolution(population[fID]));
				display(tournamentNo, fID);
				solutionFound = true;
				storeResultToTheDB();	// Save the result table to the db
				break;
			} else
			{// keep track of best so far
				if (evalSolution(population[fID]) > evalSolution(bestChromosomeSoFar))
					bestChromosomeSoFar = population[fID].clone();
				System.out.println(" --- fitness = " + evalSolution(bestChromosomeSoFar));
				// and then evolve the population
				evolvePopulation();
			}
		}
	}

	// Store the result table number to the DB
	private static void storeResultToTheDB() {
		EntityTransaction guestTransaction = EM.getEM().getTransaction();
		guestTransaction.begin();
		for(int i = 0; i < guestsList.length; i++) {
			GuestDAO.findGuestById(guestsList[i].guest_id).setTableNumber(guestsList[i].tablenumber);
		}
		guestTransaction.commit();
	}
	
	// Display a result
	private static void display(int tournaments, int n)
	{
		// System.out.println("\r\n==============================\r\n");
		System.out.println("After " + tournaments + " generations, the best seating arrangement is:");
		for (int i = 0; i < chromLength; i++)
		{
			System.out.println(guestsList[i].firstname + " " + "(" + guestsList[i].tablenumber + ")");
		}
	}

	// Evaluate the solution/chromosome
	private static int evalSolution(int[] chromosome)
	{
		List<IndividualGuest> guestsList = asList(chromosome);
		int fitness = 0;
		for (IndividualGuest guest : guestsList)
		{
			// Check for same table request
			if (guest.sametable1 != 0)
			{
				if (Utility.FindGuestById(guest.sametable1, guestsList).tablenumber == guest.tablenumber)
					fitness++;
			}

			if (guest.sametable2 != 0)
				if (Utility.FindGuestById(guest.sametable2, guestsList).tablenumber == guest.tablenumber)
					fitness++;

			if (guest.sametable3 != 0)
				if (Utility.FindGuestById(guest.sametable3, guestsList).tablenumber == guest.tablenumber)
					fitness++;

			// Check for NOT same table request
			if (guest.notsametable1 != 0)
				if (Utility.FindGuestById(guest.notsametable1, guestsList).tablenumber != guest.tablenumber)
					fitness++;

			if (guest.notsametable2 != 0)
				if (Utility.FindGuestById(guest.notsametable2, guestsList).tablenumber != guest.tablenumber)
					fitness++;
		}
		return fitness;
	}

	/**
	 * This method takes an array of ints and returns a list
	 * of guests is used later for finding fitness
	 */
	public static List<IndividualGuest> asList(int[] ints)
	{
		List<IndividualGuest> list = new ArrayList<IndividualGuest>();
		for (int index = 0; index < ints.length; index++)
		{
			list.add(guestsList[ints[index]]);
		}
		AssignTableNumber(list);
		return list;
	}

	/**
	 * performs Order 1 crossover on two arrays of ints two arrays must be of
	 * same length!
	 */
	public static int[] orderOneCrossover(int[] parent1, int[] parent2)
	{
		int l = parent1.length;
		// get 2 random ints between 0 and size of array
		int r1 = Utility.randomNumber(0, l);
		int r2 = Utility.randomNumber(0, l);
		// to make sure the r1 < r2
		while (r1 >= r2)
		{
			r1 = Utility.randomNumber(0, l);
			r2 = Utility.randomNumber(0, l);
		}
		// create the child .. initial elements are -1
		int[] child = new int[l];
		for (int i = 0; i < l; i++)
		{
			child[i] = -1;
		}

		// copy elements between r1, r2 from parent1 into child
		for (int i = r1; i <= r2; i++)
		{
			child[i] = parent1[i];
		}

		// array to hold elements of parent1 which are not in child yet
		int[] y = new int[l - (r2 - r1) - 1];
		int j = 0;
		for (int i = 0; i < l; i++)
		{
			if (!Utility.arrayContains(child, parent1[i]))
			{
				y[j] = parent1[i];
				j++;
			}
		}
		// rotate parent2
		// number of places is the same as the number of elements after r2
		int[] copy = parent2.clone();
		Utility.rotate(copy, l - r2 - 1);

		// now order the elements in y according to their order in parent2
		int[] y1 = new int[l - (r2 - r1) - 1];
		j = 0;
		for (int i = 0; i < l; i++)
		{
			if (Utility.arrayContains(y, copy[i]))
			{
				y1[j] = copy[i];
				j++;
			}
		}
		// now copy the remaining elements (i.e. remaining in parent1) into
		// child
		// according to their order in parent2 .. starting after r2!
		j = 0;
		for (int i = 0; i < y1.length; i++)
		{
			int ci = (r2 + i + 1) % l;// current index
			child[ci] = y1[i];
		}
		return child;
	}

	// Set expected fitness value
	public static int expectedFitnessValue(List<Guest> GuestsList)
	{
		int expected = 0;
		for (Guest guest : GuestsList)
		{
			if (guest.GetSameTable1() != 0)
				expected++;
			if (guest.GetSameTable2() != 0)
				expected++;
			if (guest.GetSameTable3() != 0)
				expected++;
			if (guest.GetNotSameTable1() != 0)
				expected++;
			if (guest.GetNotSameTable2() != 0)
				expected++;
		}
		return expected;
	}
}
