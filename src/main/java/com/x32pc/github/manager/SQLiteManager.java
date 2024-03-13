package com.x32pc.github.manager;

import com.x32pc.github.PunishSystem32;

import java.sql.*;

public class SQLiteManager {
    private Connection connection;
    private final PunishSystem32 punishSystem32;

    public SQLiteManager(String dbName, PunishSystem32 main) {
        this.punishSystem32 = main;
        try {
            String dbPath = "jdbc:sqlite:" + dbName;
            connection = DriverManager.getConnection(dbPath);
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void createTables() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS mutes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player_uuid VARCHAR(36) NOT NULL," +
                "mute_time BIGINT NOT NULL," +
                "unmute_time BIGINT NOT NULL," +
                "reason TEXT" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMute(String playerUUID, long durationInMillis, String reason, boolean isPermanent) {
        long currentTime = System.currentTimeMillis();
        long unmuteTime = currentTime + durationInMillis;

        if(isPermanent) {
            unmuteTime = -1;
        }

        String insertQuery = "INSERT INTO mutes (player_uuid, mute_time, unmute_time, reason) VALUES (?, ?, ?, ?);";

        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            pstmt.setString(1, playerUUID);
            pstmt.setLong(2, currentTime);
            pstmt.setLong(3, unmuteTime);
            pstmt.setString(4, reason);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlayerMuted(String playerUUID) {
        String query = "SELECT COUNT(*) FROM mutes WHERE player_uuid = ?;";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, playerUUID);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void removeMute(String playerUUID) {
        String query = "DELETE FROM mutes WHERE player_uuid = ?;";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, playerUUID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getMuteReason(String playerUUID) {
        String query = "SELECT reason FROM mutes WHERE player_uuid = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, playerUUID);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("reason");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getUnmuteTime(String playerUUID) {
        String query = "SELECT unmute_time FROM mutes WHERE player_uuid = ?;";
        long unmuteTime = 0;

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, playerUUID);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                unmuteTime = resultSet.getLong("unmute_time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return unmuteTime;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isMuteExpired(String uuid) {
        long currentTime = System.currentTimeMillis();
        String query = "SELECT unmute_time FROM mutes WHERE player_uuid = ?;";
        try (PreparedStatement pstmt = punishSystem32.sqLiteManager.getConnection().prepareStatement(query)) {
            pstmt.setString(1, uuid);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                long unmuteTime = resultSet.getLong("unmute_time");
                if (unmuteTime == -1) {
                    return false;
                } else {
                    return currentTime >= unmuteTime;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPermanentMute(String playerUUID) {
        String query = "SELECT COUNT(*) FROM mutes WHERE player_uuid = ? AND unmute_time = -1;";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, playerUUID);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
