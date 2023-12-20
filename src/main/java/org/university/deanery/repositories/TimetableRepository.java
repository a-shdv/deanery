package org.university.deanery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.university.deanery.models.Group;
import org.university.deanery.models.Timetable;
import org.university.deanery.models.enums.DayOfWeek;
import org.university.deanery.models.enums.TimeOfClass;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    Optional<Timetable> findTimetableByClassroom_IdAndDayOfWeekAndTimeOfClass(Long classroomId, DayOfWeek dayOfWeek, TimeOfClass timeOfClass);
    Optional<List<Timetable>> findAllByGroup(Group group);
}
