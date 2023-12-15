package org.university.deanery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.university.deanery.dtos.TeacherDto;
import org.university.deanery.exceptions.*;
import org.university.deanery.models.Subject;
import org.university.deanery.models.Teacher;
import org.university.deanery.models.TeacherSubject;
import org.university.deanery.services.SubjectService;
import org.university.deanery.services.TeacherService;
import org.university.deanery.services.TeacherSubjectService;

import java.util.Optional;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private final SubjectService subjectService;
    private final TeacherSubjectService teacherSubjectService;

    @Autowired
    public TeacherController(TeacherService teacherService, SubjectService subjectService, TeacherSubjectService teacherSubjectService) {
        this.teacherService = teacherService;
        this.subjectService = subjectService;
        this.teacherSubjectService = teacherSubjectService;
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
    public String findAll(@RequestAttribute int page,
                            @Req,Model model) {
        String success = (String) model.getAttribute("success");
        String error = (String) model.getAttribute("error");
        if (success != null)
            model.addAttribute("message", success);
        if (error != null)
            model.addAttribute("error", error);
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
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

    @GetMapping("/{id}/subjects")
    public String findSubjects(@PathVariable Long id, Model model) {
        String message;
        String error = (String) model.getAttribute("error");
        try {
            if (error != null)
                model.addAttribute(error);
            model.addAttribute("teacher", teacherService.findById(id).orElseThrow(TeacherNotFoundException::new));
            model.addAttribute("subjects", subjectService.findAll());
        } catch (TeacherNotFoundException e) {
            message = "Преподаватель с id: " + id + " не найден(-а)!";
            model.addAttribute("error", message);
        }

        return "teachers/find-subjects";
    }

    @PostMapping("/{id}/subjects")
    public String addSubjects(@PathVariable Long id, @ModelAttribute("subject-title") String subjectTitle, RedirectAttributes redirectAttributes) {
        Optional<TeacherSubject> teacherSubject = Optional.empty();
        String message;
        try {
            Teacher teacher = teacherService.findById(id).orElseThrow(TeacherNotFoundException::new);
            Subject subject = subjectService.findSubjectByTitle(subjectTitle).orElseThrow(SubjectNotFoundException::new);
            teacherSubject = teacherSubjectService.findTeacherSubjectByTeacherAndSubject(teacher, subject);
            if (teacherSubject.isPresent())
                throw new TeacherSubjectAlreadyExistsException();
            teacherSubjectService.addTeacherSubject(
                    TeacherSubject.builder()
                            .teacher(teacher)
                            .subject(subject)
                            .build());

        } catch (TeacherNotFoundException e) {
            message = "Преподаватель с id: " + id + " не найден(-а)!";
            redirectAttributes.addFlashAttribute("error", message);
        } catch (SubjectNotFoundException e) {
            message = "Предмет с названием: " + subjectTitle + " не найден!";
            redirectAttributes.addFlashAttribute("error", message);
        } catch (TeacherSubjectAlreadyExistsException e) {
            message = "Связь преподавателя с id: " + teacherSubject.get().getTeacher().getId() + " и предметом с id: " +
                    teacherSubject.get().getSubject().getId() + " уже существует!";
            redirectAttributes.addFlashAttribute("error", message);
        }

        return "redirect:/teachers/" + id + "/subjects";
    }

    @DeleteMapping("/{teacherId}/subjects/{subjectId}")
    public String deleteSubject(@PathVariable Long teacherId, @PathVariable Long subjectId, RedirectAttributes redirectAttributes) {
        String message;
        TeacherSubject teacherSubject = null;
        try {
            Teacher teacher = teacherService.findById(teacherId).orElseThrow(TeacherNotFoundException::new);
            Subject subject = subjectService.findById(subjectId).orElseThrow(SubjectNotFoundException::new);
            teacherSubject = teacherSubjectService.findTeacherSubjectByTeacherAndSubject(teacher, subject)
                    .orElseThrow(TeacherSubjectNotFoundException::new);
            teacherSubjectService.deleteTeacherSubject(teacherSubject);
        } catch (TeacherNotFoundException e) {
            message = "Преподаватель с id: " + teacherId + " не найден(-а)!";
            redirectAttributes.addFlashAttribute("message", message);
        } catch (SubjectNotFoundException e) {
            message = "Предмет с id: " + subjectId + " не найден!";
            redirectAttributes.addFlashAttribute("message", message);
        } catch (TeacherSubjectNotFoundException e) {
            message = "Связь преподавателя с id: " + teacherSubject.getTeacher().getId() + " и предметом с id: " +
                    teacherSubject.getSubject().getId() + " уже существует!";;
            redirectAttributes.addFlashAttribute("message", message);
        }
        return "redirect:/teachers/" + teacherId + "/subjects";
    }
}
