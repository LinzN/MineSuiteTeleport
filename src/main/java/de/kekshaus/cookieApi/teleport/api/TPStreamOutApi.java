package de.kekshaus.cookieApi.teleport.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.kekshaus.cookieApi.bukkit.MessageDB;
import de.keks.socket.bukkit.BukkitPlugin;
import de.keks.socket.core.Channel;
import de.kekshaus.cookieApi.bukkit.CookieApiBukkit;
import de.kekshaus.cookieApi.teleport.database.TeleportHASHDB;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TPStreamOutApi {

	public static void tpAll(CommandSender sender, String targetPlayer) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = Channel.teleportChannel(b);
		try {
			out.writeUTF("TpAll");
			out.writeUTF(sender.getName());
			out.writeUTF(targetPlayer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BukkitPlugin.instance().sendBytesOut(b);
	}

	public static void tpaRequest(CommandSender sender, String targetPlayer) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = Channel.teleportChannel(b);
		try {
			out.writeUTF("TpaRequest");
			out.writeUTF(sender.getName());
			out.writeUTF(targetPlayer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BukkitPlugin.instance().sendBytesOut(b);
	}

	public static void tpaHereRequest(CommandSender sender, String targetPlayer) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = Channel.teleportChannel(b);
		try {
			out.writeUTF("TpaHereRequest");
			out.writeUTF(sender.getName());
			out.writeUTF(targetPlayer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BukkitPlugin.instance().sendBytesOut(b);
	}

	public static void tpAccept(final CommandSender sender) {
		final Player player = Bukkit.getPlayer(sender.getName());

		player.saveData();
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = Channel.teleportChannel(b);
		try {
			out.writeUTF("TpAccept");
			out.writeUTF(sender.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}

		BukkitPlugin.instance().sendBytesOut(b);
	}

	public static void tpDeny(String sender) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = Channel.teleportChannel(b);
		try {
			out.writeUTF("TpDeny");
			out.writeUTF(sender);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BukkitPlugin.instance().sendBytesOut(b);
	}

	public static void finishTPA(final Player player, final String target) {
		if (!player.hasPermission("cookieApi.bypass")) {
			TeleportHASHDB.lastTeleportLocation.put(player, player.getLocation());
			player.sendMessage(
					MessageDB.TELEPORT_TIMER.replace("{TIME}", String.valueOf(CookieApiBukkit.getWarmUpTime())));
			CookieApiBukkit.getInstance().getServer().getScheduler().runTaskLater(CookieApiBukkit.getInstance(),
					new Runnable() {
						@Override
						public void run() {

							Location loc = TeleportHASHDB.lastTeleportLocation.get(player);
							TeleportHASHDB.lastTeleportLocation.remove(player);
							if ((loc != null) && (loc.getBlock().equals(player.getLocation().getBlock()))) {
								player.saveData();
								teleportToPlayer(player.getName(), target);

							} else {
								player.sendMessage(MessageDB.TELEPORT_MOVE_CANCEL);

							}
						}
					}, 20L * CookieApiBukkit.getWarmUpTime());
		} else {
			player.saveData();
			teleportToPlayer(player.getName(), target);

		}
	}

	public static void toggleTeleports(String name) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = Channel.teleportChannel(b);
		try {
			out.writeUTF("ToggleTeleports");
			out.writeUTF(name);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BukkitPlugin.instance().sendBytesOut(b);
	}

	public static void teleportToPlayer(final String playerName, final String target) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = Channel.teleportChannel(b);
		try {
			out.writeUTF("TeleportToPlayer");
			out.writeUTF(playerName);
			out.writeUTF(target);
			out.writeBoolean(true);
			out.writeBoolean(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BukkitPlugin.instance().sendBytesOut(b);
	}

	public static void sendTeleportToLocationOut(String player, String server, String world, Double x, Double y,
			Double z, Float yaw, Float pitch) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = Channel.teleportChannel(b);
		try {
			out.writeUTF("TeleportToLocation");
			out.writeUTF(player);
			out.writeUTF(server);
			out.writeUTF(world);
			out.writeDouble(x);
			out.writeDouble(y);
			out.writeDouble(z);
			out.writeFloat(yaw);
			out.writeFloat(pitch);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BukkitPlugin.instance().sendBytesOut(b);
	}

	public static void sendDeathBackLocation(Player p) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = Channel.teleportChannel(b);
		try {
			out.writeUTF("PlayersDeathBackLocation");
			out.writeUTF(p.getName());
			Location l = p.getLocation();
			out.writeUTF(l.getWorld().getName());
			out.writeDouble(l.getX());
			out.writeDouble(l.getY());
			out.writeDouble(l.getZ());
			out.writeFloat(l.getYaw());
			out.writeFloat(l.getPitch());
		} catch (IOException e) {
			e.printStackTrace();
		}

		BukkitPlugin.instance().sendBytesOut(b);
	}

	public static void sendPlayerBack(final CommandSender sender) {
		final Player player = Bukkit.getPlayer(sender.getName());

		player.saveData();
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = Channel.teleportChannel(b);
		try {
			out.writeUTF("SendPlayerBack");
			out.writeUTF(sender.getName());
			out.writeBoolean(true);
			out.writeBoolean(false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BukkitPlugin.instance().sendBytesOut(b);
	}

}
