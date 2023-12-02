package org.university.deanery.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.university.deanery.dtos.GroupDto;
import org.university.deanery.exceptions.ClassroomNotFoundException;
import org.university.deanery.exceptions.GroupNotFoundException;
import org.university.deanery.models.Classroom;
import org.university.deanery.models.Group;
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

    public Optional<Group> findById(Long id) throws GroupNotFoundException {
        return Optional.ofNullable(groupRepository.findById(id)).orElseThrow(GroupNotFoundException::new);
    }

    public Optional<Group> findGroupByTitle(String title) {
        return groupRepository.findGroupByTitle(title);
    }

    public void updateById(Long id, GroupDto groupDto) throws GroupNotFoundException {
        Optional<Group> group = Optional.ofNullable(groupRepository.findById(id).orElseThrow(GroupNotFoundException::new));
        group.get().setTitle(groupDto.getTitle());
        groupRepository.save(group.get());
    }

    public void deleteById(Long id) {
        groupRepository.deleteById(id);
    }
}
