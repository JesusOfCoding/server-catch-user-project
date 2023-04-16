package com.side.serverusercatchproject.modules.file.controller;

import com.side.serverusercatchproject.common.exception.Exception400;
import com.side.serverusercatchproject.modules.file.dto.FileInfoDTO;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.response.FileInfoResponse;
import com.side.serverusercatchproject.modules.file.service.FileInfoService;
import com.side.serverusercatchproject.modules.notice.NoticeConst;
import com.side.serverusercatchproject.modules.notice.response.NoticeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fileInfo")
public class FileInfoController {

    private final FileInfoService fileInfoService;

    public FileInfoController(FileInfoService fileInfoService) {
        this.fileInfoService = fileInfoService;
    }

    @GetMapping
    public ResponseEntity<Page<FileInfoDTO>> getPage(Pageable pageable) {
        var page = fileInfoService.getPage(pageable);
        var content = page.getContent()
                .stream()
                .map(FileInfo::toDTO)
                .toList();

        return ResponseEntity.ok(
                new PageImpl<>(content, pageable, page.getTotalElements())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileInfoResponse> getFileInfo (@PathVariable Integer id) {
        var optionalFileInfo = fileInfoService.getFileInfo(id);
        if (optionalFileInfo.isEmpty()) {
            throw new Exception400(NoticeConst.notFound);
        }

        return ResponseEntity.ok(
                optionalFileInfo.get().toResponse()
        );
    }
}
