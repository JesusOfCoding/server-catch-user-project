package com.side.serverusercatchproject.modules.enterprise.service;

import com.side.serverusercatchproject.modules.enterprise.entity.Enterprise;
import com.side.serverusercatchproject.modules.enterprise.repository.EnterpriseRepository;
import com.side.serverusercatchproject.modules.enterprise.request.EnterpriseSaveRequest;
import com.side.serverusercatchproject.modules.enterprise.request.EnterpriseUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;

    public EnterpriseService(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }

    public Page<Enterprise> getPage(Pageable pageable) {
        return new PageImpl<>(null);
    }

    public Optional<Enterprise> getEnterprise(Integer id) {
        return Optional.empty();
    }

    public Enterprise save(EnterpriseSaveRequest request) {
        return null;
    }

    public Enterprise update(EnterpriseUpdateRequest request, Enterprise enterprise) {
        return null;
    }
}
