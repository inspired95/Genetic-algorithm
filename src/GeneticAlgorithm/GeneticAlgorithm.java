package GeneticAlgorithm;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Genetic algorithm 
 * @author Bartosz Przydatek
 *
 */
public class GeneticAlgorithm {
	static long timeIn;
	static long timeOut;
	
	//default configuration for tests
	static String fileConfigName = "had12";
	static int pop_size = 100;
	static int gen = 100;
	static double Pm = 0.01;
	static double Px = 0.7;
	static int Tour = 5;
	
	//store old generation (possible parents)
	static Generation oldGen = null;
	
	//store new generation (childs)
	static Generation newGen = null;
	
	static Random generator = new Random();
	
	/**
	 * Main method
	 * @param args Program works without extra parameters
	 */
	public static void main(String[] args) {
		timeIn = System.currentTimeMillis();
		System.out.println("Starting the program");
		
		
		execute("had12", 100, 100, 0.01, 0.7, 5, 5);
		execute("had14", 100, 100, 0.01, 0.7, 5, 4);
		execute("had16", 100, 100, 0.01, 0.7, 5, 6);
		execute("had18", 100, 100, 0.01, 0.7, 5, 3);
		execute("had20", 100, 100, 0.01, 0.7, 5, 5);
		
		timeOut = System.currentTimeMillis();
		System.out.println("Execution time : " + (timeOut-timeIn) + " ms");
	}
	
	/**
	 * Load valid file with configuration and generate first generation
	 */
	public static void initialise() {
		@SuppressWarnings("unused")
		Config config = new Config();
		oldGen = new Generation(pop_size, Config.n);
	}
	
	/**
	 * Create file for new test
	 * @param numOfCurrentTest Number of current test for file
	 */
	public static void createFile(int numOfCurrentTest) {
		
		try {
			PrintWriter writer;
			writer = new PrintWriter(fileConfigName + "RESULT"+numOfCurrentTest+".dat");
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Append line with result of generation to valid file
	 * @param line String with generation's result
	 * @param numberOfTest Number of test need to enter valid file
	 */
	public static void saveGen(String line, int numberOfTest) {
		try(
				FileWriter fw = new FileWriter(fileConfigName + "RESULT"+numberOfTest+".dat", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			    out.println(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	
	/**
	 * Execute procedure of genetic algorithm for specify parameters
	 * Save result of each generation as (the best subject rating; average value of ratings of single generation;the worst subject rating)
	 * @param fileConfigName File name with configuration
	 * @param pop_size Size of generations
	 * @param gen Amount of generations to generate
	 * @param Pm Mutation probability
	 * @param Px Crossover probability
	 * @param Tour Amount of players of each tournament
	 * @param numberOfTests Amount of tests to execute
	 */
	public static void execute(String fileConfigName, int pop_size, int gen, double Pm, Double Px, int Tour, int numberOfTests) {
		GeneticAlgorithm.fileConfigName = fileConfigName;
		GeneticAlgorithm.pop_size = pop_size;
		GeneticAlgorithm.gen = gen;
		GeneticAlgorithm.Pm = Pm;
		GeneticAlgorithm.Px = Px;
		GeneticAlgorithm.Tour = Tour;
		
		
		for(int test = 1; test <= numberOfTests; test++) {
			System.out.println("Executing " + test + ". test for configuration " + fileConfigName);
			oldGen = null;
			newGen = null;
			initialise();
			createFile(test);
			int bestRating = (int) Math.pow(oldGen.getBestSubject().rating, 2);
			int avgRating = (int)Math.pow(oldGen.getAvg(), 2);
			int worstRating = (int)Math.pow(oldGen.getWorstSubject().rating, 2) ;
			saveGen(1 + ";" + bestRating + ";" + avgRating + ";" + worstRating, test);
			for(int popAmount = 2; popAmount<=gen; popAmount++) {
				newGen = new Generation();
				while(newGen.subjects.size() < pop_size) {
					Subject parent1 = oldGen.tour(Tour);
					Subject parent2 = oldGen.tour(Tour);
					if(generator.nextDouble() < Px) {
						Subject[] childs = parent1.crossover(parent2);
						Subject child1 = childs[0];
						Subject child2 = childs[1];
						
						child1.checkAndRepair();
						child2.checkAndRepair();
						
						child1.mutation();
						child2.mutation();
						
						child1.calculate();
						child2.calculate();
						
						newGen.add(child1);
						newGen.add(child2);
					}else{
						parent1.mutation();
						parent2.mutation();
						
						parent1.calculate();
						parent2.calculate();					
						
						newGen.add(parent1);
						newGen.add(parent2);
					}
				}
				bestRating = (int) Math.pow(newGen.getBestSubject().rating, 2);
				avgRating = (int)Math.pow(newGen.getAvg(), 2);
				worstRating = (int)Math.pow(newGen.getWorstSubject().rating, 2) ;
				saveGen(popAmount + ";" + bestRating + ";" + avgRating + ";" + worstRating, test);
				oldGen = newGen;
			}
		}
	}
	
	/*
	public void nongeneticAlgorithm(String fileConfigName, int pop_size, int gen, double Pm, Double Px){
		System.out.println("Executing non-genetic algorithm for configuration " + fileConfigName);
		oldGen = null;
		GeneticAlgorithm.fileConfigName = fileConfigName;
		GeneticAlgorithm.pop_size = pop_size;
		GeneticAlgorithm.gen = gen;
		GeneticAlgorithm.Pm = Pm;
		GeneticAlgorithm.Px = Px;
		initialise();
		createFile(1);
		int bestRating = oldGen.getBestSubject().rating;
		int avgRating = oldGen.getAvg();
		int worstRating = oldGen.getWorstSubject().rating;
		
	}
	*/
}