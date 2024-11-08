package kapyrin.myshopspring.service.impl;

import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.exception.entity.UserException;
import kapyrin.myshopspring.repository.UserRepository;
import kapyrin.myshopspring.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserImplService implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserImplService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> authenticate(String email, String password) {
        log.debug("Authenticating {}", email);
        try {
            Optional<User> authenticatedUser = userRepository.findByEmailAndPassword(email, password);
            log.info("User {} authenticated", email);
            return authenticatedUser;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException("Error authenticating user", e);
        }
    }

    @Override
    public void deleteById(long id) {
        log.debug("Deleting {}", id);
        try {
            userRepository.deleteById(id);
            log.info("User {} deleted", id);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException("Error deleting user", e);
        }

    }

    @Override
    public Optional<User> getById(long id) {
        log.info("Getting {}", id);
        try {
            Optional<User> user = userRepository.findById(id);
            log.info("User {} found", id);
            return user;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException("Error getting user", e);
        }
    }

    @Override
    public void add(User entity) {
        log.debug("Adding {}", entity);
        try {
            userRepository.save(entity);
            log.info("User {} added", entity.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException("Error adding user", e);
        }

    }

    @Override
    public void update(User entity) {
        log.debug("Updating {}", entity);
        try {
            userRepository.save(entity);
            log.info("User {} updated", entity.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Override
    public void deleteByEntity(User entity) {
        log.debug("Deleting {}", entity);
        try {
            userRepository.delete(entity);
            log.info("User {} deleted", entity.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException("Error deleting user", e);
        }

    }

    @Override
    public List<User> getAll() {
        log.debug("Getting all users");
        try {
            List<User> users = userRepository.findAll();
            log.info("Users {}", users);
            return users;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new UserException("Error getting all users", e);
        }
    }
}
