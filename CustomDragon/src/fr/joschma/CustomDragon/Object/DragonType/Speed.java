package fr.joschma.CustomDragon.Object.DragonType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import fr.joschma.CustomDragon.CustomDragon;
import fr.joschma.CustomDragon.Object.Dragon;

public class Speed extends Dragon {

	final CustomDragon pl;
	final int health;
	final LivingEntity dragon;
	final String name;
	final BossBar bossBar;

//	Speedy:
//	HP: 400 health
//	Speed: Fast

	public Speed(int health, LivingEntity dragon, String name, BossBar bossBar, CustomDragon pl) {
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

			int time30 = 0;
			int time45 = 0;

			@Override
			public void run() {
				if (stop) {
					scheduler.cancelTask(taskId);
					return;
				}

				time30++;
				time45++;

				if (time30 == 30) {
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

		if (time == 30) {
//			Arrow Rain: Shoots a shower of arrows at everyone. (30 second cooldown)
			for (Player p : players) {
				for (int i = 0; i < 15; i++) {
					Location dLoc = dragon.getLocation();
					Location pLoc = p.getLocation();
					dLoc.setY(dLoc.getY() + 3);
					Arrow arrow = pLoc.getWorld().spawnArrow(dLoc, pLoc.subtract(dLoc).toVector().add(new Vector(0, 1, 0)), 0.6F, 5F);
					arrow.setGravity(false);
				}

				p.sendMessage(ChatColor.GOLD + name + ChatColor.GRAY + " as used " + ChatColor.YELLOW + "Arrow Rain");
			}
		} else if (time == 45) {
//			Distraction: Spawns 2 zombies on each player. (45 second cooldown)
			Random rand = new Random();

			for (Player p : players) {
				for (int i = 0; i < 2; i++) {
					Location loc = p.getLocation();
					int distance = rand.nextInt(6);

					loc.add(distance, 3, distance);
					Zombie zombie = p.getWorld().spawn(loc, Zombie.class);
					zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
				}

				p.sendMessage(ChatColor.GOLD + name + ChatColor.GRAY + " as used " + ChatColor.YELLOW + "Distraction");
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
