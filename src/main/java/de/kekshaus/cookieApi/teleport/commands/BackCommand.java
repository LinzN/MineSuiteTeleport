package de.kekshaus.cookieApi.teleport.commands;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.kekshaus.cookieApi.bukkit.CookieApiBukkit;
import de.kekshaus.cookieApi.bukkit.MessageDB;
import de.kekshaus.cookieApi.bukkit.managerApi.OtherApi;
import de.kekshaus.cookieApi.teleport.Teleportplugin;

public class BackCommand implements CommandExecutor {
	public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		final Player player = (Player) sender;
		if (player.hasPermission("cookieApi.teleport.back")) {
			this.executorServiceCommands.submit(new Runnable() {
				public void run() {
					if (sender instanceof Player) {
						if (!player.hasPermission("cookieApi.bypass")) {
							OtherApi.lastLocation.put(player, player.getLocation());
							player.sendMessage(MessageDB.TELEPORT_TIMER.replace("{TIME}",
									String.valueOf(CookieApiBukkit.getWarmUpTime())));
							Teleportplugin.inst().getServer().getScheduler().runTaskLater(Teleportplugin.inst(),
									new Runnable() {
								@Override
								public void run() {
									Location loc = OtherApi.lastLocation.get(player);
									OtherApi.lastLocation.remove(player);
									if ((loc != null) && (loc.getBlock().equals(player.getLocation().getBlock()))) {

										OtherApi.sendPlayerBack(sender);
										return;
									} else {
										player.sendMessage(MessageDB.TELEPORT_MOVE_CANCEL);

									}
								}
							}, 20L * CookieApiBukkit.getWarmUpTime());
						} else {

							OtherApi.sendPlayerBack(sender);

							return;

						}

					}
				}
			});
		} else {
			player.sendMessage(MessageDB.NO_PERMISSIONS);
		}
		return false;
	}
}