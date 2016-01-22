package de.kekshaus.cookieApi.teleport.commands;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.kekshaus.cookieApi.bukkit.CookieApiBukkit;
import de.kekshaus.cookieApi.bukkit.MessageDB;
import de.kekshaus.cookieApi.teleport.Teleportplugin;
import de.kekshaus.cookieApi.teleport.database.ConnectionInject;

public class SetLobby implements CommandExecutor {
	public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (sender.hasPermission("cookieApi.teleport.setspawn")) {
			this.executorServiceCommands.submit(new Runnable() {
				public void run() {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						Location coords = player.getLocation();
						String server = CookieApiBukkit.getServerName();
						String world = coords.getWorld().getName();
						Double x = coords.getX();
						Double y = coords.getY();
						Double z = coords.getZ();
						Float yaw = coords.getYaw();
						Float pitch = coords.getPitch();

						String spawnType = "lobby";

						if (ConnectionInject.isLobby(spawnType)) {
							sender.sendMessage("Dieser Spawntype ist bereits registriert.");
							return;
						}

						ConnectionInject.setLobby(spawnType, server, world, x, y, z, yaw, pitch);
						sender.sendMessage(ChatColor.GREEN + "Du hast den SpawnType " + ChatColor.YELLOW + spawnType
								+ ChatColor.GREEN + " registriert!");

						return;

					}
				}
			});
		} else {
			sender.sendMessage(MessageDB.NO_PERMISSIONS);
		}
		return false;
	}
}
