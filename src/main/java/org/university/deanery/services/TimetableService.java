package org.university.deanery.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.university.deanery.dtos.TimetableDto;
import org.university.deanery.models.*;
import org.university.deanery.models.enums.DayOfWeek;
import org.university.deanery.models.enums.TimeOfClass;
import org.university.deanery.repositories.TimetableRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Service
public class TimetableService {
    private final TimetableRepository timetableRepository;

    @Autowired
    public TimetableService(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    public void save(Timetable timetable) {
        timetableRepository.save(timetable);
    }

    public List<Timetable> findAll() {
        return timetableRepository.findAll();
    }

    public Page<Timetable> findAll(PageRequest pageRequest) {
        return timetableRepository.findAll(pageRequest);
    }

    public Optional<Timetable> findById(Long id) {
        return Optional.ofNullable(timetableRepository.findById(id)).get();
    }

    public Optional<Timetable> findTimetableByClassroom_IdAndDayOfWeekAndTimeOfClass(Long classroomId,
                                                                                     DayOfWeek dayOfWeek,
                                                                                     TimeOfClass timeOfClass) {
        return Optional.ofNullable(timetableRepository
                .findTimetableByClassroom_IdAndDayOfWeekAndTimeOfClass(classroomId, dayOfWeek, timeOfClass)).get();
    }

    public Optional<List<Timetable>> findAllByGroup(Group group) {
        return timetableRepository.findAllByGroup(group);
    }

    public void updateById(Long id, TimetableDto timetableDto) {
        Timetable timetable = timetableRepository.findById(id).get();
        timetable.setGroup(timetableDto.getGroup());
        timetable.setClassroom(timetableDto.getClassroom());
        timetable.setTeacher(timetableDto.getTeacher());
        timetable.setSubject(timetableDto.getSubject());
        timetable.setDayOfWeek(DayOfWeek.toDayOfWeek(timetableDto.getDayOfWeekId()));
        timetable.setTimeOfClass(TimeOfClass.toTimeOfClass(timetableDto.getTimeOfClassId()));
    }

    public void delete(Timetable timetable) {
        timetableRepository.delete(timetable);
    }

    public void generatePdfTimetable(List<Timetable> timetables) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(EmailSenderService.attachment));
            PdfPTable table = new PdfPTable(5);
            Paragraph p = new Paragraph();
            Font fHeader = new Font();
            Font fBody = new Font();

            document.open();

            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(15);
            fHeader.setStyle(Font.BOLD);
            fHeader.setSize(12);

            p.add("Timetable");
            table.addCell(new PdfPCell(new Phrase("Day Of Week", fHeader)));
            table.addCell(new PdfPCell(new Phrase("Classroom", fHeader)));
            table.addCell(new PdfPCell(new Phrase("Subject", fHeader)));
            table.addCell(new PdfPCell(new Phrase("Teacher", fHeader)));
            table.addCell(new PdfPCell(new Phrase("Time", fHeader)));

            fBody.setStyle(Font.NORMAL);
            fBody.setSize(10);

            for (Timetable timetable : timetables) {
                Teacher teacher = timetable.getTeacher();
                DayOfWeek dayOfWeek = timetable.getDayOfWeek();
                Classroom classroom = timetable.getClassroom();
                Subject subject = timetable.getSubject();
                TimeOfClass timeOfClass = timetable.getTimeOfClass();

                table.addCell(new PdfPCell(new Phrase(localizeDayOfWeek(dayOfWeek), fBody))); // DayOfWeek
                table.addCell(new PdfPCell(new Phrase(classroom.getClassroomNo().toString(), fBody))); // Classroom
                table.addCell(new PdfPCell(new Phrase(subject.getTitle().toString(), fBody))); // Subject
                table.addCell(new PdfPCell(new Phrase(teacher.getLastName() + " " +
                                teacher.getFirstName().substring(0, 1) + "." +
                                teacher.getPatronymicName().substring(0, 1) + ".", fBody))); // Teacher
                table.addCell(new PdfPCell(new Phrase(localizeTimeOfClass(timeOfClass), fBody))); // TimeOfClass
            }

            document.add(p);
            document.add(table);
            document.close();
        } catch (DocumentException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String localizeDayOfWeek(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek.getCode()) {
            case 0 -> "Sunday";
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            default -> throw new IllegalStateException("Unexpected value: " + dayOfWeek.getCode());
        };
    }

    private String localizeTimeOfClass(TimeOfClass timeOfClass) {
        return switch(timeOfClass.getCode()) {
            case 0 -> "0: -";
            case 1 -> "1: 08:30-09:50";
            case 2 -> "2: 10:00-11:20";
            case 3 -> "3: 11:30-12:50";
            case 4 -> "4: 13:30-14:50";
            case 5 -> "5: 15:00-16:20";
            case 6 -> "6: 16:30-17:50";
            case 7 -> "7: 18:00-19:20";
            case 8 -> "8: 19:30-21:00";
            default -> throw new IllegalStateException("Unexpected value: " + timeOfClass.getCode());
        };
    }
}
