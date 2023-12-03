package org.university.deanery.dtos;

import lombok.Getter;
import org.university.deanery.models.Group;
import org.university.deanery.models.User;

public record GroupDto(@Getter String title, @Getter User user) {

    public GroupDto(String title, User user) {
        this.title = title;
        this.user = user;
    }

    public static Group toGroup(GroupDto groupDto) {
        return new Group(groupDto.title, groupDto.user);
    }
}
