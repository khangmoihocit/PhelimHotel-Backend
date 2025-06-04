package com.codewithphelim.phelimhotel.service.Impl;

import com.codewithphelim.phelimhotel.exception.OurException;
import com.codewithphelim.phelimhotel.model.Role;
import com.codewithphelim.phelimhotel.model.User;
import com.codewithphelim.phelimhotel.repository.RoleRepository;
import com.codewithphelim.phelimhotel.repository.UserRepository;
import com.codewithphelim.phelimhotel.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Role> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles;
    }

    @Override
    public Role createRole(Role role) {
        String roleName = "ROLE_" + role.getName().toUpperCase();
        Role theRole = new Role(roleName);
        if(roleRepository.existsByName(theRole.getName())){
            throw new OurException("role này đã tồn tại.");
        }
        return roleRepository.save(theRole);
    }

    @Override
    public void deleteRole(Long roleId) {
        this.removeAllUsersFromRole(roleId);
        roleRepository.deleteById(roleId);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).get();
    }

    @Override
    public User removeUserFromRole(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);
        if(role.isPresent() && role.get().getUsers().contains(user.get())){
            role.get().removeUserFromRole(user.get());
            roleRepository.save(role.get());
            return user.get();
        }
        throw new OurException("User không tồn tại");
    }

    @Override
    public User assignRoleToUser(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);
        if(user.isPresent() && user.get().getRoles().contains(role.get())){
            throw new OurException("user " + user.get().getFirstName() + " đã có role " + role.get().getName());
        }
        if(role.isPresent()){
            role.get().assignRoleToUser(user.get());
            roleRepository.save(role.get());
        }
        return user.get();
    }

    @Override
    public Role removeAllUsersFromRole(Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        role.ifPresent(Role :: removeAllUsersFromRole);
        return roleRepository.save(role.get());
    }
}
