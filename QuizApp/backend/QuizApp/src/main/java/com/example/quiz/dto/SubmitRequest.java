package com.example.quiz.dto;
import java.util.List;

/** answers[i] = chosen option index (0-3) for question i, or -1 if skipped. */
public record SubmitRequest(String participantName, List<Integer> answers) {}
