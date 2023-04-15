package com.side.serverusercatchproject.modules.notice.request;

import com.side.serverusercatchproject.common.ValueOfEnum;
import com.side.serverusercatchproject.modules.notice.enums.NoticeStatus;
import jakarta.validation.constraints.NotBlank;

public record NoticeUpdateRequest(
    @NotBlank(message = "공지사항 제목을 입력해주세요.")
    String title,
    @NotBlank(message = "공지사항 내용을 입력해주세요.")
    String content,
    @ValueOfEnum(enumClass = NoticeStatus.class, message = "공지사항 상태 값이 이상이 있습니다. 확인해주세요.")
    String status
) {

}
