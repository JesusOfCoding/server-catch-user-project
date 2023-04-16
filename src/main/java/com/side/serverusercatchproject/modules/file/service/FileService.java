package com.side.serverusercatchproject.modules.file.service;

import com.side.serverusercatchproject.modules.file.dto.FileInfoDTO;
import com.side.serverusercatchproject.modules.file.entity.File;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.enums.FileStatus;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import com.side.serverusercatchproject.modules.file.repository.FileRepository;
import com.side.serverusercatchproject.modules.file.request.FileInfoSaveRequest;
import com.side.serverusercatchproject.modules.file.request.FileSaveRequest;
import com.side.serverusercatchproject.modules.file.request.FileUpdateRequest;
import com.side.serverusercatchproject.modules.file.response.FileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String utf(String url){
        return url;
    }

    public Page<File> getPage(Pageable pageable) {
        return fileRepository.findAll(pageable);
    }

    public Optional<File> getFile(Integer id) {
        return fileRepository.findById(id);
    }

    @Transactional
    public File save(FileSaveRequest request) {
        return null;
    }

    public File update(FileUpdateRequest request, File file) {
        file.setFileUrl(request.fileUrl());
        file.setFileName(request.fileName());
        file.setStatus(FileStatus.valueOf(request.status()));
        return fileRepository.save(file);
    }

    @Transactional
    public void delete(File file) {
        fileRepository.delete(file);
    }
}
