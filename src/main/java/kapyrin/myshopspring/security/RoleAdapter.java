package kapyrin.myshopspring.security;

import kapyrin.myshopspring.entity.Role;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleAdapter {

    private final Role role;

    public String getAuthority() {
        return "ROLE_" + role.getUserRole().toUpperCase();
    }
}
