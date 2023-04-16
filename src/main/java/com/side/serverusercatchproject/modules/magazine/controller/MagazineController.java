package com.side.serverusercatchproject.modules.magazine.controller;

import com.side.serverusercatchproject.modules.magazine.dto.MagazineDTO;
import com.side.serverusercatchproject.modules.magazine.entity.Magazine;
import com.side.serverusercatchproject.modules.magazine.repository.MagazineRepository;
import com.side.serverusercatchproject.modules.magazine.service.MagazineService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/magazines")
public class MagazineController {
    private final MagazineService magazineService;

//    private final MagazineRepository magazineRepository;

    public MagazineController(MagazineService magazineService) {
        this.magazineService = magazineService;
//        this.magazineRepository = magazineRepository;
    }

    @GetMapping
    public ResponseEntity<Page<MagazineDTO>> getPage(Pageable pageable) {
        var page = magazineService.getPage(pageable);
//        page.getContent().stream().map(Magazine::toDto);

        return ResponseEntity.ok().body(Page.empty());
    }
}
