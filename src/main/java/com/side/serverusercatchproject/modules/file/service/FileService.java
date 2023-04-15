package com.side.serverusercatchproject.modules.file.service;

import com.side.serverusercatchproject.modules.file.repository.FileRepository;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String utf(String url){
        return url;
    }

}
