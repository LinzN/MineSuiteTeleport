package de.kekshaus.cookieApi.teleport.commands;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.kekshaus.cookieApi.bukkit.MessageDB;
import de.kekshaus.cookieApi.teleport.api.TPStreamOutApi;

public class TpCommand implements CommandExecutor {
	public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (sender.hasPermission("cookieApi.teleport.tp")) {
			this.executorServiceCommands.submit(new Runnable() {
				public void run() {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						if ((args.length >= 1))
							if ((args.length == 1)) {
								String target = args[0].toLowerCase();

								TPStreamOutApi.teleportToPlayer(player.getName(), target);

								return;
							} else if ((args.length == 2)) {
								String target1 = args[0].toLowerCase();
								String target2 = args[1].toLowerCase();

								TPStreamOutApi.teleportToPlayer(target1, target2);

								return;
							} else {
								sender.sendMessage("/tp <Playername>");
								sender.sendMessage("/tp <Playername> <Playername>");
							}
					}
				}
			});
		} else {
			sender.sendMessage(MessageDB.NO_PERMISSIONS);
		}
		return false;
	}
}
