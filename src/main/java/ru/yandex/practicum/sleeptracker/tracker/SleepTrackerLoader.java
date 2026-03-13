package ru.yandex.practicum.sleeptracker.tracker;

import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.sleeptracker.SleepTrackerApp.LOG_TIME_FORMATTER;
import static ru.yandex.practicum.sleeptracker.SleepTrackerApp.SEPARATOR;


public class SleepTrackerLoader {

    public File getFile(String fileName) throws FileNotFoundException {
        Path filePath = Paths.get(fileName);
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new FileNotFoundException(String.format("Файла с именем %s не существует", fileName));
        }

        return file;
    }

    public List<SleepingSession> readFile(File file) throws IOException {
        List<SleepingSession> sessions = new ArrayList<>();

        try (FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(fileReader)) {

            while (reader.ready()) {
                sessions = reader.lines()
                        .map(this::parseLine)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();
            }
        } catch (IOException exception) {
            throw exception;
        }

        return sessions;
    }

    private Optional<SleepingSession> parseLine(String line) {
        try {
            String[] parts = line.split(SEPARATOR);
            LocalDateTime start = LocalDateTime.parse(parts[0].trim(), LOG_TIME_FORMATTER);
            LocalDateTime end = LocalDateTime.parse(parts[1].trim(), LOG_TIME_FORMATTER);
            SleepQuality quality = SleepQuality.valueOf(parts[2].trim().toUpperCase());

            return Optional.of(new SleepingSession(start, end, quality));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
