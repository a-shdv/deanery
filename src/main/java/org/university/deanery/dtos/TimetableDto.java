package org.university.deanery.dtos;

import lombok.Builder;
import lombok.Getter;
import org.university.deanery.models.Classroom;
import org.university.deanery.models.Group;
import org.university.deanery.models.Subject;
import org.university.deanery.models.Teacher;

@Builder
public record TimetableDto(@Getter Group group, @Getter Subject subject, @Getter Teacher teacher,
                           @Getter Classroom classroom, @Getter int dayOfWeekId, @Getter int timeOfClassId) {
    public TimetableDto(Group group, Subject subject, Teacher teacher, Classroom classroom, int dayOfWeekId, int timeOfClassId) {
        this.group = group;
        this.subject = subject;
        this.teacher = teacher;
        this.classroom = classroom;
        this.dayOfWeekId = dayOfWeekId;
        this.timeOfClassId = timeOfClassId;
    }
}
