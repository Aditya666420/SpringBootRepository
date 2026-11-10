package com.example.quiz.repo;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.quiz.section.Attempt;
public interface AttemptRepository extends MongoRepository<Attempt, String> {
    List<Attempt> findByQuizIdOrderByScoreDescCreatedAtAsc(String quizId);
}
