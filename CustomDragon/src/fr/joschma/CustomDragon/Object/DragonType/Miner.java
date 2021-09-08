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
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import fr.joschma.CustomDragon.CustomDragon;
import fr.joschma.CustomDragon.Object.Dragon;

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
		dragon.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 3));
		
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
//			Fire in the hole!: Drops tnt on everyone (that does not damage blocks) (30 second cooldown).
			Random rand = new Random();
			
			for (Player p : players) {
				for(int i = 0; i < 10; i++) {
					Location loc = p.getLocation();
					int range = rand.nextInt(6);
					loc.add(range, 3, range);
					TNTPrimed tnt = (TNTPrimed) p.getWorld().spawn(loc, TNTPrimed.class);
					pl.getTntm().addTnt(tnt);
				}
				
				p.sendMessage(ChatColor.GOLD + name + ChatColor.GRAY + " as used " + ChatColor.YELLOW + "Fire in the hole");
			}
		} else if (time == 45) {
//			Gold Rush: Spawns 2 zombie pigmen armed with gold pickaxes and gold helmet and gold chestplate on each player. (45 second cooldown).

			for (Player p : players) {
				Random rand = new Random();

				for (int i = 0; i < 3; i++) {
					Location loc = p.getLocation();
					int distance = rand.nextInt(6);
					
					loc.add(distance, 3, distance);

					PigZombie pigman = loc.getWorld().spawn(loc, PigZombie.class);
					pigman.setAngry(true);
					
					pigman.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
					pigman.getEquipment().setChestplate(new ItemStack(Material.GOLDEN_CHESTPLATE));
					pigman.getEquipment().setItemInMainHand(new ItemStack(Material.GOLDEN_AXE));
				}
				
				p.sendMessage(ChatColor.GOLD + name + ChatColor.GRAY + " as used " + ChatColor.YELLOW + "Gold Rush");
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
