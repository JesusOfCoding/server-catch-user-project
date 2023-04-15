package com.side.serverusercatchproject.modules.magazine.service;

import com.side.serverusercatchproject.modules.magazine.entity.Magazine;
import com.side.serverusercatchproject.modules.magazine.repository.MagazineRepository;
import com.side.serverusercatchproject.modules.notice.entity.Notice;
import com.side.serverusercatchproject.modules.notice.repository.NoticeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MagazineService {
    private final MagazineRepository magazineRepository;

    public MagazineService(MagazineRepository magazineRepository) {
        this.magazineRepository = magazineRepository;
    }

    public Page<Magazine> getPage(Pageable pageable) {
        return magazineRepository.findAll(pageable);
    }

    public Optional<Magazine> getNotice(Integer id) {
        return magazineRepository.findById(id);
    }
}
