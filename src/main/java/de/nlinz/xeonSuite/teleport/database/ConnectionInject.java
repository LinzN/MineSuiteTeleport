package de.nlinz.xeonSuite.teleport.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionInject {

	public static void setSpawn(String spawntype, String server, String world, double x, double y, double z, float yaw,
			float pitch) {
		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {
			Connection conn = manager.getConnection("mineteleport");
			PreparedStatement sql = conn.prepareStatement("SELECT spawntype FROM spawns WHERE spawntype = '" + spawntype
					+ "' AND server = '" + server + "';");
			ResultSet result = sql.executeQuery();
			if (result.next()) {
				PreparedStatement update = conn
						.prepareStatement("UPDATE spawns SET server = '" + server + "', world = '" + world + "', x = '"
								+ x + "', y = '" + y + "', z = '" + z + "', yaw = '" + yaw + "', pitch = '" + pitch
								+ "' WHERE spawntype = '" + spawntype + "' AND server = '" + server + "';");
				update.executeUpdate();
				update.close();
			} else {
				PreparedStatement insert = conn
						.prepareStatement("INSERT INTO spawns (spawntype, server, world, x, y, z, yaw, pitch) VALUES ('"
								+ spawntype + "', '" + server + "', '" + world + "', '" + x + "', '" + y + "', '" + z
								+ "', '" + yaw + "', '" + pitch + "');");
				insert.executeUpdate();
				insert.close();
			}
			result.close();
			sql.close();
			manager.release("mineteleport", conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void setLobby(String spawntype, String server, String world, double x, double y, double z, float yaw,
			float pitch) {
		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {
			Connection conn = manager.getConnection("mineteleport");
			PreparedStatement sql = conn
					.prepareStatement("SELECT spawntype FROM spawns WHERE spawntype = '" + spawntype + "';");
			ResultSet result = sql.executeQuery();
			if (result.next()) {
				PreparedStatement update = conn.prepareStatement("UPDATE spawns SET server = '" + server
						+ "', world = '" + world + "', x = '" + x + "', y = '" + y + "', z = '" + z + "', yaw = '" + yaw
						+ "', pitch = '" + pitch + "' WHERE spawntype = '" + spawntype + "';");
				update.executeUpdate();
				update.close();
			} else {
				PreparedStatement insert = conn
						.prepareStatement("INSERT INTO spawns (spawntype, server, world, x, y, z, yaw, pitch) VALUES ('"
								+ spawntype + "', '" + server + "', '" + world + "', '" + x + "', '" + y + "', '" + z
								+ "', '" + yaw + "', '" + pitch + "');");
				insert.executeUpdate();
				insert.close();
			}
			result.close();
			sql.close();
			manager.release("mineteleport", conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void delSpawn(String warp) {
		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {
			Connection conn = manager.getConnection("mineteleport");
			PreparedStatement sql = conn
					.prepareStatement("SELECT warp_name FROM warps WHERE warp_name = '" + warp + "';");
			ResultSet result = sql.executeQuery();
			if (result.next()) {
				PreparedStatement update = conn.prepareStatement("DELETE FROM warps WHERE warp_name = '" + warp + "';");
				update.executeUpdate();
				update.close();
			}
			result.close();
			sql.close();
			manager.release("mineteleport", conn);

		}

		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<String> getSpawn(String spawn, String server) {
		final List<String> rlist = new ArrayList<String>();

		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {
			Connection conn = manager.getConnection("mineteleport");
			PreparedStatement sql = conn
					.prepareStatement("SELECT world, server, x, y, z, yaw, pitch FROM spawns WHERE spawntype = '"
							+ spawn + "' AND server = '" + server + "';");
			final ResultSet result = sql.executeQuery();
			if (result.next()) {
				rlist.add(0, "empty");
				rlist.add(1, result.getString(1));
				rlist.add(2, result.getString(2));
				rlist.add(3, result.getString(3));
				rlist.add(4, result.getString(4));
				rlist.add(5, result.getString(5));
				rlist.add(6, result.getString(6));
				rlist.add(7, result.getString(7));
				result.close();
				sql.close();
				manager.release("mineteleport", conn);
			} else {
				result.close();
				sql.close();
				manager.release("mineteleport", conn);
				return null;
			}
			return rlist;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> getLobby(String spawn) {
		final List<String> rlist = new ArrayList<String>();

		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {
			Connection conn = manager.getConnection("mineteleport");
			PreparedStatement sql = conn.prepareStatement(
					"SELECT world, server, x, y, z, yaw, pitch FROM spawns WHERE spawntype = '" + spawn + "';");
			final ResultSet result = sql.executeQuery();
			if (result.next()) {
				rlist.add(0, "empty");
				rlist.add(1, result.getString(1));
				rlist.add(2, result.getString(2));
				rlist.add(3, result.getString(3));
				rlist.add(4, result.getString(4));
				rlist.add(5, result.getString(5));
				rlist.add(6, result.getString(6));
				rlist.add(7, result.getString(7));
				result.close();
				sql.close();
				manager.release("mineteleport", conn);
			} else {
				result.close();
				sql.close();
				manager.release("mineteleport", conn);
				return null;
			}
			return rlist;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isSpawn(String spawn, String servername) {
		boolean isspawn = false;
		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {
			Connection conn = manager.getConnection("mineteleport");
			PreparedStatement sql = conn.prepareStatement("SELECT spawntype FROM spawns WHERE spawntype = '" + spawn
					+ "' AND server = '" + servername + "';");
			ResultSet result = sql.executeQuery();
			if (result.next()) {
				isspawn = true;
			} else {
				isspawn = false;
			}
			result.close();
			sql.close();
			manager.release("mineteleport", conn);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isspawn;
	}

	public static boolean isLobby(String lobby) {
		boolean isspawn = false;
		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {
			Connection conn = manager.getConnection("mineteleport");
			PreparedStatement sql = conn
					.prepareStatement("SELECT spawntype FROM spawns WHERE spawntype = '" + lobby + "';");
			ResultSet result = sql.executeQuery();
			if (result.next()) {
				isspawn = true;
			} else {
				isspawn = false;
			}
			result.close();
			sql.close();
			manager.release("mineteleport", conn);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isspawn;
	}

}
