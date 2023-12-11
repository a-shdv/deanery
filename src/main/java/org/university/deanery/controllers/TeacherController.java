package org.university.deanery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.university.deanery.dtos.ClassroomDto;
import org.university.deanery.dtos.TeacherDto;
import org.university.deanery.exceptions.ClassroomAlreadyExistsException;
import org.university.deanery.exceptions.SubjectNotFoundException;
import org.university.deanery.exceptions.TeacherAlreadyExistsException;
import org.university.deanery.exceptions.TeacherNotFoundException;
import org.university.deanery.models.Teacher;
import org.university.deanery.services.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    public String save(@ModelAttribute TeacherDto teacherDto, RedirectAttributes redirectAttributes) {
        String message;
        try {
            if (teacherService.findTeacherByLastNameAndFirstNameAndPatronymicName(
                            teacherDto.getLastName(),
                            teacherDto.firstName(),
                            teacherDto.patronymicName())
                    .isPresent())
                throw new TeacherAlreadyExistsException();
            teacherService.save(TeacherDto.toTeacher(teacherDto));
            message = "Преподаватель " + teacherDto.getLastName() + " "
                    + teacherDto.getFirstName() + " "
                    + teacherDto.getPatronymicName() + " успешно создан!";
            redirectAttributes.addFlashAttribute("success", message);
        } catch (TeacherAlreadyExistsException e) {
            message = "Преподаватель " + teacherDto.getLastName() + " "
                    + teacherDto.getFirstName() + " "
                    + teacherDto.getPatronymicName() + " уже существует!";
            redirectAttributes.addFlashAttribute("error", message);
        }

        return "redirect:/teachers";
    }

    @GetMapping
    public String findAll(Model model) {
        String success = (String) model.getAttribute("success");
        String error = (String) model.getAttribute("error");
        if (success != null)
            model.addAttribute("message", success);
        if (error != null)
            model.addAttribute("error", error);
        model.addAttribute("teachers", teacherService.findAll());
        return "teachers/find-all";
    }

    @GetMapping("{id}")
    public String findById(@PathVariable Long id, Model model) {
        String message;
        try {
            Teacher teacher = teacherService.findById(id).orElseThrow(TeacherNotFoundException::new);
            model.addAttribute("teacher", teacher);
        } catch (TeacherNotFoundException e) {
            message = "Преподаватель с id " + id + " не найден(-а)!";
            model.addAttribute("error", message);
        }
        return "teachers/find-by-id";
    }

    @PutMapping("{id}")
    public String updateById(@PathVariable Long id, @ModelAttribute TeacherDto teacherDto, Model model) {
        String message;
        try {
            if (teacherService.findById(id).isEmpty())
                throw new TeacherNotFoundException();
            if (teacherService.findTeacherByLastNameAndFirstNameAndPatronymicName(teacherDto.getLastName(),
                            teacherDto.getFirstName(),
                            teacherDto.getPatronymicName())
                    .isPresent())
                throw new TeacherAlreadyExistsException();
            teacherService.updateById(id, teacherDto);
            message = "ФИО преподавателя успешно обновлено!";
            model.addAttribute("success", message);
            model.addAttribute("teachers", teacherService.findAll());
        } catch (TeacherNotFoundException e) {
            message = "Преподаватель " + teacherDto.lastName() + " " + teacherDto.getFirstName() + " " + teacherDto.getFirstName() + " не найден(-а)!";
            model.addAttribute("error", message);
            model.addAttribute("teachers", teacherService.findAll());
        } catch (TeacherAlreadyExistsException e) {
            message = "Преподаватель " + teacherDto.lastName() + " " + teacherDto.getFirstName() + " " + teacherDto.getFirstName() + " уже существует!";
            model.addAttribute("error", message);
            model.addAttribute("teachers", teacherService.findAll());
        }
        return "redirect:/teachers";
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String message;
        try {
            teacherService.findById(id).orElseThrow(SubjectNotFoundException::new);
            teacherService.deleteById(id);
            message = "Преподаватель успешно удален!";
            redirectAttributes.addAttribute("success", message);
        } catch (SubjectNotFoundException e) {
            message = "Преподаватель с id: " + id + " не найден";
            redirectAttributes.addAttribute("error", message);
        }
        return "redirect:/teachers";
    }
}
