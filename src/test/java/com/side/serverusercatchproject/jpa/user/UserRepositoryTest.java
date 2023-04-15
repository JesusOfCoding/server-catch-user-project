package com.side.serverusercatchproject.jpa.user;

import com.side.serverusercatchproject.modules.user.entity.User;
import com.side.serverusercatchproject.modules.user.enums.UserStatus;
import com.side.serverusercatchproject.modules.user.repository.UserRepository;
import com.side.serverusercatchproject.util.type.RoleType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void init() {
        setUp("ssar", "1234", RoleType.USER, "ssar@nate.com", "01012341234", UserStatus.ACTIVE);
    }

    @Test
    public void selectAll() {
        var users = userRepository.findAll();
        Assertions.assertNotEquals(users.size(), 0);

        User user = users.get(0);
        Assertions.assertEquals(user.getUsername(), "권경렬");
    }

    @Test
    @Transactional
    public void selectAndUpdate() {
        var optionalUser = userRepository.findById(1);
        if (optionalUser.isPresent()) {

            var result = optionalUser.get();
            Assertions.assertEquals(result.getUsername(), "권경렬");

            var username = "cos";
            result.setUsername(username);
            User merge = entityManager.merge(result);
            Assertions.assertEquals(merge.getUsername(), "cos");
        } else {
            Assertions.assertNotNull(optionalUser.get());
        }
    }

    @Test
    @Transactional
    public void insertAndDelete() {
        User user = setUp("cos", "1234", RoleType.USER, "cos@nate.com", "010-1234-1234", UserStatus.ACTIVE);
        Optional<User> findUser = userRepository.findById(user.getId());

        if (findUser.isPresent()) {
            var result = findUser.get();
            Assertions.assertEquals(result.getUsername(), "cos");
            entityManager.remove(result);
            Optional<User> deleteUser = userRepository.findById(user.getId());
            if (deleteUser.isPresent()) {
                Assertions.assertNull(deleteUser.get());
            }
        } else {
            Assertions.assertNotNull(findUser.get());
        }
    }

    private User setUp(String username, String password, RoleType role, String email, String tel, UserStatus status) {
        User user = User.builder()
                .username(username)
                .password(password)
                .role(role)
                .email(email)
                .tel(tel)
                .status(status)
                .build();
        return entityManager.persist(user);
    }
}
