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
import de.linzn.mineSuite.core.configurations.YamlFiles.GeneralLanguage;
import de.linzn.mineSuite.core.database.hashDatabase.PendingTeleportsData;
import de.linzn.mineSuite.core.utils.LocationUtil;
import de.linzn.mineSuite.teleport.TeleportPlugin;
import de.linzn.mineSuite.teleport.socket.JClientTeleportOutput;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;


public class TeleportManager {

    public static void teleportPlayerToPlayer(UUID playerUUID, UUID targetUUID) {
        Player p = Bukkit.getPlayer(playerUUID);
        Player t = Bukkit.getPlayer(targetUUID);
        if (p != null) {
            Bukkit.getScheduler().runTask(TeleportPlugin.inst(), () -> {
                p.setFallDistance(0F);
                p.teleport(t);
            });
        } else {
            PendingTeleportsData.pendingLocations.put(playerUUID, t.getLocation());
            // clear pending teleport if they dont connect
            Bukkit.getScheduler().runTaskLaterAsynchronously(TeleportPlugin.inst(), () -> {
                if (PendingTeleportsData.pendingLocations.containsKey(playerUUID)) {
                    PendingTeleportsData.pendingLocations.remove(playerUUID);
                }

            }, 100L);
        }
    }

    public static void teleportToLocation(UUID playerUUID, String world, double x, double y, double z, float yaw,
                                          float pitch) {
        World w = Bukkit.getWorld(world);
        Location t;

        if (w != null) {
            t = new Location(w, x, y, z, yaw, pitch);
        } else {
            w = Bukkit.getWorlds().get(0);
            t = w.getSpawnLocation();
        }
        Player p = Bukkit.getPlayer(playerUUID);
        if (p != null) {
            Bukkit.getScheduler().runTask(TeleportPlugin.inst(), () -> {
                // Check if Block is safe
                if (LocationUtil.isBlockUnsafe(t.getWorld(), t.getBlockX(), t.getBlockY(), t.getBlockZ())) {
                    try {
                        Location l = LocationUtil.getSafeDestination(p, t);
                        if (l != null) {
                            p.setFallDistance(0F);
                            p.teleport(l);
                            p.sendMessage(GeneralLanguage.teleport_success);

                        } else {
                            p.sendMessage(ChatColor.RED + "Unable to find a safe location for teleport.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    p.setFallDistance(0F);
                    p.teleport(t);
                    p.sendMessage(GeneralLanguage.teleport_success);
                }
            });
        } else {
            PendingTeleportsData.pendingLocations.put(playerUUID, t);
            // clear pending teleport if they dont connect
            Bukkit.getScheduler().runTaskLaterAsynchronously(TeleportPlugin.inst(), () -> {
                if (PendingTeleportsData.pendingLocations.containsKey(playerUUID)) {
                    PendingTeleportsData.pendingLocations.remove(playerUUID);
                }
            }, 100L);
        }

    }

    public static void finishTPA(UUID playerUUID, UUID targetUUID) {
        Player player = Bukkit.getPlayer(playerUUID);
        player.sendMessage(GeneralLanguage.teleport_TELEPORT_TIMER);
        TeleportPlugin.inst().getServer().getScheduler().runTaskLaterAsynchronously(TeleportPlugin.inst(),
                () -> JClientTeleportOutput.teleportToPlayerUUID(player.getUniqueId(), targetUUID), (long) MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.TELEPORT_WARMUP);

    }


}
