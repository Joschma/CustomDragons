package fr.joschma.CustomDragon.Listener;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import com.google.common.collect.Lists;

import fr.joschma.CustomDragon.CustomDragon;
import fr.joschma.CustomDragon.Object.DragonType.Fire;
import fr.joschma.CustomDragon.Object.DragonType.Miner;
import fr.joschma.CustomDragon.Object.DragonType.Speed;
import fr.joschma.CustomDragon.Object.DragonType.VoidBorn;
import fr.joschma.CustomDragon.Object.DragonType.Water;

public class onCreatureSpawnListener implements Listener {

	final CustomDragon pl;

	public onCreatureSpawnListener(CustomDragon pl) {
		super();
		this.pl = pl;
	}

	@EventHandler
	public void onCreatureSpawnEvent(CreatureSpawnEvent e) {
		LivingEntity dragon = e.getEntity();
		if (e.getEntityType() == EntityType.ENDER_DRAGON) {
			Random rand = new Random();
			int randDragon = rand.nextInt(6);
			BossBar bossBar = Bukkit.createBossBar(ChatColor.GOLD + "Ender Dragon", BarColor.PURPLE, BarStyle.SOLID);
			bossBar.setProgress(1);

			List<KeyedBossBar> myList = Lists.newArrayList(Bukkit.getBossBars());

			switch (randDragon) {
			case 0:
				for (Entity en : dragon.getNearbyEntities(150, 300, 150)) {
					if (en instanceof Player) {
						Player p = (Player) en;
						p.sendMessage(ChatColor.GOLD + "Ender Dragon" + ChatColor.WHITE + " was summoned");
					}
				}
				break;
			case 1:
				pl.getDrm().addDragon(new Miner(pl.getConfig().getInt("Dragons.Miner.Hp"), dragon,
						pl.getConfig().getString("Dragons.Miner.Name"), bossBar, pl));
				break;
			case 2:
				pl.getDrm().addDragon(new VoidBorn(pl.getConfig().getInt("Dragons.VoidBorn.Hp"), dragon,
						pl.getConfig().getString("Dragons.VoidBorn.Name"), bossBar, pl));
				break;
			case 3:
				pl.getDrm().addDragon(new Speed(pl.getConfig().getInt("Dragons.Speed.Hp"), dragon,
						pl.getConfig().getString("Dragons.Speed.Name"), bossBar, pl));
				break;
			case 4:
				pl.getDrm().addDragon(new Fire(pl.getConfig().getInt("Dragons.Fire.Hp"), dragon,
						pl.getConfig().getString("Dragons.Fire.Name"), bossBar, pl));
				break;
			case 5:
				pl.getDrm().addDragon(new Water(pl.getConfig().getInt("Dragons.Water.Hp"), dragon,
						pl.getConfig().getString("Dragons.Water.Name"), bossBar, pl));
				break;
			default:
				break;
			}

			for (KeyedBossBar keyedBossBar : myList) {
				if (keyedBossBar.getColor() == BarColor.PINK && keyedBossBar.getTitle().equals("Ender Dragon")
						&& keyedBossBar.getStyle() == BarStyle.SOLID) {
					keyedBossBar.removeAll();
				}
			}
		}
	}
}
