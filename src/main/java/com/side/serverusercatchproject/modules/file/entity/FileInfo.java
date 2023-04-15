package com.side.serverusercatchproject.modules.file.entity;

import com.side.serverusercatchproject.common.jpa.BaseTime;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FILE_INFO_LIST")
public class FileInfo extends BaseTime {
    @Id
    @Comment("고유번호")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Comment("파일 출처")
    private FileType type;

    @Builder
    public FileInfo(FileType type) {
        this.type = type;
    }
}
