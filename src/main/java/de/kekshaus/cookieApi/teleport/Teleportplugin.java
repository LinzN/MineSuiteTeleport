package de.kekshaus.cookieApi.teleport;

import org.bukkit.plugin.java.JavaPlugin;
import de.kekshaus.cookieApi.teleport.commands.BackCommand;
import de.kekshaus.cookieApi.teleport.commands.LobbyCommand;
import de.kekshaus.cookieApi.teleport.commands.SetLobby;
import de.kekshaus.cookieApi.teleport.commands.SetSpawn;
import de.kekshaus.cookieApi.teleport.commands.SpawnCommand;
import de.kekshaus.cookieApi.teleport.commands.TpAcceptCommand;
import de.kekshaus.cookieApi.teleport.commands.TpCommand;
import de.kekshaus.cookieApi.teleport.commands.TpDenyCommand;
import de.kekshaus.cookieApi.teleport.commands.TpHereCommand;
import de.kekshaus.cookieApi.teleport.commands.TpaCommand;
import de.kekshaus.cookieApi.teleport.commands.TpaHereCommand;
import de.kekshaus.cookieApi.teleport.database.MineTeleportDB;
import de.kekshaus.cookieApi.teleport.listener.BukkitSockTeleportListener;
import de.kekshaus.cookieApi.teleport.listener.TeleportListener;

public class Teleportplugin extends JavaPlugin {
	private static Teleportplugin inst;

	public void onEnable() {
		inst = this;
		loadCommands();
		getServer().getPluginManager().registerEvents(new BukkitSockTeleportListener(), this);
		getServer().getPluginManager().registerEvents(new TeleportListener(), this);
	}

	public void onDisable() {
	}

	public static Teleportplugin inst() {
		return inst;
	}

	public void loadCommands() {
		if (MineTeleportDB.create()) {
			getCommand("tp").setExecutor(new TpCommand());
			getCommand("tphere").setExecutor(new TpHereCommand());
			getCommand("tpa").setExecutor(new TpaCommand());
			getCommand("tpahere").setExecutor(new TpaHereCommand());
			getCommand("back").setExecutor(new BackCommand());
			getCommand("lobby").setExecutor(new LobbyCommand());
			getCommand("spawn").setExecutor(new SpawnCommand());
			getCommand("setspawn").setExecutor(new SetSpawn());
			getCommand("setlobby").setExecutor(new SetLobby());
			getCommand("tpaccept").setExecutor(new TpAcceptCommand());
			getCommand("tpdeny").setExecutor(new TpDenyCommand());
		}
	}

}
