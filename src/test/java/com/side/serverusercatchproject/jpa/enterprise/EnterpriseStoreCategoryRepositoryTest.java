package com.side.serverusercatchproject.jpa.enterprise;

import com.side.serverusercatchproject.modules.category.entity.Category;
import com.side.serverusercatchproject.modules.category.enums.CategoryStatus;
import com.side.serverusercatchproject.modules.enterprise.entity.Enterprise;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreCalendar;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreCategory;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.enterprise.enums.EnterpriseStatus;
import com.side.serverusercatchproject.modules.enterprise.enums.StoreCategoryStatus;
import com.side.serverusercatchproject.modules.enterprise.enums.StoreStatus;
import com.side.serverusercatchproject.modules.enterprise.repository.EnterpriseStoreCategoryRepository;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import com.side.serverusercatchproject.util.type.RoleType;
import graphql.Assert;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;


@DataJpaTest
@ExtendWith(SpringExtension.class)
public class EnterpriseStoreCategoryRepositoryTest {

    @Autowired
    private EnterpriseStoreCategoryRepository enterpriseStoreCategoryRepository;

    @Autowired
    private TestEntityManager entityManager; // 데이터를 건드는 것은 무조건 이걸로 해야함

    @Test
    @Transactional
    void selectAll() {
        var enterpriseStoreCategories = enterpriseStoreCategoryRepository.findAll();
        Assertions.assertNotEquals(enterpriseStoreCategories.size(), 0);

        EnterpriseStoreCategory enterpriseStoreCategory = enterpriseStoreCategories.get(0);
        Assertions.assertEquals(enterpriseStoreCategory.getCategory().getName(), "카테고리이름");
    }

    @Test
    @Transactional
    void selectAndUpdate() {
        var optionalEnterpriseStoreCategories = this.enterpriseStoreCategoryRepository.findById(1);

        if (optionalEnterpriseStoreCategories.isPresent()){
            var result = optionalEnterpriseStoreCategories.get();
            Assertions.assertEquals(result.getCategory().getName(), "카테고리이름");

            var status = StoreCategoryStatus.WAIT;
            result.setStatus(status);

            EnterpriseStoreCategory merge = entityManager.merge(result);

            Assertions.assertEquals(merge.getStatus(), StoreCategoryStatus.WAIT);
        } else {
            Assertions.assertNotNull(optionalEnterpriseStoreCategories.get());
        }
    }

    @Test
    @Transactional
    void insertAndDelete() {
        EnterpriseStoreCategory enterpriseStoreCategory = setUp(
                StoreCategoryStatus.ACTIVE
        );
        Optional<EnterpriseStoreCategory> findEnterpriseSotreCategory = this.enterpriseStoreCategoryRepository.findById(enterpriseStoreCategory.getId());

        if (findEnterpriseSotreCategory.isPresent()){
            var result = findEnterpriseSotreCategory.get();
            Assertions.assertEquals(result.getStatus(), StoreCategoryStatus.ACTIVE);
            entityManager.remove(enterpriseStoreCategory);
            Optional<EnterpriseStoreCategory> deleteEnterpriseStoreCategory = this.enterpriseStoreCategoryRepository.findById(enterpriseStoreCategory.getId());
            if (deleteEnterpriseStoreCategory.isPresent()){
                Assertions.assertNull(deleteEnterpriseStoreCategory.get());
            }
        } else {
            Assertions.assertNotNull(findEnterpriseSotreCategory.get());
        }
    }

    private EnterpriseStoreCategory setUp(
            StoreCategoryStatus status
    ){
        FileInfo fileInfo = FileInfo.builder().type(FileType.BANNER).build();
        Enterprise enterprise = Enterprise.builder().username("기업이름").password("기업비번").role(RoleType.ENTERPRISE).email("이메일").tel("전화번호").status(EnterpriseStatus.ACTIVE).build();
        EnterpriseStoreInfo store = EnterpriseStoreInfo.builder().enterprise(enterprise).name("매장정보").address("매장주소").reservationPrice(10000).reservationTerm("예약간격").reservationCancelDay(LocalDateTime.now()).lat(20.1).lon(20.145).fileInfo(fileInfo).status(StoreStatus.OPEN).build();

        Category category = Category.builder().name("카테고리이름").status(CategoryStatus.ACTIVE).build();

        var enterpriseStoreCategory = new EnterpriseStoreCategory();

        enterpriseStoreCategory.setStore(store);
        enterpriseStoreCategory.setCategory(category);
        enterpriseStoreCategory.setStatus(status);

        return this.entityManager.persist(enterpriseStoreCategory);
    }
}