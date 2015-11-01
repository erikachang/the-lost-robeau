package robeau_environment;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;

public class RobeauWorldModel extends GridWorldModel {

	public static final int CIRCLE_T = 32;
	public static final int CIRCLE_S = 64;
	public static final int CIRCLE_M = 128;
	public static final int CIRCLE_L = 256;
	public static final int EXIT	 = 512;
	public static final int PRISONER = 1024;
	
	private final int robeau = 0;
	private final int[] prisoners = new int[] { 1, 2, 3 };
	
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
		instance = new RobeauWorldModel(16, 4, 4, 1);
		return instance;
	}
	
	public static RobeauWorldModel world2() {
		instance = new RobeauWorldModel(16, 4, 4, 2);
		return instance;
	}
	
	public static RobeauWorldModel world3() {
		instance = new RobeauWorldModel(4, 4, 4, 3);
		return instance;
	}
	
	public static RobeauWorldModel world4() {
		instance = new RobeauWorldModel(8, 8, 4, 4);
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

	synchronized public boolean move(int agentId, Direction direction) {
		int agX = agPos[agentId].x;
		int agY = agPos[agentId].y;
		
		if (direction.equals(Direction.NORTH)) {
			if (isFreeOfObstacle(agX, agY-1)) {
				setAgPos(agentId, agPos[0].x, agPos[0].y-1);
			} else {
				return false;
			}
		} else if (direction.equals(Direction.EAST)) {
			if (isFreeOfObstacle(agX+1, agY)) {
				setAgPos(agentId, agPos[0].x+1, agPos[0].y);
			} else {
				return false;
			}
		} else if (direction.equals(Direction.WEST)) {
			if (isFreeOfObstacle(agX-1, agY)) {
				setAgPos(agentId, agPos[0].x-1, agPos[0].y);
			} else {
				return false;
			}
		} else if (direction.equals(Direction.SOUTH)) {
			if (isFreeOfObstacle(agX, agY+1)) {
				setAgPos(agentId, agPos[0].x, agPos[0].y+1);
			} else {
				return false;
			}
		}
		return true;
	}
	
	public List<Integer> neighbours(int s) {
		List<Integer> neighbours = new LinkedList<Integer>();
		Location l;
		if (s % width >= 0 && s % width < (width-1)) {
			l = locationFromInteger(s+1, width);
			if (isFreeOfObstacle(l.x, l.y))
				neighbours.add(s+1);
		}
		if (s % width > 0 && s % width < width) {
			l = locationFromInteger(s-1, width);
			if (isFreeOfObstacle(l.x, l.y))
				neighbours.add(s-1);
		}
		if (s - width >= 0) {
			l = locationFromInteger(s-width, width);
			if (isFreeOfObstacle(l.x, l.y))
				neighbours.add(s-width);
		}
		if (s + width < (width*height)) {
			l = locationFromInteger(s+width, width);
			if (isFreeOfObstacle(l.x, l.y))
				neighbours.add(s+width);
		}
		return neighbours;
	}
	
	public BitSet getSurroundingBlockades(int i) {
		Location l = locationFromInteger(i, width);
		BitSet readings = new BitSet(4);
		
		if (!isFreeOfObstacle(l.x, l.y-1)) {
			readings.set(0);
		}
		if (!isFreeOfObstacle(l.x+1, l.y)) {
			readings.set(1);
		}
		if (!isFreeOfObstacle(l.x-1, l.y)) {
			readings.set(2);
		}
		if (!isFreeOfObstacle(l.x, l.y+1)) {
			readings.set(3);
		}
		
		return readings;
	}
	
	public BitSet getSurroundingBlockades (int x, int y) {
		return getSurroundingBlockades(integerPosition(x, y, width));
	}
	
	public List<Integer> getFreePositions() {
		List<Integer> freeSquares = new LinkedList<Integer>();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (isFreeOfObstacle(j, i))
					freeSquares.add(integerPosition(j, i, this.getWidth()));
			}
		}
		
		return freeSquares;
	}
	
	public void drawMatrix(ArrayList<double[]> matrix) {		
		for (double[] m: matrix) {
			int x = (int) m[0];
			int y = (int) m[1];
			double prob = m[2];
			
			if (this.hasObject(EXIT, x, y))
				continue;
			
			if ((prob) < 0.001 ) {
				set(CIRCLE_T, x, y);
			} else if (prob < 0.05) {
				set(CIRCLE_S, x, y);
			} else if (prob < 0.15) {
				set(CIRCLE_M, x, y);
			} else {
				set(CIRCLE_L, x, y);
			}
		}
	}
		
	public void drawDot(int x, int y, double prob) {
		set(CLEAN, x, y);
		
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
	
	public static Location locationFromInteger(int i, int w) {
		int x = i % w;
		int y = i / w;
		return new Location(x, y);
	}
	
	public static int integerPosition(int x, int y, int w) {
		return (y * w) + x;
	}
	
	public static int integerPosition(Location l, int w) {
		return integerPosition(l.x, l.y, w);
	}
	
	public List<Integer> prisonersAt(Location l) {
		List<Integer> lPrisoners = new LinkedList<Integer>();
		for (int prisoner: prisoners) {
			if (prisoner < 0) continue;
			if (getAgPos(prisoner).equals(l))
				lPrisoners.add(prisoner);
		}
		return lPrisoners;
	}
	
	public boolean hasPrisonerIn(Location l) {
		for (int prisoner: prisoners) {
			if (prisoner < 0) continue;
			if (getAgPos(prisoner).equals(l))
				return true;
		}
		return false;
	}
	
	@Override
	public void setAgPos(int agId, int x, int y) {
		super.setAgPos(agId, x, y);
		if (agId == robeau)
			stateSequence.add(integerPosition(x, y, width));
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
		
		add(EXIT, 15, 2);

		setAgPos(robeau, 0, 0);
		setAgPos(prisoners[0], 0, 3);
		setAgPos(prisoners[1], 10, 1);
		setAgPos(prisoners[2], 7, 3);
	}
	
	private void map2() {		
		for (int i = 0; i < 4; i++) {
			add(OBSTACLE, (i*height)+0, 1);
			add(OBSTACLE, (i*height)+0, 2);
			
			add(OBSTACLE, (i*height)+1, 1);
			
			add(OBSTACLE, (i*height)+2, 3);
			add(OBSTACLE, (i*height)+3, 0);
			add(OBSTACLE, (i*height)+3, 1);
		}
		
		setAgPos(robeau, 0, 0);
		setAgPos(prisoners[0], 0, 3);
		setAgPos(prisoners[1], 4, 0);
		setAgPos(prisoners[2], 15, 3);
	}
	
	private void map3() {
		add(OBSTACLE, 0, 1);
		add(OBSTACLE, 0, 2);
		add(OBSTACLE, 1, 1);
		add(OBSTACLE, 2, 3);
		add(OBSTACLE, 3, 1);
		
		setAgPos(0, 0, 0);
		setAgPos(prisoners[0], 0, 3);
		setAgPos(prisoners[1], 3, 3);
		setAgPos(prisoners[2], 3, 0);
	}
	
	private void map4() {	
		setAgPos(0, 4, 4);
		setAgPos(prisoners[0], 0, 0);
		setAgPos(prisoners[1], 0, 7);
		setAgPos(prisoners[2], 7, 7);
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

	public void setPrisonerFree(Integer i) {
		prisoners[i-1] = -1;
		
	}
}
