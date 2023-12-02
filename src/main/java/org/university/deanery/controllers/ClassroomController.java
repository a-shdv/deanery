package org.university.deanery.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.university.deanery.dtos.SaveClassroomDto;
import org.university.deanery.exceptions.ClassroomNoAlreadyExistsException;
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
    public String save(@ModelAttribute("saveClassroomDto") SaveClassroomDto saveClassroomDto, Model model) {
        Optional<Classroom> classroom = classroomService.findClassroomByClassroomNo(saveClassroomDto.getClassroomNo());
        String message;
        try {
            if (classroom.isPresent())
                throw new ClassroomNoAlreadyExistsException();
            classroomService.saveClassroom(SaveClassroomDto.toClassroom(saveClassroomDto));
            message = "Аудитория №" + saveClassroomDto.getClassroomNo() + " успешно создана!";
            model.addAttribute("success", message);
            model.addAttribute("classrooms", classroomService.findAll());
        } catch (ClassroomNoAlreadyExistsException e) {
            message = "Аудитория №" + saveClassroomDto.classroomNo() + " уже существует!";
            model.addAttribute("error", message);
            model.addAttribute("classrooms", classroomService.findAll());
        }

        return "classrooms/find-all";
    }

    @PutMapping("/{id}")
    public String updateById(@PathVariable("id") Long id, @ModelAttribute("saveClassroomDto") SaveClassroomDto saveClassroomDto, Model model) {
        String message;
        try {
            log.info("updateById()");
            Optional<Classroom> classroom = Optional.ofNullable(classroomService.findById(id).orElseThrow(ClassroomNotFoundException::new));
            classroom = Optional.of(SaveClassroomDto.toClassroom(saveClassroomDto));
            if (classroomService.findClassroomByClassroomNo(saveClassroomDto.classroomNo()).isPresent())
                throw new ClassroomNoAlreadyExistsException();

            classroomService.saveClassroom(classroom.get());
            message = "Аудитория успешно обновлена!";
            model.addAttribute("success", message);
            model.addAttribute("classrooms", classroomService.findAll());
        } catch (ClassroomNotFoundException e) {
            message = "Аудитория №" + id + " не найдена!";
            model.addAttribute("error", message);
            model.addAttribute("classrooms", classroomService.findAll());
        } catch (ClassroomNoAlreadyExistsException e) {
            message = "Аудитория №" + saveClassroomDto.getClassroomNo() + " уже существует!";
            model.addAttribute("error", message);
            model.addAttribute("classrooms", classroomService.findAll());
        }
        return "classrooms/find-all";
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable("id") Long id, Model model) {
        String message;
        log.info("deleteById()");

        try {
            Optional<Classroom> classroom = Optional.ofNullable(classroomService.findById(id).orElseThrow(ClassroomNotFoundException::new));
            classroomService.deleteClassroom(classroom.get());
            message = "Аудитория успешно удалена!";
            model.addAttribute("success", message);
            model.addAttribute("classrooms", classroomService.findAll());
        } catch (ClassroomNotFoundException e) {
            message = "Аудитория №" + id + " не найдена!";
            model.addAttribute("error", message);
            model.addAttribute("classrooms", classroomService.findAll());
        }
        return "classrooms/find-all";
    }
}
