package kapyrin.myshopspring.service.impl;

import kapyrin.myshopspring.AbstractTest;
import kapyrin.myshopspring.entity.Role;
import kapyrin.myshopspring.service.interfaces.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RoleImplServiceTest extends AbstractTest {
    @Autowired
    RoleService roleService;
    private Role testRole;


    @Test
    void getByRoleName() {
        Optional<Role> optionalRole = roleService.getByRoleName("testRole");
        assertTrue(optionalRole.isPresent());

        Optional<Role> notExistedRole = roleService.getByRoleName("testRole2");
        assertFalse(notExistedRole.isPresent());
    }

    @Test
    void deleteById() {
        Optional<Role> optionalRole = roleService.getByRoleName("testRole");
        assertTrue(optionalRole.isPresent());

        roleService.deleteById(optionalRole.get().getId());
        Optional<Role> notExistedRole = roleService.getByRoleName("testRole");
        assertFalse(notExistedRole.isPresent());
    }

    @Test
    void getById() {
        Optional<Role> roleById = roleService.getById(1);
        assertTrue(roleById.isPresent());
        Optional<Role> notExistedRole = roleService.getById(100);
        assertFalse(notExistedRole.isPresent());
    }

    @Test
    void add() {
        Role addedRole = Role.builder()
                .userRole("addedRole")
                .build();

        roleService.add(addedRole);
        Optional<Role> optionalRole = roleService.getByRoleName("addedRole");
        assertTrue(optionalRole.isPresent());
    }

    @Test
    void update() {
        Optional<Role> optionalRole = roleService.getByRoleName("testRole");
        Role updatedRole = optionalRole.orElse(testRole);
        updatedRole.setUserRole("updatedRole");
        roleService.update(updatedRole);

        Optional<Role> updatedTestRole = roleService.getByRoleName("updatedRole");
        assertTrue(updatedTestRole.isPresent());
        assertFalse(updatedTestRole.get().getUserRole().equals("testRole"));

    }

    @Test
    void deleteByEntity() {
        Optional<Role> optionalRole = roleService.getByRoleName("testRole");
        assertTrue(optionalRole.isPresent());

        roleService.deleteByEntity(optionalRole.get());
        Optional<Role> notExistedRole = roleService.getByRoleName("testRole");
        assertFalse(notExistedRole.isPresent());
    }

    @Test
    void getAll() {
        List<Role> allRoles = roleService.getAll();
        assertTrue(allRoles.size() > 0);
    }

    @Override
    protected void createTestEntity() {
        testRole = Role.builder()
                .userRole("testRole")
                .build();
    }

    @Override
    protected void saveTestEntity() {
        roleService.add(testRole);
    }
}