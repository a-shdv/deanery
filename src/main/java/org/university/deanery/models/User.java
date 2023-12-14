package org.university.deanery.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.university.deanery.models.enums.Role;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String password;
    private LocalDateTime passwordLastChanged;
    private boolean accountNonLocked;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"))
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    /*Срок действия учетной записи*/
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /*Статус блокировки учетной записи*/
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /*Срок действия учетных данных пользователя (пароль)*/
    @Override
    public boolean isCredentialsNonExpired() {
        return ChronoUnit.DAYS.between(passwordLastChanged, LocalDateTime.now()) <= 30;
    }

    /*Проверяет, активирована ли учетная запись пользователя*/
    @Override
    public boolean isEnabled() {
        return true;
    }

    public String toString() {
        return this.getId() + ". " + this.getUsername() + " " + this.getEmail();
    }
}
