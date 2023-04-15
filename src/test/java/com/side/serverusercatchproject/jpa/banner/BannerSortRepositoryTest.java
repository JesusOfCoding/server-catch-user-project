package com.side.serverusercatchproject.jpa.banner;


import com.side.serverusercatchproject.modules.banner.entity.Banner;
import com.side.serverusercatchproject.modules.banner.entity.BannerSort;
import com.side.serverusercatchproject.modules.banner.enums.BannerStatus;
import com.side.serverusercatchproject.modules.banner.repository.BannerSortRepository;
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
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class BannerSortRepositoryTest {

    @Autowired
    private BannerSortRepository bannerSortRepository; // 조회 용도로만 사용

    @Autowired
    private TestEntityManager entityManager; // 데이터를 건드는 것은 무조건 이걸로 해야함

    @BeforeEach
    public void init() {
        FileInfo fileInfo = setUpByFileInfo(FileType.Image);

        Banner banner = setUpByBanner(fileInfo, LocalDateTime.of(2023, 04, 9, 9, 00), LocalDateTime.of(2023, 05, 9, 21, 00), BannerStatus.WAIT);

        setUpByBannerSort(banner, "배너이름", "빨간색");
    }

    @Test
    @Transactional
    void selectAll() {
        var bannerSorts = bannerSortRepository.findAll();
        Assertions.assertNotEquals(bannerSorts.size(), 0);

        BannerSort bannerSort = bannerSorts.get(0);
        Assertions.assertEquals(bannerSort.getName(), "배너이름");
    }

    @Test
    @Transactional
    void selectAndUpdate() {
        var optionalBannerSorts = this.bannerSortRepository.findById(4);

        if(optionalBannerSorts.isPresent()) {
            var result = optionalBannerSorts.get();
            Assertions.assertEquals(result.getName(),"배너이름");

            var content = "파랑색";
            result.setColor(content);
            BannerSort merge = entityManager.merge(result);

            Assertions.assertEquals(merge.getColor(),"파랑색");
        } else {
            Assertions.assertNotNull(optionalBannerSorts.get());
        }
    }

    @Test
    @Transactional
    void insertAndDelete() {
        FileInfo fileInfo = setUpByFileInfo(FileType.Image);
        Banner banner = setUpByBanner(fileInfo, LocalDateTime.of(2023, 04, 9, 9, 00), LocalDateTime.of(2023, 05, 9, 21, 00), BannerStatus.WAIT);

        BannerSort bannerSort = setUpByBannerSort(banner, "배너이름2", "초록색");
        Optional<BannerSort> findBannerSort = this.bannerSortRepository.findById(bannerSort.getId());

        if(findBannerSort.isPresent()) {
            var result = findBannerSort.get();
            Assertions.assertEquals(result.getName(), "배너이름2");
            entityManager.remove(bannerSort);
            Optional<BannerSort> deleteNotice = this.bannerSortRepository.findById(bannerSort.getId());
            if (deleteNotice.isPresent()) {
                Assertions.assertNull(deleteNotice.get());
            }
        } else {
            Assertions.assertNotNull(findBannerSort.get());
        }
    }

    private FileInfo setUpByFileInfo(FileType fileType) {
        var fileInfo = new FileInfo();
        fileInfo.setType(fileType);
        return this.entityManager.persist(fileInfo);
    }

    private Banner setUpByBanner(
            FileInfo fileInfo, LocalDateTime startTime, LocalDateTime endTime, BannerStatus status) {
        var banner = new Banner();
        banner.setFileInfo(fileInfo);
        banner.setStartTime(startTime);
        banner.setEndTime(endTime);
        banner.setStatus(status);
        return this.entityManager.persist(banner);
    }

    private BannerSort setUpByBannerSort(
            Banner banner, String name, String color) {
        var bannerSort = new BannerSort();
        bannerSort.setBanner(banner);
        bannerSort.setName(name);
        bannerSort.setColor(color);
        return this.entityManager.persist(bannerSort);
    }
}