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
import de.linzn.mineSuite.teleport.socket.JClientTeleportOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class TpCommand implements CommandExecutor {
    public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        if (sender.hasPermission("mineSuite.teleport.tp")) {
            this.executorServiceCommands.submit(() -> {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (args.length >= 1) {
                        if ((args.length == 1)) {
                            String target = args[0].toLowerCase();
                            JClientTeleportOutput.teleportToPlayer(player.getName(), target);
                        } else if ((args.length == 2)) {
                            String target1 = args[0].toLowerCase();
                            String target2 = args[1].toLowerCase();
                            JClientTeleportOutput.teleportToPlayer(target1, target2);
                        } else if ((args.length == 3)) {

                        } else if ((args.length == 4)) {

                        } else if ((args.length == 5)) {

                        } else if ((args.length == 6)) {
                        }
                    } else {
                        sender.sendMessage("/tp <Playername>");
                        sender.sendMessage("/tp <Playername> <Playername>");
                        sender.sendMessage("/tp <X> <Y> <Z>");
                        sender.sendMessage("/tp <World> <X> <Y> <Z>");
                        sender.sendMessage("/tp <Server> <World> <X> <Y> <Z>");
                        sender.sendMessage("/tp <Playername> <Server> <World> <X> <Y> <Z>");
                    }
                }
            });
        } else {
            sender.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.NO_PERMISSIONS);
        }
        return false;
    }
}
