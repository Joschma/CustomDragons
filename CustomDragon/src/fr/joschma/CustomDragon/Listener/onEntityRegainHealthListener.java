package fr.joschma.CustomDragon.Listener;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import fr.joschma.CustomDragon.CustomDragon;
import fr.joschma.CustomDragon.Object.Dragon;

public class onEntityRegainHealthListener implements Listener {
	
	CustomDragon pl;
	
	public onEntityRegainHealthListener(CustomDragon pl) {
		super();
		this.pl = pl;
	}

	@EventHandler
	public void onEntityRegainHealt(EntityRegainHealthEvent e) {
		if(e.getRegainReason() == RegainReason.ENDER_CRYSTAL) {
			if(e.getEntityType() == EntityType.ENDER_DRAGON) {
				for(Dragon dragon : pl.getDrm().getDragons()) {
					if(dragon == e.getEntity()) {
						dragon.getBossbar().setProgress(dragon.getDragon().getHealth() / dragon.getDragon().getMaxHealth());
					}
				}
			}
		}
	}
}
