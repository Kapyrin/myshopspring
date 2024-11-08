package kapyrin.myshopspring.repository;

import kapyrin.myshopspring.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByUserRole(String userRole);
}
