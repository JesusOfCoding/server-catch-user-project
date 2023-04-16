package com.side.serverusercatchproject.jpa.enterprise;

import com.side.serverusercatchproject.modules.enterprise.entity.Enterprise;
import com.side.serverusercatchproject.modules.enterprise.entity.EnterpriseStoreInfo;
import com.side.serverusercatchproject.modules.enterprise.enums.EnterpriseStatus;
import com.side.serverusercatchproject.modules.enterprise.enums.StoreStatus;
import com.side.serverusercatchproject.modules.enterprise.repository.EnterpriseRepository;
import com.side.serverusercatchproject.modules.enterprise.repository.EnterpriseStoreInfoRepository;
import com.side.serverusercatchproject.modules.file.entity.FileInfo;
import com.side.serverusercatchproject.modules.file.enums.FileType;
import com.side.serverusercatchproject.util.type.RoleType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@ExtendWith(SpringExtension.class)
public class EnterpriseStoreInfoRepositoryTest {

    @Autowired
    private EnterpriseStoreInfoRepository enterpriseStoreInfoRepository;

    @Autowired
    private TestEntityManager entityManager; // 데이터를 건드는 것은 무조건 이걸로 해야함

//    @BeforeEach
//    public void init() {
//        setUpByEnterpriseStoreInfo(
//                "매장이름1",
//                "매장 주소",
//                1000,
//                "1일",
//                LocalDateTime.of(2023,5,20,1,1,1),
//                123.123,
//                123.123,
//                StoreStatus.OPEN
//                );
//    }

    @Test
    @Transactional
    void selectAll(){
        var enterpriseStoreInfos = enterpriseStoreInfoRepository.findAll();
        Assertions.assertNotEquals(enterpriseStoreInfos.size(), 0);

        EnterpriseStoreInfo enterpriseStoreInfo = enterpriseStoreInfos.get(0);
        Assertions.assertEquals(enterpriseStoreInfo.getName(), "매장이름1");

    }

    @Test
    @Transactional
    void selectAndUpdate(){
        var optionalEnterpriseStoreInfos = this.enterpriseStoreInfoRepository.findById(1);

        if (optionalEnterpriseStoreInfos.isPresent()){
            var result = optionalEnterpriseStoreInfos.get();
            Assertions.assertEquals(result.getName(), "매장이름1");

            var address = "매장 주소2";
            result.setAddress(address);

            EnterpriseStoreInfo merge = entityManager.merge(result);

            Assertions.assertEquals(merge.getAddress(), "매장 주소2");
        } else {
            Assertions.assertNotNull(optionalEnterpriseStoreInfos.get());
        }
    }

    @Test
    @Transactional
    void insertAndDelete() {

        EnterpriseStoreInfo enterpriseStoreInfo = setUp(
                "매장 이름2",
                "매장 주소3",
                1000,
                "1일",
                LocalDateTime.of(2023,2,22,2,2,2),
                987.123,
                987.123,
                StoreStatus.OPEN
                );
        Optional<EnterpriseStoreInfo> findEnterpriseStoreInfo = this.enterpriseStoreInfoRepository.findById(enterpriseStoreInfo.getId());

        if (findEnterpriseStoreInfo.isPresent()) {
            var result = findEnterpriseStoreInfo.get();
            Assertions.assertEquals(result.getName(),"매장 이름2");
            entityManager.remove(enterpriseStoreInfo);
            Optional<EnterpriseStoreInfo> deleteEnterpriseStoreInfo = this.enterpriseStoreInfoRepository.findById(enterpriseStoreInfo.getId());
            if (deleteEnterpriseStoreInfo.isPresent()){
                Assertions.assertNull(deleteEnterpriseStoreInfo.get());
            }
        } else {
            Assertions.assertNotNull(findEnterpriseStoreInfo.get());
        }
    }

    private EnterpriseStoreInfo setUp(
            String name,
            String address,
            Integer reservationPrice,
            String reservationTerm,
            LocalDateTime reservationCancelDay,
            Double lat,
            Double lon,
            StoreStatus status
    ){
        FileInfo fileInfo = new FileInfo().builder().type(FileType.STORE).build();
        Enterprise enterprise = new Enterprise().builder().username("기업아이디1").password("1234").role(RoleType.ENTERPRISE).email("enterprise@test.com").tel("010-1111-1111").status(EnterpriseStatus.ACTIVE).build();
        var enterpriseStoreInfo = new EnterpriseStoreInfo();
        enterpriseStoreInfo.setEnterprise(enterprise);
        enterpriseStoreInfo.setName(name);
        enterpriseStoreInfo.setAddress(address);
        enterpriseStoreInfo.setReservationPrice(reservationPrice);
        enterpriseStoreInfo.setReservationTerm(reservationTerm);
        enterpriseStoreInfo.setReservationCancelDay(reservationCancelDay);
        enterpriseStoreInfo.setLat(lat);
        enterpriseStoreInfo.setLon(lon);
        enterpriseStoreInfo.setFileInfo(fileInfo);
        enterpriseStoreInfo.setStatus(status);
        return this.entityManager.persist(enterpriseStoreInfo);
    }

}