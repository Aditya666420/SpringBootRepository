package com.example.quiz.section;
import java.util.List;

public record Question(int index, int sectionIndex, String question, List<String> options,
                       int correctIndex, String explanation) {}
