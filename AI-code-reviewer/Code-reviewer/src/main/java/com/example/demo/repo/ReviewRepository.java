package com.example.demo.repo;




import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entity.Review;

public interface ReviewRepository
        extends MongoRepository<Review, String> {
	Optional<List<Review>> findByUserId(String userId);
}
