import java.io.Serializable;

public class SavedGame implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int score;
	int experience = 0;
	

	SavedGame(int score, int experience) {
		this.score = score;
		this.experience = experience;
	}
	
	

}
