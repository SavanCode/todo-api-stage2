package com.example.todoapi.repository;

import com.example.todoapi.entity.Todo;
import com.example.todoapi.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TodoRepository todoRepository;

    private User testUser;
    private Todo testTodo;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser = entityManager.persistAndFlush(testUser);

        testTodo = new Todo();
        testTodo.setTitle("Test Todo");
        testTodo.setDescription("Test Description");
        testTodo.setCompleted(false);
        testTodo.setUser(testUser);
    }

    @Test
    void save_shouldPersistTodo() {
        // Act
        Todo savedTodo = todoRepository.save(testTodo);

        // Assert
        assertNotNull(savedTodo.getId());
        assertEquals("Test Todo", savedTodo.getTitle());
        assertEquals("Test Description", savedTodo.getDescription());
        assertFalse(savedTodo.getCompleted());
        assertEquals(testUser.getId(), savedTodo.getUser().getId());
        assertNotNull(savedTodo.getCreatedAt());
        assertNotNull(savedTodo.getUpdatedAt());
    }

    @Test
    void findByUser_shouldReturnUserTodos() {
        // Arrange
        Todo savedTodo = entityManager.persistAndFlush(testTodo);

        Todo todo2 = new Todo();
        todo2.setTitle("Another Todo");
        todo2.setDescription("Another Description");
        todo2.setCompleted(true);
        todo2.setUser(testUser);
        entityManager.persistAndFlush(todo2);

        // Act
        List<Todo> userTodos = todoRepository.findByUser(testUser);

        // Assert
        assertEquals(2, userTodos.size());
        assertTrue(userTodos.stream().anyMatch(t -> "Test Todo".equals(t.getTitle())));
        assertTrue(userTodos.stream().anyMatch(t -> "Another Todo".equals(t.getTitle())));
    }

    @Test
    void findByUserAndCompleted_shouldReturnFilteredTodos() {
        // Arrange
        Todo completedTodo = new Todo();
        completedTodo.setTitle("Completed Todo");
        completedTodo.setDescription("Completed Description");
        completedTodo.setCompleted(true);
        completedTodo.setUser(testUser);
        entityManager.persistAndFlush(completedTodo);

        Todo incompleteTodo = new Todo();
        incompleteTodo.setTitle("Incomplete Todo");
        incompleteTodo.setDescription("Incomplete Description");
        incompleteTodo.setCompleted(false);
        incompleteTodo.setUser(testUser);
        entityManager.persistAndFlush(incompleteTodo);

        // Act
        List<Todo> completedTodos = todoRepository.findByUserAndCompleted(testUser, true);
        List<Todo> incompleteTodos = todoRepository.findByUserAndCompleted(testUser, false);

        // Assert
        assertEquals(1, completedTodos.size());
        assertEquals("Completed Todo", completedTodos.get(0).getTitle());
        assertEquals(1, incompleteTodos.size());
        assertEquals("Incomplete Todo", incompleteTodos.get(0).getTitle());
    }

    @Test
    void findByIdAndUser_shouldReturnTodo_whenTodoExists() {
        // Arrange
        Todo savedTodo = entityManager.persistAndFlush(testTodo);

        // Act
        Optional<Todo> foundTodo = todoRepository.findByIdAndUser(savedTodo.getId(), testUser);

        // Assert
        assertTrue(foundTodo.isPresent());
        assertEquals(savedTodo.getId(), foundTodo.get().getId());
        assertEquals("Test Todo", foundTodo.get().getTitle());
    }

    @Test
    void findByIdAndUser_shouldReturnEmpty_whenTodoDoesNotExist() {
        // Act
        Optional<Todo> foundTodo = todoRepository.findByIdAndUser(999L, testUser);

        // Assert
        assertFalse(foundTodo.isPresent());
    }

    @Test
    void findByIdAndUser_shouldReturnEmpty_whenTodoBelongsToDifferentUser() {
        // Arrange
        User otherUser = new User();
        otherUser.setUsername("otheruser");
        otherUser.setEmail("other@example.com");
        otherUser.setPassword("password");
        otherUser = entityManager.persistAndFlush(otherUser);

        Todo savedTodo = entityManager.persistAndFlush(testTodo);

        // Act
        Optional<Todo> foundTodo = todoRepository.findByIdAndUser(savedTodo.getId(), otherUser);

        // Assert
        assertFalse(foundTodo.isPresent());
    }

    @Test
    void findByUserWithPagination_shouldReturnPaginatedTodos() {
        // Arrange
        for (int i = 1; i <= 5; i++) {
            Todo todo = new Todo();
            todo.setTitle("Todo " + i);
            todo.setDescription("Description " + i);
            todo.setCompleted(false);
            todo.setUser(testUser);
            entityManager.persistAndFlush(todo);
        }

        Pageable pageable = PageRequest.of(0, 3);

        // Act
        Page<Todo> todoPage = todoRepository.findByUser(testUser, pageable);

        // Assert
        assertEquals(5, todoPage.getTotalElements());
        assertEquals(3, todoPage.getContent().size());
        assertEquals(2, todoPage.getTotalPages());
    }

    @Test
    void findByUserAndCompletedWithPagination_shouldReturnPaginatedFilteredTodos() {
        // Arrange
        for (int i = 1; i <= 3; i++) {
            Todo completedTodo = new Todo();
            completedTodo.setTitle("Completed Todo " + i);
            completedTodo.setDescription("Completed Description " + i);
            completedTodo.setCompleted(true);
            completedTodo.setUser(testUser);
            entityManager.persistAndFlush(completedTodo);
        }

        for (int i = 1; i <= 2; i++) {
            Todo incompleteTodo = new Todo();
            incompleteTodo.setTitle("Incomplete Todo " + i);
            incompleteTodo.setDescription("Incomplete Description " + i);
            incompleteTodo.setCompleted(false);
            incompleteTodo.setUser(testUser);
            entityManager.persistAndFlush(incompleteTodo);
        }

        Pageable pageable = PageRequest.of(0, 2);

        // Act
        Page<Todo> completedTodosPage = todoRepository.findByUserAndCompleted(testUser, true, pageable);

        // Assert
        assertEquals(3, completedTodosPage.getTotalElements());
        assertEquals(2, completedTodosPage.getContent().size());
        assertEquals(2, completedTodosPage.getTotalPages());
    }

    @Test
    void searchTodosByUser_shouldReturnSearchResults() {
        // Arrange
        Todo todo1 = new Todo();
        todo1.setTitle("Project Meeting");
        todo1.setDescription("Discuss project timeline");
        todo1.setCompleted(false);
        todo1.setUser(testUser);
        entityManager.persistAndFlush(todo1);

        Todo todo2 = new Todo();
        todo2.setTitle("Code Review");
        todo2.setDescription("Review pull request");
        todo2.setCompleted(true);
        todo2.setUser(testUser);
        entityManager.persistAndFlush(todo2);

        Todo todo3 = new Todo();
        todo3.setTitle("Documentation");
        todo3.setDescription("Update API docs");
        todo3.setCompleted(false);
        todo3.setUser(testUser);
        entityManager.persistAndFlush(todo3);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Todo> searchResults = todoRepository.searchTodosByUser(testUser, "project", null, pageable);

        // Assert
        assertEquals(1, searchResults.getTotalElements());
        assertEquals("Project Meeting", searchResults.getContent().get(0).getTitle());
    }

    @Test
    void searchTodosByUser_shouldFilterByCompletedStatus() {
        // Arrange
        Todo completedTodo = new Todo();
        completedTodo.setTitle("Completed Task");
        completedTodo.setDescription("This is completed");
        completedTodo.setCompleted(true);
        completedTodo.setUser(testUser);
        entityManager.persistAndFlush(completedTodo);

        Todo incompleteTodo = new Todo();
        incompleteTodo.setTitle("Incomplete Task");
        incompleteTodo.setDescription("This is incomplete");
        incompleteTodo.setCompleted(false);
        incompleteTodo.setUser(testUser);
        entityManager.persistAndFlush(incompleteTodo);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Todo> completedResults = todoRepository.searchTodosByUser(testUser, "task", true, pageable);
        Page<Todo> incompleteResults = todoRepository.searchTodosByUser(testUser, "task", false, pageable);

        // Assert
        assertEquals(1, completedResults.getTotalElements());
        assertEquals("Completed Task", completedResults.getContent().get(0).getTitle());
        assertEquals(1, incompleteResults.getTotalElements());
        assertEquals("Incomplete Task", incompleteResults.getContent().get(0).getTitle());
    }

    @Test
    void delete_shouldRemoveTodo() {
        // Arrange
        Todo savedTodo = entityManager.persistAndFlush(testTodo);

        // Act
        todoRepository.delete(savedTodo);

        // Assert
        Optional<Todo> foundTodo = todoRepository.findById(savedTodo.getId());
        assertFalse(foundTodo.isPresent());
    }

    @Test
    void findById_shouldReturnTodo_whenTodoExists() {
        // Arrange
        Todo savedTodo = entityManager.persistAndFlush(testTodo);

        // Act
        Optional<Todo> foundTodo = todoRepository.findById(savedTodo.getId());

        // Assert
        assertTrue(foundTodo.isPresent());
        assertEquals(savedTodo.getId(), foundTodo.get().getId());
    }

    @Test
    void findById_shouldReturnEmpty_whenTodoDoesNotExist() {
        // Act
        Optional<Todo> foundTodo = todoRepository.findById(999L);

        // Assert
        assertFalse(foundTodo.isPresent());
    }
} 