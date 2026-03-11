package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.exception.EmptySessionsException;
import ru.yandex.practicum.sleeptracker.model.Chronotype;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChronotypeAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "Ваш хронотип";

    private static final LocalTime FALL_ASLEEP_OWL = LocalTime.of(23, 0);
    private static final LocalTime WAKE_UP_OWL = LocalTime.of(9, 0);
    private static final LocalTime FALL_ASLEEP_LARK = LocalTime.of(22, 0);
    private static final LocalTime WAKE_UP_LARK = LocalTime.of(7, 0);

    private static boolean isOwl(SleepingSession session) {
        LocalTime start = session.start().toLocalTime();
        LocalTime end = session.end().toLocalTime();

        return start.isAfter(FALL_ASLEEP_OWL) && end.isAfter(WAKE_UP_OWL);
    }

    private static boolean isLark(SleepingSession session) {
        LocalTime start = session.start().toLocalTime();
        LocalTime end = session.end().toLocalTime();

        return start.isBefore(FALL_ASLEEP_LARK) && end.isBefore(WAKE_UP_LARK);
    }

    private static boolean isIntersectsNightSession(SleepingSession session) {
        LocalDate startDate = session.start().toLocalDate();
        LocalDate endDate = session.end().toLocalDate();

        return Stream.iterate(startDate, day -> !day.isAfter(endDate), day -> day.plusDays(1))
                .anyMatch(day -> session.start().isBefore(day.atTime(6, 0))
                        && session.end().isAfter(day.atStartOfDay()));
    }

    private static Chronotype chronotypeAnalyzer(List<SleepingSession> sessions) {
        Map<Chronotype, Long> counts = sessions.stream()
                .filter(ChronotypeAnalyzer::isIntersectsNightSession)
                .map(session -> {
                    if (isOwl(session)) return Chronotype.OWL;
                    if (isLark(session)) return Chronotype.LARK;

                    return Chronotype.PIGEON;
                })
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long owlCount = counts.getOrDefault(Chronotype.OWL, 0L);
        long larkCount = counts.getOrDefault(Chronotype.LARK, 0L);
        long pigeonCount = counts.getOrDefault(Chronotype.PIGEON, 0L);

        if (pigeonCount > owlCount && pigeonCount > larkCount) {
            return Chronotype.PIGEON;
        } else if (owlCount > pigeonCount && owlCount > larkCount) {
            return Chronotype.OWL;
        } else if (larkCount > pigeonCount && larkCount > owlCount) {
            return Chronotype.LARK;
        } else {
            return Chronotype.PIGEON;
        }
    }

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) throws EmptySessionsException {
        if (sessions == null || sessions.isEmpty()) {
            throw new EmptySessionsException("Список сессий сна не может быть null или пустым");
        }

        return new SleepAnalysisResult(TITLE, chronotypeAnalyzer(sessions));
    }
}
