package org.university.deanery.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.university.deanery.exceptions.PasswordLengthException;
import org.university.deanery.models.Group;
import org.university.deanery.models.User;
import org.university.deanery.repositories.RoleRepository;
import org.university.deanery.repositories.UserRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
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

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(roleRepository.findRoleByTitle("USER")));
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username);
    }

    public void changeUserPassword(User user, String newPassword) throws PasswordLengthException {
        user.setPassword(passwordEncoder.encode(newPassword));
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
