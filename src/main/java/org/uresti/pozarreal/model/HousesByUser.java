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
@Table(name = "houses_by_user")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HousesByUser {
    @Id
    private String id;

    private String userId;

    private String houseId;
}
