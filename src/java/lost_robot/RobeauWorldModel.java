package lost_robot;

import jason.environment.grid.GridWorldModel;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

public class RobeauWorldModel extends GridWorldModel {

	public static final int CIRCLE_T = 32;
	public static final int CIRCLE_S = 64;
	public static final int CIRCLE_M = 128;
	public static final int CIRCLE_L = 256;
	
	private List<Integer> stateSequence;
	
	private double sensorError;
	
	protected RobeauWorldModel(int arg0, int arg1, int arg2, int world) {
		super(arg0, arg1, arg2);
		
		stateSequence = new LinkedList<Integer>();
		
		switch(world) {
		case 1:
			map1();
			break;
		case 2:
			map2();
			break;
		case 3:
			map3();
			break;
		case 4:
			map4();
			break;
		case 5:
			map5();
			break;
		}
	}
	
	protected static RobeauWorldModel instance;
	
	public static RobeauWorldModel world1() {
		instance = new RobeauWorldModel(16, 4, 1, 1);
		return instance;
	}
	
	public static RobeauWorldModel world2() {
		instance = new RobeauWorldModel(16, 4, 1, 2);
		return instance;
	}
	
	public static RobeauWorldModel world3() {
		instance = new RobeauWorldModel(4, 4, 1, 3);
		return instance;
	}
	
	public static RobeauWorldModel world4() {
		instance = new RobeauWorldModel(4, 4, 1, 4);
		return instance;
	}
	
	public static RobeauWorldModel world5() {
		instance = new RobeauWorldModel(16, 4, 1, 5);
		return instance;
	}	
	
	public static RobeauWorldModel get() {
		return instance;
	}
	
	synchronized public List<Integer> getStateSequence(int t) {
		List<Integer> seq = new LinkedList<Integer>();
		for (int i = 0; i < t; i++) {
			seq.add(stateSequence.get(i));
		}
		return seq;
	}
	
	public void setSensorError(double e) {
		sensorError = e;
	}
	
	public double getSensorError() {
		return sensorError;
	}

	synchronized public boolean move(int agentId, String direction) {
		int agX = agPos[agentId].x;
		int agY = agPos[agentId].y;
		
		if (direction.equals("north")) {
			if (this.isFreeOfObstacle(agX, agY-1)) {
				this.setAgPos(0, this.agPos[0].x, this.agPos[0].y-1);
			} else {
				return false;
			}
		} else if (direction.equals("east")) {
			if (this.isFreeOfObstacle(agX+1, agY)) {
				this.setAgPos(0, this.agPos[0].x+1, this.agPos[0].y);
			} else {
				return false;
			}
		} else if (direction.equals("west")) {
			if (this.isFreeOfObstacle(agX-1, agY)) {
				this.setAgPos(0, this.agPos[0].x-1, this.agPos[0].y);
			} else {
				return false;
			}
		} else if (direction.equals("south")) {
			if (this.isFreeOfObstacle(agX, agY+1)) {
				this.setAgPos(0, this.agPos[0].x, this.agPos[0].y+1);
			} else {
				return false;
			}
		}
		
		return true;
	}
	
	public ArrayList<Integer> neighbours(int s) {
		ArrayList<Integer> neighbours = new ArrayList<Integer>();
		
		if (s % width >= 0 && s % width < (width-1)) {
			int[] coords = positionFromInteger(s+1, width);
			if (isFreeOfObstacle(coords[0], coords[1]))
				neighbours.add(s+1);
		}
		if (s % width > 0 && s % width < width) {
			int[] coords = positionFromInteger(s-1, width);
			if (isFreeOfObstacle(coords[0], coords[1]))
				neighbours.add(s-1);
		}
		if (s - width >= 0) {
			int[] coords = positionFromInteger(s-width, width);
			if (isFreeOfObstacle(coords[0], coords[1]))
				neighbours.add(s-width);
		}
		if (s + width < (width*height)) {
			int[] coords = positionFromInteger(s+width, width);
			if (isFreeOfObstacle(coords[0], coords[1]))
				neighbours.add(s+width);
		}
		
		return neighbours;
	}
	
	public BitSet getSurroundingBlockades(int i) {
		int[] coords = positionFromInteger(i, width);
		BitSet readings = new BitSet(4);
		
		if (!isFreeOfObstacle(coords[0], coords[1]-1)) {
			readings.set(0);
		}
		if (!isFreeOfObstacle(coords[0]+1, coords[1])) {
			readings.set(1);
		}
		if (!isFreeOfObstacle(coords[0]-1, coords[1])) {
			readings.set(2);
		}
		if (!isFreeOfObstacle(coords[0], coords[1]+1)) {
			readings.set(3);
		}
		
		return readings;
	}
	
	public BitSet getSurroundingBlockades (int x, int y) {
		return getSurroundingBlockades(positionAsInteger(x, y, width));
	}
	
	public ArrayList<Integer> getFreePositions() {
		ArrayList<Integer> freeSquares = new ArrayList<Integer>();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (isFreeOfObstacle(j, i))
					freeSquares.add(positionAsInteger(j, i, this.getWidth()));
			}
		}
		
		return freeSquares;
	}
		
	public void drawDot(int x, int y, double prob) {
		set(CLEAN, x, y);
//		add(AGENT, getAgPos(0));
		
		if ((prob) < 0.001 ) {
			add(CIRCLE_T, x, y);
		} else if (prob < 0.05) {
			add(CIRCLE_S, x, y);
		} else if (prob < 0.15) {
			add(CIRCLE_M, x, y);
		} else {
			add(CIRCLE_L, x, y);
		}
	}
	
	public static int[] positionFromInteger(int x, int w) {
		int[] coords = new int[2];
		
		coords[0] = x % w;
		coords[1] = x / w;
		
		return coords;
	}
	
	public static int positionAsInteger(int x, int y, int w) {
		int pos = 0;
		
		pos += (y * w) + x;
		
		return pos;
	}
	
	@Override
	public void setAgPos(int agId, int x, int y) {
		super.setAgPos(agId, x, y);
		stateSequence.add(positionAsInteger(x, y, width));
	}
	
	private void map1() {
		add(OBSTACLE, 0, 1);
		add(OBSTACLE, 0, 2);
		
		add(OBSTACLE, 1, 1);
		
		add(OBSTACLE, 2, 3);
		
		add(OBSTACLE, 4, 0);
		add(OBSTACLE, 4, 1);
		add(OBSTACLE, 4, 2);
		
		add(OBSTACLE, 6, 1);
		add(OBSTACLE, 6, 2);
		add(OBSTACLE, 6, 3);
		
		add(OBSTACLE, 7, 1);
		add(OBSTACLE, 7, 2);
		
		add(OBSTACLE, 9, 1);
		
		add(OBSTACLE, 10, 0);
		
		add(OBSTACLE, 11, 1);
		add(OBSTACLE, 11, 3);
		
		add(OBSTACLE, 13, 1);
		add(OBSTACLE, 13, 2);
		
		add(OBSTACLE, 14, 0);
		add(OBSTACLE, 14, 1);
		add(OBSTACLE, 14, 2);
		
		add(OBSTACLE, 15, 1);
		
		setAgPos(0, 0, 0);
		
	}
	
	private void map2() {
//		boolean alt = true;
//		for (int i = 1; i < width; i+=2) {
//			if (alt) {
//				add(OBSTACLE, i, 0);
//				add(OBSTACLE, i, 1);
//				add(OBSTACLE, i, 2);
//			} else {
//				add(OBSTACLE, i, 1);
//				add(OBSTACLE, i, 2);
//				add(OBSTACLE, i, 3);
//			}
//			alt = !alt;
//		}
		
		for (int i = 0; i < 4; i++) {
			add(OBSTACLE, (i*height)+0, 1);
			add(OBSTACLE, (i*height)+0, 2);
			
			add(OBSTACLE, (i*height)+1, 1);
			
			add(OBSTACLE, (i*height)+2, 3);
			add(OBSTACLE, (i*height)+3, 0);
			add(OBSTACLE, (i*height)+3, 1);
		}
		
		setAgPos(0, 0, 0);
	}
	
	private void map3() {
		add(OBSTACLE, 0, 1);
		add(OBSTACLE, 0, 2);
		add(OBSTACLE, 1, 1);
		add(OBSTACLE, 2, 3);
		add(OBSTACLE, 3, 1);
		
		setAgPos(0, 0, 0);
	}
	
	private void map4() {	
		setAgPos(0, 2, 2);
	}
	
	private void map5() {
		add(OBSTACLE, 0, 1);
		add(OBSTACLE, 0, 2);
		
		add(OBSTACLE, 1, 2);
		
		add(OBSTACLE, 2, 1);
		add(OBSTACLE, 2, 2);
		
		add(OBSTACLE, 4, 0);
		add(OBSTACLE, 4, 1);
		add(OBSTACLE, 4, 2);
		
		add(OBSTACLE, 5, 2);
		
		add(OBSTACLE, 6, 1);
		add(OBSTACLE, 6, 2);
		
		add(OBSTACLE, 8, 1);
		add(OBSTACLE, 8, 2);
		add(OBSTACLE, 8, 3);
		
		add(OBSTACLE, 9, 1);
		add(OBSTACLE, 9, 2);
		
		add(OBSTACLE, 11, 0);
		add(OBSTACLE, 11, 3);
		
		add(OBSTACLE, 12, 1);
		
		add(OBSTACLE, 13, 0);
		add(OBSTACLE, 13, 3);
		
		add(OBSTACLE, 14, 1);
		
		setAgPos(0, 0, 0);
	}
	
//	private void map5() {
//		add(OBSTACLE, 1, 1);
//		add(OBSTACLE, 2, 1);
////		add(OBSTACLE, 3, 1);
//		add(OBSTACLE, 4, 1);
//		add(OBSTACLE, 5, 1);
//		add(OBSTACLE, 6, 1);
//		
//		add(OBSTACLE, 1, 2);
//		add(OBSTACLE, 4, 2);
//		
//		add(OBSTACLE, 1, 3);
//		add(OBSTACLE, 2, 3);
//		add(OBSTACLE, 3, 3);
//		add(OBSTACLE, 4, 3);
//		add(OBSTACLE, 5, 3);
//		
//		add(OBSTACLE, 1, 4);
//		add(OBSTACLE, 3, 4);
//		add(OBSTACLE, 4, 4);
//		add(OBSTACLE, 6, 4);
//		
//		add(OBSTACLE, 3, 5);
////		add(OBSTACLE, 4, 5);
//		
//		add(OBSTACLE, 1, 6);
//		add(OBSTACLE, 2, 6);
//		add(OBSTACLE, 3, 6);
////		add(OBSTACLE, 4, 6);
//		add(OBSTACLE, 5, 6);
//		add(OBSTACLE, 6, 6);
//		
//		add(OBSTACLE, 7, 7);
//		
//		setAgPos(0, 0, 0);
//		
////		view.setSize(800, 800);
//	}
}
