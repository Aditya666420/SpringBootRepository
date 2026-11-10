package com.example.quiz.dto;
import java.time.Instant;

import com.example.quiz.section.Quiz;

public record QuizSummary(String id, String title, String fileName, int questionCount, Instant createdAt) {
    public static QuizSummary from(Quiz q) {
        return new QuizSummary(q.id(), q.title(), q.fileName(), q.questions().size(), q.createdAt());
    }
}