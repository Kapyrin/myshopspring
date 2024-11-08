package kapyrin.myshopspring.repository;

import kapyrin.myshopspring.entity.User;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User>  findByEmailAndPassword(String email, String password);
}
