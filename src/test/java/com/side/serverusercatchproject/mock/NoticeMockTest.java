package com.side.serverusercatchproject.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.serverusercatchproject.modules.notice.*;
import com.side.serverusercatchproject.modules.notice.controller.NoticeController;
import com.side.serverusercatchproject.modules.notice.entity.Notice;
import com.side.serverusercatchproject.modules.notice.enums.NoticeStatus;
import com.side.serverusercatchproject.modules.notice.request.NoticeSaveRequest;
import com.side.serverusercatchproject.modules.notice.request.NoticeUpdateRequest;
import com.side.serverusercatchproject.modules.notice.service.NoticeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//@SpringBootTest
@WebMvcTest(NoticeController.class)
public class NoticeMockTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private NoticeService noticeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("공지사항 조회 (페이지)")
    void getPage() throws Exception {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Notice> page = new PageImpl<>(
                List.of(
                    new Notice(1, "공지사항(1)", "내용 1", NoticeStatus.WAIT),
                    new Notice(2, "공지사항(2)", "내용 2", NoticeStatus.ACTIVE)
                )
        );


        // given
        given(this.noticeService.getPage(pageable)).willReturn(page);


        // When
        ResultActions perform = this.mvc.perform(
                get("/notice?page={page}&size={size}", 1, 10)
                        .accept(MediaType.APPLICATION_JSON)
        );


        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("공지사항(1)"))
                .andExpect(jsonPath("$.content[0].content").value("내용 1"))
                .andExpect(jsonPath("$.content[0].status").value(NoticeStatus.WAIT.name()))


                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].title").value("공지사항(2)"))
                .andExpect(jsonPath("$.content[1].content").value("내용 2"))
                .andExpect(jsonPath("$.content[1].status").value(NoticeStatus.ACTIVE.name()))
        ;
    }

    @Test
    @DisplayName("공지사항 조회 실패")
    void getNoticeFail() throws Exception {

        // given
        int id = 0;
        given(this.noticeService.getNotice(id)).willReturn(Optional.empty());


        // When
        ResultActions perform = this.mvc.perform(
                get("/notice/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
        );


        // Then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value("공지사항이 존재하지 않습니다."))
        ;
    }




    @Test
    @DisplayName("공지사항 조회")
    void getNotice() throws Exception {

        // given
        int id = 0;
        given(this.noticeService.getNotice(id))
                .willReturn(
                        Optional.of(new Notice(1, "공지사항(1)", "내용 1", NoticeStatus.WAIT))
                );


        // When
        ResultActions perform = this.mvc.perform(
                get("/notice/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
        );


        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("공지사항(1)"))
                .andExpect(jsonPath("$.content").value("내용 1"))
                .andExpect(jsonPath("$.status").value(NoticeStatus.WAIT.name()))
        ;
    }

    @Test
    @DisplayName("공지사항 저장 실패")
    void saveNoticeFail() throws Exception {


        // given
        NoticeSaveRequest request = new NoticeSaveRequest("", "내용");

        // When
        ResultActions perform = this.mvc.perform(
                post("/notice")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );


        // Then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value("공지사항 제목을 입력해주세요."))
        ;
    }

    @Test
    @DisplayName("공지사항 저장 성공")
    void saveNotice() throws Exception {


        // given
        NoticeSaveRequest request = new NoticeSaveRequest("제목", "내용");
        given(this.noticeService.save(request))
                .willReturn(
                        request.toEntity()
                );


        // When
        ResultActions perform = this.mvc.perform(
                post("/notice")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );


        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andExpect(jsonPath("$.status").value(NoticeStatus.WAIT.name()))
        ;
    }


    @Test
    @DisplayName("공지사항 수정 Valid 실패")
    void updateValidFail1() throws Exception {


        // given
        int id = 0;
        NoticeUpdateRequest request = new NoticeUpdateRequest("", "내용", "WAIT");

        // When
        ResultActions perform = this.mvc.perform(
                put("/notice/{id}", id)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );


        // Then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value("공지사항 제목을 입력해주세요."))
        ;
    }

    @Test
    @DisplayName("공지사항 수정 Enums 실패")
    void updateValidFail2() throws Exception {


        // given
        int id = 0;
        NoticeUpdateRequest request = new NoticeUpdateRequest("제목", "내용", "asdsadsad");

        // When
        ResultActions perform = this.mvc.perform(
                put("/notice/{id}", id)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );


        // Then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value("공지사항 상태 값이 이상이 있습니다. 확인해주세요."))
        ;
    }

    @Test
    @DisplayName("공지사항 수정 조회 실패")
    void updateNotFoundNotice() throws Exception {

        // given
        int id = 0;
        NoticeUpdateRequest request = new NoticeUpdateRequest("제목", "내용", "WAIT");
        given(this.noticeService.getNotice(id)).willReturn(Optional.empty());

        // When
        ResultActions perform = this.mvc.perform(
                put("/notice/{id}", id)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value(NoticeConst.notFound))
        ;
    }

    @Test
    @DisplayName("공지사항 수정")
    void updateNotice() throws Exception {

        // given
        int id = 1;
        NoticeUpdateRequest request = new NoticeUpdateRequest("제목", "내용", "WAIT");

        Optional<Notice> optional = Optional.of(new Notice(1, "공지사항(1)", "내용 1", NoticeStatus.WAIT));
        given(this.noticeService.getNotice(id)).willReturn(optional);

        given(noticeService.update(request, optional.get())).willReturn(new Notice(1, "제목", "내용", NoticeStatus.WAIT));


        // When
        ResultActions perform = this.mvc.perform(
                put("/notice/{id}", id)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("내용"))
                .andExpect(jsonPath("$.status").value(NoticeStatus.WAIT.name()))
        ;
    }

    @Test
    @DisplayName("공지사항 삭제 실패")
    void deleteNotFoundNotice() throws Exception {

        // given
        int id = 0;
        given(this.noticeService.getNotice(id)).willReturn(Optional.empty());


        // When
        ResultActions perform = this.mvc.perform(
                delete("/notice/{id}", id)
        );

        // Then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value(NoticeConst.notFound))
        ;
    }

    @Test
    @DisplayName("공지사항 삭제")
    void deleteNotice() throws Exception {

        // given
        int id = 0;
        Optional<Notice> optional = Optional.of(new Notice());
        given(this.noticeService.getNotice(id)).willReturn(optional);


        // When
        ResultActions perform = this.mvc.perform(
                delete("/notice/{id}", id)
        );

        // Then
        MvcResult mvcResult = perform
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        Assertions.assertEquals(mvcResult.getResponse().getContentAsString(), "삭제가 완료되었습니다.");
    }
}
