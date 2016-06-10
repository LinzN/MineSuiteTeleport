package de.nlinz.xeonSuite.teleport.commands;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.nlinz.xeonSuite.bukkit.utils.languages.GeneralLanguage;
import de.nlinz.xeonSuite.teleport.api.TPStreamOutApi;

public class TpaHereCommand implements CommandExecutor {
	public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (sender.hasPermission("xeonSuite.teleport.tpahere")) {
			this.executorServiceCommands.submit(new Runnable() {
				@Override
				public void run() {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						if ((args.length >= 1)) {
							String target = args[0].toLowerCase();
							TPStreamOutApi.tpaHereRequest(player, target);
							return;
						} else {
							sender.sendMessage("Du musst einen Player angeben!");
						}
					}
				}
			});
		} else {
			sender.sendMessage(GeneralLanguage.NO_PERMISSIONS);
		}
		return false;
	}
}
