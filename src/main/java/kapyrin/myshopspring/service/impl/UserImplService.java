package kapyrin.myshopspring.service.impl;

import kapyrin.myshopspring.entity.User;
import kapyrin.myshopspring.exception.entity.UserException;
import kapyrin.myshopspring.repository.UserRepository;
import kapyrin.myshopspring.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserImplService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> authenticate(String email, String rawPassword) {
        log.debug("Authenticating {}", email);
        try {
            Optional<User> authenticatedUser = userRepository.findByEmail(email);
            if (authenticatedUser.isEmpty()) {
                log.warn("User not found: {}", email);
                return Optional.empty();
            }
            User user = authenticatedUser.get();

            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                log.info("User {} authenticated", email);
                return Optional.of(user);
            } else {
                log.warn("Invalid password for user {}", email);
                return Optional.empty();
            }
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
            if (!entity.getPassword().startsWith("$2")) {
                entity.setPassword(passwordEncoder.encode(entity.getPassword()));
            }
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
            if (!entity.getPassword().startsWith("$2")) {
                log.info("Encrypting password for user {}", entity.getEmail());
                entity.setPassword(passwordEncoder.encode(entity.getPassword()));
            } else {
                log.info("Password for user {} is already encrypted", entity.getEmail());
            }
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

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("Finding user by email: {}", email);
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                log.info("User found with email: {}", email);
            } else {
                log.warn("No user found with email: {}", email);
            }
            return user;
        } catch (Exception e) {
            log.error("Error finding user by email: {}", e.getMessage());
            throw new UserException("Error finding user by email", e);
        }
    }

}
