package org.university.deanery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.university.deanery.dtos.SubjectDto;
import org.university.deanery.exceptions.SubjectAlreadyExistsException;
import org.university.deanery.exceptions.SubjectNotFoundException;
import org.university.deanery.models.Subject;
import org.university.deanery.services.SubjectService;

@Controller
@RequestMapping("/subjects")
public class SubjectController {
    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    public String save(@ModelAttribute("subjectDto") SubjectDto subjectDto, RedirectAttributes redirectAttributes) {
        String message;
        try {
            if (subjectService.findSubjectByTitle(subjectDto.getTitle()).isPresent())
                throw new SubjectAlreadyExistsException();
            subjectService.save(SubjectDto.toSubject(subjectDto));
            message = "Предмет " + subjectDto.getTitle() + " успешно сохранен!";
            redirectAttributes.addFlashAttribute("success", message);
        } catch (SubjectAlreadyExistsException e) {
            message = "Предмет с таким названием уже существует!";
            redirectAttributes.addFlashAttribute("error", message);
        }
        return "redirect:/subjects";
    }

    @GetMapping
    public String findAll(@RequestParam(required = false, defaultValue = "0") int page,
                          @RequestParam(required = false, defaultValue = "10") int size,
                          Model model) {
        Page<Subject> subjectPage = subjectService.findAll(PageRequest.of(page, size));
        String success = (String) model.getAttribute("success");
        String error = (String) model.getAttribute("error");
        if (success != null)
            model.addAttribute("message", success);
        if (error != null)
            model.addAttribute("error", error);
        model.addAttribute("subjects", subjectPage);
        return "subjects/find-all";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model) {
        String message;
        try {
            Subject subject = subjectService.findById(id).orElseThrow(SubjectNotFoundException::new);
            model.addAttribute("subject", subject);
        } catch (SubjectNotFoundException e) {
            message = "Предмет с id: " + id + " не найден!";
            model.addAttribute("error", message);
        }
        return "subjects/find-by-id";
    }

    @PutMapping("/{id}")
    public String updateById(@PathVariable Long id, @ModelAttribute SubjectDto subjectDto, RedirectAttributes redirectAttributes) {
        String message;
        try {
            if (subjectService.findById(id).isEmpty())
                throw new SubjectNotFoundException();
            if (subjectService.findSubjectByTitle(subjectDto.getTitle()).isPresent())
                throw new SubjectAlreadyExistsException();
            subjectService.updateById(id, subjectDto);
            message = "Предмет успешно обновлен!";
            redirectAttributes.addFlashAttribute("success", message);
        } catch (SubjectNotFoundException e) {
            message = "Предмет " + id + " не найдена!";
            redirectAttributes.addFlashAttribute("error", message);
        } catch (SubjectAlreadyExistsException e) {
            message = "Предмет " + subjectDto.getTitle() + " уже существует!";
            redirectAttributes.addFlashAttribute("error", message);
        }
        return "redirect:/subjects";
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String message;
        try {
            subjectService.findById(id).orElseThrow(SubjectNotFoundException::new);
            subjectService.deleteById(id);
            message = "Предмет успешно удален!";
            redirectAttributes.addAttribute("success", message);
        } catch (SubjectNotFoundException e) {
            message = "Аудитория №" + id + " не найдена!";
            redirectAttributes.addAttribute("error", message);
        }
        return "redirect:/subjects";
    }
}
