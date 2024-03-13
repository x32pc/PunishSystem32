package com.x32pc.github.manager;

import com.x32pc.github.PunishSystem32;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class HistoryManager {
    private Connection connection;

    private final PunishSystem32 punishSystem32;

    public HistoryManager(String dbName, PunishSystem32 main) {
        this.punishSystem32 = main;
        try {
            String dbPath = "jdbc:sqlite:" + dbName;
            connection = DriverManager.getConnection(dbPath);
            createHistoryTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createHistoryTable() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS history (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player_uuid VARCHAR(36) NOT NULL," +
                "player_name TEXT NOT NULL," +
                "punishment TEXT NOT NULL," +
                "reason TEXT NOT NULL," +
                "time BIGINT NOT NULL," +
                "executor TEXT NOT NULL" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addHistory(String playerUUID, String playerName, String punishment, String reason, long time, String executor) {
        String insertQuery = "INSERT INTO history (player_uuid, player_name, punishment, reason, time, executor) VALUES (?, ?, ?, ?, ?, ?);";

        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            pstmt.setString(1, playerUUID);
            pstmt.setString(2, playerName);
            pstmt.setString(3, punishment);
            pstmt.setString(4, reason);
            pstmt.setLong(5, time);
            pstmt.setString(6, executor);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public Connection getConnection() {
        return connection;
    }
}