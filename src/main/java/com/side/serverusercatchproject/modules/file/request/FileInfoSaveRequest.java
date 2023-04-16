package com.side.serverusercatchproject.modules.file.request;

import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import jakarta.validation.constraints.NotBlank;

public record FileInfoSaveRequest(
        @NotBlank(message = "파일 출처를 입력해주세요") // TODO 출처를 Request 에서 ?
        String type
) {
    public FileInfo toEntity() {
        return new FileInfo(null, FileType.BANNER);
    }
}
