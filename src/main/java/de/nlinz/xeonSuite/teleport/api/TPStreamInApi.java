package de.nlinz.xeonSuite.teleport.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.nlinz.xeonSuite.bukkit.XeonSuiteBukkit;
import de.nlinz.xeonSuite.bukkit.utils.LocationUtil;
import de.nlinz.xeonSuite.bukkit.utils.languages.TeleportLanguage;
import de.nlinz.xeonSuite.bukkit.utils.tables.TeleportDataTable;

public class TPStreamInApi {

	public static void teleportPlayerToPlayer(final String player, String target) {
		Player p = Bukkit.getPlayer(player);
		Player t = Bukkit.getPlayer(target);
		if (p != null) {
			p.teleport(t);
		} else {
			TeleportDataTable.pendingTeleport.put(player, t);
			// clear pending teleport if they dont connect
			Bukkit.getScheduler().runTaskLaterAsynchronously(XeonSuiteBukkit.getInstance(), new Runnable() {
				@Override
				public void run() {
					if (TeleportDataTable.pendingTeleport.containsKey(player)) {
						TeleportDataTable.pendingTeleport.remove(player);
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
						p.sendMessage(TeleportLanguage.Teleport_Teleport);

					} else {
						p.sendMessage(ChatColor.RED + "Unable to find a safe location for teleport.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				p.teleport(t);
				p.sendMessage(TeleportLanguage.Teleport_Teleport);
				return;
			}
		} else {
			TeleportDataTable.pendingTeleportLocations.put(player, t);
			// clear pending teleport if they dont connect
			Bukkit.getScheduler().runTaskLaterAsynchronously(XeonSuiteBukkit.getInstance(), new Runnable() {
				@Override
				public void run() {
					if (TeleportDataTable.pendingTeleportLocations.containsKey(player)) {
						TeleportDataTable.pendingTeleportLocations.remove(player);
					}
				}
			}, 100L);
		}

	}

}
