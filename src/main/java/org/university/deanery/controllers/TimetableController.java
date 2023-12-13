package org.university.deanery.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.university.deanery.models.enums.DayOfWeek;
import org.university.deanery.models.Group;
import org.university.deanery.models.Timetable;
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
                       @ModelAttribute("time-of-class-id") int timeOfClassId) {
        Group group = groupService.findById(groupId).get();
        Timetable timetable = Timetable.builder()
                .group(group)
                .subject(subjectService.findById(subjectId).get())
                .teacher(teacherService.findById(teacherId).get())
                .classroom(classroomService.findById(classroomId).get())
                .dayOfWeek(DayOfWeek.toDayOfWeek(dayOfWeekId))
                .timeOfClass(TimeOfClass.toTimeOfClass(timeOfClassId))
                .build();
        timetableService.save(timetable);
        return "redirect:/timetables";
    }
}
