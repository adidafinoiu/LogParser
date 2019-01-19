package com.ef.service;

import com.ef.model.LogLine;
import com.ef.util.DatabaseManager;
import com.ef.repository.LogLineDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogLineService {

    private LogLineDAO logLineDAO;

    public LogLineService() {
        this.logLineDAO = new LogLineDAO();
    }

    public void initializateDb(List<LogLine> logLines) {
        DatabaseManager db = new DatabaseManager();
        try (Connection connection = db.getDbConnection()) {
            // Start new transaction
            connection.setAutoCommit(false);
            // Delete old values
            logLineDAO.deleteAllLogData(connection);
            // Insert new values
            logLineDAO.saveLogLines(logLines, connection);
            // Commit transaction
            connection.commit();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public List<String> getIpForThresholdAndInterval(Integer threshold, LocalDateTime startDate, LocalDateTime endDate) {
        List<String> ips = new ArrayList<>();
        DatabaseManager db = new DatabaseManager();
        try (Connection connection = db.getDbConnection()) {
            // Start new transaction
            connection.setAutoCommit(false);
            // Get first
            ips = logLineDAO.getIpForThresholdAndInterval(threshold, startDate, endDate, connection);
            // Commit transaction
            connection.commit();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return ips;
    }

}
