package com.ef.repository;

import com.ef.model.LogLine;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogLineDAO {

    public void deleteAllLogData(Connection connection) throws SQLException {
        // Delete old log lines
        PreparedStatement statement = connection.prepareStatement("DELETE from LOG");
        System.out.println("Deleting old data from DB...");
        statement.executeUpdate();
        statement.close();
        System.out.println("Deleted successful.");
    }

    public void saveLogLines(List<LogLine> logLines, Connection connection) throws SQLException {
        // Insert new log lines
        PreparedStatement statement = connection.prepareStatement("INSERT INTO LOG " +
                "(DATE_TIME, IP, REQUEST, STATUS, USER_AGENT) " +
                "VALUES (?,?,?,?,?)");

        for (LogLine line : logLines) {
            statement.setTimestamp(1, Timestamp.valueOf(line.getDateTime()));
            statement.setString(2, line.getIp());
            statement.setString(3, line.getRequest());
            statement.setInt(4, line.getStatus());
            statement.setString(5, line.getUserAgent());
            statement.addBatch();
        }

        System.out.println("Initializing DB...");
        statement.executeBatch();
        statement.close();
        System.out.println("DB Initialization done.");
    }

    public List<String> getIpForThresholdAndInterval(Integer threshold, LocalDateTime startDate, LocalDateTime endDate,
                                                     Connection connection) throws SQLException {

        List<String> ips = new ArrayList<>();

        final String sql = "SELECT IP FROM LOG " +
                " WHERE DATE_TIME BETWEEN ? AND ? " +
                " GROUP BY IP " +
                " HAVING count(IP) > ? ";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setTimestamp(1, Timestamp.valueOf(startDate));
        statement.setTimestamp(2, Timestamp.valueOf(endDate));
        statement.setInt(3, threshold);

        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            ips.add(rs.getString("IP"));
        }
        statement.close();

        return ips;
    }

    public LogLine findByIP(String ip, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT ID, DATE_TIME, IP, REQUEST, STATUS, USER_AGENT FROM LOG WHERE IP = ?");
        statement.setString(1, ip);
        ResultSet rs = statement.executeQuery();
        LogLine logLine = null;

        if (rs.next()) {
            logLine = new LogLine.Builder()
                    .withId(rs.getInt("ID"))
                    .withDateTime(rs.getTimestamp("DATE_TIME").toLocalDateTime())
                    .withIp(rs.getString("IP"))
                    .withRequest(rs.getString("REQUEST"))
                    .withStatus(rs.getInt("STATUS"))
                    .withUserAgent(rs.getString("USER_AGENT"))
                    .build();

            statement.close();
        }

        return logLine;
    }

}
