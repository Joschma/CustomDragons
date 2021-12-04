package fr.joschma.CustomDragon.Listener;

import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;

import fr.joschma.CustomDragon.CustomDragon;
import fr.joschma.CustomDragon.Object.Dragon;

public class EnderDragonChangePhaseListener implements Listener {
	
	CustomDragon pl;
	
	public EnderDragonChangePhaseListener(CustomDragon pl) {
		super();
		this.pl = pl;
	}

	@EventHandler
	public void onEnderDragonChangePhase(EnderDragonChangePhaseEvent e) {
		for(Dragon dragon : pl.getDrm().getDragons()) {
			if(e.getEntity() == dragon) {
				if(e.getNewPhase() == Phase.SEARCH_FOR_BREATH_ATTACK_TARGET) {
					e.setNewPhase(Phase.CHARGE_PLAYER);
				}
			}
		}
	}
}
