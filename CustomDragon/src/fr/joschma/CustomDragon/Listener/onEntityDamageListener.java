package fr.joschma.CustomDragon.Listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import fr.joschma.CustomDragon.CustomDragon;
import fr.joschma.CustomDragon.Object.Dragon;

public class onEntityDamageListener implements Listener {

	CustomDragon pl;

	public onEntityDamageListener(CustomDragon pl) {
		super();
		this.pl = pl;
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (e.getEntityType() == EntityType.ENDER_DRAGON) {
			List<Dragon> dragons = new ArrayList<Dragon>();
			dragons.addAll(pl.getDrm().getDragons());

			for (Dragon dragon : dragons) {
				if (e.getEntity() == dragon.getDragon()) {
					dragon.getBossbar().setProgress(dragon.getDragon().getHealth() / dragon.getDragon().getMaxHealth());
				}
			}
		}
	}
}
