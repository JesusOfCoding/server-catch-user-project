package com.side.serverusercatchproject.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.serverusercatchproject.modules.file.service.FileService;
import com.side.serverusercatchproject.modules.notice.controller.NoticeController;
import com.side.serverusercatchproject.modules.notice.entity.Notice;
import com.side.serverusercatchproject.modules.notice.enums.NoticeStatus;
import com.side.serverusercatchproject.modules.notice.service.NoticeService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoticeController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class FileMockTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private FileService fileService;

    private final ObjectMapper objectMapper = new ObjectMapper();

//    @Test
//    @DisplayName("공지사항 조회 (페이지)")
//    void getPage() throws Exception {
//        Pageable pageable = PageRequest.of(1, 10);
//        Page<Notice> page = new PageImpl<>(
//                List.of(
//                        new Notice(1, "공지사항(1)", "내용 1", NoticeStatus.WAIT),
//                        new Notice(2, "공지사항(2)", "내용 2", NoticeStatus.ACTIVE)
//                )
//        );
//
//
//        // given
////        given(this.fileService.getPage(pageable)).willReturn(page);
//
//
//        // When
//        ResultActions perform = this.mvc.perform(
//                get("/notice?page={page}&size={size}", 1, 10)
//                        .accept(MediaType.APPLICATION_JSON)
//        );
//
//
//        // Then
//        perform
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.content[0].id").value(1))
//                .andExpect(jsonPath("$.content[0].title").value("공지사항(1)"))
//                .andExpect(jsonPath("$.content[0].content").value("내용 1"))
//                .andExpect(jsonPath("$.content[0].status").value(NoticeStatus.WAIT.name()))
//
//
//                .andExpect(jsonPath("$.content[1].id").value(2))
//                .andExpect(jsonPath("$.content[1].title").value("공지사항(2)"))
//                .andExpect(jsonPath("$.content[1].content").value("내용 2"))
//                .andExpect(jsonPath("$.content[1].status").value(NoticeStatus.ACTIVE.name()))
//        ;
//    }
}
