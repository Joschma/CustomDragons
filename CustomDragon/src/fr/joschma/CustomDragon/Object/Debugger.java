package fr.joschma.CustomDragon.Object;

import org.bukkit.entity.Player;

public class Debugger {
	
	public static void sysPlayer(Player p, String msg) {
		p.sendMessage(msg);
		System.out.println(msg);
	}
}
