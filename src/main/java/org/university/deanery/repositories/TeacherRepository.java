package org.university.deanery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.university.deanery.models.Teacher;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findTeacherByLastNameAndFirstNameAndPatronymicName(String lastName, String firstName, String patronymicName);
}
