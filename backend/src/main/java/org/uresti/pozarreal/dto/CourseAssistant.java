package org.uresti.pozarreal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseAssistant {
    private String id;
    private String courseId;
    private String name;
    private LocalDate birthDate;
    private int age;
    private String phone;
    private String responsibleName;
    private String responsiblePhone;
    private String responsibleName2;
    private String responsiblePhone2;
    private String email;
    private String notes;
}
