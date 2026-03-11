package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class SleeplessNightsSession implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "Количество бессонных ночей";

    private static final LocalTime NOON = LocalTime.of(12, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            return new SleepAnalysisResult(TITLE, 0L);
        }

        LocalDate firstSessionDate = sessions.get(0).start().toLocalDate();
        LocalDate lastSessionDate = sessions.get(sessions.size() - 1).end().toLocalDate();

        LocalDate firstNightDate = sessions.get(0).start().toLocalTime().isAfter(NOON)
                ? firstSessionDate.plusDays(1)
                : firstSessionDate;
        LocalDate lastNightDate = lastSessionDate;

        long sleeplessCount = Stream.iterate(firstNightDate, day -> !day.isAfter(lastNightDate), day -> day.plusDays(1))
                .filter(nightDate -> noSessionIntersectsNight(sessions, nightDate))
                .count();

        return new SleepAnalysisResult(TITLE, sleeplessCount);
    }

    private static boolean noSessionIntersectsNight(List<SleepingSession> sessions, LocalDate nightDate) {
        return sessions.stream()
                .noneMatch(session -> session.start().isBefore(nightDate.atTime(6, 0))
                        && session.end().isAfter(nightDate.atStartOfDay()));
    }
}