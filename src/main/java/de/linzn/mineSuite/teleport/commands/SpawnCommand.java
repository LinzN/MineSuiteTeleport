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
import de.linzn.mineSuite.core.configurations.YamlFiles.GeneralLanguage;
import de.linzn.mineSuite.teleport.TeleportPlugin;
import de.linzn.mineSuite.teleport.socket.JClientTeleportOutput;
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
                final String spawnType = "serverspawn";
                final String serverName = MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.BUNGEE_SERVER_NAME;
                player.sendMessage(GeneralLanguage.teleport_TELEPORT_TIMER);
                this.executorServiceCommands.submit(() -> {
                    TeleportPlugin.inst().getServer().getScheduler().runTaskLaterAsynchronously(TeleportPlugin.inst(),
                            () -> JClientTeleportOutput.teleportToSpawnType(player.getUniqueId(), spawnType, serverName, player.getLocation().getWorld().getName()), (long) MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.TELEPORT_WARMUP);
                });
            });
        } else {
            sender.sendMessage(GeneralLanguage.global_NO_PERMISSIONS);
        }
        return false;
    }
}
