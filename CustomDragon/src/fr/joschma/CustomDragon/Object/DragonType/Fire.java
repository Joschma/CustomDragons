package fr.joschma.CustomDragon.Object.DragonType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import fr.joschma.CustomDragon.CustomDragon;
import fr.joschma.CustomDragon.Object.Debugger;
import fr.joschma.CustomDragon.Object.Dragon;
import fr.joschma.CustomDragon.Utils.SpeedUtils;

public class Fire extends Dragon {

	final CustomDragon pl;
	final int health;
	final LivingEntity dragon;
	final String name;
	final BossBar bossBar;

//	Fire:
//	HP: 400 health
//	Speed: Normal

	public Fire(int health, LivingEntity dragon, String name, BossBar bossBar, CustomDragon pl) {
		super(health, dragon, name, bossBar, pl);
		this.health = health;
		this.dragon = dragon;
		this.name = name;
		this.pl = pl;
		this.bossBar = bossBar;

		dragon.setMaxHealth(health);
		dragon.setHealth(health);
		
		SpeedUtils.speedConverter(dragon, name, pl);

		bossBar.setTitle(ChatColor.GOLD + name);

		for (Entity en : dragon.getNearbyEntities(150, 300, 150)) {
			if (en instanceof Player) {
				Player p = (Player) en;
				bossBar.addPlayer(p);
				Debugger.sysPlayer(p, pl.getFm().getString("Dragons.Fire.SummondMessage"));
			}
		}

		startAbilities();
	}

	boolean stop;
	int taskId;

	@Override
	public void startAbilities() {
		stop = false;

		BukkitScheduler scheduler = pl.getServer().getScheduler();

		taskId = scheduler.scheduleSyncRepeatingTask(pl, new Runnable() {

			int FireVoleyCooldown = 0;
			int FireStormCooldown = 0;
			int SatanicAssistanceCooldown = 0;

			@Override
			public void run() {
				if (stop) {
					scheduler.cancelTask(taskId);
					return;
				}

				FireVoleyCooldown++;
				FireStormCooldown++;
				SatanicAssistanceCooldown++;

				if (FireVoleyCooldown == pl.getFm().getInt("Dragons.Fire.FireVoley.CooldownInSeconds")) {
					doAbilitie("FireVoley");
					FireVoleyCooldown = 0;
				} else if (FireStormCooldown == pl.getFm().getInt("Dragons.Fire.FireStorm.CooldownInSeconds")) {
					doAbilitie("FireStorm");
					FireStormCooldown = 0;
				} else if (SatanicAssistanceCooldown == pl.getFm().getInt("Dragons.Fire.SatanicAssistance.CooldownInSeconds")) {
					doAbilitie("SatanicAssistance");
					SatanicAssistanceCooldown = 0;
				}
			}
		}, 0, 20L);
	}

	public void doAbilitie(String attack) {
		List<Player> players = new ArrayList<Player>();

		for (Entity en : dragon.getNearbyEntities(150, 300, 150)) {
			if (en instanceof Player) {
				players.add((Player) en);
			}
		}

		if (players.isEmpty())
			return;
		
		for (Player p : players) {
			p.sendMessage(pl.getFm().getString("Dragons.Fire." + attack + " .Message"));
		}

		if (attack.equals("FireVoley")) {
//			FireVoley: Shoots 10 arrow in fire in a string. (25 second cooldown).
			Random rand = new Random();

			for (Player p : players) {
				for (int i = 0; i < pl.getFm().getInt("Dragons.Fire.FireVoley.NumberOfArrows"); i++) {
					int distance = rand.nextInt(4);
					Location dLoc = dragon.getLocation().add(distance, 3, distance);
					Location pLoc = p.getLocation().add(new Vector(0, 1, 0));
					Vector direction = pLoc.toVector().subtract(dLoc.toVector());

					Arrow arrow = pLoc.getWorld().spawnArrow(dLoc, direction, 3F, 5F);
					arrow.setGravity(false);
					arrow.setFireTicks(20 * 10);
					arrow.setVisualFire(true);
				}
			}
		} else if (attack.equals("FireStorm")) {
//			Fire Storm: Spawns 8 blazes. (30 second cooldown).
			Random rand = new Random();

			for (Player p : players) {
				for (int i = 0; i < pl.getFm().getInt("Dragons.Fire.FireStorm.NumberOfBlazes"); i++) {
					Location loc = p.getLocation();
					int distance = rand.nextInt(11);

					loc.add(distance, 3, distance);
					Blaze blaze = p.getWorld().spawn(loc, Blaze.class);
				}
			}
		} else if (attack.equals("SatanicAssistance")) {
//			Satanic Assistance: Spawns 3 (large) magma cubes on each player. (45 second cooldown).
			Random rand = new Random();

			for (Player p : players) {
				for (int i = 0; i < pl.getFm().getInt("Dragons.Fire.SatanicAssistance.NumberOfMagma"); i++) {
					Location loc = p.getLocation();
					int distance = rand.nextInt(9);

					loc.add(distance, 3, distance);
					MagmaCube magmaCube = p.getWorld().spawn(loc, MagmaCube.class);
					magmaCube.setSize(3);
				}
			}
		}
	}

	public void stopAbilities() {
		stop = true;
		bossBar.removeAll();
	}

	public BossBar getBossBar() {
		return bossBar;
	}

	public LivingEntity getDragon() {
		return dragon;
	}
}
