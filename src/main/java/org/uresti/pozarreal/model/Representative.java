package org.uresti.pozarreal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String id;
    private String street;
    private String name;
    private String phone;
    private String address;

}
