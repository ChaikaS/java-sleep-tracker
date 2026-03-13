package ru.yandex.practicum.sleeptracker.function;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MinSleepingSessionTest {

    private final MinSleepingSession function = new MinSleepingSession();

    @Test
    public void apply_returnsMinDurationInMinutes_whenSeveralSessions() {
        LocalDateTime base = LocalDateTime.of(2025, 1, 1, 22, 0);
        List<SleepingSession> sessions = List.of(
                session(base, 480, SleepQuality.GOOD),
                session(base, 360, SleepQuality.NORMAL),
                session(base, 420, SleepQuality.BAD)
        );
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(MinSleepingSession.TITLE, result.getFunctionTitle());
        assertEquals(360L, result.getResult());
    }

    @Test
    public void apply_returnsSameValue_whenSingleSession() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.now(), 300, SleepQuality.GOOD)
        );
        SleepAnalysisResult result = function.apply(sessions);
        assertEquals(300L, result.getResult());
    }

    private static SleepingSession session(LocalDateTime start, long minutes, SleepQuality quality) {
        return new SleepingSession(start, start.plusMinutes(minutes), quality);
    }
}