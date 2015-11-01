package rescue;

import jason.environment.grid.Location;

import java.util.LinkedList;
import java.util.List;

import robeau_environment.RobeauWorldModel;

public class RescueHelper {
	private List<Integer> statesToVisit;
	
	private RescueHelper() {
		statesToVisit = new LinkedList<Integer>();
	}
	
	private static RescueHelper instance = null;
	
	public static RescueHelper getInstance() {
		if (instance == null) {
			instance = new RescueHelper();
		}
		return instance;
	}
	
	public boolean isEmpty() {
		return statesToVisit.isEmpty();
	}
	
	public void addTarget(int state) {
		if (!statesToVisit.contains(state))
			statesToVisit.add(state);
	}
	
	public boolean contains(int state) {
		return statesToVisit.contains(state);
	}
	
	public void removeTarget(Integer state) {
		statesToVisit.remove(state);
	}
		
	public int topCandidate(int from) {
		RobeauWorldModel worldModel = RobeauWorldModel.get();
		
		int topCandidate = -1;
		int minCost = (worldModel.getWidth() + worldModel.getHeight()) + 1;
		
		int d1 = minCost;
		
		for (int a = 0; a < statesToVisit.size(); a++) {
			if (statesToVisit.get(a).equals(from))
				continue;
			
			Location l1 = RobeauWorldModel.locationFromInteger(from, RobeauWorldModel.get().getWidth());
			Location l2 = RobeauWorldModel.locationFromInteger(statesToVisit.get(a), RobeauWorldModel.get().getWidth());
			
			d1 = l1.distanceManhattan(l2);
			
			if (d1 <= minCost) {
				topCandidate = statesToVisit.get(a);
				minCost = d1;
			}
		}
		
		return topCandidate;
	}
}
