package com.example.quiz.dto;
import java.time.Instant;

import com.example.quiz.section.Attempt;

public record AttemptSummary(String id, String participantName, int score, int total,
                             int percentage, Instant createdAt) {
    public static AttemptSummary from(Attempt a) {
        return new AttemptSummary(a.id(), a.participantName(), a.score(), a.total(),
                a.percentage(), a.createdAt());
    }
}