package com.side.serverusercatchproject.modules.enterprise.entity;

import com.side.serverusercatchproject.common.jpa.BaseTime;
import com.side.serverusercatchproject.modules.enterprise.enums.EnterpriseStatus;
import com.side.serverusercatchproject.util.type.RoleType;
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
@Table(name = "ENTERPRISE_LIST")
public class Enterprise extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("고유번호")
    private Integer id;

    @Comment("기업아이디")
    private String username;

    @Comment("기업비밀번호")
    private String password;

    @Enumerated(EnumType.STRING)
    @Comment("권한")
    private RoleType role;

    @Comment("기업이메일")
    private String email;

    @Comment("기업전화번호")
    private String tel;

    @Enumerated(EnumType.STRING)
    @Comment("기업 활성화 상태")
    private EnterpriseStatus status;

    @Builder
    public Enterprise(String username, String password, RoleType role, String email, String tel, EnterpriseStatus status) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.tel = tel;
        this.status = status;
    }
}