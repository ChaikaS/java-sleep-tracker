package ru.yandex.practicum.sleeptracker.function;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SleepingSessionCounterTest {

    private final SleepingSessionCounter counter = new SleepingSessionCounter();

    @Test
    public void apply_returnsSizeAsResult_whenSessionsNotEmpty() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.now(), 60, SleepQuality.GOOD),
                session(LocalDateTime.now(), 120, SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = counter.apply(sessions);
        assertEquals(SleepingSessionCounter.TITLE, result.getFunctionTitle());
        assertEquals(2, result.getResult());
    }

    @Test
    public void apply_returnsZero_whenSessionsEmpty() {
        SleepAnalysisResult result = counter.apply(List.of());
        assertEquals(0, result.getResult());
    }

    private static SleepingSession session(LocalDateTime start, long minutes, SleepQuality quality) {
        return new SleepingSession(start, start.plusMinutes(minutes), quality);
    }
}