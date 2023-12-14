package org.university.deanery.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    DEAN(0),
    STUDENT(1);
    private int code;

    Role(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
