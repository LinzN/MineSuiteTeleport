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
import de.linzn.mineSuite.core.database.hashDatabase.PendingTeleportsData;
import de.linzn.mineSuite.teleport.socket.JClientTeleportOutput;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class TpCommand implements CommandExecutor {
    public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        if (sender.hasPermission("mineSuite.teleport.tp")) {
            Player player = (Player) sender;
            if (!PendingTeleportsData.playerCommand.contains(player.getUniqueId())) {
                PendingTeleportsData.addCommandSpam(player.getUniqueId());
                this.executorServiceCommands.submit(() -> {
                    if (args.length >= 1) {
                        if ((args.length == 1)) {
                            String target = args[0].toLowerCase();
                            JClientTeleportOutput.teleportToPlayer(player.getName(), target);
                        } else if ((args.length == 2)) {
                            String target1 = args[0].toLowerCase();
                            String target2 = args[1].toLowerCase();
                            JClientTeleportOutput.teleportToPlayer(target1, target2);
                        } else if ((args.length == 3)) {
                            String arg0 = args[0];
                            String arg1 = args[1];
                            String arg2 = args[2];

                            UUID playerUUID = player.getUniqueId();
                            String serverName = MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.BUNGEE_SERVER_NAME;
                            String worldName = player.getWorld().getName();
                            double x;
                            double y;
                            double z;
                            float yaw = 0F;
                            float pitch = 0F;
                            try {
                                x = Double.parseDouble(arg0);
                                y = Double.parseDouble(arg1);
                                z = Double.parseDouble(arg2);
                                JClientTeleportOutput.sendTeleportToLocationOut(playerUUID, serverName, worldName, x, y, z, yaw, pitch);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(GeneralLanguage.teleport_TP_3);
                            }


                        } else if ((args.length == 4)) {
                            String arg0 = args[0];
                            String arg1 = args[1];
                            String arg2 = args[2];
                            String arg3 = args[3];

                            UUID playerUUID = player.getUniqueId();
                            String serverName = MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.BUNGEE_SERVER_NAME;
                            String worldName;
                            double x;
                            double y;
                            double z;
                            float yaw = 0F;
                            float pitch = 0F;
                            if (Bukkit.getWorld(arg0) == null){
                                sender.sendMessage(GeneralLanguage.teleport_TP_4);
                                return;
                            }
                            worldName = Bukkit.getWorld(arg0).getName();
                            try {
                                x = Double.parseDouble(arg1);
                                y = Double.parseDouble(arg2);
                                z = Double.parseDouble(arg3);
                                JClientTeleportOutput.sendTeleportToLocationOut(playerUUID, serverName, worldName, x, y, z, yaw, pitch);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(GeneralLanguage.teleport_TP_4);
                            }

                        } else if ((args.length == 5)) {
                            String arg0 = args[0];
                            String arg1 = args[1];
                            String arg2 = args[2];
                            String arg3 = args[3];
                            String arg4 = args[4];

                            UUID playerUUID = player.getUniqueId();
                            String serverName;
                            String worldName;
                            double x;
                            double y;
                            double z;
                            float yaw = 0F;
                            float pitch = 0F;

                            serverName = arg0;
                            worldName = arg1;
                            try {
                                x = Double.parseDouble(arg2);
                                y = Double.parseDouble(arg3);
                                z = Double.parseDouble(arg4);
                                JClientTeleportOutput.sendTeleportToLocationOut(playerUUID, serverName, worldName, x, y, z, yaw, pitch);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(GeneralLanguage.teleport_TP_5);
                            }
                        }
                    } else {
                        sender.sendMessage(GeneralLanguage.teleport_TP_0);
                        sender.sendMessage(GeneralLanguage.teleport_TP_1);
                        sender.sendMessage(GeneralLanguage.teleport_TP_2);
                        sender.sendMessage(GeneralLanguage.teleport_TP_3);
                        sender.sendMessage(GeneralLanguage.teleport_TP_4);
                        sender.sendMessage(GeneralLanguage.teleport_TP_5);
                    }

                });
            } else {
                player.sendMessage(GeneralLanguage.global_COMMAND_PENDING);
            }
        } else {
            sender.sendMessage(GeneralLanguage.global_NO_PERMISSIONS);
        }
        return false;
    }
}
