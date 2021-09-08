package fr.joschma.CustomDragon.Manager;

import java.util.ArrayList;
import java.util.List;

import fr.joschma.CustomDragon.Object.Dragon;

public class DragonManager {
	
	List<Dragon> dragons = new ArrayList<Dragon>();
	
	public void addDragon(Dragon dragon) {
		dragons.add(dragon);
	}
	
	public void rmvDragon(Dragon dragon) {
		dragons.remove(dragon);
	}
	
	public List<Dragon> getDragons() {
		return dragons;
	}
}
