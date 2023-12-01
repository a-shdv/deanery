package org.university.deanery.dtos;

import lombok.Getter;
import org.university.deanery.models.Classroom;

public record SaveClassroomDto(@Getter Integer classroomNo) {
    public SaveClassroomDto(Integer classroomNo) {
        this.classroomNo = classroomNo;
    }

    public static Classroom toClassroom(SaveClassroomDto saveClassroomDto) {
        return new Classroom(saveClassroomDto.classroomNo);
    }
}
