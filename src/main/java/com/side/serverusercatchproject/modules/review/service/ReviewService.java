package com.side.serverusercatchproject.modules.review.service;

import org.springframework.stereotype.Service;

import com.side.serverusercatchproject.modules.review.repository.ReviewRepository;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

}
