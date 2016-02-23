package de.kekshaus.cookieApi.teleport.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import de.kekshaus.cookieApi.bukkit.CookieApiBukkit;
import de.kekshaus.cookieApi.bukkit.GlobalMessageDB;
import de.kekshaus.cookieApi.teleport.api.TPStreamOutApi;
import de.kekshaus.cookieApi.teleport.database.TeleportHASHDB;

public class TeleportListener implements Listener {
	@EventHandler(ignoreCancelled = true)
	public void playerDeath(PlayerDeathEvent e) {
		if (CookieApiBukkit.isWorldAllowed(e.getEntity().getLocation().getWorld())) {
			TPStreamOutApi.sendDeathBackLocation(e.getEntity());
			TeleportHASHDB.ignoreTeleport.add(e.getEntity());
		}
	}

	@EventHandler
	public void playerConnect(PlayerSpawnLocationEvent e) {
		if (TeleportHASHDB.pendingTeleport.containsKey(e.getPlayer().getName())) {
			Player t = TeleportHASHDB.pendingTeleport.get(e.getPlayer().getName());
			TeleportHASHDB.pendingTeleport.remove(e.getPlayer().getName());
			if ((t == null) || (!t.isOnline())) {
				e.getPlayer().sendMessage("Player is no longer online");
				return;
			}
			TeleportHASHDB.ignoreTeleport.add(e.getPlayer());
			e.setSpawnLocation(t.getLocation());
			sendWarpMSG(e.getPlayer());
		} else if (TeleportHASHDB.pendingTeleportLocations.containsKey(e.getPlayer().getName())) {
			Location l = TeleportHASHDB.pendingTeleportLocations.get(e.getPlayer().getName());
			TeleportHASHDB.ignoreTeleport.add(e.getPlayer());
			e.setSpawnLocation(l);
			sendWarpMSG(e.getPlayer());
		}
	}

	public void sendWarpMSG(final Player p) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(CookieApiBukkit.getInstance(), new Runnable() {
			@Override
			public void run() {
				TeleportHASHDB.ignoreTeleport.remove(p);
				p.sendMessage(GlobalMessageDB.Teleport_Teleport);
			}
		}, 20);

	}
}
