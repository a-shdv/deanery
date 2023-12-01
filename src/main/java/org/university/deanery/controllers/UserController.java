package org.university.deanery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.university.deanery.dtos.ChangePasswordDto;
import org.university.deanery.dtos.SignUpDto;
import org.university.deanery.exceptions.*;
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
        return "users/sign-in";
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute("signUpDto") SignUpDto signUpDto, Model model) {
        User user = (User) userService.loadUserByUsername(signUpDto.getUsername());
        String message = "";
        model.asMap().clear();
        try {
            if (userService.findUserByEmail(signUpDto.getEmail()) != null)
                throw new UserEmailAlreadyExistsException();
            if (user != null)
                throw new UserUsernameAlreadyExistsException();
            if (!signUpDto.getPassword().equals(signUpDto.getPasswordConfirm()))
                throw new PasswordsMismatchException();
            if (signUpDto.getPassword().length() < UserService.passwordLength)
                throw new PasswordLengthException();
            userService.saveUser(SignUpDto.toUser(signUpDto));
            message = "Пользователь " + signUpDto.getUsername() + " успешно создан!";
            model.addAttribute("success", message);
        } catch (UserUsernameAlreadyExistsException e) {
            message = "Пользователь с именем " + signUpDto.getUsername() + " уже существует!";
            model.addAttribute("error", message);
            return "users/sign-up";
        } catch (PasswordsMismatchException e) {
            message = "Пароли не совпадают!";
            model.addAttribute("error", message);
            return "users/sign-up";
        } catch (PasswordLengthException e) {
            message = "Длина пароля должна быть больше " + UserService.passwordLength + " символов!";
            model.addAttribute("error", message);
            return "users/sign-up";
        } catch (UserEmailAlreadyExistsException e) {
            message = "Пользователь с электронной почтой " + signUpDto.getEmail() + " уже существует!";
            model.addAttribute("error", message);
            return "users/sign-up";
        }

        return "users/sign-in";
    }

    @GetMapping("/change-password")
    public String changePassword() {
        return "users/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@ModelAttribute("changePasswordDto") ChangePasswordDto changePasswordDto, Model model) {
        String message;
        try {
            User user = userService.findUserByEmail(changePasswordDto.getEmail());
            if (userService.findUserByEmail(changePasswordDto.getEmail()) == null)
                throw new UserEmailNotExistsException();
            if (user == null)
                throw new UserUsernameNotExistsException();
            if (changePasswordDto.passwordOld().equals(changePasswordDto.passwordNew()))
                throw new PasswordMustBeNewException();
            if (!changePasswordDto.passwordNew().equals(changePasswordDto.passwordConfirm()))
                throw new PasswordsMismatchException();
            if (changePasswordDto.getPasswordNew().length() < UserService.passwordLength)
                throw new PasswordLengthException();
            userService.changeUserPassword(user, changePasswordDto.getPasswordNew());
            message = "Пароль успешно изменен!";
            model.addAttribute("success", message);
        } catch (UserEmailNotExistsException e) {
            message = "Пользователь с электронной почтой " + changePasswordDto.email() + " не найден!";
            model.addAttribute("error", message);
        } catch (UserUsernameNotExistsException e) {
            message = "Пользователь с именем " + changePasswordDto.username() + " не найден!";
            model.addAttribute("error", message);
        } catch (PasswordsMismatchException ex) {
            message = "Пароли не совпадают!";
            model.addAttribute("error", message);
        } catch (PasswordMustBeNewException e) {
            message = "Новый пароль не может быть таким же, как старый!";
            model.addAttribute("error", message);
        } catch (PasswordLengthException e) {
            message = "Длина пароля должна быть больше " + UserService.passwordLength + " символов!";
            model.addAttribute("error", message);
        }
        return "users/change-password";
    }
}
