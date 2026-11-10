package com.example.quiz.section;
public record SectionResult(int sectionIndex, String title, int correct, int total,
                            int percentage, boolean weak) {}