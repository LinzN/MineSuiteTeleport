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

package de.linzn.mineSuite.teleport.api;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.core.database.hashDatabase.TeleportDataTable;
import de.linzn.mineSuite.core.utils.LocationUtil;
import de.linzn.mineSuite.teleport.TeleportPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;


public class TeleportManager {

    public static void teleportPlayerToPlayer(final String player, String target) {
        Player p = Bukkit.getPlayer(player);
        Player t = Bukkit.getPlayer(target);
        if (p != null) {
            p.teleport(t);
        } else {
            TeleportDataTable.pendingTeleport.put(player, t);
            // clear pending teleport if they dont connect
            Bukkit.getScheduler().runTaskLaterAsynchronously(TeleportPlugin.inst(), () -> {
                if (TeleportDataTable.pendingTeleport.containsKey(player)) {
                    TeleportDataTable.pendingTeleport.remove(player);
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
                        p.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.Teleport_Teleport);

                    } else {
                        p.sendMessage(ChatColor.RED + "Unable to find a safe location for teleport.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                p.teleport(t);
                p.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.Teleport_Teleport);
                return;
            }
        } else {
            TeleportDataTable.pendingTeleportLocations.put(player, t);
            // clear pending teleport if they dont connect
            Bukkit.getScheduler().runTaskLaterAsynchronously(TeleportPlugin.inst(), () -> {
                if (TeleportDataTable.pendingTeleportLocations.containsKey(player)) {
                    TeleportDataTable.pendingTeleportLocations.remove(player);
                }
            }, 100L);
        }

    }


}
