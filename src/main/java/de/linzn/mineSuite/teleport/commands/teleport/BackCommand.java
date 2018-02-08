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

package de.linzn.mineSuite.teleport.commands.teleport;

import de.linzn.mineSuite.core.configurations.YamlFiles.GeneralLanguage;
import de.linzn.mineSuite.core.database.hashDatabase.PendingTeleportsData;
import de.linzn.mineSuite.teleport.TeleportPlugin;
import de.linzn.mineSuite.teleport.socket.JClientTeleportOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class BackCommand implements CommandExecutor {
    public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        final Player player = (Player) sender;
        if (player.hasPermission("mineSuite.teleport.back")) {
            if (!PendingTeleportsData.playerCommand.contains(player.getUniqueId())) {
                PendingTeleportsData.addCommandSpam(player.getUniqueId());
                this.executorServiceCommands.submit(() -> {
                    TeleportPlugin.inst().getServer().getScheduler().runTaskAsynchronously(TeleportPlugin.inst(),
                            () -> JClientTeleportOutput.sendPlayerBack(player.getUniqueId()));
                });
            } else {
                player.sendMessage(GeneralLanguage.global_COMMAND_PENDING);
            }
        } else {
            player.sendMessage(GeneralLanguage.global_NO_PERMISSIONS);

        }
        return false;
    }
}
