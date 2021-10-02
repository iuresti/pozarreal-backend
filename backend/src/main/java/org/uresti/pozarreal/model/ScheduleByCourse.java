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
@Table(name = "schedule_by_course")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleByCourse {
    @Id
    private String id;
    @Column(name = "course_id")
    private String courseId;
    private String weekday;
    @Column(name = "start_time")
    private int startTime;
    @Column(name = "end_time")
    private int endTime;
}
