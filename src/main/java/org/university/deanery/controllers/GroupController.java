package org.university.deanery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.university.deanery.dtos.GroupDto;
import org.university.deanery.exceptions.GroupAlreadyExistsException;
import org.university.deanery.exceptions.GroupNotFoundException;
import org.university.deanery.models.Group;
import org.university.deanery.services.GroupService;

import java.util.Optional;

@Controller
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public String save(@ModelAttribute("groupDto") GroupDto groupDto, Model model) {
        Optional<Group> group = groupService.findGroupByTitle(groupDto.getTitle());
        String message;
        try {
            if (!group.isEmpty())
                throw new GroupAlreadyExistsException();
            groupService.save(GroupDto.toGroup(groupDto));
            message = "Группа " + groupDto.getTitle() + " успешно создана!";
            model.addAttribute("success", message);
            model.addAttribute("groups", groupService.findAll());
        } catch (GroupAlreadyExistsException e) {
            message = "Группа " + groupDto.getTitle() + " уже существует!";
            model.addAttribute("error", message);
            model.addAttribute("groups", groupService.findAll());
        }
        return "groups/find-all";
    }

    @GetMapping
    public String findAll(@ModelAttribute("groupDto") GroupDto groupDto, Model model) {
        model.addAttribute("groups", groupService.findAll());
        return "groups/find-all";
    }

    @GetMapping("{id}")
    public String findById(@PathVariable Long id, Model model) {
        String message;
        try {
            Group group = groupService.findById(id).get();
            model.addAttribute("group", group);
        } catch (GroupNotFoundException e) {
            message = "Группа с id " + id + " не найдена!";
            model.addAttribute("error", message);
        }
        return "groups/find-by-id";
    }

    @PutMapping("{id}")
    public String updateById(@PathVariable("id") Long id, @ModelAttribute("groupDto") GroupDto groupDto, Model model) {
        String message;
        try {
            if (groupService.findById(id).isEmpty())
                throw new GroupNotFoundException();
            if (groupService.findGroupByTitle(groupDto.getTitle()).isPresent())
                throw new GroupAlreadyExistsException();
            groupService.updateById(id, groupDto);
            message = "Группа успешно обновлена!";
            model.addAttribute("success", message);
            model.addAttribute("groups", groupService.findAll());
        } catch (GroupNotFoundException e) {
            message = "Группа " + groupDto.getTitle() + " не найдена!";
            model.addAttribute("error", message);
            model.addAttribute("groups", groupService.findAll());
        } catch (GroupAlreadyExistsException e) {
            message = "Группа " + groupDto.getTitle() + " уже существует!";
            model.addAttribute("error", message);
            model.addAttribute("groups", groupService.findAll());
        }
        return "redirect:/groups";
    }

    @DeleteMapping("{id}")
    public String deleteById(@PathVariable("id") Long id, Model model) {
        String message;
        try {
            if (groupService.findById(id).isEmpty())
                throw new GroupNotFoundException();
            groupService.deleteById(id);
            message = "Группа успешно удалена!";
            model.addAttribute("success", message);
            model.addAttribute("groups", groupService.findAll());
        } catch (GroupNotFoundException e) {
            message = "Группа " + id + " не найдена!";
            model.addAttribute("error", message);
            model.addAttribute("groups", groupService.findAll());
        }
        return "redirect:/groups";
    }
}
