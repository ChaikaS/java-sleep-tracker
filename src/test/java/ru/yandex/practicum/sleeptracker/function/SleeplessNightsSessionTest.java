package ru.yandex.practicum.sleeptracker.function;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleeplessNightsSessionTest {

    private final SleeplessNightsSession function = new SleeplessNightsSession();

    @Test
    public void apply_returnsZero_whenSessionsEmpty() {
        SleepAnalysisResult result = function.apply(List.of());
        assertEquals(SleeplessNightsSession.TITLE, result.getFunctionTitle());
        assertEquals(0L, result.getResult());
    }

    @Test
    public void apply_returnsZero_whenOneNightFullyCoveredBySession() {
        LocalDateTime start = LocalDateTime.of(2025, 10, 1, 23, 0);
        List<SleepingSession> sessions = List.of(session(start, 8 * 60, SleepQuality.GOOD));
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(0L, result.getResult());
    }

    @Test
    public void apply_returnsOneSleeplessNight_whenOneNightInRangeHasNoSleep() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 23, 0), 8 * 60, SleepQuality.NORMAL),
                session(LocalDateTime.of(2025, 10, 3, 14, 0), 60, SleepQuality.GOOD)
        );
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(1L, result.getResult());
    }

    @Test
    public void apply_returnsCorrectCount_whenPeriodSpansTwoMonths() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 30, 23, 0), 8 * 60, SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 11, 2, 10, 0), 60, SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(2L, result.getResult());
    }

    private static SleepingSession session(LocalDateTime start, long minutes, SleepQuality quality) {
        return new SleepingSession(start, start.plusMinutes(minutes), quality);
    }
}