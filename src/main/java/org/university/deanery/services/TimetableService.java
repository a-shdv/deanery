package org.university.deanery.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.university.deanery.dtos.TimetableDto;
import org.university.deanery.models.Classroom;
import org.university.deanery.models.Timetable;
import org.university.deanery.models.enums.DayOfWeek;
import org.university.deanery.models.enums.TimeOfClass;
import org.university.deanery.repositories.TimetableRepository;

import javax.swing.text.html.Option;
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

    public Optional<Timetable> findById(Long id) {
        return Optional.ofNullable(timetableRepository.findById(id)).get();
    }

    public Optional<Timetable> findTimetableByClassroom_IdAndDayOfWeekAndTimeOfClass(Long classroomId,
                                                                                     DayOfWeek dayOfWeek,
                                                                                     TimeOfClass timeOfClass) {
        return Optional.ofNullable(timetableRepository
                .findTimetableByClassroom_IdAndDayOfWeekAndTimeOfClass(classroomId, dayOfWeek, timeOfClass)).get();
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
}
