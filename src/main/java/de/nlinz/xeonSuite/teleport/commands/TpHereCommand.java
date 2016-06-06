package de.nlinz.xeonSuite.teleport.commands;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.nlinz.xeonSuite.bukkit.GlobalMessageDB;
import de.nlinz.xeonSuite.teleport.api.TPStreamOutApi;

public class TpHereCommand implements CommandExecutor {
	public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (sender.hasPermission("cookieApi.teleport.tphere")) {
			this.executorServiceCommands.submit(new Runnable() {
				public void run() {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						if ((args.length >= 1)) {
							String target = args[0].toLowerCase();

							TPStreamOutApi.teleportToPlayer(target, player.getName());
							return;
						} else {
							sender.sendMessage("Du musst einen Player angeben!");
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