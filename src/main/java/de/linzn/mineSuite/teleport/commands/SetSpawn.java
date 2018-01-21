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
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class SetSpawn implements CommandExecutor {
    public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        if (sender.hasPermission("mineSuite.teleport.setspawn")) {
            this.executorServiceCommands.submit(() -> {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (args.length < 1) {
                        sender.sendMessage("Du musst einen SpawnType angeben. Z.b. ServerSpawn oder lobby");
                        return;
                    }

                    String spawnType = args[0].toLowerCase();
                    Location coords = player.getLocation();
                    String server = MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.BUNGEE_SERVER_NAME;
                    JClientTeleportOutput.setSpawnType(player.getUniqueId(), spawnType, server, coords);
                    return;

                }
            });
        } else {
            sender.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.NO_PERMISSIONS);
        }
        return true;
    }
}
