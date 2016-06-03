package de.nlinz.xeonSuite.teleport.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import de.nlinz.xeonSuite.bukkit.XeonSuiteBukkit;
import de.nlinz.xeonSuite.bukkit.GlobalMessageDB;
import de.nlinz.xeonSuite.teleport.api.TPStreamOutApi;
import de.nlinz.xeonSuite.teleport.database.TeleportDataTable;

public class TeleportListener implements Listener {
	@EventHandler(ignoreCancelled = true)
	public void playerDeath(PlayerDeathEvent e) {
		if (XeonSuiteBukkit.isWorldAllowed(e.getEntity().getLocation().getWorld())) {
			TPStreamOutApi.sendDeathBackLocation(e.getEntity());
			TeleportDataTable.ignoreTeleport.add(e.getEntity());
		}
	}

	@EventHandler
	public void playerConnect(PlayerSpawnLocationEvent e) {
		if (TeleportDataTable.pendingTeleport.containsKey(e.getPlayer().getName())) {
			Player t = TeleportDataTable.pendingTeleport.get(e.getPlayer().getName());
			TeleportDataTable.pendingTeleport.remove(e.getPlayer().getName());
			if ((t == null) || (!t.isOnline())) {
				e.getPlayer().sendMessage("Player is no longer online");
				return;
			}
			TeleportDataTable.ignoreTeleport.add(e.getPlayer());
			e.setSpawnLocation(t.getLocation());
			sendWarpMSG(e.getPlayer());
		} else if (TeleportDataTable.pendingTeleportLocations.containsKey(e.getPlayer().getName())) {
			Location l = TeleportDataTable.pendingTeleportLocations.get(e.getPlayer().getName());
			TeleportDataTable.ignoreTeleport.add(e.getPlayer());
			e.setSpawnLocation(l);
			sendWarpMSG(e.getPlayer());
		}
	}

	public void sendWarpMSG(final Player p) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(XeonSuiteBukkit.getInstance(), new Runnable() {
			@Override
			public void run() {
				TeleportDataTable.ignoreTeleport.remove(p);
				p.sendMessage(GlobalMessageDB.Teleport_Teleport);
			}
		}, 20);

	}
}
