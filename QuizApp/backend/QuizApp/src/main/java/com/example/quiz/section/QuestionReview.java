package com.example.quiz.section;
public record QuestionReview(int questionIndex, int sectionIndex, String question,
                             String yourAnswer, String correctAnswer, boolean correct,
                             String explanation) {}
