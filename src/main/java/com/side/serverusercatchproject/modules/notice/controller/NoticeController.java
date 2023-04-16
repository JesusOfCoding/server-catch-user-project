package com.side.serverusercatchproject.modules.notice.controller;

import com.side.serverusercatchproject.common.exception.Exception400;
import com.side.serverusercatchproject.modules.notice.*;
import com.side.serverusercatchproject.modules.notice.dto.NoticeDTO;
import com.side.serverusercatchproject.modules.notice.entity.Notice;
import com.side.serverusercatchproject.modules.notice.request.NoticeSaveRequest;
import com.side.serverusercatchproject.modules.notice.request.NoticeUpdateRequest;
import com.side.serverusercatchproject.modules.notice.response.NoticeResponse;
import com.side.serverusercatchproject.modules.notice.service.NoticeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping
    public ResponseEntity<Page<NoticeDTO>> getPage(Pageable pageable) {
        var page = noticeService.getPage(pageable);
        var content = page.getContent()
                .stream()
                .map(Notice::toDTO)
                .toList();

        return ResponseEntity.ok(
                new PageImpl<>(content, pageable, page.getTotalElements())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponse> getNotice (@PathVariable Integer id) {
        var optionalNotice = noticeService.getNotice(id);
        if (optionalNotice.isEmpty()) {
            throw new Exception400(NoticeConst.notFound);
        }

        return ResponseEntity.ok(
            optionalNotice.get().toResponse()
        );
    }

    @PostMapping
    public ResponseEntity<NoticeResponse> saveNotice (
            @Valid @RequestBody NoticeSaveRequest request,
            Errors error
    ) {
        if (error.hasErrors()) {
            throw new Exception400(error.getAllErrors().get(0).getDefaultMessage());
        }

        var notice = noticeService.save(request);
        return ResponseEntity.ok(notice.toResponse());
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoticeResponse> saveNotice (
            @Valid @RequestBody NoticeUpdateRequest request,
            Errors error,
            @PathVariable Integer id
    ) {
        if (error.hasErrors()) {
            throw new Exception400(error.getAllErrors().get(0).getDefaultMessage());
        }

        var optionalNotice = noticeService.getNotice(id);
        if (optionalNotice.isEmpty()) {
            throw new Exception400(NoticeConst.notFound);
        }

        var notice = noticeService.update(request, optionalNotice.get());
        return ResponseEntity.ok(notice.toResponse());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> saveNotice (
            @PathVariable Integer id
    ) {
        var optionalNotice = noticeService.getNotice(id);
        if (optionalNotice.isEmpty()) {
            throw new Exception400(NoticeConst.notFound);
        }

        noticeService.delete(optionalNotice.get());

        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }

}
