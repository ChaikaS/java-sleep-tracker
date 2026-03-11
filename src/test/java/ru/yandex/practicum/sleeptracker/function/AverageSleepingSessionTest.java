package ru.yandex.practicum.sleeptracker.function;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AverageSleepingSessionTest {

    private final AverageSleepingSession function = new AverageSleepingSession();

    @Test
    public void apply_returnsAverageDurationInMinutes_whenSeveralSessions() {
        LocalDateTime base = LocalDateTime.of(2025, 1, 1, 22, 0);
        List<SleepingSession> sessions = List.of(
                session(base, 360, SleepQuality.GOOD),
                session(base, 480, SleepQuality.NORMAL),
                session(base, 420, SleepQuality.BAD)
        );
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(AverageSleepingSession.TITLE, result.getFunctionTitle());
        assertEquals(420L, result.getResult());
    }

    @Test
    public void apply_returnsSameValue_whenSingleSession() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.now(), 390, SleepQuality.GOOD)
        );
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(390L, result.getResult());
    }

    private static SleepingSession session(LocalDateTime start, long minutes, SleepQuality quality) {
        return new SleepingSession(start, start.plusMinutes(minutes), quality);
    }
}