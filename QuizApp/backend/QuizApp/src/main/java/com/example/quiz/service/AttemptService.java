package com.example.quiz.service;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.quiz.config.QuizProperties;
import com.example.quiz.dto.SubmitRequest;
import com.example.quiz.repo.AttemptRepository;
import com.example.quiz.repo.QuizRepository;
import com.example.quiz.section.Attempt;
import com.example.quiz.section.Question;
import com.example.quiz.section.QuestionReview;
import com.example.quiz.section.Quiz;
import com.example.quiz.section.Section;
import com.example.quiz.section.SectionResult;

@Service
public class AttemptService {

    private final QuizRepository quizzes;
    private final AttemptRepository attempts;
    private final OllamaClient ollama;
    private final QuizProperties props;

    public AttemptService(QuizRepository quizzes, AttemptRepository attempts, OllamaClient ollama,
                          QuizProperties props) {
        this.quizzes = quizzes;
        this.attempts = attempts;
        this.ollama = ollama;
        this.props = props;
    }

    public Attempt submit(String quizId, SubmitRequest req) {
        Quiz quiz = quizzes.findById(quizId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));

        if (req.participantName() == null || req.participantName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "participantName is required");
        }
        List<Question> qs = quiz.questions();
        if (req.answers() == null || req.answers().size() != qs.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "answers must contain exactly " + qs.size() + " entries (use -1 for skipped)");
        }

        int score = 0;
        int[] correctPerSection = new int[quiz.sections().size()];
        int[] totalPerSection = new int[quiz.sections().size()];
        List<QuestionReview> review = new ArrayList<>();

        for (Question q : qs) {
            int chosen = req.answers().get(q.index());
            boolean correct = chosen == q.correctIndex();
            if (correct) {
                score++;
                correctPerSection[q.sectionIndex()]++;
            }
            totalPerSection[q.sectionIndex()]++;
            String yours = (chosen >= 0 && chosen < q.options().size()) ? q.options().get(chosen) : "(no answer)";
            review.add(new QuestionReview(q.index(), q.sectionIndex(), q.question(), yours,
                    q.options().get(q.correctIndex()), correct, q.explanation()));
        }

        List<SectionResult> sectionResults = new ArrayList<>();
        List<String> weak = new ArrayList<>();
        for (Section s : quiz.sections()) {
            int total = totalPerSection[s.index()];
            if (total == 0) continue;
            int pct = Math.round(100f * correctPerSection[s.index()] / total);
            boolean isWeak = pct < props.weakThresholdPercent();
            if (isWeak) weak.add(s.title());
            sectionResults.add(new SectionResult(s.index(), s.title(), correctPerSection[s.index()],
                    total, pct, isWeak));
        }

        int pct = Math.round(100f * score / qs.size());
        String tips = buildStudyTips(weak, review);

        return attempts.save(new Attempt(null, quizId, req.participantName().trim(), req.answers(),
                score, qs.size(), pct, sectionResults, review, weak, tips, Instant.now()));
    }

    private String buildStudyTips(List<String> weak, List<QuestionReview> review) {
        if (weak.isEmpty()) {
            return "Great work. You scored well in every section, so keep revising regularly to stay sharp.";
        }
        StringBuilder missed = new StringBuilder();
        review.stream().filter(r -> !r.correct()).limit(6).forEach(r ->
                missed.append("- ").append(r.question()).append(" (correct answer: ")
                        .append(r.correctAnswer()).append(")\n"));

        String prompt = """
                A student just took a quiz and is weak in these sections: %s.
                Questions they got wrong:
                %s
                In 3 to 5 short sentences, tell them what to revise and how. Be specific and encouraging.
                Plain text only, no markdown.
                """.formatted(String.join("; ", weak), missed);

        String ai = ollama.studyTips(prompt);
        // fall back to a fixed message if the model is unavailable
        return (ai != null && !ai.isBlank()) ? ai
                : "Focus your revision on: " + String.join("; ", weak)
                  + ". Re-read those parts and review the missed questions.";
    }
}
