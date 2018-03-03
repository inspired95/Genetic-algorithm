package GeneticAlgorithm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * ArrayList of subjects.
 * @author Bartosz Przydatek
 *
 */
public class Generation {
	ArrayList<Subject> subjects;
	
	/**
	 * Create new 'clear' generation 
	 */
	public Generation() {
		subjects = new ArrayList<Subject>();
	}
	
	/**
	 * Generate new subjects based on given parameters
	 * @param pop_size Size of new generation to create
	 * @param N Maximum size and maximum value of each gene in each subject
	 */
	public Generation(int pop_size, int N) {
		subjects = new ArrayList<Subject>();
		
		
		for(int j = 0; j < pop_size; j++) {
			Subject s = new Subject(N);
			subjects.add(s);
		}
	}
	
	/**
	 * Add subject to generation
	 * @param subject Subject which will be add to generation
	 */
	public void add(Subject subject) {
		subjects.add(subject);
	}
	
	/**
	 * Execute tournament for selected amount of players
	 * @param numOfPlayers Numbers of players elected from generation
	 * @return Best player from tournament. Player with the smallest rating.
	 */
	public Subject tour(int numOfPlayers) {
		ArrayList<Subject> players = new ArrayList<Subject>();

		Random generator = new Random();
		while(players.size() < numOfPlayers) {
			players.add(subjects.get(generator.nextInt(subjects.size())));
			
		}
	
		Subject bestPlayer = players.get(0);
		for(int i=0;i<players.size();i++) {
			Subject candidate = players.get(i);
			if(candidate.rating < bestPlayer.rating) {
				bestPlayer = candidate;
			}
		}
		return bestPlayer;
	}
	
	/**
	 * Select the worst subject of generation
	 * @return The worst subject. Subject with the biggest rating.
	 */
	public Subject getWorstSubject() {
		
		Subject someSubject = subjects.get(0);
		
		for(int i=0;i<subjects.size();i++) {
			Subject candidate = subjects.get(i);
			if(candidate.rating > someSubject.rating) {
				someSubject = candidate;
			}
		}
		return someSubject;
	}
	
	/**
	 * Select the best subject of generation
	 * @return The best subject. Subject with the smallest rating.
	 */
	public Subject getBestSubject() {
		
		Subject someSubject = subjects.get(0);
		
		for(int i=0;i<subjects.size();i++) {
			Subject candidate = subjects.get(i);
			if(candidate.rating < someSubject.rating) {
				someSubject = candidate;
			}
		}
		return someSubject;
	}
	
	/**
	 * Calculate average value subjects of generation.
	 * @return Average value subjects of generation
	 */
	public int getAvg() {
		int sumOfRatings = 0;
		for(int i = 0; i < subjects.size(); i++) {
			sumOfRatings += subjects.get(i).rating;
		}
		sumOfRatings = (int) sumOfRatings/subjects.size();
		return sumOfRatings;
	}
	
	/**
	 * Show genotypes of subjects from generation
	 */
	public void showGen() {
		for(int i = 0; i < subjects.size(); i++) {
			System.out.println(Arrays.toString(subjects.get(i).genotype.toArray()) + "       " + subjects.get(i).rating);
		}
	}
}
