package de.nlinz.xeonSuite.teleport.commands;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.nlinz.xeonSuite.bukkit.XeonSuiteBukkit;
import de.nlinz.xeonSuite.bukkit.utils.languages.GlobalLanguage;
import de.nlinz.xeonSuite.bukkit.utils.tables.TeleportDataTable;
import de.nlinz.xeonSuite.teleport.Teleportplugin;
import de.nlinz.xeonSuite.teleport.api.TPStreamOutApi;

public class BackCommand implements CommandExecutor {
	public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		final Player player = (Player) sender;
		if (player.hasPermission("xeonSuite.teleport.back")) {
			this.executorServiceCommands.submit(new Runnable() {
				@Override
				public void run() {
					if (sender instanceof Player) {
						if (!player.hasPermission("xeonSuite.bypass")) {
							TeleportDataTable.lastTeleportLocation.put(player, player.getLocation());
							player.sendMessage(GlobalLanguage.TELEPORT_TIMER.replace("{TIME}",
									String.valueOf(XeonSuiteBukkit.getWarmUpTime())));
							Teleportplugin.inst().getServer().getScheduler().runTaskLater(Teleportplugin.inst(),
									new Runnable() {
										@Override
										public void run() {
											Location loc = TeleportDataTable.lastTeleportLocation.get(player);
											TeleportDataTable.lastTeleportLocation.remove(player);
											if ((loc != null)
													&& (loc.getBlock().equals(player.getLocation().getBlock()))) {

												TPStreamOutApi.sendPlayerBack(sender);
												return;
											} else {
												player.sendMessage(GlobalLanguage.TELEPORT_MOVE_CANCEL);

											}
										}
									}, 20L * XeonSuiteBukkit.getWarmUpTime());
						} else {

							TPStreamOutApi.sendPlayerBack(sender);

							return;

						}

					}
				}
			});
		} else {
			player.sendMessage(GlobalLanguage.NO_PERMISSIONS);
		}
		return false;
	}
}
