package com.side.serverusercatchproject.mock.enterprise;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.side.serverusercatchproject.modules.enterprise.controller.EnterpriseController;
import com.side.serverusercatchproject.modules.enterprise.entity.Enterprise;
import com.side.serverusercatchproject.modules.enterprise.enums.EnterpriseStatus;
import com.side.serverusercatchproject.modules.enterprise.request.EnterpriseSaveRequest;
import com.side.serverusercatchproject.modules.enterprise.request.EnterpriseUpdateRequest;
import com.side.serverusercatchproject.modules.enterprise.service.EnterpriseService;
import com.side.serverusercatchproject.util.type.RoleType;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EnterpriseController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class EnterpriseMockTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private EnterpriseService enterpriseService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("기업 회원 조회 (페이지)")
    void getPage() throws Exception{
        Pageable pageable = PageRequest.of(1, 10);
        Page<Enterprise> page = new PageImpl<>(
                List.of(
                        new Enterprise(1, "alss", "1234", RoleType.ENTERPRISE, "alss@naver.com", "01011112222", EnterpriseStatus.ACTIVE),
                        new Enterprise(2, "hoho", "1234", RoleType.ENTERPRISE, "hoho@naver.com", "01022223333", EnterpriseStatus.ACTIVE),
                        new Enterprise(3, "haha", "1234", RoleType.ENTERPRISE, "haha@naver.com", "01044445555", EnterpriseStatus.ACTIVE)
                )
        );

        // given
        given(this.enterpriseService.getPage(pageable)).willReturn(page);

        // when
        ResultActions perform = this.mvc.perform(
                get("/enterprise?page={page}&size={size}", 1, 10)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].username").value("alss"))
                .andExpect(jsonPath("$.content[0].role").value("ENTERPRISE"))
                .andExpect(jsonPath("$.content[0].email").value("alss@naver.com"))
                .andExpect(jsonPath("$.content[0].tel").value("01011112222"))
                .andExpect(jsonPath("$.content[0].status").value("ACTIVE"))

                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].username").value("hoho"))
                .andExpect(jsonPath("$.content[1].role").value("ENTERPRISE"))
                .andExpect(jsonPath("$.content[1].email").value("hoho@naver.com"))
                .andExpect(jsonPath("$.content[1].tel").value("01022223333"))
                .andExpect(jsonPath("$.content[1].status").value("ACTIVE"));
    }

    @Test
    @DisplayName("기업 회원 조회 실패")
    void getEnterpriseFail() throws Exception {

        // given
        int id = 0;
        given(this.enterpriseService.getEnterprise(id)).willReturn(Optional.empty());

        // when
        ResultActions perform = this.mvc.perform(
                get("/enterprise/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value("기업 회원이 존재하지 않습니다."))
        ;
    }

    @Test
    @DisplayName("기업 회원 조회")
    void getEnterprise() throws Exception {

        // given
        int id = 0;
        given(this.enterpriseService.getEnterprise(id))
                .willReturn(
                        Optional.of(new Enterprise(1, "alss", "1234", RoleType.ENTERPRISE, "alss@naver.com", "01011112222", EnterpriseStatus.ACTIVE))
                );

        // when
        ResultActions perform = this.mvc.perform(
                get("/enterprise/{id}", id)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("alss"))
                .andExpect(jsonPath("$.role").value("ENTERPRISE"))
                .andExpect(jsonPath("$.email").value("alss@naver.com"))
                .andExpect(jsonPath("$.tel").value("01011112222"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
        ;

    }

    @Test
    @DisplayName("기업 회원 저장 실패")
    void saveEnterpriseFail() throws Exception {

        // given
        EnterpriseSaveRequest request = new EnterpriseSaveRequest("", "1234", "alss@naver.com", "01011112222");

        // when
        ResultActions perform = this.mvc.perform(
                post("/enterprise")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value("아이디를 입력해주세요"))
        ;
    }

    @Test
    @DisplayName("기업 회원 저장")
    void saveEnterprise() throws Exception {

        // given
        EnterpriseSaveRequest request = new EnterpriseSaveRequest("alss", "1234", "alss@naver.com", "01011112222");
        given(this.enterpriseService.save(request))
                .willReturn(
                        new Enterprise(1, "alss", "1234", RoleType.ENTERPRISE, "alss@naver.com", "01011112222", EnterpriseStatus.ACTIVE)
                );

        // when
        ResultActions perform = this.mvc.perform(
                post("/enterprise")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("alss"))
                .andExpect(jsonPath("$.role").value("ENTERPRISE"))
                .andExpect(jsonPath("$.email").value("alss@naver.com"))
                .andExpect(jsonPath("$.tel").value("01011112222"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
        ;
    }

    @Test
    @DisplayName("기업 회원 수정 유효성 실패")
    void updateEnterpriseValidFail() throws Exception{

        // given
        int id = 0;
        EnterpriseUpdateRequest request = new EnterpriseUpdateRequest("", "1234", "alss@naver.com", "01011112222", "ACTIVE");

        // when
        ResultActions perform = this.mvc.perform(
                put("/enterprise/{id}", id)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value("아이디를 입력해주세요"))
        ;
    }

    @Test
    @DisplayName("기업 회원 수정 조회 실패")
    void updateEnterpriseNotFoundFail() throws Exception {

        // given
        int id = 0;
        EnterpriseUpdateRequest request = new EnterpriseUpdateRequest("alss", "1234", "alss@naver.com", "01011112222", "ACTIVE");
        given(this.enterpriseService.getEnterprise(id)).willReturn(Optional.empty());

        // when
        ResultActions perform = this.mvc.perform(
                put("/enterprise/{id}", id)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        perform
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.detail").value("기업 회원이 존재하지 않습니다."))
        ;
    }

    @Test
    @DisplayName("기업 회원 수정 조회 실패")
    void updateEnterprise() throws Exception {

        // given
        int id = 0;
        EnterpriseUpdateRequest request = new EnterpriseUpdateRequest("alss", "1234", "alss@naver.com", "01011112222", "ACTIVE");
        var optionalEnterprise = Optional.of(new Enterprise(1, "alss11", "1234", RoleType.ENTERPRISE, "alss@naver.com", "01011112222", EnterpriseStatus.ACTIVE));
        given(this.enterpriseService.getEnterprise(id)).willReturn(optionalEnterprise);

        given(this.enterpriseService.update(
                request, optionalEnterprise.get())
            ).willReturn(
                new Enterprise(1, "alss", "1234", RoleType.ENTERPRISE, "alss@naver.com", "01011112222", EnterpriseStatus.ACTIVE)
        );

        // when
        ResultActions perform = this.mvc.perform(
                put("/enterprise/{id}", id)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        );

        // Then
        perform
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("alss"))
                .andExpect(jsonPath("$.role").value("ENTERPRISE"))
                .andExpect(jsonPath("$.email").value("alss@naver.com"))
                .andExpect(jsonPath("$.tel").value("01011112222"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
        ;
    }
}
