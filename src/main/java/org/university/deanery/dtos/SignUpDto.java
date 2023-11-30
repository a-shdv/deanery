package org.university.deanery.dtos;

import lombok.Getter;
import org.university.deanery.models.User;

public record SignUpDto(@Getter String username, @Getter String password, @Getter String passwordConfirm) {
    public SignUpDto(String username, String password, String passwordConfirm) {
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public static User toUser(SignUpDto dto) {
        return new User(dto.getUsername(), dto.getPassword(), dto.getPasswordConfirm());
    }
}
