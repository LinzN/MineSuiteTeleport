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
import de.linzn.mineSuite.core.configurations.YamlFiles.GeneralLanguage;
import de.linzn.mineSuite.core.database.hashDatabase.PendingTeleportsData;
import de.linzn.mineSuite.teleport.TeleportPlugin;
import de.linzn.mineSuite.teleport.socket.JClientTeleportOutput;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class TeleportListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void playerDeath(PlayerDeathEvent e) {
        String serverName = MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.BUNGEE_SERVER_NAME;
        if (!MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.DISABLED_WORLDS.contains(e.getEntity().getLocation().getWorld().getName())) {
            JClientTeleportOutput.sendDeathBackLocation(e.getEntity(), serverName);
            PendingTeleportsData.ignoreActions.add(e.getEntity().getUniqueId());
        }
    }

    @EventHandler
    public void playerConnect(PlayerSpawnLocationEvent e) {
        if (PendingTeleportsData.pendingLocations.containsKey(e.getPlayer().getUniqueId())) {
            Location l = PendingTeleportsData.pendingLocations.get(e.getPlayer().getUniqueId());
            PendingTeleportsData.ignoreActions.add(e.getPlayer().getUniqueId());
            e.getPlayer().setFallDistance(0F);
            e.setSpawnLocation(l);
        } else if (!e.getPlayer().hasPlayedBefore()) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(TeleportPlugin.inst(), () -> {
                String serverName = MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.BUNGEE_SERVER_NAME;
                JClientTeleportOutput.teleportToSpawnType(e.getPlayer().getUniqueId(), "serverSpawn", serverName, e.getPlayer().getWorld().getName());
            }, 10);
        }
    }

    @EventHandler
    public void onPlayerVoidEvent(PlayerMoveEvent e) {
        if(!MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.VOID_DISABLED_WORLDS.contains(e.getPlayer().getWorld().getName())) {
            if (e.getTo().getBlockY() < 0) {
                e.setCancelled(true);
                e.getPlayer().setFallDistance(0F);
                String serverName = MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.BUNGEE_SERVER_NAME;
                JClientTeleportOutput.teleportToSpawnType(e.getPlayer().getUniqueId(), "serverSpawn", serverName, e.getPlayer().getWorld().getName());
            }
        }
    }
}
