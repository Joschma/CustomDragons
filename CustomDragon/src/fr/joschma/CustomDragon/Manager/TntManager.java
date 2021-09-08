package fr.joschma.CustomDragon.Manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.TNTPrimed;

public class TntManager {
	
	List<TNTPrimed> tnts = new ArrayList<TNTPrimed>();
	
	public void addTnt(TNTPrimed tnt) {
		tnts.add(tnt);
	}
	
	public void rmvTnt(TNTPrimed tnt) {
		tnts.remove(tnt);
	}
	
	public List<TNTPrimed> getTnts() {
		return tnts;
	}
}
