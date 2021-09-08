package fr.joschma.CustomDragon.Object;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;

import fr.joschma.CustomDragon.CustomDragon;

public class Dragon {

	final int health;
	final LivingEntity dragon;
	final String name;
	final CustomDragon pl;
	final BossBar bossbar;

	public Dragon(int health, LivingEntity dragon, String name, BossBar bossbar, CustomDragon pl) {
		super();
		this.health = health;
		this.dragon = dragon;
		this.name = name;
		this.pl = pl;
		this.bossbar = bossbar;
	}
	
	public void stopAbilities() {
	}
	
	public void startAbilities() {
	}

	public LivingEntity getDragon() {
		return dragon;
	}

	public int getHealth() {
		return health;
	}

	public String getName() {
		return name;
	}

	public CustomDragon getPl() {
		return pl;
	}

	public BossBar getBossbar() {
		return bossbar;
	}
}
