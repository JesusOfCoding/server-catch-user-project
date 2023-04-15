package com.side.serverusercatchproject.modules.review.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.side.serverusercatchproject.modules.enterprise.dto.EnterpriseStoreInfoDTO;

import jakarta.validation.constraints.NotBlank;

public record ReviewSaveRequest(

        @NotBlank(message = "매장 정보를 입력해주세요")
        EnterpriseStoreInfoDTO store,

        @NotBlank(message = "리뷰 내용을 입력해주세요")
        String content,

        @NotBlank(message = "맛 평가 점수를 입력해주세요")
        Double tasteRating,

        @NotBlank(message = "분위기 평가 점수를 입력해주세요")
        Double moodRating,

        @NotBlank(message = "서비스 평가 점수를 입력해주세요")
        Double serviceRating,

        @NotBlank(message = "리뷰 사진을 입력해주세요")
        List<MultipartFile> files


) {

}