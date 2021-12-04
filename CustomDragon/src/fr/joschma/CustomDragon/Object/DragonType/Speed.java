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
import fr.joschma.CustomDragon.Object.Debugger;
import fr.joschma.CustomDragon.Object.Dragon;
import fr.joschma.CustomDragon.Utils.SpeedUtils;

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
		
		SpeedUtils.speedConverter(dragon, name, pl);

		bossBar.setTitle(ChatColor.GOLD + name);

		for (Entity en : dragon.getNearbyEntities(150, 300, 150)) {
			if (en instanceof Player) {
				Player p = (Player) en;
				bossBar.addPlayer(p);
				Debugger.sysPlayer(p, pl.getFm().getString("Dragons.Speed.SummondMessage"));
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

			int ArrowRainCooldown = 0;
			int DistractionCooldown = 0;

			@Override
			public void run() {
				if (stop) {
					scheduler.cancelTask(taskId);
					return;
				}
				
				ArrowRainCooldown++;
				DistractionCooldown++;

				if (ArrowRainCooldown == pl.getFm().getInt("Dragons.Speed.ArrowRain.CooldownInSeconds")) {
					doAbilitie("ArrowRain");
					ArrowRainCooldown = 0;
				} else if (DistractionCooldown == pl.getFm().getInt("Dragons.Speed.Distraction.CooldownInSeconds")) {
					doAbilitie("Distraction");
					DistractionCooldown = 0;
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
			p.sendMessage(pl.getFm().getString("Dragons.Speed." + attack + " .Message"));
		}
		
		if (attack.equals("ArrowRain")) {
//			Arrow Rain: Shoots a shower of arrows at everyone. (30 second cooldown)
			Random rand = new Random();
			
			for (Player p : players) {
				for (int i = 0; i < pl.getFm().getInt("Dragons.Speed.ArrowRain.NumberOfArrows"); i++) {
					int distance = rand.nextInt(4);
					Location dLoc = dragon.getLocation().add(distance, 3, distance);
					Location pLoc = p.getLocation().add(new Vector(0, 1, 0));
					Vector direction = pLoc.toVector().subtract(dLoc.toVector());
					
					Arrow arrow = pLoc.getWorld().spawnArrow(dLoc, direction, 5F, 5F);
                    arrow.setGravity(false);
				}
			}
		} else if (attack.equals("Distraction")) {
//			Distraction: Spawns 2 zombies on each player. (45 second cooldown)
			Random rand = new Random();

			for (Player p : players) {
				for (int i = 0; i < pl.getFm().getInt("Dragons.Speed.Distraction.NumberOfZombies"); i++) {
					Location loc = p.getLocation();
					int distance = rand.nextInt(6);

					loc.add(distance, 3, distance);
					Zombie zombie = p.getWorld().spawn(loc, Zombie.class);
					zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
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
