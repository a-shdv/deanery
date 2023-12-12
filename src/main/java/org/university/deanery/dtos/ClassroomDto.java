package org.university.deanery.dtos;

import lombok.Getter;
import org.university.deanery.models.Classroom;

public record ClassroomDto(@Getter Integer classroomNo) {
    public ClassroomDto(Integer classroomNo) {
        this.classroomNo = classroomNo;
    }

    public static Classroom toClassroom(ClassroomDto classroomDto) {
        return Classroom.builder()
                .classroomNo(classroomDto.classroomNo())
                .build();
    }
}
