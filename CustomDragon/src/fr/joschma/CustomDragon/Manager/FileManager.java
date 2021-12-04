package fr.joschma.CustomDragon.Manager;

import org.bukkit.ChatColor;

import fr.joschma.CustomDragon.CustomDragon;

public class FileManager {

	CustomDragon pl;

	public FileManager(CustomDragon pl) {
		super();
		this.pl = pl;
	}

	public String getString(String path) {
		path = path.replace(" ", "");
		return convert(pl.getConfig().getString(path));
	}

	public int getInt(String path) {
		return pl.getConfig().getInt(path);
	}

	public String convert(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		return message;
	}
}
