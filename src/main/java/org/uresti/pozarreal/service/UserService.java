package org.uresti.pozarreal.service;

import org.uresti.pozarreal.dto.User;

import java.util.List;

public interface UserService {
    List<String> removeRole(String userId, String role);

    List<String> addRole(String userId, String role);

    List<User> getAllUsers();
}
