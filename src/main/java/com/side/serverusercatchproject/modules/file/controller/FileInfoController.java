package com.side.serverusercatchproject.modules.file.controller;

import com.side.serverusercatchproject.common.exception.Exception400;
import com.side.serverusercatchproject.modules.file.FileInfoConst;
import com.side.serverusercatchproject.modules.file.dto.FileInfoDTO;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.request.FileInfoSaveRequest;
import com.side.serverusercatchproject.modules.file.response.FileInfoResponse;
import com.side.serverusercatchproject.modules.file.service.FileInfoService;
import com.side.serverusercatchproject.modules.notice.NoticeConst;
import com.side.serverusercatchproject.modules.notice.request.NoticeSaveRequest;
import com.side.serverusercatchproject.modules.notice.response.NoticeResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

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
            throw new Exception400(FileInfoConst.notfound);
        }

        return ResponseEntity.ok(
                optionalFileInfo.get().toResponse()
        );
    }

    @PostMapping
    public ResponseEntity<FileInfoResponse> saveFileInfo (
            @Valid @RequestBody FileInfoSaveRequest request,
            Errors error
    ) {
        if (error.hasErrors()) {
            throw new Exception400(error.getAllErrors().get(0).getDefaultMessage());
        }

        var fileInfo = fileInfoService.save(request);
        return ResponseEntity.ok(fileInfo.toResponse());
    }
}
