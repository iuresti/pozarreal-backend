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
@Table(name = "courses")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    private String id;
    private String name;
    @Column(name = "professor_id")
    private String professorId;
}
