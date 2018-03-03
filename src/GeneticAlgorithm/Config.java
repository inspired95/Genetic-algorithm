package GeneticAlgorithm;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Contain digit N, matrixes[NxN] of distance and flow. All data is load from file had__.dat
 * @author Bartosz Przydatek
 *
 */

public class Config {
	static int n = 0;
	static int[][] distMatrix = null;
	static int[][] flowMatrix = null;
	
	/**
	 * Constructor initializes procedure of reading file
	 */
	public Config() {
		ReadConfig();
	}
	
	/**
	 * Loading data from file which name is give in main class
	 */
	public void ReadConfig() {
		try {
			Scanner reading = new Scanner(new File(GeneticAlgorithm.fileConfigName + ".dat"));
			n = reading.nextInt();
			//System.out.println(n);
			distMatrix = new int[n][n];
			for(int i=0; i<n;i++) {
				for(int j=0; j<n;j++) {
					distMatrix[i][j] = reading.nextInt();
				}
			}
			flowMatrix = new int[n][n];
			for(int i=0; i<n;i++) {
				for(int j=0; j<n;j++) {
					flowMatrix[i][j] = reading.nextInt();
				}
			}
			reading.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
