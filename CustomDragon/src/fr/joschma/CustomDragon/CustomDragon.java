package fr.joschma.CustomDragon;

import org.bukkit.plugin.java.JavaPlugin;

import fr.joschma.CustomDragon.Listener.EnderDragonChangePhaseListener;
import fr.joschma.CustomDragon.Listener.EntityExplodeListener;
import fr.joschma.CustomDragon.Listener.onCreatureSpawnListener;
import fr.joschma.CustomDragon.Listener.onEntityDamageListener;
import fr.joschma.CustomDragon.Listener.onEntityDeath;
import fr.joschma.CustomDragon.Listener.onEntityRegainHealthListener;
import fr.joschma.CustomDragon.Listener.onPlayerMoveListener;
import fr.joschma.CustomDragon.Manager.DragonManager;
import fr.joschma.CustomDragon.Manager.FileManager;
import fr.joschma.CustomDragon.Manager.TntManager;
import fr.joschma.CustomDragon.Object.Dragon;

public class CustomDragon extends JavaPlugin {
	
	DragonManager drm = new DragonManager();
	TntManager tntm = new TntManager();
	FileManager fm = new FileManager(this);

	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		getServer().getPluginManager().registerEvents(new EntityExplodeListener(this), this);
		getServer().getPluginManager().registerEvents(new onCreatureSpawnListener(this), this);
		getServer().getPluginManager().registerEvents(new onEntityDeath(this), this);
		getServer().getPluginManager().registerEvents(new onEntityDamageListener(this), this);
		getServer().getPluginManager().registerEvents(new onPlayerMoveListener(this), this);
		getServer().getPluginManager().registerEvents(new onEntityRegainHealthListener(this), this);
		getServer().getPluginManager().registerEvents(new EnderDragonChangePhaseListener(this), this);
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		for(Dragon dragon : getDrm().getDragons()) {
			dragon.getBossbar().removeAll();
			dragon.getDragon().setHealth(0);
		}
		super.onDisable();
	}
	
	public DragonManager getDrm() {
		return drm;
	}

	public TntManager getTntm() {
		return tntm;
	}

	public FileManager getFm() {
		return fm;
	}	
	
//		Ender Dragon (vanilla):
//		HP: 100 health
//		Speed: normal
//		Abilities: none, just the normal dragon breath.
//		Drops: Dragon egg to the person who got final blow on the dragon. 25% chance of getting an elytra. (For anyone who hit the dragon even 1 time).
}
