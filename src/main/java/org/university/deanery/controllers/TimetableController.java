package org.university.deanery.controllers;


import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.university.deanery.dtos.TimetableDto;
import org.university.deanery.exceptions.GroupAlreadyExistsException;
import org.university.deanery.exceptions.GroupNotFoundException;
import org.university.deanery.exceptions.TimetableAlreadyExistsException;
import org.university.deanery.exceptions.TimetableNotFoundException;
import org.university.deanery.models.Group;
import org.university.deanery.models.Timetable;
import org.university.deanery.models.enums.DayOfWeek;
import org.university.deanery.models.enums.TimeOfClass;
import org.university.deanery.services.*;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/timetables")
@Slf4j
public class TimetableController {
    private final TimetableService timetableService;
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final TeacherService teacherService;
    private final ClassroomService classroomService;
    private final EmailSenderService emailSenderService;

    @Autowired
    public TimetableController(TimetableService timetableService, GroupService groupService,
                               SubjectService subjectService, TeacherService teacherService,
                               ClassroomService classroomService, EmailSenderService emailSenderService) {
        this.timetableService = timetableService;
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.teacherService = teacherService;
        this.classroomService = classroomService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping
    public String findAll(@RequestParam(required = false, defaultValue = "0") int page,
                          @RequestParam(required = false, defaultValue = "10") int size,
                          Model model) {
        Page<Timetable> timetablePage = timetableService.findAll(PageRequest.of(page, size));
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
        model.addAttribute("timetables", timetablePage
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
        return "/timetables/admin/find-by-id";
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

    @GetMapping("/student/find-all")
    public String studentTimetableFindAll(@RequestParam(required = false, defaultValue = "0") int page,
                                          @RequestParam(required = false, defaultValue = "10") int size,
                                          Model model) {
        Page<Group> groupPage = groupService.findAll(PageRequest.of(page, size));
        String error = (String) model.getAttribute("error");
        if (error != null)
            model.addAttribute("error", error);
        model.addAttribute("groups", groupPage);
        return "timetables/student/find-all";
    }

    @GetMapping("/student/find-all/{id}")
    public String studentTimetableFindById(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        String message;
        String success = (String) model.getAttribute("success");
        String error = (String) model.getAttribute("error");
        if (success != null)
            model.addAttribute("success", success);
        if (error != null)
            model.addAttribute("error", error);

        try {
            Group group = groupService
                    .findById(id)
                    .orElseThrow(GroupNotFoundException::new);

            List<Timetable> timetables
                    = timetableService
                    .findAllByGroup(group)
                    .orElseThrow(TimetableNotFoundException::new);

            model.addAttribute("timetables", timetables);
        } catch (TimetableNotFoundException e) {
            message = "Расписание не найдено!";
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/timetables/student/find-all";
        } catch (GroupNotFoundException e) {
            message = "Группа с id: " + id + " не найдена!";
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/timetables/student/find-all";
        }
        return "timetables/student/find-by-id";
    }

    @PostMapping("/student/find-all/{id}/generate-pdf")
    public String generatePdf(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String message;
        try {
            Group group = groupService
                    .findById(id)
                    .orElseThrow(GroupNotFoundException::new);

            List<Timetable> timetables
                    = timetableService
                    .findAllByGroup(group)
                    .orElseThrow(TimetableNotFoundException::new);

            timetableService.generatePdfTimetable(timetables);
            message = "Отчет успешно сгенерирован!";
            redirectAttributes.addFlashAttribute("success", message);
        } catch (TimetableNotFoundException e) {
            message = "Расписание не найдено!";
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/timetables/student/find-all";
        } catch (GroupNotFoundException e) {
            message = "Группа с id: " + id + " не найдена!";
            redirectAttributes.addFlashAttribute("error", message);
            return "redirect:/timetables/student/find-all";
        }
        return "redirect:/timetables/student/find-all/" + id;
    }

    @PostMapping("/student/find-all/{id}/send-email")
    public String sendEmail(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String message;
        try {
            emailSenderService.sendEmailWithAttachment(EmailSenderService.fromAddress, "Report", "", EmailSenderService.attachment);
        } catch (MessagingException | FileNotFoundException e) {
            log.info(e.getMessage());
        }
        message = "Отчет успешно отправлен!";
        redirectAttributes.addFlashAttribute("success", message);
        return "redirect:/timetables/student/find-all/" + id;
    }
}
