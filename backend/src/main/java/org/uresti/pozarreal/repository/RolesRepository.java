package org.uresti.pozarreal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.uresti.pozarreal.model.RoleByUser;

import java.util.List;

@Repository
public interface RolesRepository extends JpaRepository<RoleByUser, String> {
    @Query("SELECT r.role FROM RoleByUser r WHERE r.userId = :user")
    List<String> findRolesByUser(String user);

    void deleteRoleByUserIdAndRole(String userId, String role);
}
