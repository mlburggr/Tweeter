package com.tweeter.app;

// Enumeration for the Note object
// 	-name stores the key corresponding to this note
//	-semi stores the integer number of semitones from the root note.  
//				to recover the interval perform (interval mod 12)
//  -getInterval static method finds the musical interval between two notes.

public enum Note {
	A('a', 0), S('s', 7), D('d', 14), Q('q', 16), W('w', 21), E('e', 24);
	private final char name;
	private final int semi;
	
	Note(char c, int i){
		this.name = c;
		this.semi = i;}
	
	public char getName(Note n){
		return n.name;}
	
	public static Note getNote(char c){
		switch(c){
		case 'a':
			return A;
		case 's':
			return S;
		case 'd':
			return D;
		case 'q':
			return Q;
		case 'w':
			return W;
		case 'e':
			return E;
		default:
			throw new IllegalArgumentException(String.format("argument %c is not  note" ,c));}
	}
	
	public static int getInterval(Note n1, Note n2){
		int diff = n1.compareTo(n2);
		if ( diff < 0)
			return (n2.semi - n1.semi) % 12;
		else 
			return 12 - ((n1.semi - n2.semi) % 12);
	}
}

