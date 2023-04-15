package com.side.serverusercatchproject.modules.review.response;

import com.side.serverusercatchproject.modules.enterprise.dto.EnterpriseStoreInfoDTO;
import com.side.serverusercatchproject.modules.file.dto.FileInfoDTO;
import com.side.serverusercatchproject.modules.user.dto.UserDTO;

public record ReviewResponse(
        Integer id,

        UserDTO user,

        EnterpriseStoreInfoDTO store,

        String content,

        Double tasteRating,

        Double moodRating,

        Double serviceRating,

        FileInfoDTO fileInfo,

        String status
) {

}
