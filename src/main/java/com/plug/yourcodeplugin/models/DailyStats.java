package com.plug.yourcodeplugin.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "daily_stats")
public class DailyStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public LocalDate date =  LocalDate.now();

    public int linesAdded;
    public int linesRemoved;
    public int filesOpened;
    public int filesClosed;
    public long codingTimeMs;

    public DailyStats(LocalDate date, int linesAdded, int linesRemoved, int filesOpened, int filesClosed, long codingTimeMs) {
        this.date = date;
        this.linesAdded = linesAdded;
        this.linesRemoved = linesRemoved;
        this.filesOpened = filesOpened;
        this.filesClosed = filesClosed;
        this.codingTimeMs = codingTimeMs;
    }
}
