package GeneticAlgorithm;

import java.util.List;
import java.util.Random;

public final class Utility
{

	/**
	 * returns a random int value within a given range min inclusive .. max not
	 * inclusive
	 */
	public static int randomNumber(int min, int max)
	{
		Random r = new Random();
		double d = min + r.nextDouble() * (max - min);
		return (int) d;
	}

	/**
	 * this method returns a random number n such that 0.0 <= n <= 1.0
	 */
	static double randomDouble()
	{
		Random r = new Random();
		return r.nextInt(1000) / 1000.0;
	}

	/**
	 * returns the index of an int element in an int array
	 *
	 */
	public static int indexOfArrayElement(int[] arr, int e)
	{
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i] == e)
				return i;
		}
		return -1;
	}

	/**
	 * rotates (right) an IndividualGuest array a number of places
	 *
	 */
	public static void rotate(IndividualGuest[] arr, int order)
	{
		int offset = arr.length - order % arr.length;
		if (offset > 0)
		{
			IndividualGuest[] copy = arr.clone();
			for (int i = 0; i < arr.length; ++i)
			{
				int j = (i + offset) % arr.length;
				arr[i] = copy[j];
			}
		}
	}

	/**
	 * rotates (right) an int array a number of places
	 *
	 */
	public static void rotate(int[] arr, int order)
	{
		int offset = arr.length - order % arr.length;
		if (offset > 0)
		{
			int[] copy = arr.clone();
			for (int i = 0; i < arr.length; ++i)
			{
				int j = (i + offset) % arr.length;
				arr[i] = copy[j];
			}
		}
	}

	/**
	 * checks if an IndividualGuest array contains an IndividualGuest element
	 *
	 */
	public static boolean arrayContains(IndividualGuest[] arr, IndividualGuest e)
	{
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i] == e)
				return true;
		}
		return false;
	}

	/**
	 * checks if an int array contains an int element
	 */
	public static boolean arrayContains(int[] arr, int e)
	{
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i] == e)
				return true;
		}
		return false;
	}

	/**
	 * This method randomises the order of array elements i.e. shuffles array
	 * elements Implements Fisher–Yates shuffle
	 */
	static IndividualGuest[] shuffleArray(IndividualGuest[] array)
	{
		IndividualGuest[] copy = array.clone();
		Random rnd = new Random();
		for (int i = copy.length - 1; i > 0; i--)
		{
			int index = rnd.nextInt(i + 1);
			// Simple swap
			IndividualGuest a = copy[index];
			copy[index] = copy[i];
			copy[i] = a;
		}
		return copy;
	}

	/**
	 * This method randomises the order of array elements i.e. shuffles array
	 * elements Implements Fisher–Yates shuffle
	 */
	static int[] shuffleArray(int[] array)
	{
		int[] copy = array.clone();
		Random rnd = new Random();
		for (int i = copy.length - 1; i > 0; i--)
		{
			int index = rnd.nextInt(i + 1);
			// Simple swap
			int a = copy[index];
			copy[index] = copy[i];
			copy[i] = a;
		}
		return copy;
	}

	// Find guest by id
	public static IndividualGuest FindGuestById(int id, IndividualGuest[] chromosome)
	{
		for (int i = 0; i < chromosome.length; i++)
		{
			if (chromosome[i].guest_id == id)
			{
				return chromosome[i];
			}
		}
		return null;
	}

	// Find guest by id
	public static IndividualGuest FindGuestById(int id, List<IndividualGuest> guestsList)
	{
		for (IndividualGuest guest : guestsList)
		{
			if (guest.guest_id == id)
			{
				return guest;
			}
		}
		return null;
	}

}
