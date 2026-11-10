package com.example.quiz.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.quiz.dto.AttemptSummary;
import com.example.quiz.dto.QuizSummary;
import com.example.quiz.dto.QuizView;
import com.example.quiz.dto.SubmitRequest;
import com.example.quiz.repo.AttemptRepository;
import com.example.quiz.repo.QuizRepository;
import com.example.quiz.section.Attempt;
import com.example.quiz.section.Quiz;
import com.example.quiz.service.AttemptService;
import com.example.quiz.service.QuizGenerationService;
import com.example.quiz.service.QuizPdfService;

@RestController
@RequestMapping("/api")
public class QuizController {

    private final QuizGenerationService generator;
    private final AttemptService attemptService;
    private final QuizPdfService pdfService;
    private final QuizRepository quizzes;
    private final AttemptRepository attempts;

    public QuizController(QuizGenerationService generator, AttemptService attemptService,
                          QuizPdfService pdfService,
                          QuizRepository quizzes, AttemptRepository attempts) {
        this.generator = generator;
        this.attemptService = attemptService;
        this.pdfService = pdfService;
        this.quizzes = quizzes;
        this.attempts = attempts;
    }

    /** Upload a PDF and get back a generated quiz (answers hidden). Can take a while. */
    @PostMapping(value = "/quizzes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public QuizView createQuiz(@RequestParam("file") MultipartFile file,
                               @RequestParam(value = "title", required = false) String title) throws IOException {
        String name = file.getOriginalFilename() == null ? "document.pdf" : file.getOriginalFilename();
        if (file.isEmpty() || !name.toLowerCase().endsWith(".pdf")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please upload a non-empty .pdf file");
        }
        return QuizView.from(generator.generate(name, title, file.getBytes()));
    }

    @GetMapping("/quizzes")
    public List<QuizSummary> listQuizzes() {
        return quizzes.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream().map(QuizSummary::from).toList();
    }

    @GetMapping("/quizzes/{id}")
    public QuizView getQuiz(@PathVariable String id) {
        return quizzes.findById(id).map(QuizView::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));
    }

    /** Download the quiz as a PDF. answers=false gives a blank question paper. */
    @GetMapping("/quizzes/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable String id,
                                              @RequestParam(defaultValue = "true") boolean answers) {
        Quiz quiz = quizzes.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));

        // ADJUST: if Quiz is a normal class, use quiz.getTitle() / quiz.getQuestions()
        byte[] pdf = pdfService.generate(quiz.title(), quiz.questions(), answers);

        String fileName = "quiz-" + id + (answers ? "-with-answers" : "") + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());
        headers.setContentLength(pdf.length);
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    /** Submit answers: stores the score and returns per-section results plus feedback. */
    @PostMapping("/quizzes/{id}/attempts")
    @ResponseStatus(HttpStatus.CREATED)
    public Attempt submit(@PathVariable String id, @RequestBody SubmitRequest req) {
        return attemptService.submit(id, req);
    }

    /** Scoreboard for one quiz: every participant's score, best first. */
    @GetMapping("/quizzes/{id}/attempts")
    public List<AttemptSummary> scoreboard(@PathVariable String id) {
        return attempts.findByQuizIdOrderByScoreDescCreatedAtAsc(id).stream().map(AttemptSummary::from).toList();
    }

    @GetMapping("/attempts/{id}")
    public Attempt getAttempt(@PathVariable String id) {
        return attempts.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));
    }
}