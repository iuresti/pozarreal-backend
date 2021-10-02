package org.uresti.pozarreal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "chips")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chip {

    @Id
    private String id;
    private String house;
    private String code;
    private boolean valid;
    private String notes;

}
