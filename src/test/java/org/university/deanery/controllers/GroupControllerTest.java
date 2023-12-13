package org.university.deanery.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.university.deanery.dtos.GroupDto;
import org.university.deanery.exceptions.GroupNotFoundException;
import org.university.deanery.models.Group;
import org.university.deanery.models.User;
import org.university.deanery.services.GroupService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class GroupControllerTest {
    @Mock
    private GroupService groupService;
    @InjectMocks
    private GroupController groupController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    public void testSaveNewGroup() {
        // Arrange
        GroupDto groupDto = new GroupDto("new-group", null);
        RedirectAttributes model = mock(RedirectAttributes.class);

        when(groupService.findGroupByTitle(groupDto.getTitle())).thenReturn(Optional.empty());

        // Act
        String result = groupController.save(groupDto, model);

        // Assert
        assertEquals("groups/find-all", result);
        verify(groupService, times(1)).save(any(Group.class));
        verify(model, times(1)).addAttribute(eq("success"), anyString());
        verify(model, times(1)).addAttribute(eq("groups"), any());
    }

    @Test
    public void testSaveExistingGroup() {
        // Arrange
        GroupDto groupDto = new GroupDto("ExistingGroup", null);
        RedirectAttributes model = mock(RedirectAttributes.class);

        when(groupService.findGroupByTitle(groupDto.getTitle())).thenReturn(Optional.of(new Group()));

        // Act
        String result = groupController.save(groupDto, model);

        // Assert
        assertEquals("groups/find-all", result);
        verify(model, times(1)).addAttribute(eq("error"), anyString());
        verify(model, times(1)).addAttribute(eq("groups"), any());
    }

    @Test
    void testFindAll() throws Exception {
        mockMvc.perform(get("/groups")).andExpect(status().isOk());
        verify(groupService, times(1)).findAll();
    }

    @Test
    public void testFindByIdValidGroup() throws GroupNotFoundException {
        // Arrange
        Long groupId = 1L;
        Model model = mock(Model.class);
        Group group = new Group();

        when(groupService.findById(groupId)).thenReturn(Optional.of(group));

        // Act
        String result = groupController.findById(groupId, model);

        // Assert
        assertEquals("groups/find-by-id", result);
        verify(model, times(1)).addAttribute(eq("group"), eq(group));
    }

    @Test
    public void testFindByIdException() throws GroupNotFoundException {
        // Arrange
        Long groupId = 2L;
        Model model = mock(Model.class);

        when(groupService.findById(groupId)).thenThrow(new GroupNotFoundException("Group not found"));

        // Act
        String result = groupController.findById(groupId, model);

        // Assert
        assertEquals("groups/find-by-id", result);
        verify(model, times(1)).addAttribute(eq("error"), anyString());
    }

    @Test
    public void testUpdateByIdValidUpdate() throws GroupNotFoundException {
        // Arrange
        Long groupId = 1L;
        GroupDto groupDto = new GroupDto("UpdatedGroup", null); // Create a sample GroupDto object for testing

        Model model = mock(Model.class);
        Group existingGroup = new Group(); // Create a sample existing Group object for testing

        when(groupService.findById(groupId)).thenReturn(Optional.of(existingGroup));
        when(groupService.findGroupByTitle(groupDto.getTitle())).thenReturn(Optional.empty());

        // Act
        String result = groupController.updateById(groupId, groupDto, model);

        // Assert
        assertEquals("redirect:/groups", result);
        verify(groupService, times(1)).updateById(eq(groupId), eq(groupDto));
        verify(model, times(1)).addAttribute(eq("success"), anyString());
        verify(model, times(1)).addAttribute(eq("groups"), any());
    }

    @Test
    public void testUpdateByIdGroupNotFound() throws GroupNotFoundException {
        // Arrange
        Long groupId = 2L;
        GroupDto groupDto = new GroupDto("NewGroup", null); // Create a sample GroupDto object for testing

        Model model = mock(Model.class);

        when(groupService.findById(groupId)).thenReturn(Optional.empty());

        // Act
        String result = groupController.updateById(groupId, groupDto, model);

        // Assert
        assertEquals("redirect:/groups", result);
        verify(model, times(1)).addAttribute(eq("error"), anyString());
        verify(model, times(1)).addAttribute(eq("groups"), any());
    }


    @Test
    public void testUpdateByIdGroupAlreadyExists() throws GroupNotFoundException {
        // Arrange
        Long groupId = 3L;
        GroupDto groupDto = new GroupDto("ExistingGroup", null); // Create a sample GroupDto object for testing

        Model model = mock(Model.class);
        Group existingGroup = new Group(); // Create a sample existing Group object for testing

        when(groupService.findById(groupId)).thenReturn(Optional.of(existingGroup));
        when(groupService.findGroupByTitle(groupDto.getTitle())).thenReturn(Optional.of(existingGroup));

        // Act
        String result = groupController.updateById(groupId, groupDto, model);

        // Assert
        assertEquals("redirect:/groups", result);
        verify(model, times(1)).addAttribute(eq("error"), anyString());
        verify(model, times(1)).addAttribute(eq("groups"), any());
    }

    @Test
    public void testDeleteByIdValidDelete() throws GroupNotFoundException {
        // Arrange
        Long groupId = 1L;

        RedirectAttributes model = mock(RedirectAttributes.class);
        Group existingGroup = new Group(); // Create a sample existing Group object for testing

        when(groupService.findById(groupId)).thenReturn(Optional.of(existingGroup));

        // Act
        String result = groupController.deleteById(groupId, model);

        // Assert
        assertEquals("redirect:/groups", result);
        verify(groupService, times(1)).deleteById(eq(groupId));
        verify(model, times(1)).addAttribute(eq("success"), anyString());
        verify(model, times(1)).addAttribute(eq("groups"), any());
    }

    @Test
    public void testDeleteByIdGroupNotFound() throws GroupNotFoundException {
        // Arrange
        Long groupId = 2L;

        RedirectAttributes model = mock(RedirectAttributes.class);

        when(groupService.findById(groupId)).thenReturn(Optional.empty());

        // Act
        String result = groupController.deleteById(groupId, model);

        // Assert
        assertEquals("redirect:/groups", result);
        verify(model, times(1)).addAttribute(eq("error"), anyString());
        verify(model, times(1)).addAttribute(eq("groups"), any());
    }
}

