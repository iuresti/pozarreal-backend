package org.uresti.pozarreal.service.mappers;

import org.uresti.pozarreal.dto.CourseAssistant;

import java.time.LocalDate;
import java.time.Period;

public class CourseAssistantMapper {

    public static CourseAssistant entityToDto(org.uresti.pozarreal.model.CourseAssistant courseAssistant) {
        return CourseAssistant.builder()
                .id(courseAssistant.getId())
                .name(courseAssistant.getName())
                .courseId(courseAssistant.getCourseId())
                .email(courseAssistant.getEmail())
                .birthDate(courseAssistant.getBirthDate())
                .phone(courseAssistant.getPhone())
                .responsibleName(courseAssistant.getResponsibleName())
                .responsibleName2(courseAssistant.getResponsibleName2())
                .responsiblePhone(courseAssistant.getResponsiblePhone())
                .responsiblePhone2(courseAssistant.getResponsiblePhone2())
                .notes(courseAssistant.getNotes())
                .age(courseAssistant.getBirthDate() != null ?
                        Period.between(courseAssistant.getBirthDate(), LocalDate.now()).getYears() : 0)
                .build();
    }

    public static org.uresti.pozarreal.model.CourseAssistant dtoToEntity(CourseAssistant courseAssistant) {
        return org.uresti.pozarreal.model.CourseAssistant.builder()
                .id(courseAssistant.getId())
                .name(courseAssistant.getName())
                .courseId(courseAssistant.getCourseId())
                .email(courseAssistant.getEmail())
                .birthDate(courseAssistant.getBirthDate())
                .phone(courseAssistant.getPhone())
                .responsibleName(courseAssistant.getResponsibleName())
                .responsibleName2(courseAssistant.getResponsibleName2())
                .responsiblePhone(courseAssistant.getResponsiblePhone())
                .responsiblePhone2(courseAssistant.getResponsiblePhone2())
                .notes(courseAssistant.getNotes())
                .build();
    }
}
