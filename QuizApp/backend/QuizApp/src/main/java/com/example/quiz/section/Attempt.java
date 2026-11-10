package com.example.quiz.section;
import java.time.Instant;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("attempts")
@CompoundIndex(def = "{'quizId': 1, 'score': -1}")
public record Attempt(@Id String id, String quizId, String participantName, List<Integer> answers,
                      int score, int total, int percentage, List<SectionResult> sections,
                      List<QuestionReview> review, List<String> weakSections, String studyTips,
                      Instant createdAt) {}
