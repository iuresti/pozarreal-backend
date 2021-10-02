package org.uresti.pozarreal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles_by_user")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleByUser {
    @Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    private String role;

}
