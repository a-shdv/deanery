package org.university.deanery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping
    public String findAll(Model model) {
        String success = (String) model.getAttribute("success");
        String error = (String) model.getAttribute("error");
        if (success != null)
            model.addAttribute("message", success);
        if (error != null)
            model.addAttribute("error", success);
        model.addAttribute("subjects", subjectService.findAll());
        return "subjects/find-all";
    }

    @PostMapping
    public String save(@ModelAttribute("subjectDto") SubjectDto subjectDto, RedirectAttributes redirectAttributes) throws SubjectNotFoundException {
        String message;
        try {
            Subject subject = subjectService.findSubjectByTitle(subjectDto.getTitle()).get();
            if (subject != null)
                throw new SubjectAlreadyExistsException();
            subjectService.save(subject);
            message = "Предмет " + subjectDto.getTitle() + " успешно сохранен!";
            redirectAttributes.addAttribute("success", message);
        } catch (SubjectAlreadyExistsException e) {
            message = "Предмет с таким названием уже существует!";
            redirectAttributes.addFlashAttribute("error", message);
        }
        return "redirect:/subjects";
    }
}
