package com.side.serverusercatchproject.integrated;

import com.side.serverusercatchproject.modules.notice.request.NoticeSaveRequest;
import com.side.serverusercatchproject.modules.notice.request.NoticeUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NoticeTest extends AbstractIntegrated{

    @Test
    void getPage() throws Exception {

        this.mockMvc.perform(
                    get ("/notice?page={page}&size={size}&sort={sort}"
                            , 1
                            , 1
                            , "id,desc"
                    ) .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("notice-list",
                                queryParameters(getPageParameterDescriptors()),
                                responseFields(
                                    subsectionWithPath("content") .description("내용 배열")
                                ).and(getNoticeResponseField("content[].")).and(getPageResponseField())
                        )
                );
    }

    @Test
    void getFail() throws Exception {
        this.mockMvc.perform(
                        get ("/notice/{id}"
                                , 0
                        ) .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andDo(print())
//                .andDo(document("{class-name}/{method-name}", responseFields(getFailResponseField())));
                .andDo(document("notice-fail-detail", responseFields(getFailResponseField())));

    }

    @Test
    void getSuccess() throws Exception {
        this.mockMvc.perform(
                        get ("/notice/{id}"
                                , 1
                        ) .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("notice-detail",
                                pathParameters(parameterWithName("id").description("notice 고유의 값")),
                                responseFields(getNoticeResponseField(""))
                        )
                );

    }



    @Test
    void saveFail() throws Exception {
        NoticeSaveRequest request = new NoticeSaveRequest("", "내용");
        this.mockMvc.perform(
                        post("/notice")
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("notice-save-fail", responseFields(getFailResponseField())));

    }

    @Test
    void saveSuccess() throws Exception {
        NoticeSaveRequest request = new NoticeSaveRequest("제목", "내용");
        this.mockMvc.perform(
                        post("/notice")
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("notice-save",
                                requestFields(getNoticeRequestField()),
                                responseFields(getNoticeResponseField(""))
                        )
                );

    }


    @Test
    void updateFail() throws Exception {
        NoticeUpdateRequest request = new NoticeUpdateRequest("", "내용", "WAIT");
        this.mockMvc.perform(
                        put("/notice/{id}", 0)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("notice-update-fail", responseFields(getFailResponseField())));

    }

    @Test
    void updateSuccess() throws Exception {

        NoticeUpdateRequest request = new NoticeUpdateRequest("제목 (1)", "내용", "WAIT");
        this.mockMvc.perform(
                        put("/notice/{id}", 1)
                                .content(objectMapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(
                        document("notice-update",
                                pathParameters(parameterWithName("id").description("notice 고유의 값")),
                                requestFields(getNoticeRequestField()).and(
                                        fieldWithPath("status").description("공지 상태 - WAIT, COMPLETE")
                                ),
                                responseFields(getNoticeResponseField(""))
                        )
                );

    }


    private FieldDescriptor[] getNoticeRequestField() {
        return new FieldDescriptor[] {
                fieldWithPath("title").description("공지 제목"),
                fieldWithPath("content").description("공지 내용")
        };
    }

    private FieldDescriptor[] getNoticeResponseField(String prefix) {
        return new FieldDescriptor[] {
            fieldWithPath(prefix+"id").description("공지의 id"),
            fieldWithPath(prefix+"title").description("공지 제목"),
            fieldWithPath(prefix+"content").description("공지 내용"),
            fieldWithPath(prefix+"status").description("공지 상태")
        };
    }
//  TODO subsectionWithPath("content").type(JsonFieldType.ARRAY) .description("내용 배열"),

}
