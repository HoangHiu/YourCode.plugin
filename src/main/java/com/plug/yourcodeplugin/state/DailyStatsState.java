package com.plug.yourcodeplugin.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.plug.yourcodeplugin.models.DailyStats;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

@Getter
@State(
        name = "DailyCodeStats",
        storages = @Storage("dailyStats.xml")
)
public class DailyStatsState implements PersistentStateComponent<DailyStats> {

    private DailyStats stats = new DailyStats();

    public static DailyStatsState getInstance() {
        return com.intellij.openapi.application.ApplicationManager
                .getApplication()
                .getService(DailyStatsState.class);
    }

    @Override
    public @Nullable DailyStats getState() {
        return stats;
    }

    @Override
    public void loadState(@NotNull DailyStats state) {
        this.stats = state;

        // Reset if date changed
        if (!stats.date.equals(LocalDate.now())) {
            stats = new DailyStats();
        }
    }

}
