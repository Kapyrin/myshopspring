package kapyrin.myshopspring.service.impl;

import kapyrin.myshopspring.AbstractTest;
import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.service.interfaces.RoleService;
import kapyrin.myshopspring.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)

class UserServiceTest extends AbstractTest {

    @Autowired
    protected UserService userService;
    @Autowired
    protected RoleService roleService;
    @Autowired
    ServiceTestUtil serviceTestUtil;
    protected User testUser;


    @Test
    void authenticate() {
        Optional<User> testUser = userService.authenticate("bla@bla.com", "password");
        assertTrue(testUser.isPresent());
        assertTrue("Tver".equals(testUser.get().getAddress()));
        assertTrue("+7999324023".equals(testUser.get().getPhoneNumber()));
        assertTrue("Customer".equals(testUser.get().getFirstName()));

        Optional<User> testWrongUser = userService.authenticate("wrong@user.com", "password");
        assertFalse(testWrongUser.isPresent());
    }

    @Test
    void deleteById() {
        userService.deleteById(testUser.getId());

        Optional<User> deletedUser = userService.getById(testUser.getId());
        assertFalse(deletedUser.isPresent());

    }

    @Test
    void getById() {
        Optional<User> testFoundUser = userService.getById(testUser.getId());
        assertTrue(testFoundUser.isPresent());
        assertEquals("User", testFoundUser.get().getLastName());

        Optional<User> testWrongFoundUser = userService.getById(1000);
        assertFalse(testWrongFoundUser.isPresent());
    }

    @Test
    void add() {
        User addedUser = User.builder()
                .firstName("Donald")
                .lastName("Trump")
                .email("donald@trump.com")
                .password("password")
                .phoneNumber("1234")
                .address("White House")
                .role(roleService.getByRoleName("admin").get())
                .build();

        userService.add(addedUser);
        Optional<User> testFoundUser = userService.authenticate("donald@trump.com", "password");
        assertTrue(testFoundUser.isPresent());
        assertEquals("White House", testFoundUser.get().getAddress());
    }

    @Test
    void update() {
        String newPhoneNumber = "+19099096655";
        testUser.setPhoneNumber(newPhoneNumber);
        userService.update(testUser);
        Optional<User> testFoundUser = userService.authenticate(testUser.getEmail(), testUser.getPassword());
        assertEquals(newPhoneNumber, testFoundUser.get().getPhoneNumber());
    }

    @Test
    void deleteByEntity() {
        userService.deleteByEntity(testUser);
        Optional<User> testFoundUser = userService.authenticate(testUser.getEmail(), testUser.getPassword());
        assertFalse(testFoundUser.isPresent());
    }

    @Test
    void getAll() {
        List<User> allUsers = userService.getAll();
        assertTrue(allUsers.size() > 2);
    }

    @Override
    protected void createTestEntity() {
        testUser = serviceTestUtil.customer();
    }

    @Override
    protected void saveTestEntity() {
        userService.add(testUser);

    }
}