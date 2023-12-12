package org.university.deanery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.university.deanery.models.Subject;
import org.university.deanery.models.Teacher;
import org.university.deanery.models.TeacherSubject;

import java.util.Optional;

@Repository
public interface TeacherSubjectRepository extends JpaRepository<TeacherSubject, Long> {
    Optional<TeacherSubject> findTeacherSubjectByTeacherAndSubject(Teacher teacher, Subject subject);
}
