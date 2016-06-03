package de.nlinz.xeonSuite.teleport.listener;

import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.Bukkit;

import de.nlinz.javaSocket.client.api.XeonSocketClientManager;
import de.nlinz.javaSocket.client.events.SocketDataEvent;
import de.nlinz.javaSocket.client.interfaces.IDataListener;
import de.nlinz.xeonSuite.bukkit.XeonSuiteBukkit;
import de.nlinz.xeonSuite.teleport.api.TPStreamInApi;
import de.nlinz.xeonSuite.teleport.api.TPStreamOutApi;

public class XeonTeleport implements IDataListener {

	@Override
	public String getChannel() {
		// TODO Auto-generated method stub
		return channelName;
	}

	public static String channelName = "xeonTeleport";

	@Override
	public void onDataRecieve(SocketDataEvent event) {
		// TODO Auto-generated method stub
		DataInputStream in = XeonSocketClientManager.readDataInput(event.getStreamBytes());
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
