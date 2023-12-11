package org.university.deanery.dtos;

import lombok.Getter;
import org.university.deanery.models.User;

public record ChangePasswordDto(@Getter String email, @Getter String username, @Getter String passwordOld,
                                @Getter String passwordConfirm, @Getter String passwordNew) {
    public ChangePasswordDto(String email, String username, String passwordOld, String passwordConfirm, String passwordNew) {
        this.email = email;
        this.username = username;
        this.passwordOld = passwordOld;
        this.passwordConfirm = passwordConfirm;
        this.passwordNew = passwordNew;
    }

    public static User toUser(ChangePasswordDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(dto.getPasswordNew())
                .build();
    }
}
