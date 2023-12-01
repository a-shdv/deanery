package org.university.deanery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.university.deanery.exceptions.ClassroomNotFoundException;
import org.university.deanery.models.Classroom;
import org.university.deanery.services.ClassroomService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("classrooms")
public class ClassroomController {
    private final ClassroomService classroomService;

    @Autowired
    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("classrooms", classroomService.findAll());
        return "classrooms/find-all";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        String message;
        try {
            Optional<Classroom> classroom = classroomService.findById(id);
            model.addAttribute("classroom", classroom);
        } catch (ClassroomNotFoundException e) {
            message = "Аудитория №" + id + " не найдена!";
            model.addAttribute("error", message);
        }
        return "classrooms/find-by-id";
    }
}
