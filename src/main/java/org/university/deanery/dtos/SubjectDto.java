package org.university.deanery.dtos;

import lombok.Getter;
import org.university.deanery.models.Group;
import org.university.deanery.models.Subject;
import org.university.deanery.models.User;

public record SubjectDto(@Getter String title) {

    public SubjectDto(String title) {
        this.title = title;
    }

    public static Subject toSubject(SubjectDto subjectDto) {
        return Subject.builder()
                .title(subjectDto.title)
                .build();
    }
}
