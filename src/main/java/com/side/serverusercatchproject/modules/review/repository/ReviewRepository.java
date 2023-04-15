package com.side.serverusercatchproject.modules.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.side.serverusercatchproject.modules.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
}
