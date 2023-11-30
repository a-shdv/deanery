package org.university.deanery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.university.deanery.dtos.SignUpDto;
import org.university.deanery.exceptions.UserAlreadyExistsException;
import org.university.deanery.models.User;
import org.university.deanery.services.UserService;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign-in")
    public String signIn() {
        return "sign-in";
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute("signUpDto") SignUpDto signUpDto, Model model) throws UserAlreadyExistsException {
        User user = (User) userService.loadUserByUsername(signUpDto.getUsername());
        String message = "";
        try {
            if (user != null) {
                message = "Пользователь с именем " + signUpDto.getUsername() + " уже существует!";
                throw new UserAlreadyExistsException(message);
            }
            userService.saveUser(user);
        } catch (UserAlreadyExistsException ex) {
            model.addAttribute("message", message);
        }

        return "sign-up";
    }
}
