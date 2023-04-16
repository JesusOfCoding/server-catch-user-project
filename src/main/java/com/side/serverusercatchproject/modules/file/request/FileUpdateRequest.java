package com.side.serverusercatchproject.modules.file.request;

import com.side.serverusercatchproject.common.ValueOfEnum;
import com.side.serverusercatchproject.modules.file.dto.FileInfoDTO;
import com.side.serverusercatchproject.modules.file.enums.FileStatus;
import jakarta.validation.constraints.NotBlank;

public record FileUpdateRequest(

        @NotBlank(message = "파일 이름을 입력해주세요")
        String fileName,

        @NotBlank(message = "파일 출처를 입력해주세요")
        String fileUrl,

        @ValueOfEnum(enumClass = FileStatus.class, message = "파일 상태 값에 이상이 있습니다. 확인해주세요")
        String status
) {
}
