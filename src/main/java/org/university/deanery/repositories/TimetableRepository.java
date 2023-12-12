package org.university.deanery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.university.deanery.models.Timetable;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
}
