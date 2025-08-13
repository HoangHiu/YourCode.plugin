package com.plug.yourcodeplugin.services;

import com.plug.yourcodeplugin.models.DailyStats;
import com.plug.yourcodeplugin.state.DailyStatsState;

public class StatService {
    private static final StatService INSTANCE = new StatService();

    // ALWAYS-available in-memory copy used by listeners and shutdown logic
    private final DailyStats inMemory = new DailyStats();

    // idle tracking
    private long lastEditTime = System.currentTimeMillis();

    private StatService() {
        // Try to initialize from persistent state if available at construction time
        DailyStatsState state = tryGetState();
        if (state != null && state.getState() != null) {
            // copy fields to inMemory (avoid sharing object references)
            DailyStats s = state.getState();
            inMemory.date = s.date;
            inMemory.linesAdded = s.linesAdded;
            inMemory.linesRemoved = s.linesRemoved;
            inMemory.filesOpened = s.filesOpened;
            inMemory.filesClosed = s.filesClosed;
            inMemory.codingTimeMs = s.codingTimeMs;
        }
    }

    public static StatService getInstance() {
        return INSTANCE;
    }

    private DailyStatsState tryGetState() {
        try {
            return DailyStatsState.getInstance();
        } catch (Throwable t) {
            // service not available yet (or being disposed) — return null
            return null;
        }
    }

    // Call periodically or when shutting down to sync inMemory -> platform persistent state if available
    public void syncToPersistentStateIfAvailable() {
        DailyStatsState state = tryGetState();
        if (state != null && state.getState() != null) {
            DailyStats s = state.getState();
            s.date = inMemory.date;
            s.linesAdded = inMemory.linesAdded;
            s.linesRemoved = inMemory.linesRemoved;
            s.filesOpened = inMemory.filesOpened;
            s.filesClosed = inMemory.filesClosed;
            s.codingTimeMs = inMemory.codingTimeMs;
            // no explicit save call required; platform periodically persists
        }
    }

    // --- mutation methods used by listeners ---
    public void addLines(int count) {
        synchronized (inMemory) {
            inMemory.linesAdded += count;
        }
    }

    public void removeLines(int count) {
        synchronized (inMemory) {
            inMemory.linesRemoved += count;
        }
    }

    public void fileOpened() {
        synchronized (inMemory) {
            inMemory.filesOpened++;
        }
    }

    public void fileClosed() {
        synchronized (inMemory) {
            inMemory.filesClosed++;
        }
    }

    public void recordEditActivity() {
        long now = System.currentTimeMillis();
        synchronized (inMemory) {
            if (now - lastEditTime < 5 * 60 * 1000) { // less than 5 min idle
                inMemory.codingTimeMs += (now - lastEditTime);
            }
            lastEditTime = now;
        }
    }

    // returns a copy snapshot safe to use on other threads
    public DailyStats snapshot() {
        synchronized (inMemory) {
            DailyStats copy = new DailyStats();
            copy.date = inMemory.date;
            copy.linesAdded = inMemory.linesAdded;
            copy.linesRemoved = inMemory.linesRemoved;
            copy.filesOpened = inMemory.filesOpened;
            copy.filesClosed = inMemory.filesClosed;
            copy.codingTimeMs = inMemory.codingTimeMs;
            return copy;
        }
    }

    // convenience getter used by UI/tool-window code, etc.
    public DailyStats getDailyStats() {
        return snapshot();
    }
}
