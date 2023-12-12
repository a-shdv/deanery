package org.university.deanery.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.university.deanery.dtos.TeacherDto;
import org.university.deanery.models.Subject;
import org.university.deanery.models.Teacher;
import org.university.deanery.models.TeacherSubject;
import org.university.deanery.repositories.TeacherRepository;
import org.university.deanery.repositories.TeacherSubjectRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final TeacherSubjectRepository teacherSubjectRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, TeacherSubjectRepository teacherSubjectRepository) {
        this.teacherRepository = teacherRepository;
        this.teacherSubjectRepository = teacherSubjectRepository;
    }

    public void save(Teacher teacher) {
        teacher.setFirstName(capitalizeFirstLetter(teacher.getFirstName()));
        teacher.setLastName(capitalizeFirstLetter(teacher.getLastName()));
        teacher.setPatronymicName(capitalizeFirstLetter(teacher.getPatronymicName()));
        teacherRepository.save(teacher);
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> findById(Long id) {
        return Optional.ofNullable(teacherRepository.findById(id)).get();
    }

    public Optional<Teacher> findTeacherByLastNameAndFirstNameAndPatronymicName(String lastName, String firstName, String patronymicName) {
        return Optional.ofNullable(teacherRepository.findTeacherByLastNameAndFirstNameAndPatronymicName(lastName, firstName, patronymicName)).get();
    }

    public void updateById(Long id, TeacherDto teacherDto) {
        Teacher teacher = teacherRepository.findById(id).get();
        teacher.setFirstName(capitalizeFirstLetter(teacherDto.getFirstName()));
        teacher.setLastName(capitalizeFirstLetter(teacherDto.getLastName()));
        teacher.setPatronymicName(capitalizeFirstLetter(teacherDto.getPatronymicName()));
        teacherRepository.save(teacher);
    }

    public void deleteById(Long id) {
        teacherRepository.deleteById(id);
    }

    public void addTeacherSubject(TeacherSubject teacherSubject) {
        teacherSubjectRepository.save(teacherSubject);
    }

    public List<TeacherSubject> findAllTeacherSubjects(Teacher teacher) {
        return teacher.getTeacherSubjects();
    }

    private String capitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
