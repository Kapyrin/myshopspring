package kapyrin.myshopspring.service.impl;

import kapyrin.myshopspring.entity.Role;
import kapyrin.myshopspring.exception.entity.RoleException;
import kapyrin.myshopspring.repository.RoleRepository;
import kapyrin.myshopspring.service.interfaces.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RoleImplService implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleImplService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> getByRoleName(String name) {
        log.debug("Getting role by name: {}", name);
        try {
            Optional<Role> roleByName = roleRepository.findByUserRole(name);
            log.info("Get role by name: {}", roleByName);
            return roleByName;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RoleException("Failed to find role by name: " + name);
        }
    }

    @Override
    public void deleteById(long id) {
        log.debug("Deleting role with id {}", id);
        try {
            roleRepository.deleteById(id);
            log.info("Role with id {} has been deleted", id);
        } catch (Exception e) {
            log.error("Error while deleting role with id {}", id, e);
            throw new RoleException("Error while deleting role with id " + id, e);
        }

    }

    @Override
    public Optional<Role> getById(long id) {
        log.debug("Getting role with id {}", id);
        try {
            Optional<Role> role = roleRepository.findById(id);
            log.info("Role with id {} has been found", id);
            return role;
        } catch (Exception e) {
            log.error("Error while getting role with id {}", id, e);
            throw new RoleException("Error while getting role with id " + id, e);
        }
    }

    @Override
    public void add(Role entity) {
        log.debug("Adding role {}", entity);
        try {
            roleRepository.save(entity);
            log.info("Role with id {} has been added", entity.getId());
        } catch (Exception e) {
            log.error("Error while adding role {}", entity.getId(), e);
            throw new RoleException("Error while adding role " + entity.getId(), e);
        }

    }

    @Override
    public void update(Role role) {
        log.debug("Updating role {}", role);

        try {
            roleRepository.save(role);
            log.info("Role with id {} has been updated", role.getId());
        } catch (Exception e) {
            log.error("Error while updating role {}", role.getId(), e);
            throw new RoleException("Error while updating role " + role.getId(), e);
        }

    }

    @Override
    public void deleteByEntity(Role entity) {
        log.debug("Deleting role {}", entity);
        try {
            roleRepository.delete(entity);
            log.info("Role with id {} has been deleted", entity.getId());
        } catch (Exception e) {
            log.error("Error while deleting role {}", entity.getId(), e);
            throw new RoleException("Error while deleting role " + entity.getId(), e);
        }

    }

    @Override
    public List<Role> getAll() {
        log.debug("Getting all roles");
        try {
            List<Role> roles = roleRepository.findAll();
            log.info("Roles found");
            return roles;
        } catch (Exception e) {
            log.error("Error while getting all roles", e);
            throw new RoleException("Error while getting all roles", e);
        }
    }
}
