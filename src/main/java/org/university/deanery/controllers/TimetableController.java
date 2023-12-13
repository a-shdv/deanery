package org.university.deanery.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.university.deanery.dtos.TimetableDto;
import org.university.deanery.exceptions.TimetableAlreadyExistsException;
import org.university.deanery.exceptions.TimetableNotFoundException;
import org.university.deanery.models.Timetable;
import org.university.deanery.models.enums.DayOfWeek;
import org.university.deanery.models.enums.TimeOfClass;
import org.university.deanery.services.*;

import java.util.Comparator;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/timetables")
public class TimetableController {
    private final TimetableService timetableService;
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final TeacherService teacherService;
    private final ClassroomService classroomService;

    @Autowired
    public TimetableController(TimetableService timetableService, GroupService groupService, SubjectService subjectService, TeacherService teacherService, ClassroomService classroomService) {
        this.timetableService = timetableService;
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.teacherService = teacherService;
        this.classroomService = classroomService;
    }

    @GetMapping
    public String findAll(Model model) {
        String success = (String) model.getAttribute("success");
        String error = (String) model.getAttribute("error");
        if (success != null)
            model.addAttribute("success", success);
        if (error != null)
            model.addAttribute("error", error);
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("classrooms", classroomService.findAll());
        model.addAttribute("timetables", timetableService.findAll()
                .stream()
                .sorted(Comparator.comparing(Timetable::getDayOfWeek).thenComparing(Timetable::getTimeOfClass))
                .collect(Collectors.toList()));
        return "timetables/find-all";
    }

    @PostMapping
    public String save(@ModelAttribute("group-id") Long groupId,
                       @ModelAttribute("subject-id") Long subjectId,
                       @ModelAttribute("teacher-id") Long teacherId,
                       @ModelAttribute("classroom-id") Long classroomId,
                       @ModelAttribute("day-of-week-id") int dayOfWeekId,
                       @ModelAttribute("time-of-class-id") int timeOfClassId,
                       RedirectAttributes redirectAttributes) {
        String message;
        DayOfWeek dayOfWeek = DayOfWeek.toDayOfWeek(dayOfWeekId);
        TimeOfClass timeOfClass = TimeOfClass.toTimeOfClass(timeOfClassId);
        try {
            if (timetableService
                    .findTimetableByClassroom_IdAndDayOfWeekAndTimeOfClass(classroomId, dayOfWeek, timeOfClass)
                    .isPresent())
                throw new TimetableAlreadyExistsException();
            Timetable timetable = Timetable.builder()
                    .group(groupService.findById(groupId).get())
                    .subject(subjectService.findById(subjectId).get())
                    .teacher(teacherService.findById(teacherId).get())
                    .classroom(classroomService.findById(classroomId).get())
                    .dayOfWeek(dayOfWeek)
                    .timeOfClass(timeOfClass)
                    .build();
            timetableService.save(timetable);
            message = "Расписание успешно добавлено!";
            redirectAttributes.addFlashAttribute("success", message);
        } catch (TimetableAlreadyExistsException e) {
            message = "Расписание в аудитории с classroom: " + classroomId + " в dayOfWeekId: " + dayOfWeekId
                    + " в timeOfClassId: " + timeOfClassId + " уже существует!";
            redirectAttributes.addFlashAttribute("error", message);
        }
        return "redirect:/timetables";
    }

    @GetMapping("/{id}")
    public String editById(@PathVariable Long id, Model model) {
        return "/timetables/find-by-id";
    }

    @PutMapping("/{id}")
    public String editById(@PathVariable Long id,
                           @ModelAttribute("group-id") Long groupId,
                           @ModelAttribute("subject-id") Long subjectId,
                           @ModelAttribute("teacher-id") Long teacherId,
                           @ModelAttribute("classroom-id") Long classroomId,
                           @ModelAttribute("day-of-week-id") int dayOfWeekId,
                           @ModelAttribute("time-of-class-id") int timeOfClassId,
                           RedirectAttributes redirectAttributes) {
        String message;
        try {
            timetableService.findById(id).orElseThrow(TimetableNotFoundException::new);
            timetableService.updateById(id, TimetableDto.builder()
                    .group(groupService.findById(groupId).get())
                    .subject(subjectService.findById(subjectId).get())
                    .teacher(teacherService.findById(teacherId).get())
                    .classroom(classroomService.findById(classroomId).get())
                    .dayOfWeekId(dayOfWeekId)
                    .timeOfClassId(timeOfClassId).build());
            message = "Расписание с id: " + id + " успешно обновлено";
            redirectAttributes.addFlashAttribute("success", message);
        } catch (TimetableNotFoundException e) {
            message = "Расписание с id: " + id + " не найдено!";
            redirectAttributes.addFlashAttribute("error", message);
        }
        return "redirect:/timetables";
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String message;
        try {
            timetableService.delete(timetableService.findById(id).orElseThrow(TimetableAlreadyExistsException::new));
            message = "Расписание с id: " + id + " успешно удалено!";
            redirectAttributes.addFlashAttribute("success", message);
        } catch (TimetableAlreadyExistsException e) {
            message = "Расписание с id: " + id + " не найдено!";
            redirectAttributes.addFlashAttribute("error", message);
        }
        return "redirect:/timetables";
    }
}
