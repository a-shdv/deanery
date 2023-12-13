package org.university.deanery.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.university.deanery.dtos.GroupDto;
import org.university.deanery.exceptions.ClassroomNotFoundException;
import org.university.deanery.exceptions.GroupNotFoundException;
import org.university.deanery.models.Classroom;
import org.university.deanery.models.Group;
import org.university.deanery.models.User;
import org.university.deanery.repositories.GroupRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void save(Group group) {
        groupRepository.save(group);
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Optional<Group> findById(Long id) {
        return Optional.ofNullable(groupRepository.findById(id)).get();
    }

    public Optional<Group> findGroupByTitle(String title) {
        return groupRepository.findGroupByTitle(title);
    }

    public void updateById(Long id, GroupDto groupDto) {
        Group group = groupRepository.findById(id).get();
        group.setTitle(groupDto.getTitle());
        groupRepository.save(group);
    }

    public void deleteById(Long id) {
        groupRepository.deleteById(id);
    }
}
