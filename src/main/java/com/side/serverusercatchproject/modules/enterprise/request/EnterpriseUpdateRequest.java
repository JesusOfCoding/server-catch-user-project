package com.side.serverusercatchproject.modules.enterprise.request;

import com.side.serverusercatchproject.common.ValueOfEnum;
import com.side.serverusercatchproject.modules.enterprise.enums.EnterpriseStatus;
import com.side.serverusercatchproject.util.type.RoleType;
import jakarta.validation.constraints.NotBlank;

public record EnterpriseUpdateRequest(
        @NotBlank(message = "아이디를 입력해주세요")
        String username,
        
        @NotBlank(message = "비밀번호를 입력해주세요")
        String password,

        @NotBlank(message = "이메일을 입력해주세요")
        String email,

        @NotBlank(message = "전화번호를 입력해주세요")
        String tel,
        
        @ValueOfEnum(enumClass = EnterpriseStatus.class, message = "기업 상태 값이 이상이 있습니다. 확인해주세요.")
        String status
) {
}
