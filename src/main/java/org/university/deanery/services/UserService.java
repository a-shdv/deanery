package org.university.deanery.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.university.deanery.exceptions.PasswordLengthException;
import org.university.deanery.models.User;
import org.university.deanery.models.enums.Role;
import org.university.deanery.repositories.UserRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public static final byte passwordLength = 3;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(User user) {
        user.setAccountNonLocked(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPasswordLastChanged(LocalDateTime.now());
        user.setRoles(Collections.singleton(Role.STUDENT));
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(userRepository.findById(id)).get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username);
    }

    public void changeUserPassword(User user, String newPassword) throws PasswordLengthException {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordLastChanged(LocalDateTime.now());
        userRepository.save(user);
    }

    public void setAccountNonLocked(User user, boolean status) {
        user.setAccountNonLocked(status);
        userRepository.save(user);
    }

//    @EventListener(ApplicationReadyEvent.class)
    public void generatePdf() {
        List<User> users = userRepository.findAll();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream("/Users/a-shdv/IdeaProjects/deanery/test.pdf"));

            document.open();

            Paragraph p = new Paragraph();
            p.add("List of all users");
            p.setAlignment(Element.ALIGN_CENTER);

            document.add(p);

            Paragraph p2 = new Paragraph();
            for (User user : users) {
                p2.add(user.toString() + "\n");
            }
            document.add(p2);

            Font f = new Font();
            f.setStyle(Font.BOLD);
            f.setSize(8);

            document.close();
        } catch (DocumentException | IOException e) {
            log.info(e.getMessage());
        }
    }
}
