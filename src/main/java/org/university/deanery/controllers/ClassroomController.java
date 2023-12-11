package org.university.deanery.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.university.deanery.dtos.ClassroomDto;
import org.university.deanery.exceptions.ClassroomAlreadyExistsException;
import org.university.deanery.exceptions.ClassroomNotFoundException;
import org.university.deanery.models.Classroom;
import org.university.deanery.services.ClassroomService;

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
    public String save(@ModelAttribute("classroomDto") ClassroomDto classroomDto, RedirectAttributes redirectAttributes) {
        String message;
        try {
            if (classroomService.findClassroomByClassroomNo(classroomDto.getClassroomNo()).isPresent())
                throw new ClassroomAlreadyExistsException();
            classroomService.save(ClassroomDto.toClassroom(classroomDto));
            message = "Аудитория №" + classroomDto.getClassroomNo() + " успешно создана!";
            redirectAttributes.addFlashAttribute("success", message);
        } catch (ClassroomAlreadyExistsException e) {
            message = "Аудитория №" + classroomDto.classroomNo() + " уже существует!";
            redirectAttributes.addFlashAttribute("error", message);
        }

        return "redirect:/classrooms";
    }

    @GetMapping
    public String findAll(Model model) {
        String success = (String) model.getAttribute("success");
        String error = (String) model.getAttribute("error");
        if (success != null)
            model.addAttribute("success", success);
        if (error != null)
            model.addAttribute("error", error);
        model.addAttribute("classrooms", classroomService.findAll());
        return "classrooms/find-all";
    }

    @GetMapping("{id}")
    public String findById(@PathVariable Long id, Model model) {
        String message;
        try {
            Classroom classroom = classroomService.findById(id).orElseThrow(ClassroomNotFoundException::new);
            model.addAttribute("classroom", classroom);
        } catch (ClassroomNotFoundException e) {
            message = "Аудитория №" + id + " не найдена!";
            model.addAttribute("error", message);
        }
        return "classrooms/find-by-id";
    }


    @PutMapping("{id}")
    public String updateById(@PathVariable("id") Long id, @ModelAttribute("classroomDto") ClassroomDto classroomDto, RedirectAttributes redirectAttributes) {
        String message;
        try {
            if (classroomService.findById(id).isEmpty())
                throw new ClassroomNotFoundException();
            if (classroomService.findClassroomByClassroomNo(classroomDto.classroomNo()).isPresent())
                throw new ClassroomAlreadyExistsException();
            classroomService.updateById(id, classroomDto);
            message = "Аудитория успешно обновлена!";
            redirectAttributes.addFlashAttribute("success", message);
        } catch (ClassroomNotFoundException e) {
            message = "Аудитория №" + id + " не найдена!";
            redirectAttributes.addFlashAttribute("error", message);
        } catch (ClassroomAlreadyExistsException e) {
            message = "Аудитория №" + classroomDto.getClassroomNo() + " уже существует!";
            redirectAttributes.addFlashAttribute("error", message);
        }
        return "redirect:/classrooms";
    }

    @DeleteMapping("{id}")
    public String deleteById(@PathVariable("id") Long id, RedirectAttributes model) {
        String message;
        try {
            Classroom classroom = classroomService.findById(id).orElseThrow(ClassroomNotFoundException::new);
            classroomService.delete(classroom);
            message = "Аудитория успешно удалена!";
            model.addAttribute("success", message);
        } catch (ClassroomNotFoundException e) {
            message = "Аудитория №" + id + " не найдена!";
            model.addAttribute("error", message);
        }
        return "redirect:/classrooms";
    }
}
