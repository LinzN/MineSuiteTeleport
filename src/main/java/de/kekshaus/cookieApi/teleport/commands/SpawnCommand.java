package de.kekshaus.cookieApi.teleport.commands;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.kekshaus.cookieApi.bukkit.CookieApiBukkit;
import de.kekshaus.cookieApi.bukkit.GlobalMessageDB;
import de.kekshaus.cookieApi.teleport.Teleportplugin;
import de.kekshaus.cookieApi.teleport.api.TPStreamOutApi;
import de.kekshaus.cookieApi.teleport.database.ConnectionInject;
import de.kekshaus.cookieApi.teleport.database.TeleportHASHDB;
import net.md_5.bungee.api.ChatColor;

public class SpawnCommand implements CommandExecutor {
	public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		final Player player = (Player) sender;
		if (player.hasPermission("cookieApi.teleport.spawn")) {
			this.executorServiceCommands.submit(new Runnable() {
				public void run() {
					if (sender instanceof Player) {
						final String spawnName = "serverspawn";

						final String servername = CookieApiBukkit.getServerName();
						if (ConnectionInject.isSpawn(spawnName, servername)) {
							if (!player.hasPermission("cookieApi.bypass")) {
								TeleportHASHDB.lastTeleportLocation.put(player, player.getLocation());
								player.sendMessage(GlobalMessageDB.TELEPORT_TIMER.replace("{TIME}",
										String.valueOf(CookieApiBukkit.getWarmUpTime())));
								Teleportplugin.inst().getServer().getScheduler().runTaskLater(Teleportplugin.inst(),
										new Runnable() {
									@Override
									public void run() {

										Location loc = TeleportHASHDB.lastTeleportLocation.get(player);
										TeleportHASHDB.lastTeleportLocation.remove(player);
										if ((loc != null) && (loc.getBlock().equals(player.getLocation().getBlock()))) {
											List<String> list = ConnectionInject.getSpawn(spawnName, servername);
											String world = list.get(1);
											String server = list.get(2);
											double x = Double.parseDouble(list.get(3));
											double y = Double.parseDouble(list.get(4));
											double z = Double.parseDouble(list.get(5));
											float yaw = Float.parseFloat(list.get(6));
											float pitch = Float.parseFloat(list.get(7));
											TPStreamOutApi.sendTeleportToLocationOut(player.getName(), server, world, x,
													y, z, yaw, pitch);
											return;
										} else {
											player.sendMessage(GlobalMessageDB.TELEPORT_MOVE_CANCEL);

										}
									}
								}, 20L * CookieApiBukkit.getWarmUpTime());
							} else {
								List<String> list = ConnectionInject.getSpawn(spawnName, servername);
								String world = list.get(1);
								String server = list.get(2);

								double x = Double.parseDouble(list.get(3));
								double y = Double.parseDouble(list.get(4));
								double z = Double.parseDouble(list.get(5));
								float yaw = Float.parseFloat(list.get(6));
								float pitch = Float.parseFloat(list.get(7));

								TPStreamOutApi.sendTeleportToLocationOut(player.getName(), server, world, x, y, z, yaw,
										pitch);

								return;
							}
						} else {
							player.sendMessage(ChatColor.GOLD + "Dieser Spawn wurde nicht gesetzt!");
						}
					}
				}
			});
		} else {
			sender.sendMessage(GlobalMessageDB.NO_PERMISSIONS);
		}
		return false;
	}
}
