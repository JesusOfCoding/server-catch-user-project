package com.side.serverusercatchproject.jpa;

import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import com.side.serverusercatchproject.modules.file.repository.FileInfoRepository;
import com.side.serverusercatchproject.modules.file.service.FileService;
import com.side.serverusercatchproject.modules.notice.entity.Notice;
import com.side.serverusercatchproject.modules.notice.repository.NoticeRepository;
import com.side.serverusercatchproject.modules.notice.enums.NoticeStatus;
import org.junit.jupiter.api.*;
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
@DisplayName("NOTICE JPA 테스트")
public class NoticeRepositoryTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private TestEntityManager entityManager;


    @BeforeEach
    public void init() {
        setUp("공지사항", "내용", NoticeStatus.WAIT);
    }

    @Test
    @Transactional
    void selectAll() {
        var notices = noticeRepository.findAll();
        Assertions.assertNotEquals(notices.size(), 0);

        Notice notice = notices.get(0);
        Assertions.assertEquals(notice.getTitle(), "공지 1");
    }

    @Test
    @Transactional
    void selectAndUpdate() {
        var optionalNotices = this.noticeRepository.findById(1);

        if(optionalNotices.isPresent()) {
            var result = optionalNotices.get();
            Assertions.assertEquals(result.getTitle(),"공지 1");

            var content = "내용 222";
            result.setContent(content);
            Notice merge = entityManager.merge(result);

            Assertions.assertEquals(merge.getContent(),"내용 222");
        } else {
            Assertions.assertNotNull(optionalNotices.get());
        }
    }

    @Test
    @Transactional
    void insertAndDelete() {
        Notice notice = setUp("공지사항333", "내용333", NoticeStatus.WAIT);
        Optional<Notice> findNotice = this.noticeRepository.findById(notice.getId());

        if(findNotice.isPresent()) {
            var result = findNotice.get();
            Assertions.assertEquals(result.getTitle(), "공지사항333");
            entityManager.remove(notice);
            Optional<Notice> deleteNotice = this.noticeRepository.findById(notice.getId());
            if (deleteNotice.isPresent()) {
                Assertions.assertNull(deleteNotice.get());
            }
        } else {
            Assertions.assertNotNull(findNotice.get());
        }
    }

    private Notice setUp(String title, String content, NoticeStatus status) {
        var notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setStatus(status);
        return this.entityManager.persist(notice);
    }
}
