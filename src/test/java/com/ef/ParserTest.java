package com.ef;

import com.ef.model.LogLine;
import com.ef.util.DatabaseManager;
import com.ef.repository.LogLineDAO;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ParserTest {

    private static final String CMD_DATE_TIME_PATHERN = "yyyy-MM-dd.HH:mm:ss";

    @Test
    public void logByIpTest() {
        LogLineDAO dao = new LogLineDAO();
        LogLine logLine = null;

        try (Connection connection = new DatabaseManager().getDbConnection()) {
            logLine = dao.findByIP("192.168.11.231", connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(logLine);
    }

    @Test
    public void logsByIntervalAndFrequencyTest() {
        LogLineDAO dao = new LogLineDAO();
        List<String> ips = new ArrayList<>();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(CMD_DATE_TIME_PATHERN);
        LocalDateTime startDate = LocalDateTime.parse("2017-01-01.14:00:00", dateTimeFormatter);

        try (Connection connection = new DatabaseManager().getDbConnection()) {
            ips = dao.getIpForThresholdAndInterval(10, startDate, startDate.plusHours(1), connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Assert.assertFalse(ips.isEmpty());
    }

}
