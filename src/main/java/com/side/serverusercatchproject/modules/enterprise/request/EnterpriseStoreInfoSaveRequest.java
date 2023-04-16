package com.side.serverusercatchproject.modules.enterprise.request;

import com.side.serverusercatchproject.modules.enterprise.dto.EnterpriseDTO;
import com.side.serverusercatchproject.modules.enterprise.entity.Enterprise;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.Comment;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public record EnterpriseStoreInfoSaveRequest(

        @NotBlank(message = "기업 정보를 입력해주세요")
        EnterpriseDTO enterprise,

        @NotBlank(message = "매장 이름을 입력해주세요")
        String name,

        @NotBlank(message = "매장 주소를 입력해주세요")
        String address,

        @NotBlank(message = "예약 선금을 입력해주세요")
        Integer reservationPrice,

        @NotBlank(message = "예약 간격을 입력해주세요")
        String reservationTerm,

        @NotBlank(message = "예약 취소 가능 날짜를 입력해주세요")
        LocalDateTime reservationCancelDay,

        @NotBlank(message = "위도를 입력해주세요")
        Double lat,

        @NotBlank(message = "경도를 입력해주세요")
        Double lon,

        @NotBlank(message = "매장 사진을 입력해주세요")
        List<MultipartFile> files
) {
}
