package com.side.serverusercatchproject.modules.file.controller;

import com.side.serverusercatchproject.common.exception.Exception400;
import com.side.serverusercatchproject.modules.file.FileConst;
import com.side.serverusercatchproject.modules.file.FileInfoConst;
import com.side.serverusercatchproject.modules.file.dto.FileDTO;
import com.side.serverusercatchproject.modules.file.dto.FileInfoDTO;
import com.side.serverusercatchproject.modules.file.entity.File;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.request.FileInfoSaveRequest;
import com.side.serverusercatchproject.modules.file.request.FileSaveRequest;
import com.side.serverusercatchproject.modules.file.request.FileUpdateRequest;
import com.side.serverusercatchproject.modules.file.response.FileInfoResponse;
import com.side.serverusercatchproject.modules.file.response.FileResponse;
import com.side.serverusercatchproject.modules.file.service.FileService;
import com.side.serverusercatchproject.modules.notice.NoticeConst;
import com.side.serverusercatchproject.modules.notice.request.NoticeUpdateRequest;
import com.side.serverusercatchproject.modules.notice.response.NoticeResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public ResponseEntity<Page<FileDTO>> getPage(Pageable pageable) {
        var page = fileService.getPage(pageable);
        var content = page.getContent()
                .stream()
                .map(File::toDTO)
                .toList();

        return ResponseEntity.ok(
                new PageImpl<>(content, pageable, page.getTotalElements())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileResponse> getFile (@PathVariable Integer id) {
        var optionalFile = fileService.getFile(id);
        if (optionalFile.isEmpty()) {
            throw new Exception400(FileConst.notfound);
        }

        return ResponseEntity.ok(
                optionalFile.get().toResponse()
        );
    }

    @PostMapping
    public ResponseEntity<FileResponse> saveFile (
            @Valid @RequestBody FileSaveRequest request,
            Errors error
    ) {
        if (error.hasErrors()) {
            throw new Exception400(error.getAllErrors().get(0).getDefaultMessage());
        }

        var fileInfo = fileService.save(request);
        return ResponseEntity.ok(fileInfo.toResponse());
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<FileResponse> updateFile (
//            @Valid @RequestBody FileUpdateRequest request,
//            Errors error,
//            @PathVariable Integer id
//    ) {
//        if (error.hasErrors()) {
//            throw new Exception400(error.getAllErrors().get(0).getDefaultMessage());
//        }
//
//        var optionalFile = fileService.getFile(id);
//        if (optionalFile.isEmpty()) {
//            throw new Exception400(FileConst.notfound);
//        }
//
//        var file = fileService.update(request, optionalFile.get());
//        return ResponseEntity.ok(file.toResponse());
//    }

}
