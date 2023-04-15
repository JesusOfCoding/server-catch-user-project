package com.side.serverusercatchproject.jpa.enterprise;

import com.side.serverusercatchproject.modules.enterprise.entity.Enterprise;
import com.side.serverusercatchproject.modules.enterprise.repository.EnterpriseRepository;
import com.side.serverusercatchproject.util.type.RoleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@DataJpaTest
@ExtendWith(SpringExtension.class) // TODO 뭔지 지금 알아봐야함
public class EnterpriseRepositoryTest {

    // findByUsername data.sql add

    @Autowired
    private EnterpriseRepository enterpriseRepository; // 조회 용도로만 사용

    @Autowired
    private TestEntityManager entityManager; // 데이터를 건드는 것은 무조건 이걸로 해야함

    @BeforeEach
    public void init() {
        setUp("aaa","1234","aaa@nate.com", RoleType.ACTIVE, "010-1234-5678");
    }


    @Test
    @Transactional
    void selectAll() {
        var enterprises = enterpriseRepository.findAll();
        Assertions.assertNotEquals(enterprises.size(), 0);

        Enterprise enterprise = enterprises.get(0);
        Assertions.assertEquals(enterprise.getUsername(), "ssar");
    }


    @Test
    @Transactional
    void selectAndUpdate() {
        var optionalEnterprise = findEnterprise("ssar");

        if (optionalEnterprise.isPresent()) {
            var result = optionalEnterprise.get();
            Assertions.assertEquals(result.getUsername(), "ssar");

            var tel = "010-0980-0089";
            result.setTel(tel);

            var mergeEntity = entityManager.merge(result);
            Assertions.assertEquals(mergeEntity.getTel(), tel);
        } else {
            Assertions.assertNotNull(optionalEnterprise.get());
        }
    }


    @Test
    @Transactional
    void insertAndDelete() {
        var persist = setUp("ho","1235","aaasdfa@nate.com", RoleType.ACTIVE, "010-2342-5678");
        var findEnterprise = this.enterpriseRepository.findByUsername(persist.getUsername());

        if (findEnterprise.isPresent()) {
            var result = findEnterprise.get();
            Assertions.assertEquals(result.getUsername(), "ho");
            entityManager.remove(persist);
            var deleteUser = this.enterpriseRepository.findByUsername("ho");
            if (deleteUser.isPresent()) {
                Assertions.assertNull(deleteUser.get());
            }
        } else {
            Assertions.assertNotNull(findEnterprise.get());
        }
    }

    private Optional<Enterprise> findEnterprise(String name) {
        return enterpriseRepository.findByUsername(name);
    }


    private Enterprise setUp(String username, String password, String email, RoleType roleType, String tel) {
        var enterprise = new Enterprise();
        enterprise.setUsername(username);
        enterprise.setPassword(password);
        enterprise.setEmail(email);
        enterprise.setRole(roleType);
        enterprise.setTel(tel);
        return this.entityManager.persist(enterprise);
    }

}