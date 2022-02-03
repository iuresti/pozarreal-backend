package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.dto.User;
import org.uresti.pozarreal.exception.BadRequestDataException;
import org.uresti.pozarreal.model.Login;
import org.uresti.pozarreal.model.RoleByUser;
import org.uresti.pozarreal.repository.LoginRepository;
import org.uresti.pozarreal.repository.RepresentativeRepository;
import org.uresti.pozarreal.repository.RolesRepository;
import org.uresti.pozarreal.repository.UserRepository;
import org.uresti.pozarreal.service.UserService;
import org.uresti.pozarreal.service.mappers.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final RolesRepository rolesRepository;
    private final UserRepository userRepository;
    private final RepresentativeRepository representativeRepository;
    private final LoginRepository loginRepository;

    public UserServiceImpl(RolesRepository rolesRepository,
                           UserRepository userRepository,
                           RepresentativeRepository representativeRepository,
                           LoginRepository loginRepository) {
        this.rolesRepository = rolesRepository;
        this.userRepository = userRepository;
        this.representativeRepository = representativeRepository;
        this.loginRepository = loginRepository;
    }

    @Override
    @Transactional
    public List<String> removeRole(String userId, String role) {

        rolesRepository.deleteRoleByUserIdAndRole(userId, role);

        return rolesRepository.findRolesByUser(userId);
    }

    @Override
    @Transactional
    public List<String> addRole(String userId, String role) {
        RoleByUser roleByUser = new RoleByUser();

        roleByUser.setId(UUID.randomUUID().toString());
        roleByUser.setRole(role);
        roleByUser.setUserId(userId);

        rolesRepository.save(roleByUser);

        return rolesRepository.findRolesByUser(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {

        return userRepository.findAll().stream().map(UserMapper::entityToDto)
                .peek(user -> user.setRoles(rolesRepository.findRolesByUser(user.getId())))
                .peek(user -> {
                    if (user.getRoles().contains(Role.ROLE_REPRESENTATIVE.name())) {
                        representativeRepository.findById(user.getId()).ifPresent(representative -> user.setStreet(representative.getStreet()));
                    }
                })
                .collect(Collectors.toList());

    }

    @Override
    @Transactional(readOnly = true)
    public User buildUserForEmail(String email) {
        org.uresti.pozarreal.model.User user = userRepository.findByEmail(email).orElseThrow();

        if (!user.getStatus()) {
            throw new BadRequestDataException("user is disabled","ERROR_USER_DISABLED");
        }

        return User.builder()
                .id(user.getId())
                .picture(user.getPicture())
                .name(user.getName())
                .roles(rolesRepository.findRolesByUser(user.getId()))
                .build();
    }

    @Override
    @Transactional
    public User save(User user) {
        org.uresti.pozarreal.model.User dbUser = userRepository.findById(user.getId()).orElseThrow();

        dbUser.setName(user.getName());

        userRepository.save(dbUser);

        return User.builder()
                .id(user.getId())
                .picture(user.getPicture())
                .name(user.getName())
                .roles(rolesRepository.findRolesByUser(user.getId()))
                .build();
    }


    @Override
    @Transactional
    public org.uresti.pozarreal.model.User findOrRegister(String email, String picture, String name) {
        return userRepository.findByEmail(email).or(() -> registerUser(email, picture, name)).orElseThrow();
    }

    @Override
    public User updateStatus(String userId, Boolean status) {
        org.uresti.pozarreal.model.User user = userRepository.findById(userId).orElseThrow();

        user.setStatus(status);

        return UserMapper.entityToDto(userRepository.save(user));
    }

    private Optional<org.uresti.pozarreal.model.User> registerUser(String email, String picture, String name) {
        org.uresti.pozarreal.model.User user = new org.uresti.pozarreal.model.User();

        user.setId(UUID.randomUUID().toString());
        user.setName(name);
        user.setPicture(picture);

        userRepository.save(user);

        Login login = new Login();

        login.setUserId(user.getId());
        login.setEmail(email);
        login.setId(UUID.randomUUID().toString());

        loginRepository.save(login);

        return Optional.of(user);
    }
}