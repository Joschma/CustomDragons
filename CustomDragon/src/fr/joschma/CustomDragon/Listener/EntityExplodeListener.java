package fr.joschma.CustomDragon.Listener;

import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import fr.joschma.CustomDragon.CustomDragon;

public class EntityExplodeListener implements Listener {

	CustomDragon pl;

	public EntityExplodeListener(CustomDragon pl) {
		super();
		this.pl = pl;
	}

	@EventHandler
	public void EntityExplode(EntityExplodeEvent e) {
		if (!(e.getEntity() instanceof TNTPrimed))
			return;

		TNTPrimed tnt = (TNTPrimed) e.getEntity();

		if (pl.getTntm().getTnts().contains(tnt)) {
			e.blockList().clear();
			e.setCancelled(true);
			pl.getTntm().rmvTnt(tnt);
		}
	}
}
