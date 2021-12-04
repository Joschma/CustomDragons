 package fr.joschma.CustomDragon.Object.DragonType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import fr.joschma.CustomDragon.CustomDragon;
import fr.joschma.CustomDragon.Object.Debugger;
import fr.joschma.CustomDragon.Object.Dragon;
import fr.joschma.CustomDragon.Utils.SpeedUtils;

public class Miner extends Dragon {

	final CustomDragon pl;
	final int health;
	final LivingEntity dragon;
	final String name;
	final BossBar bossBar;

	public Miner(int health, LivingEntity dragon, String name, BossBar bossBar, CustomDragon pl) {
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

	public void startAbilities() {
		stop = false;

		BukkitScheduler scheduler = pl.getServer().getScheduler();

		taskId = scheduler.scheduleSyncRepeatingTask(pl, new Runnable() {

			int FireInTheHoleCooldown = 0;
			int GoldRushCooldown = 0;

			@Override
			public void run() {
				if (stop) {
					scheduler.cancelTask(taskId);
					return;
				}

				FireInTheHoleCooldown++;
				GoldRushCooldown++;
				
				if (FireInTheHoleCooldown == pl.getFm().getInt("Dragons.Miner.FireInTheHole.CooldownInSeconds")) {
					doAbilitie("FireInTheHole");
					FireInTheHoleCooldown = 0;
				} else if (GoldRushCooldown == pl.getFm().getInt("Dragons.Miner.GoldRush.CooldownInSeconds")) {
					doAbilitie("GoldRush");
					GoldRushCooldown = 0;
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
			p.sendMessage(pl.getFm().getString("Dragons.Miner." + attack + " .Message"));
		}
		
		if (attack.equals("FireInTheHole")) {
//			Fire in the hole: Drops 10 tnt on everyone (that does not damage blocks) (30 second cooldown).
			Random rand = new Random();
			
			for (Player p : players) {
				for(int i = 0; i < pl.getFm().getInt("Dragons.Fire.FireVoley.NumberOfTnt"); i++) {
					Location loc = p.getLocation();
					int range = rand.nextInt(6);
					loc.add(range, 3, range);
					
					TNTPrimed tnt = (TNTPrimed) p.getWorld().spawn(loc, TNTPrimed.class);
					pl.getTntm().addTnt(tnt);
				}
			}
		} else if (attack.equals("GoldRush")) {
//			Gold Rush: Spawns 2 zombie pigmen armed with gold pickaxes and gold helmet and gold chestplate on each player. (45 second cooldown).
			for (Player p : players) {
				Random rand = new Random();

				for (int i = 0; i < pl.getFm().getInt("Dragons.Fire.FireVoley.NumberOfPigmen"); i++) {
					Location loc = p.getLocation();
					int distance = rand.nextInt(6);
					loc.add(distance, 3, distance);

					ZombieVillager zombieVillager = loc.getWorld().spawn(loc, ZombieVillager.class);
					zombieVillager.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
					zombieVillager.getEquipment().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
					zombieVillager.getEquipment().setItemInMainHand(new ItemStack(Material.GOLDEN_AXE));
				}
			}
		}
	}

	public void stopAbilities() {
		stop = true;
	}

	public BossBar getBossBar() {
		return bossBar;
	}

	public LivingEntity getDragon() {
		return dragon;
	}
}
