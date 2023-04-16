package com.side.serverusercatchproject.modules.enterprise.controller;

import com.side.serverusercatchproject.common.exception.Exception400;
import com.side.serverusercatchproject.modules.EnterpriseConst;
import com.side.serverusercatchproject.modules.enterprise.dto.EnterpriseDTO;
import com.side.serverusercatchproject.modules.enterprise.entity.Enterprise;
import com.side.serverusercatchproject.modules.enterprise.request.EnterpriseSaveRequest;
import com.side.serverusercatchproject.modules.enterprise.request.EnterpriseUpdateRequest;
import com.side.serverusercatchproject.modules.enterprise.response.EnterpriseResponse;
import com.side.serverusercatchproject.modules.enterprise.service.EnterpriseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    public EnterpriseController(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    @GetMapping
    public ResponseEntity<Page<EnterpriseDTO>> getPage(Pageable pageable) {
        var page = enterpriseService.getPage(pageable);
        var content = page.getContent()
                .stream()
                .map(Enterprise::toDTO)
                .toList();

        return ResponseEntity.ok(
                new PageImpl<>(content, pageable, page.getTotalElements())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnterpriseResponse> getEnterprise (@PathVariable Integer id){
        var optionalEnterprise = enterpriseService.getEnterprise(id);

        // 기업 회원을 찾지 못할 경우
        if (optionalEnterprise.isEmpty()) {
            throw new Exception400(EnterpriseConst.notFound);
        }

        return ResponseEntity.ok(
                optionalEnterprise.get().toResponse()
        );
    }

    @PostMapping
    public ResponseEntity<EnterpriseResponse> saveEnterprise (
            @Valid @RequestBody EnterpriseSaveRequest request,
            Errors errors
    ) {
        // 유효성 검사
        if (errors.hasErrors()) {
            throw new Exception400(errors.getAllErrors().get(0).getDefaultMessage());
        }

        var enterprise = enterpriseService.save(request);
        return ResponseEntity.ok(enterprise.toResponse());

    }

    @PutMapping("/{id}")
    public ResponseEntity<EnterpriseResponse> saveEnterprise (
            @Valid @RequestBody EnterpriseUpdateRequest request,
            Errors errors,
            @PathVariable Integer id
    ) {
        // 유효성 검사
        if (errors.hasErrors()) {
            throw new Exception400(errors.getAllErrors().get(0).getDefaultMessage());
        }

        var optionalEnterprise = this.enterpriseService.getEnterprise(id);

        // 기업 회원을 찾지 못할 경우
        if (optionalEnterprise.isEmpty()) {
            throw new Exception400(EnterpriseConst.notFound);
        }

        var enterprise = enterpriseService.update(request, optionalEnterprise.get());
        return ResponseEntity.ok(enterprise.toResponse());
    }
}
