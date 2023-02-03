package edu.cuny.csi.csc330.lab6;

import java.util.Arrays;
import java.util.Date;
import java.io.*;

import edu.cuny.csi.csc330.util.Randomizer;

public class QuickPicker{
	
	// constants  specific to current game - BUT NOT ALL GAMES 
//	public final static int DEFAULT_GAME_COUNT = 1; 
//	private final static String GAME_NAME = "Lotto"; 
//	private final static int SELECTION_POOL_SIZE = 59; 
//	private final static int SELECTION_COUNT = 6;
//	private final static String DEFAULT_VENDOR = "S.I. Corner Deli";
	public long oddsOfWinning = 0;
	public String gameName;
	public int pool1Size;
	public int pool1Count;
	public int pool2Size;
	public int pool2Count;
	public int games;
	public String vendor;

//	public QuickPicker() {
//		init(GAME_NAME, SELECTION_POOL_SIZE, SELECTION_COUNT, DEFAULT_GAME_COUNT, DEFAULT_VENDOR);
//	}
	
	public QuickPicker(String gameName, int pool1Size, int pool1Count, int pool2Size, int pool2Count, int games, String vendor) {
		init(gameName, pool1Size, pool1Count, pool2Size, pool2Count, games, vendor); 
	}
  
	private void init(String gameName, int pool1Size, int pool1Count, int pool2Size, int pool2Count, int games, String vendor) {
		this.gameName = gameName;
		this.pool1Size = pool1Size;
		this.pool1Count = pool1Count;
		this.pool2Size = pool2Size;
		this.pool2Count = pool2Count;
		this.games = games;
		this.vendor = vendor;
	}
	
	//to check if the random numbers are duplicate
	public boolean isDuplicate(int temp, int count, int []nums) {
		for (int i = 0; i < count; i++) {
			if (temp == nums[i]) 
				return true;
		}
		return false;	
	}
	
	// display the 6 unique numbers if users play only 1 time
	public void displayLine() {
//		Randomizer randomizer = new Randomizer();
		int []randomNums = new int[this.pool1Count];
		// generate 6 unique numbers and put them into the array randomNums
		for (int i = 0; i < this.pool1Count; i++) {
			int num = Randomizer.generateInt(1, this.pool1Size);	
			while (isDuplicate(num, i, randomNums)) { 
				// if the number is duplicated, generate more times to get the different number
				num = Randomizer.generateInt(1, this.pool1Size);
			}
			randomNums[i] = num;
		}

		int []randomNums2 = new int[this.pool2Count];
		for (int i = 0; i < this.pool2Count; i++) {
			int num2 = Randomizer.generateInt(1, this.pool2Size);	
			while (isDuplicate(num2, i, randomNums2)) {
				// if the number is duplicated, generate more times to get the different number
				num2 = Randomizer.generateInt(1, this.pool2Size);
			}
			randomNums2[i] = num2;
		}

		//sort this array in ascending order
		Arrays.sort(randomNums);
		
		//display the sorted array
		for ( int i = 0; i < this.pool1Count; i++) {
			System.out.printf("%02d\t", randomNums[i]);
		}

		//sort this array in ascending order
		Arrays.sort(randomNums2);
		
		//display the sorted array
		if (this.pool2Count > 0) {
			System.out.printf("(( ");
			for ( int i = 0; i < this.pool2Count; i++) {
				System.out.printf("%02d ", randomNums2[i]);
			}
			System.out.printf("))");
		}

		System.out.println("\n");
	}
	
	//display the numbers multiply times if users want to play more times
	public void displayBody() {
		for (int i = 0; i < this.games; i++) {
			System.out.printf("(" + "%01d" + ")" + "\t", i+1);
			displayLine();
		}
	}
	
	//display the heading
	protected void displayHeading() {
		Date today = new Date(); 
		System.out.println("----------------------------------------------------------------------------------");
		System.out.println("------------------------------------" + this.gameName + "--------------------------------------");
		//display the time and date
		System.out.println(today);
		System.out.println();
		
	}
	
	//display the footer
	protected void displayFooter() {
		 System.out.println("--------------------" + this.vendor + "---------------------------");
	}
	
	// calculate the factory, will be used for calculating the odds of winning
	public long fact(int beginning, int ending) {
		long fac = 1;
		while (beginning <= ending) {
			fac = fac * beginning;
			beginning++;
		}
		return fac;
	}

	// calculate the odds of winning
	// to calculate the odds is to calculate the combination of C(pool1Count, pool1Size) * C(pool2Count, pool2Size)
	// for example, to calculate C(6,59) which is factorial (1, 59) divided by (factorial (1, 6) multiply factorial (7, 59))
	// it can be wrote in this shorter way: factorial ((59 - 6 + 1), 59) divided by factorial (1, 6);
	// we can adjust the calculation by passing in different parameters
	public long calculateOdds() {
		long oddsOfWinning1 = fact((this.pool1Size - this.pool1Count + 1), this.pool1Size) / fact(1, this.pool1Count);
		long oddsOfWinning2 = fact((this.pool2Size - this.pool2Count + 1), this.pool2Size) / fact(1, this.pool2Count);
		oddsOfWinning = oddsOfWinning1 * oddsOfWinning2;
		//if try the game multiple times, the odds will be divided by the same times
		oddsOfWinning = oddsOfWinning / this.games;
		System.out.printf("With %d time(s) playing, odds of Winning: 1 in %,d\n", this.games, oddsOfWinning);
		System.out.println();
		return oddsOfWinning;
	}
	
	//display the whole game
	public void displayTicket() {	
		displayHeading(); 
		displayBody();
		oddsOfWinning = calculateOdds();
		displayFooter(); 	
		return;
	}
  
	public static void main(String[] args) throws QuickPickerException {
		// requires game name to load the configuration file
		String fileName = "";

		// takes an optional command line parameter specifying number of QP games to be generated
		// By default, generate 1
		int numberOfGames  = 1; 

		try {
			if(args.length > 0) {
				fileName = args[0];
	
				if (args.length > 1) {				
					numberOfGames = Integer.parseInt(args[1]);
				}
			} else {
				throw new Exception("Game name is missing!");
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}
		
		File file = new File("/Users/christellegan/eclipse-workspace/CSC330/src/edu/cuny/csi/csc330/lab6/" + fileName + ".properties");
	 
		String gameName = "";
		int pool1Count = 0;
		int pool1Size = 0;
		int pool2Count = 0;
		int pool2Size = 0;
		String vendor = "";
		
		try {
	        BufferedReader br = new BufferedReader(new FileReader(file));
	        String str;
	        while ((str = br.readLine()) != null) {
	        	if (str.charAt(0) != '#') {
	        		if (!str.contains("=")) {
	        			throw new Exception("A required property is missing from the specified  .properties file.");
	        		}
	        		String[] keyValue = str.split("=", 2);
	        		String key = keyValue [0];
	        		String value= keyValue [1];
	        		
	        		switch (key) {
	        		case "GameName":
	        			gameName = value;
	        			break;
	        		case "Pool1":
	        			String[] pool1 = value.split("/", 2);
	        			pool1Count = Integer.parseInt(pool1[0]);
	        			pool1Size = Integer.parseInt(pool1[1]);
	        			break;
	        		case "Pool2":
	        			String[] pool2 = value.split("/", 2);
	        			pool2Count = Integer.parseInt(pool2[0]);
	        			pool2Size = Integer.parseInt(pool2[1]);
	        			break;
	        		case "Vendor":
	        			vendor = value;
	        			break;
	        		}
	        	}
	        }

			QuickPicker pool1 = new QuickPicker(gameName, pool1Size, pool1Count, pool2Size, pool2Count, numberOfGames, vendor);
			 
			pool1.displayTicket();	

			br.close();
		} catch (FileNotFoundException ex) {
	        throw new QuickPickerException(fileName);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}
	}
}
