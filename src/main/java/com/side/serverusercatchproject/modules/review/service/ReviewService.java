package com.side.serverusercatchproject.modules.review.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.side.serverusercatchproject.modules.review.entity.Review;
import com.side.serverusercatchproject.modules.review.repository.ReviewRepository;
import com.side.serverusercatchproject.modules.review.request.ReviewSaveRequest;
import com.side.serverusercatchproject.modules.review.request.ReviewUpdateRequest;

import jakarta.validation.Valid;

@Service
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Page<Review> getPage(Pageable pageable) {
        return new PageImpl<>(null);
    }

    @Transactional
    public Optional<Review> getReview(Integer id) {
        return null;
    }

    public Review save(@Valid ReviewSaveRequest request) {
        return null;
    }

    public Review update(@Valid ReviewUpdateRequest request, Review review) {
        return null;
    }

    public void delete(Review review) {
    }

}
