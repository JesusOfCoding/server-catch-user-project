package com.side.serverusercatchproject.modules.magazine.entity;

import com.side.serverusercatchproject.common.jpa.BaseTime;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.magazine.dto.MagazineDTO;
import com.side.serverusercatchproject.modules.magazine.enums.MagazineStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "MAGAZINE_LIST")
public class Magazine extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("고유번호")
    private Integer id;

    @Comment("제목")
    private String title;

    @Comment("내용")
    private String content;

    @Comment("매거진 사진")
    @OneToOne
    @JoinColumn(name = "file_info_id")
    private FileInfo fileInfo;

    @Comment("매장 정보")
    @ManyToOne
    @JoinColumn(name = "store_id")
    private EnterpriseStoreInfo store;

    @Enumerated(EnumType.STRING)
    @Comment("매거진 활성화 상태")
    private MagazineStatus status;


//    public MagazineDTO toDto() {
//          return new MagazineDTO(title, content, fileInfo, store, status);
//        return ;
//    }
}
