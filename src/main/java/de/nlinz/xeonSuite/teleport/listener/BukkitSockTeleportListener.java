package de.nlinz.xeonSuite.teleport.listener;

import java.io.DataInputStream;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import de.keks.socket.bukkit.events.plugin.BukkitSockTeleportEvent;
import de.keks.socket.core.ByteStreamConverter;
import de.nlinz.xeonSuite.bukkit.XeonSuiteBukkit;
import de.nlinz.xeonSuite.teleport.api.TPStreamInApi;
import de.nlinz.xeonSuite.teleport.api.TPStreamOutApi;

public class BukkitSockTeleportListener implements Listener {

	@EventHandler
	public void onBukkitSockBanEvent(final BukkitSockTeleportEvent e) {
		DataInputStream in = ByteStreamConverter.toDataInputStream(e.readBytes());
		String task = null;
		String servername = null;
		try {
			servername = in.readUTF();

			if (!servername.equalsIgnoreCase(XeonSuiteBukkit.getServerName())) {
				return;
			}

			task = in.readUTF();

			if (task.equals("TeleportToLocation")) {
				TPStreamInApi.teleportToLocation(in.readUTF(), in.readUTF(), in.readDouble(), in.readDouble(),
						in.readDouble(), in.readFloat(), in.readFloat());
			}

			if (task.equals("TeleportToPlayer")) {
				TPStreamInApi.teleportPlayerToPlayer(in.readUTF(), in.readUTF());
			}

			if (task.equals("TeleportAccept")) {
				TPStreamOutApi.finishTPA(Bukkit.getPlayerExact(in.readUTF()), in.readUTF());
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
