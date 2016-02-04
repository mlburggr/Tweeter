import java.util.LinkedList;
import com.tweeter.app.Note;

/**
 * 
 * @author nick
 *
 */
public class Tweet extends LinkedList<NoteXInt>{
	
//
	/**
	 * @author nick
	 * 
	 * Tweet: initial note constructor
	 * 
	 * @param c -The first note of the tweet
	 */
	public Tweet(char c){
		super();
		add(new NoteXInt(Note.getNote(c), -1));
	}
	
	/**
	 * @author nick
	 * 
	 * Tweet: full char array constructor
	 * 
	 * @param cs 
	 */
	public Tweet(char cs[]){
		super();
		Note n1 = Note.getNote(cs[0]);
		Note n2;
		for(int i = 1; i < cs.length; i++){
			n2 = Note.getNote(cs[i]);
			add(new NoteXInt(n1, Note.getInterval(n1, n2)));
			n1 = Note.getNote(cs[i-1]);
		}
	}
	
	/**
	 * 
	 * @param listener
	 * @param tweeter
	 * @return double value "similarity index" 	
	 * 					negative -> not similar
	 * 					positive -> similar
	 */
	
	public static double compare(Tweet listener, Tweet tweeter){
		return 0.0;
	}

}

//This doesn't seem right :/

/**
 * @author nick
 * 
 * Class to handle stupid shit java doesn't have or is too obscure to bother with
 * Pair of Note and Ints
 * @
 */
class NoteXInt{
	public Note note;
	public int interval;
	public boolean mutable;
	
	NoteXInt(Note n, int i){
		note = n;
		interval = i;
	}
	NoteXInt(Note n, int i, boolean m){
		note = n;
		interval = i;
		mutable = m;
	}
}