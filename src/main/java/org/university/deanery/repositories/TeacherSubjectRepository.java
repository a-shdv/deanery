package org.university.deanery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.university.deanery.models.TeacherSubject;

@Repository
public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Long> {
}
