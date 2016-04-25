package com.tweeter.app.birds;

import java.util.Random;
import java.util.Stack;

import com.tweeter.app.globals.Global;
import com.tweeter.app.globals.Map;
import com.tweeter.app.globals.TweetNode;
import com.tweeter.app.sound.Note;
import com.tweeter.app.sound.Tweet;

public class BirdComputer extends Bird implements Runnable{
	
	/**
	 * How long a computer bird is delayed when moving
	 */
	public static final int MOVE_COOLDOWN = 100;

	/*
	 * Affinity values below -0.3 cause the bids 
	 * to undergo their attack script
	 * 
	 * Above, the birds move away
	 */
	private static final double ATTACK_CUTOFF = -0.3;
	
	/*
	 * Affinity values above .3 cause the bids 
	 * to undergo their mate script
	 */
	private static final double MATE_CUTOFF = 0.3;
	
	/*
	 * determines how many times this bird waits for more energy 
	 */
	protected final int energyRegenBuffer = (int) (Math.random() * 100 / ENERGY_RECOVERY ); 
	
	/*
	 * Stores all the actions for the bird
	 */
	private Stack<CallArg> actionStack;
	protected Object energyLock, moveLock; 
	
	/*
	 * constants for bird actions (Just for sugar...)
	 */
	private CallArg DO_TWEET = new CallArg(Call.TWEET);
	private CallArg DO_LISTEN = new CallArg(Call.LISTEN);
	private CallArg DO_MOVETOWARD = new CallArg(Call.MOVETOWARD);
	private CallArg DO_MOVEAWAY = new CallArg(Call.MOVEAWAY);
	private CallArg DO_MATE = new CallArg(Call.MATE); 
	private CallArg DO_ATTACK = new CallArg(Call.ATTACK);
	private CallArg DO_DIE = new CallArg(Call.DIE);
	
	/**
	 * Constructor for new computer bird
	 * 
	 * @param origX
	 * @param origY
	 */
	public BirdComputer(int origX, int origY, Object energyLock, Object moveLock){
		super(origX, origY);
		
		//Add random length tweet
		Random random = new Random();
		int length = (Math.abs(random.nextInt()) % (TWEET_MAXLENGTH-1)) +1;
		this.tweet = new Tweet();
		for (int i =0; i < length; i ++)
			tweet.add(Note.getNote( Math.abs( random.nextInt() ) % 6 ) );
		//
		
		// set up move and energy interrupts
		this.energyLock = energyLock;
		this.moveLock = moveLock;
		
		mood = BirdMood.NEUTRAL;
		
		//initialize action stack
		actionStack = new Stack<CallArg>();
		actionStack.push( DO_LISTEN );
		actionStack.push( DO_TWEET );
	}
	
	/**
	 * BirdComputer constructor for child bird spawned by dad and mom
	 * 
	 * @param origX
	 * @param origY
	 * @param dad : parent bird (The one that called "Mate"; excuse the biological sexism)
	 * @param mom : parent bird
	 */
	public BirdComputer(int origX, int origY, Bird dad, Bird mom, Object energyLock, Object moveLock){
		super(origX, origY);
		this.energyLock = energyLock;
		this.moveLock = moveLock;
		
		this.tweet = new Tweet(dad.tweet, mom.tweet);
		this.actionStack = new Stack<CallArg>();
		
		// listen after it's done moving
		actionStack.push( DO_LISTEN );
		
		// move away from spawning cell
		actionStack.push( DO_MOVEAWAY.XY(origX, origY) );
		actionStack.push( DO_MOVEAWAY.XY(origX, origY) );
		actionStack.push( DO_MOVEAWAY.XY(origX, origY) );
		actionStack.push( DO_MOVEAWAY.XY(origX, origY) );
		actionStack.push( DO_MOVEAWAY.XY(origX, origY) );
	}
	
	/**
	 * Execute the bird's action stack
	 */
	public void run(){
		while ( !actionStack.empty() )
			execute( actionStack.pop() );
	}
	
	
	private void execute(CallArg callarg) {
		switch(callarg.call){
		case ATTACK:
			attack(callarg.x, callarg.y);
			checkEnergy();
			break;
		case LISTEN:
			listen();
			break;
		case MATE:
			checkEnergy();
			mate();
			break;
		case MOVEAWAY:
			checkMove();
			move(false, callarg.x, callarg.y);
			break;
		case MOVETOWARD:
			checkMove();
			move(true, callarg.x, callarg.y);
			break;
		case TWEET:
			checkEnergy();
			tweet();
			break;
		default:
			break;
		}
	}

	/**
	 * Checks if this bird's energy is too low, if so waits.  
	 */
	private boolean checkEnergy() {
		if (energy >= ENERGY_COST){
			energy -= ENERGY_COST;
			return false;
		} else {
			for (int i = 0; i <= energyRegenBuffer; i ++){
				
				try {
					energyLock.wait();
				} catch (InterruptedException e) {}
				
				energy += ENERGY_COST;
			}
			return true;
		}
	}
	
	/**
	 * Checks energy and makes the bird wait a little bit to move
	 */
	private void checkMove(){
		if ( !checkEnergy() ) {
			try {
				moveLock.wait();
			} catch (InterruptedException e) {}
		}
	}

	/**
	 * Moves this bird one space
	 * 
	 * @param toward : true if moving toward target, 
	 * false if moving away
	 * 
	 * @param x : target x coordinate
	 * @param y : target y coordinate
	 * 
	 * ## if the bird does not move, it dies. (Conway's over-population rule) ##
	 * 
	 * TODO check if the right left/ up down movement is even-ish
	 */
	private void move(boolean toward, int x, int y){
		boolean horizontalfirst = Math.random() < 0.5 ;
		if (horizontalfirst){
			if (posX < x){
				if ( toward ? Global.map.moveRight(this) : Global.map.moveLeft(this) )
					return;
				else if (posY < y)
					if ( toward ? Global.map.moveDown(this) : Global.map.moveUp(this) )
						return;
					else if	( toward ? Global.map.moveUp(this) : Global.map.moveDown(this) )
						return;
			} else {
				if ( toward ? Global.map.moveLeft(this) : Global.map.moveRight(this) )
					return;
				else if (posY < y)
					if ( toward ? Global.map.moveDown(this) : Global.map.moveUp(this) )
						return;
					else if	( toward ? Global.map.moveUp(this) : Global.map.moveDown(this) )
						return;
			}
		} else {
			if (posY < y){
				if (toward ? Global.map.moveDown(this) : Global.map.moveUp(this) )
					return;
				else if (posX < x)
					if (toward ? Global.map.moveLeft(this) : Global.map.moveRight(this) )
						return;
					else if	( toward ? Global.map.moveRight(this) : Global.map.moveLeft(this) )
						return;
			} else {
				if (toward ? Global.map.moveDown(this) : Global.map.moveUp(this) )
					return;
				else if (posY < y)
					if (toward ? Global.map.moveLeft(this) : Global.map.moveRight(this) )
						return;
					else if	( toward ? Global.map.moveRight(this) : Global.map.moveLeft(this) )
						return;
			}
		}
		actionStack.push( DO_DIE );
		return;
	}
	
	protected void tweet(){
		super.tweet();
		actionStack.push( DO_LISTEN );
	}
	
	
	/**
	 * Bird takes in it's surroundings
	 * 
	 * pushes contingent actions to the stack
	 * TODO test this shit out
	 */
	public void listen(){
		TweetNode tweetedNode = Global.tweetQueue.listen(posX, posY);
		double affinity = Tweet.compare(tweet, tweetedNode.tweet);
		
		if (affinity > MATE_CUTOFF){
			this.tweet.learn(tweetedNode.tweet);
			this.setMood(BirdMood.MATE);
			
			actionStack.push( DO_LISTEN );
			actionStack.push( DO_MATE );
			actionStack.push( DO_MOVETOWARD.XY(tweetedNode.x0, tweetedNode.y0) );
			actionStack.push( DO_MOVETOWARD.XY(tweetedNode.x0, tweetedNode.y0) );
			
		} else if (affinity > ATTACK_CUTOFF) {
			this.setMood(BirdMood.NEUTRAL);
			actionStack.push( DO_LISTEN );
			actionStack.push( DO_TWEET );
			actionStack.push( DO_MOVEAWAY.XY(tweetedNode.x0, tweetedNode.y0) );
			actionStack.push( DO_MOVEAWAY.XY(tweetedNode.x0, tweetedNode.y0) );
			
		} else {
			this.setMood(BirdMood.ATTACK);
			actionStack.push( DO_LISTEN );
			actionStack.push( DO_TWEET );
			actionStack.push( DO_ATTACK.XY(tweetedNode.x0, tweetedNode.y0) );
			actionStack.push( DO_MOVETOWARD.XY(tweetedNode.x0, tweetedNode.y0) );
			actionStack.push( DO_MOVETOWARD.XY(tweetedNode.x0, tweetedNode.y0) );
		}
	}
	
	/**
	 * Bird looks at all adjacent squares in random order and 
	 * if there is a bird in mate mode it mates, otherwise it doesn't.
	 * 
	 * This method replaces the calling bird if the bird is surrounded.
	 */
	public void mate() {
		int firstChoice = (int) Math.round( Math.random() * 3 );
		boolean foundBird = false;
		Bird partner = null;
		// Find a bird to mate with
		int checkX = 0;
		int checkY = 0;
		for (int i = firstChoice ; i % 4 != firstChoice || foundBird ; i++, i %= 4 ){
			checkX = (i % 2 == 0 ? this.posX + (i - 1) : this.posX);
			checkY = (i % 2 == 1 ? this.posY + (i - 1) : this.posY);
			partner = Global.map.getMateBird(checkX, checkY);
			foundBird = null != partner;}
		// Find an empty space to spawn a new bird
		if (foundBird) {
			firstChoice = (int) Math.round( Math.random() * 3 );
			boolean foundSpace = false;
			int spawnX = 0;
			int spawnY = 0;
			for (int i = firstChoice ; i % 4 != firstChoice || foundBird ; i++, i %= 4 ){
				checkX = (i % 2 == 0 ? this.posX : this.posX);
				checkY = (i % 2 == 1 ? this.posY + (i - 1) : this.posY);
				partner = Global.map.getBird(checkX, checkY);
				foundBird = null == partner;}
			// Put the bird in the empty space
			if (foundSpace) {
				BirdComputer child = new BirdComputer(spawnX, spawnY, this, partner, energyLock, moveLock);
				Thread thread = new Thread(child);
				thread.start();
			// Replace father bird with the child (So the user bird is never replaced)	
			} else {
				BirdComputer child = new BirdComputer(this.posX, this.posY, this, partner, energyLock, moveLock);
				Thread thread = new Thread(child);
				thread.start();
			}
			this.mood = BirdMood.NEUTRAL;
			partner.mood = BirdMood.NEUTRAL;
		// No bird to mate with... listen again
		} else 
			return;
	}
	
	/**
	 * Checks the surrounding spaces and attacks the bird
	 * 
	 * @param x : x coordinate of target
	 * @param y : y coordinate of target
	 */
	public void attack(int x, int y) {
				
		boolean firstChoice = 0.5 < Math.random() ;
		boolean foundBird = false;
		Bird enemy = null;
		int i = 0;
		
		// Find a bird to attack
		while ( !foundBird && (i <= 1)){
			if (posX < x) {
				if (posY < y)
					enemy = (firstChoice ? Global.map.getBird(posX, posY + 1) : Global.map.getBird(posX - 1, posY));
				else
					enemy = (firstChoice ? Global.map.getBird(posX, posY + 1) : Global.map.getBird(posX - 1, posY)); }
			else if (posY < y)
				enemy = (firstChoice ? Global.map.getBird(posX, posY + 1) : Global.map.getBird(posX - 1, posY));
			else 
				enemy = (firstChoice ? Global.map.getBird(posX, posY - 1) : Global.map.getBird(posX - 1, posY));
			
			foundBird = null == enemy;
			firstChoice = !firstChoice;
			i++; }
		
		if ( !(enemy == null) ) {
			this.health -= DAMAGE;
			enemy.health -= DAMAGE;
			// check if this bird dies
			if (this.health <= 0)
				this.die();
			else 
				this.mood = BirdMood.NEUTRAL;
			
			// check if enemy bird dies
			if (enemy.health <= 0)
				enemy.die();
			else
				enemy.mood = BirdMood.NEUTRAL;
		} else {
			//
			actionStack.push( DO_LISTEN );
			actionStack.push( DO_MOVETOWARD.XY(x, y) );
		}
	}
	
	 protected void die(){
		super.die();
		actionStack.clear();
	}
}



enum Call {
	TWEET, LISTEN, MOVETOWARD, MOVEAWAY, MATE, ATTACK, DIE;
}

class CallArg {
	int x;
	int y;
	Call call;
	
	CallArg(Call c){
		call = c; }
	
	CallArg(Call c, int xx, int yy){
		this(c);
		x = xx;
		y = yy; }
	

	public CallArg XY( int xx, int yy){
		return new CallArg(this.call, xx, yy);}
}