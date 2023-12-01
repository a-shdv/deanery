package org.university.deanery.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.university.deanery.exceptions.PasswordLengthException;
import org.university.deanery.exceptions.PasswordRegexpException;
import org.university.deanery.models.User;
import org.university.deanery.repositories.RoleRepository;
import org.university.deanery.repositories.UserRepository;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    public static final byte passwordLength = 4;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(User user) throws PasswordRegexpException, PasswordLengthException {
        if (!checkRegexp(user.getPassword()))
            throw new PasswordRegexpException();
        if (user.getPassword().length() <= 4)
            throw new PasswordLengthException();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(roleRepository.findRoleByTitle("USER")));
        userRepository.save(user);
    }

    public void changeUserPassword(User user, String newPassword) throws PasswordRegexpException, PasswordLengthException {
        if (!checkRegexp(newPassword))
            throw new PasswordRegexpException();
        if (newPassword.length() <= 4)
            throw new PasswordLengthException();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private boolean checkRegexp(String password) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username);
    }
}
