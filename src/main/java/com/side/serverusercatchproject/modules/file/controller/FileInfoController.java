package com.side.serverusercatchproject.modules.file.controller;

import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.service.FileInfoService;
import com.side.serverusercatchproject.modules.notice.dto.NoticeDTO;
import com.side.serverusercatchproject.modules.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fileInfo")
public class FileInfoController {

    private final FileInfoService fileInfoService;

    public FileInfoController(FileInfoService fileInfoService) {
        this.fileInfoService = fileInfoService;
    }

//    @GetMapping
//    public ResponseEntity<Page<FileInfo>> getPage(Pageable pageable) {
//        fileInfoService.getPage(pageable);
//        var content = page.getContent()
//                .stream()
//                .map(Notice::toDTO)
//                .toList();
//
//        return ResponseEntity.ok(
//                new PageImpl<>(content, pageable, page.getTotalElements())
//        );
//    }
}
