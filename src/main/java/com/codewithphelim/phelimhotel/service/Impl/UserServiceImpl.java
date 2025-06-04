package com.codewithphelim.phelimhotel.service.Impl;

import com.codewithphelim.phelimhotel.exception.OurException;
import com.codewithphelim.phelimhotel.model.Role;
import com.codewithphelim.phelimhotel.model.User;
import com.codewithphelim.phelimhotel.repository.RoleRepository;
import com.codewithphelim.phelimhotel.repository.UserRepository;
import com.codewithphelim.phelimhotel.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            throw new OurException(user.getEmail() + "đã tồn tại");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singletonList(userRole)); //mặc định khi đăng ký là role_user
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User theUser = getUser(email);
        if(theUser != null){
            userRepository.deleteByEmail(email);
        }
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(()-> new OurException("Tài khoản không tồn tại"));
    }


}
