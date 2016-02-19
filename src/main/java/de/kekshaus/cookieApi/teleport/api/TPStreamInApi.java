package de.kekshaus.cookieApi.teleport.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.kekshaus.cookieApi.bukkit.MessageDB;
import de.kekshaus.cookieApi.bukkit.CookieApiBukkit;
import de.kekshaus.cookieApi.bukkit.utils.LocationUtil;
import de.kekshaus.cookieApi.teleport.database.TeleportHASHDB;

public class TPStreamInApi {

	public static void teleportPlayerToPlayer(final String player, String target) {
		Player p = Bukkit.getPlayer(player);
		Player t = Bukkit.getPlayer(target);
		if (p != null) {
			p.teleport(t);
		} else {
			TeleportHASHDB.pendingTeleport.put(player, t);
			// clear pending teleport if they dont connect
			Bukkit.getScheduler().runTaskLaterAsynchronously(CookieApiBukkit.getInstance(), new Runnable() {
				@Override
				public void run() {
					if (TeleportHASHDB.pendingTeleport.containsKey(player)) {
						TeleportHASHDB.pendingTeleport.remove(player);
					}

				}
			}, 100L);
		}
	}

	public static void teleportToLocation(final String player, String world, double x, double y, double z, float yaw,
			float pitch) {
		World w = Bukkit.getWorld(world);
		Location t;

		if (w != null) {
			t = new Location(w, x, y, z, yaw, pitch);
		} else {
			w = Bukkit.getWorlds().get(0);
			t = w.getSpawnLocation();
		}
		Player p = Bukkit.getPlayer(player);
		if (p != null) {
			// Check if Block is safe
			if (LocationUtil.isBlockUnsafe(t.getWorld(), t.getBlockX(), t.getBlockY(), t.getBlockZ())) {
				try {
					Location l = LocationUtil.getSafeDestination(p, t);
					if (l != null) {
						p.teleport(l);
						p.sendMessage(MessageDB.Teleport_Teleport);

					} else {
						p.sendMessage(ChatColor.RED + "Unable to find a safe location for teleport.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				p.teleport(t);
				p.sendMessage(MessageDB.Teleport_Teleport);
				return;
			}
		} else {
			TeleportHASHDB.pendingTeleportLocations.put(player, t);
			// clear pending teleport if they dont connect
			Bukkit.getScheduler().runTaskLaterAsynchronously(CookieApiBukkit.getInstance(), new Runnable() {
				@Override
				public void run() {
					if (TeleportHASHDB.pendingTeleportLocations.containsKey(player)) {
						TeleportHASHDB.pendingTeleportLocations.remove(player);
					}
				}
			}, 100L);
		}

	}

}
