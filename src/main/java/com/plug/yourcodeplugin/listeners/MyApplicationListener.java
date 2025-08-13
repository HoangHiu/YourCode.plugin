package com.plug.yourcodeplugin.listeners;

import com.intellij.ide.AppLifecycleListener;
import com.plug.yourcodeplugin.models.DailyStats;
import com.plug.yourcodeplugin.repository.CodingStatDAO;
import com.plug.yourcodeplugin.services.StatService;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class MyApplicationListener implements AppLifecycleListener {
    private final long appStartTime;

    public MyApplicationListener() {
        this.appStartTime = System.currentTimeMillis();
        System.out.println("IDE opened at: " + appStartTime);
    }

    @Override
    public void appClosing() {
        long appEndTime = System.currentTimeMillis();
        long appDuration = appEndTime - appStartTime;

        StatService.getInstance().recordEditActivity(); // update last edit time -> codingTime if applicable

        DailyStats snapshot = StatService.getInstance().snapshot();
        StatService.getInstance().syncToPersistentStateIfAvailable();
        DailyStats entity = new DailyStats(
                snapshot.date,
                snapshot.linesAdded,
                snapshot.linesRemoved,
                snapshot.filesOpened,
                snapshot.filesClosed,
                snapshot.codingTimeMs
        );

        try {
            new CodingStatDAO().save(entity);
            System.out.println("Saved stats to DB.");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Failed to save to DB, writing fallback file.");

            try {
                writeFallback(snapshot);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void writeFallback(DailyStats snapshot) throws Exception {
        String home = System.getProperty("user.home");
        Path dir = Path.of(home, ".yourplugin", "pending_stats");
        Files.createDirectories(dir);
        String fileName = "stats-" + System.currentTimeMillis() + ".json";
        File out = dir.resolve(fileName).toFile();

        String json = "{\n" +
                "  \"date\": \"" + snapshot.date + "\",\n" +
                "  \"linesAdded\": " + snapshot.linesAdded + ",\n" +
                "  \"linesRemoved\": " + snapshot.linesRemoved + ",\n" +
                "  \"filesOpened\": " + snapshot.filesOpened + ",\n" +
                "  \"filesClosed\": " + snapshot.filesClosed + ",\n" +
                "  \"codingTimeMs\": " + snapshot.codingTimeMs + "\n" +
                "}\n";

        try (FileWriter w = new FileWriter(out)) {
            w.write(json);
        }
    }
}
