package com.side.serverusercatchproject.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.serverusercatchproject.modules.file.controller.FileInfoController;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import com.side.serverusercatchproject.modules.file.request.FileInfoSaveRequest;
import com.side.serverusercatchproject.modules.file.service.FileInfoService;
import com.side.serverusercatchproject.modules.notice.controller.NoticeController;
import com.side.serverusercatchproject.modules.notice.entity.Notice;
import com.side.serverusercatchproject.modules.notice.enums.NoticeStatus;
import com.side.serverusercatchproject.modules.notice.request.NoticeSaveRequest;
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
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileInfoController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class FileInfoMockTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private FileInfoService fileInfoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("파일정보 조회 (페이지)")
    void getPage() throws Exception {
        Pageable pageable = PageRequest.of(1, 10);
        Page<FileInfo> page = new PageImpl<>(
                List.of(
                        new FileInfo(1, FileType.BANNER),
                        new FileInfo(2, FileType.STORE)
                )
        );


        // given
        given(this.fileInfoService.getPage(pageable)).willReturn(page);


        // When
        ResultActions perform = this.mvc.perform(
                get("/fileInfo?page={page}&size={size}", 1, 10)
                        .accept(MediaType.APPLICATION_JSON)
        );


        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].type").value("BANNER"))

                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].type").value("STORE"))
        ;
    }

    @Test
    @DisplayName("파일정보 조회 실패")
    void getFileInfoFail() throws Exception {

        // given
        int id = 0;
        given(this.fileInfoService.getFileInfo(id)).willReturn(Optional.empty());


        // When
        ResultActions perform = this.mvc.perform(
                get("/fileInfo/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
        );


        // Then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value("파일 정보를 받을 수 없습니다."))
        ;
    }

    @Test
    @DisplayName("파일정보 조회")
    void getFileInfo() throws Exception {

        // given
        int id = 0;
        given(this.fileInfoService.getFileInfo(id))
                .willReturn(
                        Optional.of(new FileInfo(1, FileType.BANNER))
                );


        // When
        ResultActions perform = this.mvc.perform(
                get("/fileInfo/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
        );


        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("BANNER"))
        ;
    }

    @Test
    @DisplayName("파일정보 저장 실패")
    void saveFileInfoFail() throws Exception {


        // given
        FileInfoSaveRequest request = new FileInfoSaveRequest("");

        // When
        ResultActions perform = this.mvc.perform(
                post("/fileInfo")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );


        // Then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value("파일 출처를 입력해주세요"))
        ;
    }

    @Test
    @DisplayName("파일정보 저장 성공")
    void saveFileInfo() throws Exception {


        // given
        FileInfoSaveRequest request = new FileInfoSaveRequest("BANNER");
        given(this.fileInfoService.save(request))
                .willReturn(
                        request.toEntity()
                );

        // When
        ResultActions perform = this.mvc.perform(
                post("/fileInfo")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );


        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.type").value("BANNER"))
        ;
    }


}
