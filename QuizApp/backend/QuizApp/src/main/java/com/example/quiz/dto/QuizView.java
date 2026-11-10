package com.example.quiz.dto;
import java.time.Instant;
import java.util.List;
import com.example.quiz.section.Quiz;
public record QuizView(String id, String title, String fileName, List<String> sections,
                       List<QuestionView> questions, Instant createdAt) {

    public record QuestionView(int index, int sectionIndex, String question, List<String> options) {}

    public static QuizView from(Quiz q) {
        return new QuizView(
                q.id(), q.title(), q.fileName(),
                q.sections().stream().map(s -> s.title()).toList(),
                q.questions().stream()
                        .map(x -> new QuestionView(x.index(), x.sectionIndex(), x.question(), x.options()))
                        .toList(),
                q.createdAt());
    }
}