package com.test.demo.service.auth;

import com.test.demo.dto.UserDTO;
import com.test.demo.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findUserByEmail(String email);

    void saveAdmin(User user);
    void saveUser(User user);
    void registerUser(UserDTO userDTO) throws Exception;
}
