package org.uresti.pozarreal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "course_assistant")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseAssistant {
    @Id
    private String id;
    private String name;
    @Column(name = "course_id")
    private String courseId;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    private String phone;
    @Column(name = "responsible_name")
    private String responsibleName;
    @Column(name = "responsible_phone")
    private String responsiblePhone;
    @Column(name = "responsible_name2")
    private String responsibleName2;
    @Column(name = "responsible_phone2")
    private String responsiblePhone2;
    private String email;
    private String notes;
}
