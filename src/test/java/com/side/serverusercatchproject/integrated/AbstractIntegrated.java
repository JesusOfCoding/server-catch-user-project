package com.side.serverusercatchproject.integrated;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractIntegrated {
    // build 위치
    //build/generated-snippets

    protected MockMvc mockMvc;
    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {

//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
//                .apply(documentationConfiguration(this.restDocumentation)
//                  .uris()
//                        .withScheme("https")
//                        .withHost("example.com")
//                        .withPort(443))
//                .build();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    protected FieldDescriptor[] getPageResponseField() {
        return new FieldDescriptor[] {
                subsectionWithPath("content").description("내용 배열"),
                subsectionWithPath("pageable").description("page 종합 정보"),
                fieldWithPath("last").description("마지막 페이지 여부"),
                fieldWithPath("totalPages").description("총 페이지 수"),
                fieldWithPath("totalElements").description("총 요소 수"),
                fieldWithPath("size").description("페이지당 요소 수"),
                fieldWithPath("number").description("현재 페이지"),
                subsectionWithPath("sort").description("정렬"),
                fieldWithPath("numberOfElements").description("현재 페이지의 요소 수"),
                fieldWithPath("first").description("첫 페이지 여부"),
                fieldWithPath("empty").description("빈 페이지 여부")
        };
    }

    protected FieldDescriptor[] getFailResponseField() {
        return new FieldDescriptor[] {
                fieldWithPath("type").description("type"),
                fieldWithPath("title").description("에러 코드 (이름)"),
                fieldWithPath("status").description("에러 코드"),
                fieldWithPath("detail").description("에러 메세지 (중요)"),
                fieldWithPath("instance").description("요청 경로")
        };
    }

    protected List<ParameterDescriptor> getPageParameterDescriptors (){
        return List.of(parameterWithName("page").description("The page to retrieve"),
                parameterWithName("size").description("The page to retrieve"),
                parameterWithName("sort").description("The page to retrieve"));
    }

}
