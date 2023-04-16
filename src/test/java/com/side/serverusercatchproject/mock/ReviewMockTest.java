package com.side.serverusercatchproject.mock;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.side.serverusercatchproject.modules.enterprise.entity.Enterprise;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.enterprise.enums.EnterpriseStatus;
import com.side.serverusercatchproject.modules.enterprise.enums.StoreStatus;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import com.side.serverusercatchproject.modules.review.controller.ReviewController;
import com.side.serverusercatchproject.modules.review.entity.Review;
import com.side.serverusercatchproject.modules.review.enums.ReviewStatus;
import com.side.serverusercatchproject.modules.review.request.ReviewSaveRequest;
import com.side.serverusercatchproject.modules.review.service.ReviewService;
import com.side.serverusercatchproject.modules.user.entity.User;
import com.side.serverusercatchproject.modules.user.enums.UserStatus;
import com.side.serverusercatchproject.util.type.RoleType;

@WebMvcTest(ReviewController.class)
@MockBean(JpaMetamodelMappingContext.class) 
public class ReviewMockTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private ReviewService reviewService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("리뷰 조회 (페이지)")
    void getPage() throws Exception {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Review> page = new PageImpl<>(
                List.of(
                    new Review(1, makeUser(), makeStore(makeEnterprise()), "내용1", 5.0, 5.0, 5.0, new FileInfo(2, FileType.REVIEW), ReviewStatus.ACTIVE),
                    new Review(2, makeUser(), makeStore(makeEnterprise()), "내용2", 5.0, 5.0, 5.0, new FileInfo(2, FileType.REVIEW), ReviewStatus.ACTIVE),
                    new Review(3, makeUser(), makeStore(makeEnterprise()), "내용3", 5.0, 5.0, 5.0, new FileInfo(2, FileType.REVIEW), ReviewStatus.ACTIVE)
                )
        );


        // given
        given(reviewService.getPage(pageable)).willReturn(page);


        // When
        ResultActions perform = mvc.perform(
                get("/reviews?page={page}&size={size}", 1, 10)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].content").value("내용1"))
                .andExpect(jsonPath("$.content[0].tasteRating").value(5.0))
                .andExpect(jsonPath("$.content[0].moodRating").value(5.0))
                .andExpect(jsonPath("$.content[0].serviceRating").value(5.0))
                .andExpect(jsonPath("$.content[0].status").value("ACTIVE"))
                
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].content").value("내용2"))
                .andExpect(jsonPath("$.content[1].tasteRating").value(5.0))
                .andExpect(jsonPath("$.content[1].moodRating").value(5.0))
                .andExpect(jsonPath("$.content[1].serviceRating").value(5.0))
                .andExpect(jsonPath("$.content[0].status").value("ACTIVE"))

        ;
    }

    @Test
    @DisplayName("리뷰 조회 실패")
    void getreviewFail() throws Exception {
                // given
                int id = 0;
        
        
                // When
                ResultActions perform = this.mvc.perform(
                        get("/reviews/{id}", id)
                                .accept(MediaType.APPLICATION_JSON)
                );

                // Then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value("리뷰가 존재하지 않습니다."))
        ;
    }

    @Test
    @DisplayName("리뷰 조회")
    void getReview() throws Exception {
                // given
                int id = 0;
                given(this.reviewService.getReview(id))
                        .willReturn(
                                Optional.of(new Review(1, makeUser(), makeStore(makeEnterprise()), "내용1", 5.0, 5.0, 5.0, new FileInfo(2, FileType.REVIEW), ReviewStatus.ACTIVE)
                                )
                        );
        
        
                // When
                ResultActions perform = this.mvc.perform(
                        get("/reviews/{id}", id)
                                .accept(MediaType.APPLICATION_JSON)
                );

                // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("내용1"))
                .andExpect(jsonPath("$.tasteRating").value(5.0))
                .andExpect(jsonPath("$.moodRating").value(5.0))
                .andExpect(jsonPath("$.serviceRating").value(5.0))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
        ;
    }

    @Test
    @DisplayName("리뷰 저장 실패")
    void saveReviewFail() throws Exception {

        // given
        List<MultipartFile> files = new ArrayList<>();
        MultipartFile file = new MockMultipartFile(
    "profile", // 파라미터 이름은 프론트엔드에서 정해진 대로 "profile"로 설정합니다.
        "filename.txt", // 파일 이름은 테스트를 위해 아무 값이나 설정합니다.
    "image/jpeg", // 파일 타입은 이미지 파일인 jpeg로 설정합니다.
        "Test data".getBytes() // 파일 내용은 테스트를 위해 아무 값이나 설정합니다.
        );  
        files.add(file);
        // TODO MultiPart파일 넣어서 테스트 하기
        ReviewSaveRequest request = new ReviewSaveRequest(makeStore(makeEnterprise()).toDTO(), "", 1.0, 1.0, 1.0, null);
        

        // When
        ResultActions perform = this.mvc.perform(
                post("/reviews")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value("리뷰 내용을 입력해주세요"))
        ;
    }

    @Test
    @DisplayName("리뷰 저장 성공")
    void saveReview() throws Exception {

        // given
        List<MultipartFile> files = new ArrayList<>();
        MockMultipartFile file = new MockMultipartFile(
    "profile", // 파라미터 이름은 프론트엔드에서 정해진 대로 "profile"로 설정합니다.
        "filename.txt", // 파일 이름은 테스트를 위해 아무 값이나 설정합니다.
    "image/jpeg", // 파일 타입은 이미지 파일인 jpeg로 설정합니다.
        "Test data".getBytes() // 파일 내용은 테스트를 위해 아무 값이나 설정합니다.
        );  
        files.add(file);
        // TODO MultiPart파일 넣어서 테스트 하기
        ReviewSaveRequest request = new ReviewSaveRequest(makeStore(makeEnterprise()).toDTO(), "내용1", 1.0, 1.0, 1.0, null);
        
        Review review = new Review(1, makeUser(), makeStore(makeEnterprise()), "내용1", 5.0, 5.0, 5.0, new FileInfo(2, FileType.REVIEW), ReviewStatus.ACTIVE);

        given(reviewService.save(request)).willReturn(review);

        // When
        ResultActions perform = this.mvc.perform(
                post("/reviews")
                        .content(objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("내용1"))
                .andExpect(jsonPath("$.tasteRating").value(5.0))
                .andExpect(jsonPath("$.moodRating").value(5.0))
                .andExpect(jsonPath("$.serviceRating").value(5.0))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
        ;
    }

    private User makeUser() {
        return User.builder()
                .id(1)
                .username("gogo")
                .password("1234")
                .role(RoleType.USER)
                .email("gogo@gogo.com")
                .tel("01044443333")
                .status(UserStatus.ACTIVE)
                .build();
    }

    private Enterprise makeEnterprise() {
        return Enterprise.builder()
                .id(1)
                .username("hoho")
                .password("1234")
                .role(RoleType.ENTERPRISE)
                .email("hoho@hoho.com")
                .tel("01066667777")
                .status(EnterpriseStatus.ACTIVE)
                .build();
    }

    private EnterpriseStoreInfo makeStore(Enterprise enterprise) {
        return EnterpriseStoreInfo.builder()
                .id(1)
                .enterprise(enterprise)
                .name("store1")
                .address("busan")
                .reservationPrice(12345)
                .reservationTerm("30")
                .reservationCancelDay(LocalDateTime.now())
                .lat(1.1)
                .lon(3.1)
                .fileInfo(new FileInfo(1, FileType.STORE))
                .status(StoreStatus.OPEN)
                .build();

    }
}
