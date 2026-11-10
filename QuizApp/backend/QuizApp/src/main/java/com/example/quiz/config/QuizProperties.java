package com.example.quiz.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix = "quiz")
public record QuizProperties(int totalQuestions, int maxSections, int maxCharsPerSection,
                             int weakThresholdPercent, String allowedOrigins) {}
