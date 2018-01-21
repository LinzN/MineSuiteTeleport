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

package de.linzn.mineSuite.teleport.commands;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.core.database.hashDatabase.TeleportDataTable;
import de.linzn.mineSuite.teleport.TeleportPlugin;
import de.linzn.mineSuite.teleport.socket.JClientTeleportOutput;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SpawnCommand implements CommandExecutor {
    public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        final Player player = (Player) sender;
        if (player.hasPermission("mineSuite.teleport.spawn")) {
            this.executorServiceCommands.submit(() -> {
                if (sender instanceof Player) {
                    final String spawnType = "serverspawn";

                    final String serverName = MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.BUNGEE_SERVER_NAME;

                    if (!player.hasPermission("mineSuite.bypass")) {
                        TeleportDataTable.lastTeleportLocation.put(player, player.getLocation());
                        player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.TELEPORT_TIMER.replace("{TIME}",
                                String.valueOf(MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.TELEPORT_WARMUP)));
                        TeleportPlugin.inst().getServer().getScheduler().runTaskLater(TeleportPlugin.inst(),
                                () -> {
                                    Location loc = TeleportDataTable.lastTeleportLocation.get(player);
                                    TeleportDataTable.lastTeleportLocation.remove(player);
                                    if ((loc != null)
                                            && (loc.getBlock().equals(player.getLocation().getBlock()))) {
                                        JClientTeleportOutput.teleportToSpawnType(player.getUniqueId(), spawnType, serverName, player.getLocation().getWorld().getName());
                                        return;
                                    } else {
                                        player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.TELEPORT_MOVE_CANCEL);

                                    }
                                }, 20L * MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.TELEPORT_WARMUP);
                    } else {
                        JClientTeleportOutput.teleportToSpawnType(player.getUniqueId(), spawnType, serverName, player.getLocation().getWorld().getName());
                        return;
                    }

                }
            });
        } else {
            sender.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.NO_PERMISSIONS);
        }
        return false;
    }
}