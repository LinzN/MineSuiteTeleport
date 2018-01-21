/*
 * Copyright (C) 2018. MineGaming - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 *  You should have received a copy of the LGPLv3 license with
 *  this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.mineSuite.teleport;


import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.teleport.commands.*;
import de.linzn.mineSuite.teleport.listener.TeleportListener;
import de.linzn.mineSuite.teleport.socket.JClientTeleportListener;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleportPlugin extends JavaPlugin {
    private static TeleportPlugin inst;

    public static TeleportPlugin inst() {
        return inst;
    }

    @Override
    public void onEnable() {
        inst = this;
        loadCommands();
        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.registerIncomingDataListener("mineSuiteTeleport", new JClientTeleportListener());
        getServer().getPluginManager().registerEvents(new TeleportListener(), this);
    }

    @Override
    public void onDisable() {
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
        getCommand("unsetspawn").setExecutor(new UnsetSpawn());
        getCommand("tpaccept").setExecutor(new TpAcceptCommand());
        getCommand("tpdeny").setExecutor(new TpDenyCommand());

    }

}
