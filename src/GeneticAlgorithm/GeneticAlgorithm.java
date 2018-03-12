package GeneticAlgorithm;

import java.util.Collections;
import java.util.Random;

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
		
		GA("had20", 100, 100, 0.02, 0.7, 5, 1, "Roulette");

		timeOut = System.currentTimeMillis();
		System.out.println("Execution time : " + (timeOut-timeIn)/1000 + " s");
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
	 * Execute procedure of genetic algorithm for specify parameters
	 * Save result of each generation as (the best subject rating; average value of ratings of single generation;the worst subject rating)
	 * @param fileConfigName File name with configuration
	 * @param pop_size Size of generations
	 * @param gen Amount of generations to generate
	 * @param Pm Mutation probability
	 * @param Px Crossover probability
	 * @param Tour Amount of players of each tournament
	 * @param numberOfTests Amount of tests to execute
	 * @param selectionType String like "Tour" to use tournament selection method or "Rulette" to use rulette selection method
	 */
	public static void GA(String fileConfigName, int pop_size, int gen, double Pm, double Px, int Tour, int numberOfTests, String selectionType) {
		GeneticAlgorithm.fileConfigName = fileConfigName;
		GeneticAlgorithm.pop_size = pop_size;
		GeneticAlgorithm.gen = gen;
		GeneticAlgorithm.Pm = Pm;
		GeneticAlgorithm.Px = Px;
		GeneticAlgorithm.Tour = Tour;
		System.out.println("Executing " + numberOfTests + " tests of GA for " + fileConfigName + ".dat");

		
		for(int test = 1; test <= numberOfTests; test++) {
			oldGen = null;
			newGen = null;
			initialise();
			SaveToFile.createFile(test, "Genetic");
			int bestRating = (int) Math.pow(oldGen.getBestSubject().rating, 1);
			int avgRating = (int)Math.pow(oldGen.getAvg(), 1);
			int worstRating = (int)Math.pow(oldGen.getWorstSubject().rating, 1) ;
			SaveToFile.saveGen(1 + ";" + bestRating + ";" + avgRating + ";" + worstRating, test, "Genetic");
			for(int popAmount = 2; popAmount<=gen; popAmount++) {
				newGen = new Generation();
				while(newGen.subjects.size() < pop_size) {
					Subject parent1 = new Subject();
					Subject parent2 = new Subject();
					if(selectionType.equals("Tour")) {
						parent1 = oldGen.tour(Tour);
						parent2 = oldGen.tour(Tour);
					}else if(selectionType.equals("Roulette")) {
						parent1 = oldGen.roulette();
						parent2 = oldGen.roulette();
					}

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
				bestRating = (int) Math.pow(newGen.getBestSubject().rating, 1);
				avgRating = (int)Math.pow(newGen.getAvg(), 1);
				worstRating = (int)Math.pow(newGen.getWorstSubject().rating, 1) ;
				SaveToFile.saveGen(popAmount + ";" + bestRating + ";" + avgRating + ";" + worstRating, test, "Genetic");
				oldGen = newGen;
			}
		}
	}
	
	/**
	 * Execute greedy algorithm for configuration file and save each result to file
	 * @param fileConfigName File with configuration "had__.dat"
	 * @param amountOfTests Amount of tests to execute
	 */
	public static void greedy(String fileConfigName, int amountOfTests){
		System.out.println("Executing " + amountOfTests + " tests of greedy algorithm for " + fileConfigName + ".dat");
		GeneticAlgorithm.fileConfigName = fileConfigName;
		SaveToFile.createFile(1, "Greedy");
		for(int test = 1; test <= amountOfTests; test++) {
			Subject subject = new Subject();
	        for(int i = 1; i <= Config.n; i++) {
	            subject.genotype.add(i);
	        }
	        Collections.shuffle(subject.genotype);
	        subject.calculate();
	       
	        Subject tmpSubject = new Subject();
	       
	        for(int i = 0; i<subject.genotype.size()-1; i++) {
	            for(int j=0; j < subject.genotype.size(); j++) {
	                tmpSubject.genotype.add(subject.genotype.get(j));
	            }
	           
	            Collections.swap(tmpSubject.genotype, i, i+1);
	            tmpSubject.calculate();
	 
	           
	            if(tmpSubject.rating < subject.rating) {
	                subject.genotype.clear();
	                for(int j = 0; j< tmpSubject.genotype.size(); j++) {
	                    subject.genotype.add(tmpSubject.genotype.get(j));
	                }
	                subject.rating = tmpSubject.rating;
	                i=0;
	            }
	            tmpSubject.genotype.clear();
	        }
	        SaveToFile.saveGen(""+subject.rating, 1, "Greedy");
		}
        
    }
	
	/**
	 * Generate random population for configuration file and save result as "average value and standard deviation"
	 * @param fileConfigName File with configuration "had__.dat"
	 * @param pop_size Population size to generate
	 */
	public static void randomInit(String fileConfigName, int pop_size) {
		System.out.println("Executing random initialization for " + fileConfigName + ".dat file" );
		GeneticAlgorithm.fileConfigName = fileConfigName;
		initialise();
		SaveToFile.createFile(1, "Random");
		Generation generation = new Generation(pop_size, Config.n);
		SaveToFile.saveGen(""+generation.getAvg() + "   " + generation.getSD() , 1, "Random");
	}
}