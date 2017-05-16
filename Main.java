import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Main {
	// Just calculates the adjacent bomb count of each square
	// (This only works if the bomb locations are known.)
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String[] field = {"", ""};
		boolean readingFromFile = false;
		
		if (!readingFromFile) {
			System.out.println("Enter the field size (height, width): ");
			if (in.hasNext()) {
				field = in.nextLine().trim().split("\\s+");
			}
			// Use height in for-loop
			int height = Integer.parseInt(field[0]);
			int length = Integer.parseInt(field[1]);
			
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
		char[][] field = new char[height][length];
		Scanner in = new Scanner(System.in);
		
		System.out.println("You must now enter each row in such format: ...* \n Where '.' is a tile and '*' is a bomb.");
		for (int i=1;i<=field.length;i++) {
			System.out.println("Row #" + i + ":");
			if (in.hasNextLine())
				field[i-1] = in.nextLine().toCharArray();
		}
		in.close();
		solver(field);
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
				
				height = Integer.parseInt(dimension[0]);
				length = Integer.parseInt(dimension[1]);
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
			
			int rowCount = 0;
			
			try {
				while ((line = buf.readLine()) != null) {
					field[rowCount] = line.toCharArray();
					rowCount += 1;
				}
				buf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if ((field[0][0] == '.') || (field[0][0] == '*')) { // check something has been added
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
		int length = field[0].length;
		int count = 0;
		
		System.out.println("\nSolving...\n");
		for (int i=0;i<height;i++) { // i is row number
			for (int j=0;j<length;j++) { // j is column number
				if (i-1>=0 && i+1<height && j-1>=0 && j+1<length) {
					// not on perimeter - check all 8 surrounding tiles
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
					} // Verified
					
				} else if(i-1<0 && j-1<0) { // top left tile
					if (field[i][j+1] == '*')
						count+=1;
					if (field[i+1][j] == '*')
						count+=1;
					if (field[i+1][j+1] == '*')
						count+=1;
				} else if (i-1<0 && j+1>=length) { //top right tile
					if (field[i][j-1] == '*')
						count+=1;
					if (field[i+1][j] == '*')
						count+=1;
					if (field[i+1][j-1] == '*')
						count+=1;
				} else if (i+1>=height && j-1<0) { // bottom left tile
					if (field[i-1][j] == '*')
						count+=1;
					if (field[i-1][j+1] == '*')
						count+=1;
					if (field[i][j+1] == '*')
						count+=1;
				} else if (i+1>=height && j+1>=length) { // bottom right tile
					if (field[i-1][j] == '*')
						count+=1;
					if (field[i-1][j-1] == '*')
						count+=1;
					if (field[i][j-1] == '*')
						count+=1;
				} else if(i-1<0) { // top middle tiles
					for (int k=j-1;k<=j+1;k++) {
						// check all j's on row i
						if (field[i][k] == '*')
							count += 1;
						// check all j's for row i+1 (below)
						if (field[i+1][k] == '*')
							count+=1;
					}
				} else if (i+1>=height) { // bottom middle tiles
					for (int k=j-1;k<=j+1;k++) {
						// check all j's on row i
						if (field[i][k] == '*')
							count += 1;
						// check all j's for row i-1 (above)
						if (field[i-1][k] == '*')
							count+=1;
					}
				} else if (j-1<0) { // left middle tiles
					for (int k=i-1;k<=i+1;k++) {
						// check all i's in column j
						if (field[k][j] == '*')
							count += 1;
						// check all i's for column j+1
						if (field[k][j+1] == '*')
							count+=1;
					}
				} else if (j+1>=length) { // right middle tiles
					for (int k=i-1;k<=i+1;k++) {
						// check all i's in column j
						if (field[k][j] == '*')
							count += 1;
						// check all i's for column j-1
						if (field[k][j-1] == '*')
							count+=1;
					}
				} // end of tile locations
				if (!(field[i][j] == '*')) // Keep bombs in place
					field[i][j] = Character.forDigit(count, 10); // set tile to bomb count
				count = 0; // reset bomb count
			} // j for-loop
		} // i for-loop
		printField(field);
	} // function
	
}
