package fr.joschma.CustomDragon.Listener;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import fr.joschma.CustomDragon.CustomDragon;
import fr.joschma.CustomDragon.Object.DragonType.Fire;
import fr.joschma.CustomDragon.Object.DragonType.Miner;
import fr.joschma.CustomDragon.Object.DragonType.Speed;
import fr.joschma.CustomDragon.Object.DragonType.Water;

public class onCreatureSpawnListener implements Listener {

	final CustomDragon pl;

	public onCreatureSpawnListener(CustomDragon pl) {
		super();
		this.pl = pl;
	}

	@EventHandler
	public void onCreatureSpawnEvent(CreatureSpawnEvent e) {
		if (e.getEntityType() == EntityType.ENDER_DRAGON) {
			Random rand = new Random();
			int randDragon = rand.nextInt(6);
			BossBar bossBar = Bukkit.createBossBar("null", BarColor.PURPLE, BarStyle.SOLID);
			bossBar.setProgress(1);

			switch (randDragon) {
			case 0:
				// normal dragon
				break;
			case 1:
				pl.getDrm().addDragon(new Miner(500, e.getEntity(), "Miner", bossBar, pl));
				break;
			case 2:
				pl.getDrm().addDragon(new Miner(1000, e.getEntity(), "VoidBorn", bossBar, pl));
				break;
			case 3:
				pl.getDrm().addDragon(new Speed(400, e.getEntity(), "Speed", bossBar, pl));
				break;
			case 4:
				pl.getDrm().addDragon(new Fire(400, e.getEntity(), "Fire", bossBar, pl));
				break;
			case 5:
				pl.getDrm().addDragon(new Water(600, e.getEntity(), "Water", bossBar, pl));
				break;
			default:
				break;
			}
		}
	}
}
