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

package de.linzn.mineSuite.teleport.socket;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.core.database.hashDatabase.TeleportDataTable;
import de.linzn.mineSuite.teleport.TeleportPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;


public class JClientTeleportOutput {

    public static void setSpawnType(UUID playerUUID, String spawntype, String servername, Location location) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("client_teleport-set-spawntype");
            dataOutputStream.writeUTF(playerUUID.toString());
            dataOutputStream.writeUTF(spawntype);
            dataOutputStream.writeUTF(servername);
            dataOutputStream.writeUTF(location.getWorld().getName());
            dataOutputStream.writeDouble(location.getX());
            dataOutputStream.writeDouble(location.getY());
            dataOutputStream.writeDouble(location.getZ());
            dataOutputStream.writeFloat(location.getYaw());
            dataOutputStream.writeFloat(location.getPitch());
        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
    }

    public static void unsetSpawnType(UUID playerUUID, String spawntype, String servername, String worldname) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("client_teleport-unset-spawntype");
            dataOutputStream.writeUTF(playerUUID.toString());
            dataOutputStream.writeUTF(spawntype);
            dataOutputStream.writeUTF(servername);
            dataOutputStream.writeUTF(worldname);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
    }

    public static void teleportToSpawnType(UUID playerUUID, String spawntype, String servername, String worldname) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("client_teleport-teleport-to-spawntype");
            dataOutputStream.writeUTF(playerUUID.toString());
            dataOutputStream.writeUTF(spawntype);
            dataOutputStream.writeUTF(servername);
            dataOutputStream.writeUTF(worldname);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
    }

    public static void tpAll(CommandSender sender, String targetPlayer) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("client_teleport_teleport-all");
            dataOutputStream.writeUTF(sender.getName());
            dataOutputStream.writeUTF(targetPlayer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
    }

    public static void tpaRequest(CommandSender sender, String targetPlayer) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("client_teleport_tpa-to-request");
            dataOutputStream.writeUTF(sender.getName());
            dataOutputStream.writeUTF(targetPlayer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
        ;
    }

    public static void tpaHereRequest(CommandSender sender, String targetPlayer) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("client_teleport_tpa-request-here");
            dataOutputStream.writeUTF(sender.getName());
            dataOutputStream.writeUTF(targetPlayer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
    }

    public static void tpAccept(final CommandSender sender) {
        final Player player = Bukkit.getPlayer(sender.getName());

        player.saveData();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("client_teleport_tpa-accept");
            dataOutputStream.writeUTF(sender.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
    }

    public static void tpDeny(String sender) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("client_teleport_tpa-deny");
            dataOutputStream.writeUTF(sender);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
    }

    public static void finishTPA(final Player player, final String target) {
        if (!player.hasPermission("mineSuite.bypass")) {
            TeleportDataTable.lastTeleportLocation.put(player, player.getLocation());
            player.sendMessage(
                    MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.TELEPORT_TIMER.replace("{TIME}", String.valueOf(MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.TELEPORT_WARMUP)));
            TeleportPlugin.inst().getServer().getScheduler().runTaskLater(TeleportPlugin.inst(),
                    () -> {
                        Location loc = TeleportDataTable.lastTeleportLocation.get(player);
                        TeleportDataTable.lastTeleportLocation.remove(player);
                        if ((loc != null) && (loc.getBlock().equals(player.getLocation().getBlock()))) {
                            player.saveData();
                            teleportToPlayer(player.getName(), target);

                        } else {
                            player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.TELEPORT_MOVE_CANCEL);

                        }
                    }, 20L * MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.TELEPORT_WARMUP);
        } else {
            player.saveData();
            teleportToPlayer(player.getName(), target);

        }
    }

    public static void teleportToPlayer(final String playerName, final String target) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("client_teleport_teleport-to-player");
            dataOutputStream.writeUTF(playerName);
            dataOutputStream.writeUTF(target);
            dataOutputStream.writeBoolean(true);
            dataOutputStream.writeBoolean(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
    }

    public static void sendTeleportToLocationOut(String player, String server, String world, Double x, Double y,
                                                 Double z, Float yaw, Float pitch) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("client_teleport_teleport-location");
            dataOutputStream.writeUTF(player);
            dataOutputStream.writeUTF(server);
            dataOutputStream.writeUTF(world);
            dataOutputStream.writeDouble(x);
            dataOutputStream.writeDouble(y);
            dataOutputStream.writeDouble(z);
            dataOutputStream.writeFloat(yaw);
            dataOutputStream.writeFloat(pitch);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
    }

    public static void sendDeathBackLocation(Player p) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("client_teleport_set-dead-location");
            dataOutputStream.writeUTF(p.getName());
            Location l = p.getLocation();
            dataOutputStream.writeUTF(l.getWorld().getName());
            dataOutputStream.writeDouble(l.getX());
            dataOutputStream.writeDouble(l.getY());
            dataOutputStream.writeDouble(l.getZ());
            dataOutputStream.writeFloat(l.getYaw());
            dataOutputStream.writeFloat(l.getPitch());
        } catch (IOException e) {
            e.printStackTrace();
        }

        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
    }

    public static void sendPlayerBack(final CommandSender sender) {
        final Player player = Bukkit.getPlayer(sender.getName());

        player.saveData();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("client_teleport_teleport-player-back");
            dataOutputStream.writeUTF(sender.getName());
            dataOutputStream.writeBoolean(true);
            dataOutputStream.writeBoolean(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteTeleport", byteArrayOutputStream.toByteArray());
    }

}
