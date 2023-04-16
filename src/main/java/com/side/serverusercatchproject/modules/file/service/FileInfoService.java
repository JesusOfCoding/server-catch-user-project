package com.side.serverusercatchproject.modules.file.service;

import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.repository.FileInfoRepository;
import com.side.serverusercatchproject.modules.file.request.FileInfoSaveRequest;
import com.side.serverusercatchproject.modules.notice.entity.Notice;
import com.side.serverusercatchproject.modules.notice.repository.NoticeRepository;
import com.side.serverusercatchproject.modules.notice.request.NoticeSaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FileInfoService {

    private final FileInfoRepository fileInfoRepository;

    public FileInfoService(FileInfoRepository fileInfoRepository) {
        this.fileInfoRepository = fileInfoRepository;
    }


    public Page<FileInfo> getPage(Pageable pageable) {
        return fileInfoRepository.findAll(pageable);
    }

    public void getFileInfo(Integer id) {


    }

//    public Optional<FileInfo> getFileInfo(Integer id) {
//        return fileInfoRepository.findById(id);
//    }
//
//    @Transactional
//    public FileInfo save(FileInfoSaveRequest request) {
//        return fileInfoRepository.save(request.toEntity());
//    }

}
