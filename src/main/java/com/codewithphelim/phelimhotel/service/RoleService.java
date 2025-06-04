package com.codewithphelim.phelimhotel.service;

import com.codewithphelim.phelimhotel.model.Role;
import com.codewithphelim.phelimhotel.model.User;

import java.util.List;

public interface RoleService {
    List<Role> getRoles();
    Role createRole(Role role);
    void deleteRole(Long id);
    Role findByName(String name);
    User removeUserFromRole(Long userId, Long roleId);
    User assignRoleToUser(Long userId, Long roleId);
    Role removeAllUsersFromRole(Long roleId);
}
