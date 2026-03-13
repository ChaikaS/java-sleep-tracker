package ru.yandex.practicum.sleeptracker.function;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SleepingSessionBadCounterTest {

    private final SleepingSessionBadCounter function = new SleepingSessionBadCounter();

    @Test
    public void apply_returnsCountOfBadQualitySessions() {
        LocalDateTime base = LocalDateTime.of(2025, 1, 1, 22, 0);
        List<SleepingSession> sessions = List.of(
                session(base, 360, SleepQuality.BAD),
                session(base, 420, SleepQuality.GOOD),
                session(base, 400, SleepQuality.BAD)
        );
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(SleepingSessionBadCounter.TITLE, result.getFunctionTitle());
        assertEquals(2L, result.getResult());
    }

    @Test
    public void apply_returnsZero_whenNoBadSessions() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.now(), 420, SleepQuality.GOOD),
                session(LocalDateTime.now(), 360, SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(0L, result.getResult());
    }

    private static SleepingSession session(LocalDateTime start, long minutes, SleepQuality quality) {
        return new SleepingSession(start, start.plusMinutes(minutes), quality);
    }
}