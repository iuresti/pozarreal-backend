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
@Table(name = "logins")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    @Id
    private String id;
    private String email;
    @Column(name = "user_id")
    private String userId;

}
