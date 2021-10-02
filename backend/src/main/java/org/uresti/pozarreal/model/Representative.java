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
@Table(name = "representatives")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Representative {
    @Id
    @Column(name = "user_id")
    private String userId;
    private String street;
    private String phone;
    private String house;
}
