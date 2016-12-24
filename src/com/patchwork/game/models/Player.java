package com.patchwork.game.models;

public class Player {
	public long id;
	public int isAI;
	public int getIsAI() {
		return isAI;
	}

	public int buttons;
	public int getButtons() {
		return buttons;
	}

	public int position;
	public int getPosition() {
		return position;
	}

	public int income;
	public int getIncome() {
		return income;
	}


	public PlayerBoard board;
	public PlayerBoard getBoard() {
		return board;
	}

	public String username;
	public String getUsername() {
		return username;
	}

	public boolean currentPlayer;
	public boolean getCurrentPlayer() {
		return currentPlayer;
	}
	
	public int boardBonus;
	public int getBoardBonus() {
		return boardBonus;
	}

	public Player() {
		this.id = 0;
		this.board = new PlayerBoard();
		this.buttons = 5;
		this.position = 0;
		this.income = 0;
		this.username = "";
		this.currentPlayer = false;
		this.isAI = 0;
		this.boardBonus = 0;
	}

	public Player(Long id, String username, boolean currentPlayer) {
		this();
		this.id = id;
		this.username = username;
		this.currentPlayer = currentPlayer;
	}

	public Player clonePlayer() {
		Player clone = new Player();
		clone.id = this.id;
		clone.board = this.board.cloneBoard();
		clone.buttons = this.buttons;
		clone.boardBonus = this.boardBonus;
		clone.currentPlayer = this.currentPlayer;
		clone.income = this.income;
		clone.position = this.position;
		clone.username = this.username;
		return clone;
	}
}
