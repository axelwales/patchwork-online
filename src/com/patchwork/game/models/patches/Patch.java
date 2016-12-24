package com.patchwork.game.models.patches;


public class Patch {
	public long id;
	public int staticId;
	public int getStaticId() {
		return staticId;
	}
	public int cost;
	public int time;
	public int income;
	public int size;
	public Point[] map;
	public Point location;
	public int rotations;
	public boolean flipped;
	public int wasChosen;
	
	public Patch(int staticId, int cost, int time, int income, int size){
		this.id = 0;
		this.staticId = staticId;		
		this.cost = cost;
		this.time = time;
		this.income = income;
		this.size = size;
		this.rotations = 0;
		this.flipped = false;
		this.wasChosen = 0;
	}
	
	public Patch(int staticId, int cost, int time, int income, int size, Point[] map){
		this(staticId, cost, time, income, size);
		Point pc;
		Point[] mapc = new Point[map.length];
		int i = 0;
		for( Point p : map) {
			pc = new Point(p.x,p.y);
			mapc[i] = pc;
			i++;
		}
		this.map = mapc;
	}
	
	public Patch(int staticId, int cost, int time, int income, int size, int[][] pointArray){
		this(staticId, cost, time, income, size);
		Point[] map = new Point[pointArray.length];
		for(int i = 0; i<pointArray.length; i++) {
			map[i] = new Point(pointArray[i][0], pointArray[i][1]);
		}
		this.map = map;
	}
	
	public void rotate(int turns) {
		switch(turns) {
			case 1:
				swapXY();
				negateY();
				break;
			case 2:
				negateY();
				negateX();
				break;
			case 3:
				swapXY();
				negateX();
				break;
		}
		this.rotations = (this.rotations + turns)%4;
	}
	public void flip() {
		if(this.rotations % 2 == 1)
			negateX();
		else
			negateY();
		this.flipped = true;
	}
	private void swapXY() {
		for(Point point: map) {
			int temp = point.x;
			point.x = point.y;
			point.y = temp;
		}
	}
	private void negateY() {
		for(Point point: map) {
			point.y = -point.y;
		}
	}
	private void negateX() {
		for(Point point: map) {
			point.x = -point.x;
		}
	}
}
