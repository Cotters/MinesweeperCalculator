import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class RefinedMain {
	// Same as the main class except a padding around the field means better code in solver() func
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String[] field = {"", ""};
		boolean readingFromFile = true;
		
		if (!readingFromFile) {
			System.out.println("Enter the field size (height, width): ");
			if (in.hasNext()) {
				field = in.nextLine().trim().split("\\s+");
			}
			// Use height in for-loop
			int height = Integer.parseInt(field[0]) + 2;
			int length = Integer.parseInt(field[1]) + 2;
			
			makeField(height, length);
		} else {
			try {
				readFieldFromFile();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		in.close();
	}
	
	
	public static void makeField(int height, int length) {
		char[][] field = new char[height][length]; // boundary game
		char[][] inner = new char[height-2][length-2]; // actual game
		Scanner in = new Scanner(System.in);
		
		System.out.println("You must now enter each row in such format: ...* \n Where '.' is a tile and '*' is a bomb.");
		for (int i=1;i<field.length-2;i++) {
			System.out.println("Row #" + i + ":");
			if (in.hasNextLine())
				inner[i-1] = in.nextLine().toCharArray();
		}
		in.close();
		
		for (int i=0;i<height-2;i++) {
			for (int j=0;j<length-2;j++) {
				field[i+1][j+1] = inner[i][j]; // Middle ft. Bipolar Sunshine
			}
		}
		
		// Make boundary - seperate since field might not be a square.
		for (int i=0;i<length;i++) {
			field[0][i] = '+'; // top boundary
			field[height-1][i] = '+'; // bottom boundary
		}
		for (int i=0;i<height;i++) {
			field[i][0] = '+'; // left boundary
			field[i][length-1] = '+'; // right boundary
		}
		
		// Start solving if field is built
		if ((field[0][0] == '.') || (field[0][0] == '*')) // check something has been added
			solver(field);
		else
			System.out.println("Field error, please re-run program.");
	}
	
	public static void readFieldFromFile() throws FileNotFoundException {
		String path = "field.txt";
		String line;
		String[] dimension = {"", ""};
		int height = 0, length = 0;
		
		FileReader fr = new FileReader(path);
		BufferedReader buf = new BufferedReader(fr);
		
		try {
			if ((line = buf.readLine()) != null) {
				
				dimension = line.trim().split("\\s+");
				
				height = Integer.parseInt(dimension[0]) + 2; // boundary
				length = Integer.parseInt(dimension[1]) + 2; // ^
			}
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (height != 0 && length != 0) {
			char[][] field = new char[height][length];
			char[][] inner = new char[height-2][length-2];
			
			int rowCount = 0;
			
			try {
				while ((line = buf.readLine()) != null) {
					inner[rowCount] = line.toCharArray();
					rowCount += 1;
				}
				buf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (int i=0;i<height-2;i++) {
				for (int j=0;j<length-2;j++) {
					field[i+1][j+1] = inner[i][j]; 
				}
			}
			
			
			// Make boundary - seperate since field might not be a square.
			for (int i=0;i<length;i++) {
				field[0][i] = '+'; // top boundary
				field[height-1][i] = '+'; // bottom boundary
			}
			for (int i=0;i<height;i++) {
				field[i][0] = '+'; // left boundary
				field[i][length-1] = '+'; // right boundary
			}
			
			
			// If field is ok - solve
			if ((field[1][1] == '.') || (field[1][1] == '*')) { // check something has been added
				printField(field);
				solver(field);
			}
			else
				System.out.println("Failed reading from file...");
		
		} else {
			System.out.println("Problems getting the field size...");
		}
		
	}
	
	public static void printField(char[][] field) {
		
		for (int i=0;i<field.length;i++) {
			for (int j=0;j<field[i].length;j++) {
				System.out.print(field[i][j]);
			}
			System.out.println();
		}
	}
	
	public static void solver(char[][] field) {
		int height = field.length;
		int length = field[0].length; // at this point array must be populated
		int count = 0;
		
		System.out.println("\nSolving...\n");
		for (int i=0;i<height;i++) { // i is row number
			for (int j=0;j<length;j++) { // j is column number
				if (i-1>0 && i+1<height && j-1>=0 && j+1<length) { // eliminates boundary tiles
					// check all j's for row i-1
					for (int k=j-1;k<=j+1;k++) {
						if (field[i-1][k] == '*')
							count += 1;
						// check all j's for row i
						if (field[i][k] == '*')
							count+=1;
						// check all j's for row i+1
						if (field[i+1][k] == '*')
							count+=1;
					}
				}
				if (!(field[i][j] == '*')) // Keep bombs in place
					field[i][j] = Character.forDigit(count, 10); // set tile to bomb count
				count = 0; // reset bomb count
			} // j for-loop
		} // i for-loop
		printField(field);
	} // function
	
}
