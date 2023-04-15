package com.side.serverusercatchproject.jpa.banner;


import com.side.serverusercatchproject.modules.banner.entity.Banner;
import com.side.serverusercatchproject.modules.banner.entity.BannerSort;
import com.side.serverusercatchproject.modules.banner.enums.BannerStatus;
import com.side.serverusercatchproject.modules.banner.repository.BannerSortRepository;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
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

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@DisplayName("배너종류 JPA 테스트")
public class BannerSortRepositoryTest {

    @Autowired
    private BannerSortRepository bannerSortRepository; // 조회 용도로만 사용

    @Autowired
    private TestEntityManager entityManager; // 데이터를 건드는 것은 무조건 이걸로 해야함

    @BeforeEach
    public void init() {
        setUpByBannerSort("배너이름", "빨간색");
    }

    @Test
    @Transactional
    @DisplayName("배너종류 조회")
    void selectAll() {
        var bannerSorts = bannerSortRepository.findAll();
        Assertions.assertNotEquals(bannerSorts.size(), 0);

        BannerSort bannerSort = bannerSorts.get(0);
        Assertions.assertEquals(bannerSort.getName(), "서울에서");
    }

    @Test
    @Transactional
    @DisplayName("배너종류 조회 및 수정")
    void selectAndUpdate() {
        var optionalBannerSorts = this.bannerSortRepository.findById(1);

        if(optionalBannerSorts.isPresent()) {
            var result = optionalBannerSorts.get();
            Assertions.assertEquals(result.getName(),"서울에서");

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
    @DisplayName("배너종류 삽입 및 삭제")
    void insertAndDelete() {

        BannerSort bannerSort = setUpByBannerSort("배너이름2", "초록색");
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


    private BannerSort setUpByBannerSort(
            String name, String color) {
        FileInfo fileInfo = new FileInfo().builder().type(FileType.BANNER).build();
        Banner banner = new Banner().builder().fileInfo(fileInfo).startTime(LocalDateTime.now()).endTime(LocalDateTime.now()).status(BannerStatus.ACTIVE).build();
        var bannerSort = new BannerSort();
        bannerSort.setBanner(banner);
        bannerSort.setName(name);
        bannerSort.setColor(color);
        return this.entityManager.persist(bannerSort);
    }
}