package GeneticAlgorithm;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Methods to create or append line to given files
 * @author Bartosz Przydatek
 *
 */
public class SaveToFile {
	/**
	 * Create file for given test and type
	 * @param numOfCurrentTest Number of current test for file
	 * @param type Type of using algoritm. Need to new file name
	 */
	public static void createFile(int numOfCurrentTest, String type) {
		
		try {
			PrintWriter writer;
			writer = new PrintWriter(GeneticAlgorithm.fileConfigName + type + numOfCurrentTest+".dat");
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Append line with result to valid file
	 * @param line String with generation's result
	 * @param numberOfTest Number of test need to enter valid file
	 * @param type Type of use algorithm. Need to select file where line will append
	 */
	public static void saveGen(String line, int numberOfTest, String type) {
		try(
				FileWriter fw = new FileWriter(GeneticAlgorithm.fileConfigName + type + numberOfTest+".dat", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			    out.println(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
}
