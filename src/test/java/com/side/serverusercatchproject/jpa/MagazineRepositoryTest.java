package com.side.serverusercatchproject.jpa;

import com.side.serverusercatchproject.modules.enterprise.entity.Enterprise;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.enterprise.enums.EnterpriseStatus;
import com.side.serverusercatchproject.modules.enterprise.enums.StoreStatus;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import com.side.serverusercatchproject.modules.magazine.entity.Magazine;
import com.side.serverusercatchproject.modules.magazine.enums.MagazineStatus;
import com.side.serverusercatchproject.modules.magazine.repository.MagazineRepository;
import com.side.serverusercatchproject.util.type.RoleType;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@DisplayName("메거진 JPA 테스트")
public class MagazineRepositoryTest {

    @Autowired
    private MagazineRepository magazineRepository;

    @Autowired
    private TestEntityManager entityManager;

//    @BeforeEach
//    public void init() {
//        setUp("공지사항", "내용", MagazineStatus.WAIT);
//    }

    @Test
    @DisplayName("메거진 조회")
    void selectAll() {
        List<Magazine> magazines = magazineRepository.findAll();
        Assertions.assertNotEquals(magazines.size(), 0);

        Magazine magazine = magazines.get(0);
        Assertions.assertEquals(magazine.getTitle(), "FUEGO 푸에고");
    }

    @Test
    @Transactional
    @DisplayName("메거진 조회 및 수정")
    void selectAndUpdate() {
        var optionalMagazines = this.magazineRepository.findById(1);

        if(optionalMagazines.isPresent()) {
            var result = optionalMagazines.get();
            Assertions.assertEquals(result.getTitle(),"FUEGO 푸에고");

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
    @DisplayName("메거진 삽입 및 삭제")
    void insertAndDelete() {
        Magazine magazine = setUp("공지사항2", "내용2",  MagazineStatus.WAIT);
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

    private Magazine setUp(String title, String content, MagazineStatus status) {

        FileInfo fileInfo = FileInfo.builder().type(FileType.MAGAZINE).build();

        Enterprise enterprise = Enterprise.builder().username("기업이름").password("기업비번").role(RoleType.USER).email("이메일").tel("전화번호").status(EnterpriseStatus.ACTIVE).build();

        EnterpriseStoreInfo store = EnterpriseStoreInfo.builder().enterprise(enterprise).name("매장정보").address("매장주소").reservationPrice(10000)
                .reservationTerm("예약간격").reservationCancelDay(LocalDateTime.now()).lat(20.1).lon(20.145).fileInfo(fileInfo).status(StoreStatus.OPEN).build();

        Magazine magazine = new Magazine();
        magazine.setTitle(title);
        magazine.setContent(content);
        magazine.setFileInfo(fileInfo);
        magazine.setStore(store);
        magazine.setStatus(status);
        return this.entityManager.persist(magazine);
    }

}
