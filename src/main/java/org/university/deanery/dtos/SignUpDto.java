package org.university.deanery.dtos;

import lombok.Getter;
import org.university.deanery.models.User;

public record SignUpDto(@Getter String email, @Getter String username, @Getter String password,
                        @Getter String passwordConfirm) {
    public SignUpDto(String email, String username, String password, String passwordConfirm) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public static User toUser(SignUpDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build();
    }
}
