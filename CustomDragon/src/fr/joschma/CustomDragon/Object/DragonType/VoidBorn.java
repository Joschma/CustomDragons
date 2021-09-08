package fr.joschma.CustomDragon.Object.DragonType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import fr.joschma.CustomDragon.CustomDragon;
import fr.joschma.CustomDragon.Object.Dragon;

public class VoidBorn extends Dragon {

	final CustomDragon pl;
	final int health;
	final LivingEntity dragon;
	final String name;
	final BossBar bossBar;

	public VoidBorn(int health, LivingEntity dragon, String name, BossBar bossBar, CustomDragon pl) {
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
//			Powerful Presence: Players get the nausea effect for 10 seconds (30 second cooldown).
			for (Player p : players) {
				p.sendMessage(ChatColor.GOLD + name + ChatColor.GRAY + " as used " + ChatColor.YELLOW + "Powerful Presence");
				p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 10, 0));
			}
		} else if (time == 45) {
			Random rand = new Random();
			int number = rand.nextInt(2);

			if (number == 0) {
//				Confusion: All players are teleported as if they had just eaten a chorus fruit. (45 second cooldown).
				for (Player p : players) {
					p.sendMessage(ChatColor.GOLD + name + ChatColor.GRAY + " as used " + ChatColor.YELLOW + "Confusion");
					PlayerItemConsumeEvent event = new PlayerItemConsumeEvent(p, new ItemStack(Material.CHORUS_FRUIT));
					Bukkit.getPluginManager().callEvent(event);
				}
			} else {
//				Corrupt Minions: Spawns 2 zombies with black leather armour (protection 1) and wooden swords (sharpness 1) on each player. (45 second cooldown).
				for (Player p : players) {
					p.sendMessage(ChatColor.GOLD + name + ChatColor.GRAY + " as used " + ChatColor.YELLOW + "Corrupt Minions");
					for (int i = 0; i < 2; i++) {
						Location loc = p.getLocation();
						int distance = rand.nextInt(6);

						loc.add(distance, 3, distance);

						Zombie zombie = p.getWorld().spawn(loc, Zombie.class);
						
						ItemStack[] is = { new ItemStack(Material.LEATHER_BOOTS),
								new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_CHESTPLATE), 
								new ItemStack(Material.LEATHER_HELMET) };
						
						for(ItemStack it : is) {
							it.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
						}
						
						zombie.getEquipment().setArmorContents(is);

						ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
						sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);

						zombie.getEquipment().setItemInMainHand(sword);
					}
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
