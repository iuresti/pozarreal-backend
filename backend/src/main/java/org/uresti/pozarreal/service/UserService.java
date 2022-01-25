package org.uresti.pozarreal.service;

import org.uresti.pozarreal.dto.User;

import java.util.List;

public interface UserService {
    List<String> removeRole(String userId, String role);

    List<String> addRole(String userId, String role);

    List<User> getAllUsers();

    User buildUserForEmail(String email);

    User save(User user);

    org.uresti.pozarreal.model.User findOrRegister(String email, String picture, String credential);

    User updateName(String name, String email);
}
