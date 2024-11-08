package kapyrin.myshopspring.service.interfaces;

import kapyrin.myshopspring.entity.Role;

import java.util.Optional;

public interface RoleService extends CrudOneParameterInMethod <Role>{
    Optional<Role> getByRoleName(String name);
}
