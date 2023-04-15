package com.side.serverusercatchproject.modules.review.entity;

import com.side.serverusercatchproject.common.jpa.BaseTime;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.review.enums.ReviewStatus;
import com.side.serverusercatchproject.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

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

}
