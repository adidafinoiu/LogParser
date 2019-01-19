package com.ef;

import com.ef.model.LogLine;
import com.ef.service.LogLineService;
import org.apache.commons.cli.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {

    private static final String LOG_DATE_TIME_PATHERN = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String CMD_DATE_TIME_PATHERN = "yyyy-MM-dd.HH:mm:ss";
    private static final String START_DATE = "startDate";
    private static final String DURATION = "duration";
    private static final String THRESHOLD = "threshold";
    private static final String ACCESSLOG = "accesslog";

    private LogLineService logLineService;

    public Parser() {
        this.logLineService = new LogLineService();
        ;
    }

    private List<LogLine> parseLogFile(String accesslog) throws FileNotFoundException {
        Path path = Paths.get(accesslog);
        List<LogLine> logLines = new ArrayList<>();

        if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            System.out.println("Parsing file " + path.getFileName() + "...");

            try (Stream<String> stream = Files.lines(path)) {
                logLines = stream.map(Parser::parseLine).collect(Collectors.toList());
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        } else {
            throw new FileNotFoundException("The file/directory " + path.getFileName() + " does not exists!");
        }
        return logLines;
    }


    private Options buildInputOptions() {

        Options options = new Options();

        Option startDateOption = new Option("s", START_DATE, true, "Start date");
        startDateOption.setRequired(true);
        options.addOption(startDateOption);

        Option durationOption = new Option("d", DURATION, true, "Duration");
        durationOption.setRequired(true);
        options.addOption(durationOption);

        Option thresholdOption = new Option("t", THRESHOLD, true, "Threshold");
        thresholdOption.setRequired(true);
        options.addOption(thresholdOption);

        Option accesslogOption = new Option("a", ACCESSLOG, true, "Accesslog");
        accesslogOption.setRequired(true);
        options.addOption(accesslogOption);

        return options;
    }


    private CommandLine getCommandLine(String... args) {
        Options options = buildInputOptions();
        CommandLine cmd;
        try {
            // parse the command line arguments
            CommandLineParser commandLineParser = new DefaultParser();
            cmd = commandLineParser.parse(options, args);
        } catch (ParseException exp) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("help", options);

            throw new IllegalArgumentException("Pass \"startDate\", \"duration\" and \"threshold\" as command line arguments!", exp);
        }
        return cmd;
    }


    private static LogLine parseLine(String line) {
        List<String> strings = Arrays.asList(line.split("\\|"));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(LOG_DATE_TIME_PATHERN);
        LocalDateTime dateTime = LocalDateTime.parse(strings.get(0), dateTimeFormatter);
        String ip = strings.get(1);
        String request = strings.get(2);
        Integer status = Integer.valueOf(strings.get(3));
        String userAgent = strings.get(4);

        return new LogLine.Builder()
                .withDateTime(dateTime)
                .withIp(ip)
                .withStatus(status)
                .withRequest(request)
                .withUserAgent(userAgent)
                .build();
    }


    public static void main(String... args) throws FileNotFoundException {
        Parser parser = new Parser();

        CommandLine cmd = parser.getCommandLine(args);

        // Get start date
        String startDateValue = cmd.getOptionValue(START_DATE);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(CMD_DATE_TIME_PATHERN);
        LocalDateTime startDate = LocalDateTime.parse(startDateValue, dateTimeFormatter);

        // Get end date
        LocalDateTime endDate = null;
        String durationValue = cmd.getOptionValue(DURATION);
        switch (durationValue) {
            case "hourly":
                endDate = startDate.plusHours(1);
                break;
            case "daily":
                endDate = startDate.plusHours(1);
                break;
            default:
                System.out.println("The \"duration\" parameter accepts only \"hourly\" or \"daily\"!");
                System.exit(-1);
        }

        // Get threshold
        String thresholdValue = cmd.getOptionValue(THRESHOLD);
        Integer threshold = Integer.valueOf(thresholdValue);

        // Get accesslog
        String accesslog = cmd.getOptionValue(ACCESSLOG);
        List<LogLine> lines = parser.parseLogFile(accesslog);

        // Initialize the database
        parser.logLineService.initializateDb(lines);

        // Perform database operation
        List<String> ips = parser.logLineService.getIpForThresholdAndInterval(threshold, startDate, endDate);

        System.out.println("The IPs found for the given input are:\n" + ips);
    }


}
