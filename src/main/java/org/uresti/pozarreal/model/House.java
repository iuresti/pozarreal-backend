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
@Table(name = "houses")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class House {
    @Id
    private String id;
    private String street;
    private String number;
    @Column(name = "chips_enabled")
    private boolean chipsEnabled;

}
