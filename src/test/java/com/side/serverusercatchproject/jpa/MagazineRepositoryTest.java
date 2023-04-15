package com.side.serverusercatchproject.jpa;

import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import com.side.serverusercatchproject.modules.magazine.entity.Magazine;
import com.side.serverusercatchproject.modules.magazine.enums.MagazineStatus;
import com.side.serverusercatchproject.modules.magazine.repository.MagazineRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class MagazineRepositoryTest {

    @Autowired
    private MagazineRepository magazineRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void init() {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setType(FileType.BANNER);
        fileInfo.setId(3);
        setUp("공지사항", "내용", fileInfo, null, MagazineStatus.WAIT);
    }

    @Test
    void selectAll() {
        var magazines = magazineRepository.findAll();
        Assertions.assertNotEquals(magazines.size(), 0);

        Magazine magazine = magazines.get(0);
        Assertions.assertEquals(magazine.getTitle(), "공지사항");
    }

    @Test
    @Transactional
    void selectAndUpdate() {
        var optionalMagazines = this.magazineRepository.findById(1);

        if(optionalMagazines.isPresent()) {
            var result = optionalMagazines.get();
            Assertions.assertEquals(result.getTitle(),"공지사항");

            var content = "내용 222";
            result.setContent(content);
            Magazine merge = entityManager.merge(result);

            Assertions.assertEquals(merge.getContent(),"내용 222");
        } else {
            Assertions.assertNotNull(optionalMagazines.get());
        }
    }

    @Test
    @Transactional
    void insertAndDelete() {
        Magazine magazine = setUp("공지사항2", "내용2", null, null, MagazineStatus.WAIT);
        Optional<Magazine> findMagazine = this.magazineRepository.findById(magazine.getId());

        if(findMagazine.isPresent()) {
            var result = findMagazine.get();
            Assertions.assertEquals(result.getTitle(), "공지사항2");
            entityManager.remove(magazine);
            Optional<Magazine> deleteNotice = this.magazineRepository.findById(magazine.getId());
            if (deleteNotice.isPresent()) {
                Assertions.assertNull(deleteNotice.get());
            }
        } else {
            Assertions.assertNotNull(findMagazine.get());
        }
    }

    private Magazine setUp(String title, String content, FileInfo fileInfo, EnterpriseStoreInfo store, MagazineStatus status) {
        Magazine magazine = new Magazine();
        magazine.setTitle(title);
        magazine.setContent(content);
        magazine.setFileInfo(fileInfo);
        magazine.setStore(store);
        magazine.setStatus(status);
        return this.entityManager.persist(magazine);
    }

}
