package ru.yandex.practicum.sleeptracker.function;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.exception.EmptySessionsException;
import ru.yandex.practicum.sleeptracker.model.Chronotype;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChronotypeAnalyzerTest {

    private final ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

    @Test
    public void apply_throwsEmptySessionsException_whenSessionsNull() {
        assertThrows(EmptySessionsException.class, () -> analyzer.apply(null));
    }

    @Test
    public void apply_throwsEmptySessionsException_whenSessionsEmpty() {
        assertThrows(EmptySessionsException.class, () -> analyzer.apply(List.of()));
    }

    @Test
    public void apply_returnsOwl_whenNightSessionAfter23AndWakeAfter9() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 23, 30), 10 * 60, SleepQuality.GOOD)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(Chronotype.OWL, result.getResult());
    }

    @Test
    public void apply_returnsLark_whenNightSessionBefore22AndWakeBefore7() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 21, 0), 9 * 60, SleepQuality.GOOD)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(Chronotype.LARK, result.getResult());
    }

    @Test
    public void apply_returnsPigeon_whenNightSessionInBetween() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 22, 30), 9 * 60 + 30, SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(Chronotype.PIGEON, result.getResult());
    }

    @Test
    public void apply_returnsPigeon_whenTieBetweenLarkAndPigeon() {
        List<SleepingSession> sessions = List.of(
                session(LocalDateTime.of(2025, 10, 1, 21, 0), 8 * 60, SleepQuality.GOOD),
                session(LocalDateTime.of(2025, 10, 2, 22, 30), 8 * 60, SleepQuality.NORMAL)
        );
        SleepAnalysisResult result = analyzer.apply(sessions);
        assertEquals(Chronotype.PIGEON, result.getResult());
    }

    private static SleepingSession session(LocalDateTime start, long minutes, SleepQuality quality) {
        return new SleepingSession(start, start.plusMinutes(minutes), quality);
    }
}