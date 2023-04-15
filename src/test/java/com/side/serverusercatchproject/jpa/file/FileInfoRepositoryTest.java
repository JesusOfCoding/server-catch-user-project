package com.side.serverusercatchproject.jpa.file;

import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.repository.FileInfoRepository;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@DisplayName("파일정보 JPA 테스트")
public class FileInfoRepositoryTest {

    @Autowired
    private FileInfoRepository fileInfoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void init(){
        setUp(FileType.BANNER);
    }

    @Test
    @Transactional
    @DisplayName("파일 정보 조회")
    void selectAll() {
        List<FileInfo> fileInfoList = fileInfoRepository.findAll();
        Assertions.assertNotEquals(fileInfoList.size(), 0);

        FileInfo fileInfo = fileInfoList.get(0);
        Assertions.assertEquals(fileInfo.getType(), FileType.BANNER);
    }

    @Test
    @Transactional
    @DisplayName("파일 정보 조회 및 수정")
    void selectAndUpdate() {
        var optionalFileInfoList = this.fileInfoRepository.findById(1);

        if(optionalFileInfoList.isPresent()) {
            var result = optionalFileInfoList.get();
            Assertions.assertEquals(result.getType(), FileType.BANNER);

            var fileType = FileType.MENU;
            result.setType(fileType);
            FileInfo merge = entityManager.merge(result);

            Assertions.assertEquals(merge.getType(), FileType.MENU);
        } else {
            Assertions.assertNotNull(optionalFileInfoList.equals(FileType.MENU));
        }
    }

    @Test
    @Transactional
    @DisplayName("파일 정보 삽입 및 삭제")
    void insertAndDelete() {
        FileInfo fileInfo = setUp(FileType.BANNER);
        Optional<FileInfo> findNotice = this.fileInfoRepository.findById(fileInfo.getId());

        if(findNotice.isPresent()) {
            var result = findNotice.get();
            Assertions.assertEquals(result.getType(), FileType.BANNER);
            entityManager.remove(fileInfo);
            Optional<FileInfo> deleteFileInfo = this.fileInfoRepository.findById(fileInfo.getId());
            if (deleteFileInfo.isPresent()) {
                Assertions.assertNull(deleteFileInfo.get());
            }
        } else {
            Assertions.assertNotNull(findNotice.get());
        }
    }


    private FileInfo setUp(FileType fileType) {
        var fileInfo = new FileInfo();
        fileInfo.setType(fileType);
        return this.entityManager.persist(fileInfo);
    }
}
