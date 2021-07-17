package org.uresti.pozarreal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private String id;
    private String name;
    private String professorId;
    private String professorName;
    private String professorPhone;
    private String professorAddress;
    private List<CourseOccurrence> occurrences;

    public Course(String id, String name, String professorId, String professorName, String professorPhone, String professorAddress) {
        this.id = id;
        this.name = name;
        this.professorId = professorId;
        this.professorName = professorName;
        this.professorPhone = professorPhone;
        this.professorAddress = professorAddress;
    }
}
