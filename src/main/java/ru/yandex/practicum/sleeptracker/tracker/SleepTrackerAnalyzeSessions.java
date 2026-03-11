package ru.yandex.practicum.sleeptracker.tracker;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;

import static ru.yandex.practicum.sleeptracker.SleepTrackerApp.ANALYTIC_FUNCTION;

public class SleepTrackerAnalyzeSessions {

    public List<SleepAnalysisResult> analyzeSessions(List<SleepingSession> sessions) {
        return ANALYTIC_FUNCTION.stream()
                .map(function -> function.apply(sessions))
                .toList();
    }
}
