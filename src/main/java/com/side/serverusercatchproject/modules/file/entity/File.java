package com.side.serverusercatchproject.modules.file.entity;


import com.side.serverusercatchproject.common.jpa.BaseTime;
import com.side.serverusercatchproject.modules.file.dto.FileDTO;
import com.side.serverusercatchproject.modules.file.dto.FileInfoDTO;
import com.side.serverusercatchproject.modules.file.enums.FileStatus;
import com.side.serverusercatchproject.modules.file.response.FileResponse;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "FILES")
public class File extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("고유번호")
    private Integer id;

    @Comment("파일 출처")
    @ManyToOne
    @JoinColumn(name = "file_info_id")
    private FileInfo fileInfo;
    
    @Comment("파일 이름")
    private String fileName;

    @Comment("파일 경로")
    private String fileUrl;

    // Multi
    // File Insert
    // S3
    // File update url

    // 확장자 추가
//    jpg

    // 파일 크기
//    3MB

    @Enumerated(EnumType.STRING)
    @Comment("사진 활성화 상태")
    private FileStatus status;

    @Builder
    public File(Integer id, FileInfo fileInfo, String fileName, String fileUrl, FileStatus status) {
        this.id = id;
        this.fileInfo = fileInfo;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.status = status;
    }

    public FileDTO toDTO() {
        return new FileDTO(id, fileInfo.toDTO(), fileName, fileUrl, status.name());
    }

    public FileResponse toResponse() {
        return new FileResponse(id, fileInfo.toDTO(), fileName, fileUrl, status.name());
    }
}
