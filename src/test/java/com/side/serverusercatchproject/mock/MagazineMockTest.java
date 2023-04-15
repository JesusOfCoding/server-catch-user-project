package com.side.serverusercatchproject.mock;

import com.side.serverusercatchproject.modules.notice.controller.NoticeController;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

@WebMvcTest(NoticeController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class MagazineMockTest {



}
