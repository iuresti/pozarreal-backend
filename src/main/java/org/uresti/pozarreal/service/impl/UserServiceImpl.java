package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uresti.pozarreal.config.RoleConstants;
import org.uresti.pozarreal.dto.User;
import org.uresti.pozarreal.model.RoleByUser;
import org.uresti.pozarreal.repository.RepresentativeRepository;
import org.uresti.pozarreal.repository.RolesRepository;
import org.uresti.pozarreal.repository.UserRepository;
import org.uresti.pozarreal.service.UserService;
import org.uresti.pozarreal.service.mappers.UserMapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final RolesRepository rolesRepository;
    private final UserRepository userRepository;
    private final RepresentativeRepository representativeRepository;

    public UserServiceImpl(RolesRepository rolesRepository,
                           UserRepository userRepository,
                           RepresentativeRepository representativeRepository) {
        this.rolesRepository = rolesRepository;
        this.userRepository = userRepository;
        this.representativeRepository = representativeRepository;
    }

    @Override
    @Transactional
    public List<String> removeRole(String userId, String role) {

        this.rolesRepository.deleteRoleByUserIdAndRole(userId, role);

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
                    if (user.getRoles().contains(RoleConstants.ROLE_REPRESENTATIVE)) {
                        this.representativeRepository.findById(user.getId()).ifPresent(representative -> user.setStreet(representative.getStreet()));
                    }
                })
                .collect(Collectors.toList());


    }

    @Override
    @Transactional(readOnly = true)
    public User buildUserForEmail(String email) {
        org.uresti.pozarreal.model.User user = userRepository.findByEmail(email).orElseThrow();

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
}
