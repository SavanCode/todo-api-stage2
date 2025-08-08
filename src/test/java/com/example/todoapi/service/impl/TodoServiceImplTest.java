package com.example.todoapi.service.impl;

import com.example.todoapi.dto.TodoDTO;
import com.example.todoapi.entity.Todo;
import com.example.todoapi.entity.User;
import com.example.todoapi.exception.UnauthorizedException;
import com.example.todoapi.repository.TodoRepository;
import com.example.todoapi.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private TodoServiceImpl todoService;

    private User testUser;
    private Todo testTodo;
    private TodoDTO testTodoDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testTodo = new Todo();
        testTodo.setId(1L);
        testTodo.setTitle("Test Todo");
        testTodo.setDescription("Test Description");
        testTodo.setCompleted(false);
        testTodo.setUser(testUser);
        testTodo.setCreatedAt(LocalDateTime.now());
        testTodo.setUpdatedAt(LocalDateTime.now());

        testTodoDTO = new TodoDTO();
        testTodoDTO.setTitle("Test Todo");
        testTodoDTO.setDescription("Test Description");
        testTodoDTO.setCompleted(false);
    }

    @Test
    void getAllTodos_shouldReturnUserTodos() {
        // Arrange
        List<Todo> todos = Arrays.asList(testTodo);
        when(securityUtils.getCurrentUser()).thenReturn(testUser);
        when(todoRepository.findByUser(testUser)).thenReturn(todos);

        // Act
        List<TodoDTO> result = todoService.getAllTodos();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Todo", result.get(0).getTitle());
        assertEquals(testUser.getId(), result.get(0).getUserId());
    }

    @Test
    void getAllTodos_shouldThrowException_whenUserNotAuthenticated() {
        // Arrange
        when(securityUtils.getCurrentUser()).thenReturn(null);

        // Act & Assert
        assertThrows(UnauthorizedException.class, () -> todoService.getAllTodos());
    }

    @Test
    void getTodoById_shouldReturnTodo_whenTodoExists() {
        // Arrange
        when(securityUtils.getCurrentUser()).thenReturn(testUser);
        when(todoRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(testTodo));

        // Act
        TodoDTO result = todoService.getTodoById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Todo", result.getTitle());
        assertEquals(testUser.getId(), result.getUserId());
    }

    @Test
    void getTodoById_shouldThrowException_whenTodoNotFound() {
        // Arrange
        when(securityUtils.getCurrentUser()).thenReturn(testUser);
        when(todoRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> todoService.getTodoById(1L));
        assertEquals("Todo not found with id: 1", exception.getMessage());
    }

    @Test
    void createTodo_shouldReturnCreatedTodo() {
        // Arrange
        when(securityUtils.getCurrentUser()).thenReturn(testUser);
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

        // Act
        TodoDTO result = todoService.createTodo(testTodoDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test Todo", result.getTitle());
        assertEquals(testUser.getId(), result.getUserId());
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void createTodo_shouldSetDefaultCompletedToFalse_whenNotProvided() {
        // Arrange
        testTodoDTO.setCompleted(null);
        when(securityUtils.getCurrentUser()).thenReturn(testUser);
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

        // Act
        TodoDTO result = todoService.createTodo(testTodoDTO);

        // Assert
        assertNotNull(result);
        verify(todoRepository).save(argThat(todo -> !todo.getCompleted()));
    }

    @Test
    void updateTodo_shouldReturnUpdatedTodo() {
        // Arrange
        TodoDTO updateDTO = new TodoDTO();
        updateDTO.setTitle("Updated Todo");
        updateDTO.setDescription("Updated Description");
        updateDTO.setCompleted(true);

        when(securityUtils.getCurrentUser()).thenReturn(testUser);
        when(todoRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(testTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

        // Act
        TodoDTO result = todoService.updateTodo(1L, updateDTO);

        // Assert
        assertNotNull(result);
        verify(todoRepository).save(argThat(todo -> 
            "Updated Todo".equals(todo.getTitle()) && 
            "Updated Description".equals(todo.getDescription()) && 
            todo.getCompleted()
        ));
    }

    @Test
    void updateTodo_shouldThrowException_whenTodoNotFound() {
        // Arrange
        when(securityUtils.getCurrentUser()).thenReturn(testUser);
        when(todoRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> todoService.updateTodo(1L, testTodoDTO));
        assertEquals("Todo not found with id: 1", exception.getMessage());
    }

    @Test
    void deleteTodo_shouldDeleteTodo_whenTodoExists() {
        // Arrange
        when(securityUtils.getCurrentUser()).thenReturn(testUser);
        when(todoRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(testTodo));

        // Act
        todoService.deleteTodo(1L);

        // Assert
        verify(todoRepository).delete(testTodo);
    }

    @Test
    void deleteTodo_shouldThrowException_whenTodoNotFound() {
        // Arrange
        when(securityUtils.getCurrentUser()).thenReturn(testUser);
        when(todoRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> todoService.deleteTodo(1L));
        assertEquals("Todo not found with id: 1", exception.getMessage());
    }

    @Test
    void getTodosByCompleted_shouldReturnFilteredTodos() {
        // Arrange
        List<Todo> completedTodos = Arrays.asList(testTodo);
        when(securityUtils.getCurrentUser()).thenReturn(testUser);
        when(todoRepository.findByUserAndCompleted(testUser, true)).thenReturn(completedTodos);

        // Act
        List<TodoDTO> result = todoService.getTodosByCompleted(true);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getTodosWithPagination_shouldReturnPaginatedTodos() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Todo> todos = Arrays.asList(testTodo);
        Page<Todo> todoPage = new PageImpl<>(todos, pageable, 1);
        
        when(securityUtils.getCurrentUser()).thenReturn(testUser);
        when(todoRepository.findByUser(testUser, pageable)).thenReturn(todoPage);

        // Act
        Page<TodoDTO> result = todoService.getTodosWithPagination(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
    }

    @Test
    void searchTodos_shouldReturnSearchResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Todo> todos = Arrays.asList(testTodo);
        Page<Todo> todoPage = new PageImpl<>(todos, pageable, 1);
        
        when(securityUtils.getCurrentUser()).thenReturn(testUser);
        when(todoRepository.searchTodosByUser(testUser, "test", null, pageable)).thenReturn(todoPage);

        // Act
        Page<TodoDTO> result = todoService.searchTodos("test", null, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getTodosByCompletedWithPagination_shouldReturnPaginatedFilteredTodos() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Todo> todos = Arrays.asList(testTodo);
        Page<Todo> todoPage = new PageImpl<>(todos, pageable, 1);
        
        when(securityUtils.getCurrentUser()).thenReturn(testUser);
        when(todoRepository.findByUserAndCompleted(testUser, true, pageable)).thenReturn(todoPage);

        // Act
        Page<TodoDTO> result = todoService.getTodosByCompletedWithPagination(true, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
} 