package com.side.serverusercatchproject.jpa.banner;


import com.side.serverusercatchproject.modules.banner.entity.Banner;
import com.side.serverusercatchproject.modules.banner.enums.BannerStatus;
import com.side.serverusercatchproject.modules.banner.repository.BannerRepository;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
public class BannerRepositoryTest {

    @Autowired
    private BannerRepository bannerRepository; // 조회 용도로만 사용

    @Autowired
    private TestEntityManager entityManager; // 데이터를 건드는 것은 무조건 이걸로 해야함

    @BeforeEach
    public void init() {
        setUpByBanner(LocalDateTime.of(2023,04,15,04,15),LocalDateTime.of(2023,05,9,21,00), BannerStatus.WAIT);
    }

    @Test
    void selectAll() {
        List<Banner> bannerList = bannerRepository.findAll();
        Assertions.assertNotEquals(bannerList.size(), 0);

        Banner banner = bannerList.get(0);
        Assertions.assertEquals(banner.getStartTime(), LocalDateTime.of(2021,1,1,0,1));
    }

    @Test
    @Transactional
    void selectAndUpdate() {
        Optional<Banner> findBanner = this.bannerRepository.findById(1);

        if(findBanner.isPresent()) {
            var result = findBanner.get();
            Assertions.assertEquals(result.getStartTime(),LocalDateTime.of(2021,1,1,0,1));

            var endTime = LocalDateTime.of(2022,01,01,0,00);
            result.setEndTime(endTime);
            Banner merge = entityManager.merge(result);

            Assertions.assertEquals(merge.getEndTime(),LocalDateTime.of(2022,01,01,0,00));
        } else {
            Assertions.assertNotNull(findBanner.get());
        }
    }

    @Test
    @Transactional
    void insertAndDelete() {
        FileInfo fileInfo = setUpByFileInfo(FileType.MENU);
        Banner banner = setUpByBanner(LocalDateTime.of(2023, 04, 9, 9, 00), LocalDateTime.of(2023, 05, 9, 21, 00), BannerStatus.WAIT);
        Optional<Banner> findBanner = this.bannerRepository.findById(banner.getId());

        if(findBanner.isPresent()) {
            var result = findBanner.get();
            Assertions.assertEquals(result.getFileInfo().getType(), FileType.BANNER);
            entityManager.remove(banner);
            Optional<Banner> deleteBanner = this.bannerRepository.findById(banner.getId());
            if (deleteBanner.isPresent()) {
                Assertions.assertNull(deleteBanner.get());
            }
        } else {
            Assertions.assertNotNull(findBanner.get());
        }
    }

    private FileInfo setUpByFileInfo(FileType fileType) {
        var fileInfo = new FileInfo();
        fileInfo.setType(fileType);
        return this.entityManager.persist(fileInfo);
    }

    private Banner setUpByBanner(LocalDateTime startTime, LocalDateTime endTime, BannerStatus status) {
        FileInfo fileInfo = FileInfo.builder().type(FileType.BANNER).build();
        var banner = new Banner();
        banner.setFileInfo(fileInfo);
        banner.setStartTime(startTime);
        banner.setEndTime(endTime);
        banner.setStatus(status);
        return this.entityManager.persist(banner);
    }
}
