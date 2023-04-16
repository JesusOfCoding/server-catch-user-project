package com.side.serverusercatchproject.modules.file.response;

import com.side.serverusercatchproject.modules.file.dto.FileInfoDTO;

public record FileResponse(
        Integer id,

        FileInfoDTO fileInfo,

        String fileName,

        String fileUrl,

        String status
) {

}
