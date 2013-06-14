package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game {
    ArrayList players = new ArrayList();
    int[] places = new int[6];
    int[] purses  = new int[6];
    boolean[] inPenaltyBox  = new boolean[6];
    
    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();
    
    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;
    
    public  Game(){
    	for (int i = 0; i < 50; i++) {
            addPopQuestions(i);
            addScienceQuestions(i);
            addSportsQuestions(i);
            addRockQuestions(i);
        }
    }

    private void addRockQuestions(int i) {
        rockQuestions.addLast(createRockQuestion(i));
    }

    private void addSportsQuestions(int i) {
        sportsQuestions.addLast(("Sports Question " + i));
    }

    private void addScienceQuestions(int i) {
        scienceQuestions.addLast(("Science Question " + i));
    }

    private void addPopQuestions(int i) {
        popQuestions.addLast("Pop Question " + i);
    }

    public String createRockQuestion(int index){
		return "Rock Question " + index;
	}
	
	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}

	public boolean add(String playerName) {
	    players.add(playerName);
        initializePlacesElement();
        initializePursesElement();
        initializeInPenaltyBoxElement();

        printAddedPlayer(playerName);
        printNumberOfAddedPlayer();
        return true;
	}

    private void initializeInPenaltyBoxElement() {
        inPenaltyBox[howManyPlayers()] = false;
    }

    private void initializePursesElement() {
        purses[howManyPlayers()] = 0;
    }

    private void initializePlacesElement() {
        places[howManyPlayers()] = 0;
    }

    private void printNumberOfAddedPlayer() {
        System.out.println("They are player number " + players.size());
    }

    private void printAddedPlayer(String playerName) {
        System.out.println(playerName + " was added");
    }

    public int howManyPlayers() {
		return players.size();
	}

	public void roll(int roll) {
		System.out.println(players.get(currentPlayer) + " is the current player");
		System.out.println("They have rolled a " + roll);
		
		if (inPenaltyBox[currentPlayer]) {
			if (roll % 2 != 0) {
				isGettingOutOfPenaltyBox = true;
				
				System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
                processCurrentPlayer(roll);
			} else {
				System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
				isGettingOutOfPenaltyBox = false;
				}
			
		} else {

            processCurrentPlayer(roll);
		}
		
	}

    private void processCurrentPlayer(int roll) {
        places[currentPlayer] = places[currentPlayer] + roll;
        if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

        System.out.println(players.get(currentPlayer)
                + "'s new location is "
                + places[currentPlayer]);
        System.out.println("The category is " + currentCategory());
        askQuestion();
    }

    private void askQuestion() {
		if (currentCategory() == "Pop")
            printPopQuestion();
        if (currentCategory() == "Science")
            printScienceQuestion();
        if (currentCategory() == "Sports")
            printSportsQuestion();
        if (currentCategory() == "Rock")
            printRockQuestion();
    }

    private void printRockQuestion() {
        System.out.println(rockQuestions.removeFirst());
    }

    private void printSportsQuestion() {
        System.out.println(sportsQuestions.removeFirst());
    }

    private void printScienceQuestion() {
        System.out.println(scienceQuestions.removeFirst());
    }

    private void printPopQuestion() {
        System.out.println(popQuestions.removeFirst());
    }


    private String currentCategory() {
		if (places[currentPlayer] == 0) return "Pop";
		if (places[currentPlayer] == 4) return "Pop";
		if (places[currentPlayer] == 8) return "Pop";
		if (places[currentPlayer] == 1) return "Science";
		if (places[currentPlayer] == 5) return "Science";
		if (places[currentPlayer] == 9) return "Science";
		if (places[currentPlayer] == 2) return "Sports";
		if (places[currentPlayer] == 6) return "Sports";
		if (places[currentPlayer] == 10) return "Sports";
		return "Rock";
	}

	public boolean wasCorrectlyAnswered() {
		if (inPenaltyBox[currentPlayer]){
			if (isGettingOutOfPenaltyBox) {
				System.out.println("Answer was correct!!!!");
				purses[currentPlayer]++;
				System.out.println(players.get(currentPlayer) 
						+ " now has "
						+ purses[currentPlayer]
						+ " Gold Coins.");
				
				boolean winner = didPlayerWin();
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				
				return winner;
			} else {
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				return true;
			}
			
			
			
		} else {
		
			System.out.println("Answer was corrent!!!!");
			purses[currentPlayer]++;
			System.out.println(players.get(currentPlayer) 
					+ " now has "
					+ purses[currentPlayer]
					+ " Gold Coins.");
			
			boolean winner = didPlayerWin();
			currentPlayer++;
			if (currentPlayer == players.size()) currentPlayer = 0;
			
			return winner;
		}
	}
	
	public boolean wrongAnswer(){
		System.out.println("Question was incorrectly answered");
		System.out.println(players.get(currentPlayer)+ " was sent to the penalty box");
		inPenaltyBox[currentPlayer] = true;
		
		currentPlayer++;
		if (currentPlayer == players.size()) currentPlayer = 0;
		return true;
	}


	private boolean didPlayerWin() {
		return !(purses[currentPlayer] == 6);
	}
}
