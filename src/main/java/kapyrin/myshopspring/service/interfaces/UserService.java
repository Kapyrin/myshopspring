package kapyrin.myshopspring.service.interfaces;

import kapyrin.myshopspring.entity.User;

import java.util.Optional;

public interface UserService extends CrudOneParameterInMethod<User> {
    Optional<User> authenticate(String email, String password);
}
