package com.side.serverusercatchproject.modules.notice.service;

import com.side.serverusercatchproject.common.exception.Exception400;
import com.side.serverusercatchproject.modules.notice.entity.Notice;
import com.side.serverusercatchproject.modules.notice.enums.NoticeStatus;
import com.side.serverusercatchproject.modules.notice.repository.NoticeRepository;
import com.side.serverusercatchproject.modules.notice.request.NoticeSaveRequest;
import com.side.serverusercatchproject.modules.notice.request.NoticeUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public NoticeService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }


    public Page<Notice> getPage(Pageable pageable) {
        return noticeRepository.findAll(pageable);
    }

    public Optional<Notice> getNotice(Integer id) {
        return noticeRepository.findById(id);
    }

    @Transactional
    public Notice save(NoticeSaveRequest request) {
        return noticeRepository.save(request.toEntity());
    }

    @Transactional
    public Notice update(NoticeUpdateRequest request, Notice notice) {
        notice.setTitle(request.title());
        notice.setContent(request.content());
        notice.setStatus(NoticeStatus.valueOf(request.status()));
        return noticeRepository.save(notice);
    }

    @Transactional
    public void delete(Notice notice) {
        noticeRepository.delete(notice);
    }
}
