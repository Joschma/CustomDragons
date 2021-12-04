package fr.joschma.CustomDragon.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.joschma.CustomDragon.CustomDragon;

public class SpeedUtils {
	
	public static void speedConverter(LivingEntity dragon, String className, CustomDragon pl) {
		if(pl.getFm().getString("Dragons." + className + ".Speed").toLowerCase().equals("slow")) {
			dragon.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 3));
		} else if(pl.getFm().getString("Dragons." + className + ".Speed").toLowerCase().equals("speed")) {
			dragon.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
		} else {
			Bukkit.broadcastMessage(ChatColor.GRAY + "No speed found set dragon to normal speed");
		}
	}
}
