package com.side.serverusercatchproject.jpa;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.side.serverusercatchproject.modules.enterprise.entity.Enterprise;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.enterprise.enums.EnterpriseStatus;
import com.side.serverusercatchproject.modules.enterprise.enums.StoreStatus;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import com.side.serverusercatchproject.modules.review.entity.Review;
import com.side.serverusercatchproject.modules.review.enums.ReviewStatus;
import com.side.serverusercatchproject.modules.review.repository.ReviewRepository;
import com.side.serverusercatchproject.modules.user.entity.User;
import com.side.serverusercatchproject.modules.user.enums.UserStatus;
import com.side.serverusercatchproject.util.type.RoleType;

import jakarta.transaction.Transactional;

@DataJpaTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void init() {
        setUp("맛잇서용", 5.0, 5.0, 5.0);
    }

    @Test
    void selectAll() {
        var reviews = reviewRepository.findAll();
        Assertions.assertNotEquals(reviews.size(), 0);

        Review review = reviews.get(0);
        Assertions.assertEquals(review.getContent(), "맛잇서용");
    }

    @Test
    @Transactional
    void selectAndUpdate() {
        var optionalReview = reviewRepository.findById(1);

        if (optionalReview.isPresent()) {
            var result = optionalReview.get();
            Assertions.assertEquals(result.getContent(),"맛잇서용");

            var content = "맛업서용";
            result.setContent(content);
            Review merge = entityManager.merge(result);

            Assertions.assertEquals(merge.getContent(),"맛업서용");
        } else {
            Assertions.assertNotNull(optionalReview.get());
        }
    }

    @Test
    @Transactional
    void insertAndDelete() {
        Review review = setUp("그저 그래요", 3.0, 3.0, 3.0);
        Optional<Review> findReview = reviewRepository.findById(review.getId());

        if(findReview.isPresent()) {
            var result = findReview.get();
            Assertions.assertEquals(result.getContent(), "그저 그래요");
            entityManager.remove(review);
            Optional<Review> deleteReview = this.reviewRepository.findById(review.getId());
            if (deleteReview.isPresent()) {
                Assertions.assertNull(deleteReview.get());
            }
        } else {
            Assertions.assertNotNull(findReview.get());
        }
    }

    private Review setUp(String content, Double tasteRating, Double moodRating, Double serviceRating) {
        User user = User.builder()
                    .username("alss")
                    .password("1234")
                    .email("alss@naver.com")
                    .role(RoleType.USER)
                    .tel("01011112222")
                    .status(UserStatus.WAIT)
                    .build();
        entityManager.persist(user);

        Enterprise enterprise = Enterprise.builder()
                                .username("hoho")
                                .password("1234")
                                .role(RoleType.ENTERPRISE)
                                .email("hoho@naver.com")
                                .tel("01022223333")
                                .status(EnterpriseStatus.ACTIVE)
                                .build();        
        entityManager.persist(enterprise);

        EnterpriseStoreInfo store = EnterpriseStoreInfo.builder()
                                    .enterprise(enterprise)
                                    .name("호호네 가게")
                                    .address("부산시 양구동")
                                    .reservationPrice(30000)
                                    .reservationCancelDay(LocalDateTime.now())
                                    .lat(10.1)
                                    .lon(10.1)
                                    .status(StoreStatus.OPEN)
                                    .build();
        entityManager.persist(store);

        FileInfo fileInfo = FileInfo.builder()
                            .type(FileType.REVIEW)
                            .build();
        entityManager.persist(fileInfo);

        Review review = Review.builder()
                        .user(user)
                        .store(store)
                        .content(content)
                        .tasteRating(tasteRating)
                        .moodRating(moodRating)
                        .serviceRating(serviceRating)
                        .fileInfo(fileInfo)
                        .status(ReviewStatus.ACTIVE)
                        .build();

        return entityManager.persist(review);
    }
}
