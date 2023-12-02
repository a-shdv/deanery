package org.university.deanery.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.university.deanery.dtos.ClassroomDto;
import org.university.deanery.exceptions.ClassroomAlreadyExistsException;
import org.university.deanery.exceptions.ClassroomNotFoundException;
import org.university.deanery.models.Classroom;
import org.university.deanery.services.ClassroomService;

import java.util.Optional;

@Controller
@RequestMapping("/classrooms")
@Slf4j
public class ClassroomController {
    private final ClassroomService classroomService;

    @Autowired
    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @PostMapping
    public String save(@ModelAttribute("classroomDto") ClassroomDto classroomDto, Model model) {
        Optional<Classroom> classroom = classroomService.findClassroomByClassroomNo(classroomDto.getClassroomNo());
        String message;
        try {
            if (classroom.isPresent())
                throw new ClassroomAlreadyExistsException();
            classroomService.save(ClassroomDto.toClassroom(classroomDto));
            message = "Аудитория №" + classroomDto.getClassroomNo() + " успешно создана!";
            model.addAttribute("success", message);
            model.addAttribute("classrooms", classroomService.findAll());
        } catch (ClassroomAlreadyExistsException e) {
            message = "Аудитория №" + classroomDto.classroomNo() + " уже существует!";
            model.addAttribute("error", message);
            model.addAttribute("classrooms", classroomService.findAll());
        }

        return "classrooms/find-all";
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("classrooms", classroomService.findAll());
        return "classrooms/find-all";
    }

    @GetMapping("{id}")
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



    @PutMapping("{id}")
    public String updateById(@PathVariable("id") Long id, @ModelAttribute("classroomDto") ClassroomDto classroomDto, Model model) {
        String message;
        try {
            if (classroomService.findById(id).isEmpty())
                throw new ClassroomNotFoundException();
            if (classroomService.findClassroomByClassroomNo(classroomDto.classroomNo()).isPresent())
                throw new ClassroomAlreadyExistsException();
            classroomService.updateById(id, classroomDto);
            message = "Аудитория успешно обновлена!";
            model.addAttribute("success", message);
            model.addAttribute("classrooms", classroomService.findAll());
        } catch (ClassroomNotFoundException e) {
            message = "Аудитория №" + id + " не найдена!";
            model.addAttribute("error", message);
            model.addAttribute("classrooms", classroomService.findAll());
        } catch (ClassroomAlreadyExistsException e) {
            message = "Аудитория №" + classroomDto.getClassroomNo() + " уже существует!";
            model.addAttribute("error", message);
            model.addAttribute("classrooms", classroomService.findAll());
        }
        return "redirect:/classrooms";
    }

    @DeleteMapping("{id}")
    public String deleteById(@PathVariable("id") Long id, Model model) {
        String message;
        try {
            Optional<Classroom> classroom = Optional.ofNullable(classroomService.findById(id).orElseThrow(ClassroomNotFoundException::new));
            classroomService.delete(classroom.get());
            message = "Аудитория успешно удалена!";
            model.addAttribute("success", message);
            model.addAttribute("classrooms", classroomService.findAll());
        } catch (ClassroomNotFoundException e) {
            message = "Аудитория №" + id + " не найдена!";
            model.addAttribute("error", message);
            model.addAttribute("classrooms", classroomService.findAll());
        }
        return "redirect:/classrooms";
    }
}
