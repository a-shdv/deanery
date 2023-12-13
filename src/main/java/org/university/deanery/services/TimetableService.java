package org.university.deanery.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.university.deanery.models.Timetable;
import org.university.deanery.repositories.TimetableRepository;

import java.util.List;

@Service
public class TimetableService {
    private final TimetableRepository timetableRepository;

    @Autowired
    public TimetableService(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    public void save(Timetable timetable) {
        timetableRepository.save(timetable);
    }

    public List<Timetable> findAll() {
        return timetableRepository.findAll();
    }
}
