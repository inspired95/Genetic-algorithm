package GeneticAlgorithm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Single subject
 * @author Bartosz Przydatek
 */

public class Subject {
	ArrayList<Integer> genotype = null;
	int rating;
	
	/**
	 * Create new 'clear' subject
	 */
	public Subject() {
		genotype = new ArrayList<Integer>();
		rating = 0;
	}
	
	/**
	 * Create new subject with new genotype which contain shuffled ArrayList [1..N], next calculate his rating
	 * @param  N  size of genotype and  maximum value single gene
	 */
	public Subject(int N) {
		ArrayList<Integer> example = new ArrayList<Integer>();
		for(int i = 1; i <=N; i++ ) {
			example.add(i);
		}
		Collections.shuffle(example);
		genotype = example;
		calculate();
	}
	
	/**
	 * Calculate rating of subject and set this
	 */
	public void calculate(){
		int result = 0;
		for (int i=0; i<genotype.size(); i++){
			for (int j=0; j<genotype.size(); j++){
				result += (Config.distMatrix[i][j]*Config.flowMatrix[genotype.get(i)-1][genotype.get(j)-1]);
			}
		}
		rating = result;
		}
	
	/**
	 * Execute mutation procedure
	 */
	public void mutation() {
		for(int i = 0; i < genotype.size();i++) {
			if(GeneticAlgorithm.generator.nextDouble() < GeneticAlgorithm.Pm) {
				int posToSwap1 = GeneticAlgorithm.generator.nextInt(genotype.size());
				int posToSwap2 = GeneticAlgorithm.generator.nextInt(genotype.size());
				int gen1 = genotype.get(posToSwap1);
				int gen2 = genotype.get(posToSwap2);
				genotype.set(posToSwap1, gen2);
				genotype.set(posToSwap2, gen1);
			}
		}
	}
	
	/**
	 * Execute crossover of subject with other subject
	 * @param parent2 other subject to crossover
	 * @return Array with two subjects where each emerging from parents
	 */
	public Subject[] crossover(Subject parent2) {
		Subject child1 = new Subject();
		Subject child2 = new Subject();
		int pos = GeneticAlgorithm.generator.nextInt(genotype.size());  
		for(int i=0; i < pos; i++) {
			child1.genotype.add(genotype.get(i));
			child2.genotype.add(parent2.genotype.get(i));
		}
		for(int i = pos; i < genotype.size(); i++ ) {
			child1.genotype.add(parent2.genotype.get(i));
			child2.genotype.add(genotype.get(i));
		}
		Subject[] childs = new Subject[2];
		childs[0] = child1;
		childs[1] = child2;
		return childs;
	}
	
	/**
	 * Check correctness of genotype. If this's wrong execute repair.
	 */
	public void checkAndRepair() {
		ArrayList<Integer> kit = new ArrayList<Integer>();
		for(int i = 1; i <= genotype.size(); i++ ) {
			kit.add(i);
		}
		ArrayList<Integer> newGenotyp = new ArrayList<Integer>();
		for(int i = 0; i < genotype.size(); i++) {
			if(kit.contains(genotype.get(i))) {
				kit.remove(kit.indexOf(genotype.get(i)));
				newGenotyp.add(genotype.get(i));
			}else {
				newGenotyp.add(0);
			}
		}
		while(!kit.isEmpty()) {
			for(int i = 0; i < newGenotyp.size(); i++) {
				if(newGenotyp.get(i).equals(0) ) {
					genotype.set(i, kit.get(0));
					kit.remove(0);
				}
			}
		}
	}
	
	/**
	 * Create string with genotype and rating
	 * @return String with genotype and rating
	 */
	public String toString() {
		String subjectString = null;
		subjectString = Arrays.toString((genotype.toArray())) + " " + rating + "\n";
		return subjectString;
	}
}
