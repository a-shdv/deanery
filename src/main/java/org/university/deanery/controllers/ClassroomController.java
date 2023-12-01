package org.university.deanery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.university.deanery.dtos.SaveClassroomDto;
import org.university.deanery.exceptions.ClassroomNoAlreadyExistsException;
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

    @PostMapping
    public String saveClassroom(@ModelAttribute("saveClassroomDto") SaveClassroomDto saveClassroomDto, Model model) {
        Optional<Classroom> classroom = classroomService.findClassroomByClassroomNo(saveClassroomDto.getClassroomNo());
        String message;
            try {
                if (classroom.isPresent())
                    throw new ClassroomNoAlreadyExistsException();
                classroomService.saveClassroom(SaveClassroomDto.toClassroom(saveClassroomDto));
            } catch (ClassroomNoAlreadyExistsException e) {
                message = "Аудитория №" + saveClassroomDto.classroomNo() + " уже существует!";
                model.addAttribute("error", message);
            }

        return "redirect:/classrooms";
    }

    @PatchMapping("/{id}")
    public String updateById(@PathVariable Long id, @ModelAttribute("saveClassroomDto") SaveClassroomDto saveClassroomDto, Model model) {
        String message;
        try {
            Optional<Classroom> classroom = Optional.ofNullable(classroomService.findById(id).orElseThrow(ClassroomNotFoundException::new));

//            SaveClassroomDto.toClassroom(saveClassroomDto);
        } catch (ClassroomNotFoundException e) {
            message = "Аудитория №" + id + " не найдена!";
            model.addAttribute("error", message);
        }
        return "";
    }
}
