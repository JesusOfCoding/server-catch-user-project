package com.side.serverusercatchproject.modules.review.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.side.serverusercatchproject.common.exception.Exception400;
import com.side.serverusercatchproject.modules.notice.NoticeConst;
import com.side.serverusercatchproject.modules.review.ReviewConst;
import com.side.serverusercatchproject.modules.review.dto.ReviewDTO;
import com.side.serverusercatchproject.modules.review.entity.Review;
import com.side.serverusercatchproject.modules.review.request.ReviewSaveRequest;
import com.side.serverusercatchproject.modules.review.request.ReviewUpdateRequest;
import com.side.serverusercatchproject.modules.review.response.ReviewResponse;
import com.side.serverusercatchproject.modules.review.service.ReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<Page<ReviewDTO>> getPage(Pageable pageable) {
        var page = reviewService.getPage(pageable);
        var content = page.getContent()
                .stream()
                .map(Review::toDTO)
                .toList();

        return ResponseEntity.ok(
                new PageImpl<>(content, pageable, page.getTotalElements())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getNotice (@PathVariable Integer id) {
        var optionalReview = reviewService.getReview(id);
        if (optionalReview.isEmpty()) {
            throw new Exception400(ReviewConst.notFound);
        }

        return ResponseEntity.ok(
            optionalReview.get().toResponse()
        );
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> saveNotice (
            @Valid @RequestBody ReviewSaveRequest request,
            Errors error
    ) {
        if (error.hasErrors()) {
            throw new Exception400(error.getAllErrors().get(0).getDefaultMessage());
        }

        var review = reviewService.save(request);
        return ResponseEntity.ok(review.toResponse());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> saveNotice (
            @Valid @RequestBody ReviewUpdateRequest request,
            Errors error,
            @PathVariable Integer id
    ) {
        request.content();
        if (error.hasErrors()) {
            throw new Exception400(error.getAllErrors().get(0).getDefaultMessage());
        }

        var optionalReview = reviewService.getReview(id);
        if (optionalReview.isEmpty()) {
            throw new Exception400(ReviewConst.notFound);
        }

        var review = reviewService.update(request, optionalReview.get());
        return ResponseEntity.ok(review.toResponse());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> saveNotice (
            @PathVariable Integer id
    ) {
        var optionalReview = reviewService.getReview(id);
        if (optionalReview.isEmpty()) {
            throw new Exception400(ReviewConst.notFound);
        }

        reviewService.delete(optionalReview.get());

        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }
}
