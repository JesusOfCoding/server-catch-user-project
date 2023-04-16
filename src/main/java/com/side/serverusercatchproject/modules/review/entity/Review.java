package com.side.serverusercatchproject.modules.review.entity;

import org.hibernate.annotations.Comment;

import com.side.serverusercatchproject.common.jpa.BaseTime;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.review.dto.ReviewDTO;
import com.side.serverusercatchproject.modules.review.enums.ReviewStatus;
import com.side.serverusercatchproject.modules.review.response.ReviewResponse;
import com.side.serverusercatchproject.modules.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "REVIEWS")
public class Review extends BaseTime {
    @Id
    @Comment("고유 번호")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @Comment("유저 정보")
    private User user;

    @ManyToOne
    @Comment("매장 정보")
    private EnterpriseStoreInfo store;

    @Comment("내용")
    private String content;

    @Comment("맛 평점")
    private Double tasteRating;

    @Comment("분위기 평점")
    private Double moodRating;

    @Comment("서비스 평점")
    private Double serviceRating;

    @Comment("리뷰 사진")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_info_id")
    private FileInfo fileInfo;

    @Comment("리뷰 상태")
    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @Builder
    public Review(Integer id, User user, EnterpriseStoreInfo store, String content, Double tasteRating,
            Double moodRating, Double serviceRating, FileInfo fileInfo, ReviewStatus status) {
        this.id = id;
        this.user = user;
        this.store = store;
        this.content = content;
        this.tasteRating = tasteRating;
        this.moodRating = moodRating;
        this.serviceRating = serviceRating;
        this.fileInfo = fileInfo;
        this.status = status;
    }

    public ReviewDTO toDTO() {
        return new ReviewDTO(id, user.toDTO(), store.toDTO(), content, tasteRating, moodRating, serviceRating,
                fileInfo.toDTO(), status.name());
    }

    public ReviewResponse toResponse() {
        return new ReviewResponse(id, user.toDTO(), store.toDTO(), content, tasteRating, moodRating, serviceRating, fileInfo.toDTO(), status.name());
    }

}
