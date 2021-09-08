package fr.joschma.CustomDragon.Object.DragonType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import fr.joschma.CustomDragon.CustomDragon;
import fr.joschma.CustomDragon.Object.Dragon;

public class Water extends Dragon {

	final CustomDragon pl;
	final int health;
	final LivingEntity dragon;
	final String name;
	final BossBar bossBar;

//	Water:
//	HP: 600 health
//	Speed: Normal

	public Water(int health, LivingEntity dragon, String name, BossBar bossBar, CustomDragon pl) {
		super(health, dragon, name, bossBar, pl);
		this.health = health;
		this.dragon = dragon;
		this.name = name;
		this.pl = pl;
		this.bossBar = bossBar;

		dragon.setMaxHealth(health);
		dragon.setHealth(health);
		dragon.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));

		bossBar.setTitle(ChatColor.GOLD + name);

		for (Entity en : dragon.getNearbyEntities(150, 300, 150)) {
			if (en instanceof Player) {
				Player p = (Player) en;
				bossBar.addPlayer(p);
				p.sendMessage("You have summond a " + ChatColor.GOLD + name + ChatColor.WHITE + " dragon");
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

			int time25 = 0;
			int time30 = 0;
			int time45 = 0;

			@Override
			public void run() {
				if (stop) {
					scheduler.cancelTask(taskId);
					return;
				}

				time25++;
				time30++;
				time45++;

				if (time25 == 25) {
					doAbilitie(time25);
					time25 = 0;
				} else if (time30 == 30) {
					doAbilitie(time30);
					time30 = 0;
				} else if (time45 == 45) {
					doAbilitie(time45);
					time45 = 0;
				}
			}
		}, 0, 20L);
	}

	public void doAbilitie(int time) {
		List<Player> players = new ArrayList<Player>();

		for (Entity en : dragon.getNearbyEntities(150, 300, 150)) {
			if (en instanceof Player) {
				players.add((Player) en);
			}
		}

		if (players.isEmpty())
			return;
		if (time == 25) {
//			Spearing: Drops a trident on everyone. (25 second cooldown). (Trident can’t be picked up)
			for (Player p : players) {
				for (int i = 0; i < 15; i++) {
					Location dLoc = dragon.getLocation();
					Location pLoc = p.getLocation();
					dLoc.setY(dLoc.getY() + 3);
					Trident trident = pLoc.getWorld().spawn(dLoc, Trident.class);
					trident.setGravity(false);
					trident.setVelocity(pLoc.subtract(dLoc).toVector());
					trident.setPickupStatus(PickupStatus.DISALLOWED);
				}
				p.sendMessage(ChatColor.GOLD + name + ChatColor.GRAY + " as used " + ChatColor.YELLOW + "Spearing");
			}
		} else if (time == 30) {
//			Poseidon’s Wrath: Everyone gets struck by lightning. (30 second cooldown).
			for (Player p : players) {
				p.getLocation().getWorld().spawnEntity(p.getLocation(), EntityType.LIGHTNING);
				
				p.sendMessage(ChatColor.GOLD + name + ChatColor.GRAY + " as used " + ChatColor.YELLOW + "Poseidon’s Wrath");
			}
		} else if (time == 45) {
//			Ancient Guardians: Spawns 3 guardians on each player. (45 second cooldown).
			Random rand = new Random();

			for (Player p : players) {
				for (int i = 0; i < 3; i++) {
					Location loc = p.getLocation();
					int distance = rand.nextInt(9);

					loc.add(distance, 3, distance);
					Guardian guardian = p.getWorld().spawn(loc, Guardian.class);
				}

				p.sendMessage(ChatColor.GOLD + name + ChatColor.GRAY + " as used " + ChatColor.YELLOW + "Ancient Guardians");
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
