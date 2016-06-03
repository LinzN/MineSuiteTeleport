package de.nlinz.xeonSuite.teleport;

import org.bukkit.plugin.java.JavaPlugin;

import de.nlinz.javaSocket.client.api.XeonSocketClientManager;
import de.nlinz.xeonSuite.teleport.commands.BackCommand;
import de.nlinz.xeonSuite.teleport.commands.LobbyCommand;
import de.nlinz.xeonSuite.teleport.commands.SetLobby;
import de.nlinz.xeonSuite.teleport.commands.SetSpawn;
import de.nlinz.xeonSuite.teleport.commands.SpawnCommand;
import de.nlinz.xeonSuite.teleport.commands.TpAcceptCommand;
import de.nlinz.xeonSuite.teleport.commands.TpCommand;
import de.nlinz.xeonSuite.teleport.commands.TpDenyCommand;
import de.nlinz.xeonSuite.teleport.commands.TpHereCommand;
import de.nlinz.xeonSuite.teleport.commands.TpaCommand;
import de.nlinz.xeonSuite.teleport.commands.TpaHereCommand;
import de.nlinz.xeonSuite.teleport.listener.TeleportListener;
import de.nlinz.xeonSuite.teleport.listener.XeonTeleport;

public class Teleportplugin extends JavaPlugin {
	private static Teleportplugin inst;

	@Override
	public void onEnable() {
		inst = this;
		loadCommands();
		XeonSocketClientManager.registerDataListener(new XeonTeleport());
		getServer().getPluginManager().registerEvents(new TeleportListener(), this);
	}

	@Override
	public void onDisable() {
	}

	public static Teleportplugin inst() {
		return inst;
	}

	public void loadCommands() {

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
