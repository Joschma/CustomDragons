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
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import fr.joschma.CustomDragon.CustomDragon;
import fr.joschma.CustomDragon.Object.Debugger;
import fr.joschma.CustomDragon.Object.Dragon;
import fr.joschma.CustomDragon.Utils.SpeedUtils;

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
		
		SpeedUtils.speedConverter(dragon, name, pl);

		bossBar.setTitle(ChatColor.GOLD + name);

		for (Entity en : dragon.getNearbyEntities(150, 300, 150)) {
			if (en instanceof Player) {
				Player p = (Player) en;
				bossBar.addPlayer(p);
				Debugger.sysPlayer(p, pl.getFm().getString("Dragons.Water.SummondMessage"));
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

			int Spearing = 0;
			int PoseidonWrath = 0;
			int AncientGuardians = 0;

			@Override
			public void run() {
				if (stop) {
					scheduler.cancelTask(taskId);
					return;
				}

				Spearing++;
				PoseidonWrath++;
				AncientGuardians++;

				if (Spearing == pl.getFm().getInt("Dragons.Water.Spearing.CooldownInSeconds")) {
					doAbilitie("Spearing");
					Spearing = 0;
				} else if (PoseidonWrath == pl.getFm().getInt("Dragons.Water.PoseidonWrath.CooldownInSeconds")) {
					doAbilitie("PoseidonWrath");
					PoseidonWrath = 0;
				} else if (AncientGuardians == pl.getFm().getInt("Dragons.Water.AncientGuardians.CooldownInSeconds")) {
					doAbilitie("AncientGuardians");
					AncientGuardians = 0;
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
			p.sendMessage(pl.getFm().getString("Dragons.Water." + attack + " .Message"));
		}
		
		if (attack.equals("Spearing")) {
//			Spearing: Drops a trident on everyone. (25 second cooldown). (Trident can’t be picked up)
			Random rand = new Random();
			
			for (Player p : players) {
				for (int i = 0; i < pl.getFm().getInt("Dragons.Water.AncientGuardians.NumberOfTrident"); i++) {
					int distance = rand.nextInt(4);
					Location dLoc = dragon.getLocation().add(distance, 3, distance);
					Location pLoc = p.getLocation().add(new Vector(0, 1, 0));
					Vector direction = pLoc.toVector().subtract(dLoc.toVector());
					
					Trident trident = pLoc.getWorld().spawn(dLoc, Trident.class);
					
					trident.setGravity(false);
					trident.setVelocity(direction);
					trident.setPickupStatus(PickupStatus.DISALLOWED);
				}
			}
		} else if (attack.equals("PoseidonWrath")) {
//			Poseidon’s Wrath: Everyone gets struck by lightning. (30 second cooldown)
			for (Player p : players) {
				p.getLocation().getWorld().spawnEntity(p.getLocation(), EntityType.LIGHTNING);
			}
		} else if (attack.equals("AncientGuardians")) {
//			Ancient Guardians: Spawns 3 guardians on each player. (45 second cooldown)
			Random rand = new Random();

			for (Player p : players) {
				for (int i = 0; i < pl.getFm().getInt("Dragons.Water.AncientGuardians.NumberOfGuardians"); i++) {
					Location loc = p.getLocation();
					int distance = rand.nextInt(9);

					loc.add(distance, 3, distance);
					Guardian guardian = p.getWorld().spawn(loc, Guardian.class);
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
