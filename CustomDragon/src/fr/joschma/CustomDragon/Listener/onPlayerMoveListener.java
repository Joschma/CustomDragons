package fr.joschma.CustomDragon.Listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.joschma.CustomDragon.CustomDragon;
import fr.joschma.CustomDragon.Object.Dragon;

public class onPlayerMoveListener implements Listener {

	CustomDragon pl;

	public onPlayerMoveListener(CustomDragon pl) {
		super();
		this.pl = pl;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();

		for (Dragon drag : pl.getDrm().getDragons()) {
			LivingEntity dragon = drag.getDragon();

			if (dragon.getWorld() != p.getWorld()) {
				drag.getBossbar().removePlayer(p);
			} else if (dragon.getLocation().distance(p.getLocation()) >= 150) {
				drag.getBossbar().removePlayer(p);
			}
		}
	}
}
