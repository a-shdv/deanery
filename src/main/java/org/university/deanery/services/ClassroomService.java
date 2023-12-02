package org.university.deanery.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.university.deanery.dtos.ClassroomDto;
import org.university.deanery.exceptions.ClassroomNotFoundException;
import org.university.deanery.models.Classroom;
import org.university.deanery.repositories.ClassroomRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClassroomService {
    private final ClassroomRepository classroomRepository;

    @Autowired
    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public List<Classroom> findAll() {
        return classroomRepository.findAll();
    }

    public Optional<Classroom> findById(Long id) throws ClassroomNotFoundException {
        return Optional.ofNullable(classroomRepository.findById(id)).orElseThrow(ClassroomNotFoundException::new);
    }

    public Optional<Classroom> findClassroomByClassroomNo(Integer classroomNo) {
        return classroomRepository.findClassroomByClassroomNo(classroomNo);
    }

    public void save(Classroom classroom) {
        classroomRepository.save(classroom);
    }

    public void updateById(Long id, ClassroomDto classroomDto) throws ClassroomNotFoundException {
        Optional<Classroom> classroom = Optional.ofNullable(classroomRepository.findById(id).orElseThrow(() -> new ClassroomNotFoundException()));
        classroom.get().setClassroomNo(classroomDto.getClassroomNo());
        classroomRepository.save(classroom.get());
    }

    public void delete(Classroom classroom) {
        classroomRepository.delete(classroom);
    }
}
