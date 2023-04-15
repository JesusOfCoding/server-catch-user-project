package com.side.serverusercatchproject.jpa.banner;


import com.side.serverusercatchproject.modules.banner.entity.Banner;
import com.side.serverusercatchproject.modules.banner.entity.BannerEnterprise;
import com.side.serverusercatchproject.modules.banner.entity.BannerSort;
import com.side.serverusercatchproject.modules.banner.enums.BannerStatus;
import com.side.serverusercatchproject.modules.banner.repository.BannerEnterpriseRepository;
import com.side.serverusercatchproject.modules.enterprise.entity.Enterprise;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.enterprise.enums.EnterpriseStatus;
import com.side.serverusercatchproject.modules.enterprise.enums.StoreStatus;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import com.side.serverusercatchproject.util.type.RoleType;
import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class BannerEnterpriseRepositoryTest {

    @Autowired
    private BannerEnterpriseRepository bannerEnterpriseRepository; // 조회 용도로만 사용

    @Autowired
    private TestEntityManager entityManager; // 데이터를 건드는 것은 무조건 이걸로 해야함

    @Autowired
    private EntityManager em;
    @BeforeEach
    public void init() {
        setUp();
    }

    @Test
    @Transactional
    void selectAll() {
//        em.createNativeQuery("ALTER TABLE BANNER_ENTERPRISE_LIST ALTER COLUMN ID RESTART WITH 1").executeUpdate();

        List<BannerEnterprise> bannerEnterpriseList = bannerEnterpriseRepository.findAll();
        Assertions.assertNotEquals(bannerEnterpriseList.size(), 0);

        BannerEnterprise bannerEnterprise = bannerEnterpriseList.get(0);
        Assertions.assertEquals(bannerEnterprise.getBannerSort().getColor(), "색상");
    }

    @Test
    @Transactional
    void selectAndUpdate() {
        Optional<BannerEnterprise> findBannerEnterprise = this.bannerEnterpriseRepository.findById(1);
        List<BannerEnterprise> all = bannerEnterpriseRepository.findAll();

        if(findBannerEnterprise.isPresent()) {
            var result = findBannerEnterprise.get();
            Assertions.assertEquals(result.getBannerSort().getColor(), "색상");

            var tel = "010-1234-4567";
            result.getStore().getEnterprise().setTel(tel);
            BannerEnterprise merge = entityManager.merge(result);

            Assertions.assertEquals(merge.getStore().getEnterprise().getTel(), "010-1234-4567");
        } else {
            Assertions.assertNotNull(findBannerEnterprise.get());
        }
    }

    @Test
    @Transactional
    void insertAndDelete() {
        BannerEnterprise bannerEnterprise = setUp();
        Optional<BannerEnterprise> findBannerEnterprise = this.bannerEnterpriseRepository.findById(bannerEnterprise.getId());

        if (findBannerEnterprise.isPresent()) {
            BannerEnterprise result = findBannerEnterprise.get();
            Assertions.assertEquals(findBannerEnterprise.get().getStore().getName(), "매장정보");

            entityManager.remove(result);
            Optional<BannerEnterprise> deleteBannerEnterprise = this.bannerEnterpriseRepository.findById(bannerEnterprise.getId());
            if (deleteBannerEnterprise.isPresent()) {
                Assertions.assertNull(deleteBannerEnterprise.get());
            }
            Assertions.assertNotNull(findBannerEnterprise.get());
        }
    }
    private BannerEnterprise setUp() {

        FileInfo fileInfo = FileInfo.builder().type(FileType.BANNER).build();

        Banner banner = Banner.builder().fileInfo(fileInfo).startTime(LocalDateTime.now()).endTime(LocalDateTime.now()).status(BannerStatus.ACTIVE).build();

        BannerSort bannerSort = BannerSort.builder().banner(banner).name("배너이름").color("색상").build();

        Enterprise enterprise = Enterprise.builder().username("기업이름").password("기업비번").role(RoleType.ACTIVE).email("이메일").tel("전화번호").status(EnterpriseStatus.ACTIVE).build();

        EnterpriseStoreInfo store = EnterpriseStoreInfo.builder().enterprise(enterprise).name("매장정보").address("매장주소").reservationPrice(10000).reservationTerm("예약간격").reservationCancelDay(LocalDateTime.now()).lat(20.1).lon(20.145).fileInfo(fileInfo).status(StoreStatus.OPEN).build();

        var bannerEnterprise = BannerEnterprise.builder().bannerSort(bannerSort).store(store).build();
        return this.entityManager.persist(bannerEnterprise);
    }
}