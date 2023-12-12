package org.university.deanery.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.university.deanery.models.Subject;
import org.university.deanery.models.Teacher;
import org.university.deanery.models.TeacherSubject;
import org.university.deanery.repositories.TeacherSubjectRepository;

import java.util.Optional;

@Service
public class TeacherSubjectService {
    private final TeacherSubjectRepository teacherSubjectRepository;

    @Autowired
    public TeacherSubjectService(TeacherSubjectRepository teacherSubjectRepository) {
        this.teacherSubjectRepository = teacherSubjectRepository;
    }

    public void addTeacherSubject(TeacherSubject teacherSubject) {
        teacherSubjectRepository.save(teacherSubject);
    }

    public Optional<TeacherSubject> findTeacherSubjectByTeacherAndSubject(Teacher teacher, Subject subject) {
        return Optional.ofNullable(teacherSubjectRepository.findTeacherSubjectByTeacherAndSubject(teacher, subject)).get();
    }

    public void deleteTeacherSubject(TeacherSubject teacherSubject) {
        teacherSubjectRepository.delete(teacherSubject);
    }
}
