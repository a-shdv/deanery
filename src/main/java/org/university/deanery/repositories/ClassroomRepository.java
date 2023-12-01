package org.university.deanery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.university.deanery.models.Classroom;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}
