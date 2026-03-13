package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.function.*;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.tracker.SleepTrackerAnalyzeSessions;
import ru.yandex.practicum.sleeptracker.tracker.SleepTrackerLoader;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {

    public static final String SESSION_FILE_NAME = "src/main/resources/sleep_log.txt";
    public static final String SEPARATOR = ";";
    public static final DateTimeFormatter LOG_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
    public static final List<Function<List<SleepingSession>, SleepAnalysisResult>> ANALYTIC_FUNCTION = List.of(new SleepingSessionCounter(), new MinSleepingSession(), new MaxSleepingSession(),
            new AverageSleepingSession(), new SleepingSessionBadCounter(), new SleeplessNightsSession(), new ChronotypeAnalyzer());

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Необходимо добавить аргументы командной строки");
            return;
        }

        String filePath = args[0];

        try {
            SleepTrackerLoader loader = new SleepTrackerLoader();
            List<SleepingSession> sessions = loader.readFile(loader.getFile(filePath));

            SleepTrackerAnalyzeSessions sessionAnalyser = new SleepTrackerAnalyzeSessions();

            List<SleepAnalysisResult> sessionsResults = sessionAnalyser.analyzeSessions(sessions);
            sessionsResults.forEach(System.out::println);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
