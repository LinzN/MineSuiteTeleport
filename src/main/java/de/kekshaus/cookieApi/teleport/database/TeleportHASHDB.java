package de.kekshaus.cookieApi.teleport.database;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportHASHDB {
	public static HashMap<String, Player> pendingTeleport = new HashMap<String, Player>();
	public static HashMap<String, Location> pendingTeleportLocations = new HashMap<String, Location>();
	public static HashSet<Player> ignoreTeleport = new HashSet<Player>();
	public static HashMap<Player, Location> lastTeleportLocation = new HashMap<Player, Location>();

	public static void RemovePlayer(Player player) {
		pendingTeleport.remove(player.getName());
		pendingTeleportLocations.remove(player.getName());
		ignoreTeleport.remove(player);
		lastTeleportLocation.remove(player);
	}

}