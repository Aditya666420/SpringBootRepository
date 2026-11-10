package com.example.quiz.section;


import java.time.Instant;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("quizzes")
public record Quiz(@Id String id, String title, String fileName, List<Section> sections,
                   List<Question> questions, Instant createdAt) {}