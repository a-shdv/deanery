package org.university.deanery.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.university.deanery.dtos.SubjectDto;
import org.university.deanery.models.Subject;
import org.university.deanery.repositories.SubjectRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public void save(Subject subject) {
        subjectRepository.save(subject);
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Page<Subject> findAll(PageRequest pageRequest) {
        return subjectRepository.findAll(pageRequest);
    }

    public Optional<Subject> findSubjectByTitle(String title) {
        return Optional.ofNullable(subjectRepository.findSubjectByTitle(title)).get();
    }

    public Optional<Subject> findById(Long id) {
        return Optional.ofNullable(subjectRepository.findById(id)).get();
    }

    public void updateById(Long id, SubjectDto subjectDto) {
        Subject subject = subjectRepository.findById(id).get();
        subject.setTitle(subjectDto.getTitle());
        subjectRepository.save(subject);
    }

    public void deleteById(Long id) {
        subjectRepository.deleteById(id);
    }
}
