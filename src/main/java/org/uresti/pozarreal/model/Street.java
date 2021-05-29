package org.uresti.pozarreal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "streets")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Street {

    @Id
    private String id;
    private String name;

}
