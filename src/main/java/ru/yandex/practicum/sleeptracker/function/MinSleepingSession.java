package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MinSleepingSession implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "Минимальная продолжительность сессии (в минутах)";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long minMinutes = sessions.stream()
                .mapToInt(session -> (int) Duration.between(session.start(), session.end()).toMinutes())
                .summaryStatistics()
                .getMin();

        return new SleepAnalysisResult(TITLE, minMinutes);
    }
}
