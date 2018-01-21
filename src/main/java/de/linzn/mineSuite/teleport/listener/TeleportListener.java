/*
 * Copyright (C) 2018. MineGaming - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 *  You should have received a copy of the LGPLv3 license with
 *  this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.mineSuite.teleport.listener;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.core.database.hashDatabase.TeleportDataTable;
import de.linzn.mineSuite.teleport.TeleportPlugin;
import de.linzn.mineSuite.teleport.socket.JClientTeleportOutput;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class TeleportListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void playerDeath(PlayerDeathEvent e) {
        if (!MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.DISABLED_WORLDS.contains(e.getEntity().getLocation().getWorld().getName())) {
            JClientTeleportOutput.sendDeathBackLocation(e.getEntity());
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
        Bukkit.getScheduler().runTaskLaterAsynchronously(TeleportPlugin.inst(), () -> {
            TeleportDataTable.ignoreTeleport.remove(p);
            p.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.Teleport_Teleport);
        }, 20);

    }
}
