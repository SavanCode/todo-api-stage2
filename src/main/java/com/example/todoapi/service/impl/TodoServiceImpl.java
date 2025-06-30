package com.example.todoapi.service.impl;

import com.example.todoapi.dto.TodoDTO;
import com.example.todoapi.entity.Todo;
import com.example.todoapi.entity.User;
import com.example.todoapi.exception.UnauthorizedException;
import com.example.todoapi.repository.TodoRepository;
import com.example.todoapi.security.SecurityUtils;
import com.example.todoapi.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TodoServiceImpl implements TodoService {
    
    private final TodoRepository todoRepository;
    private final SecurityUtils securityUtils;
    
    public TodoServiceImpl(TodoRepository todoRepository, SecurityUtils securityUtils) {
        this.todoRepository = todoRepository;
        this.securityUtils = securityUtils;
    }
    
    @Override
    public List<TodoDTO> getAllTodos() {
        User currentUser = getCurrentUser();
        return todoRepository.findByUser(currentUser).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public TodoDTO getTodoById(Long id) {
        User currentUser = getCurrentUser();
        Todo todo = todoRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        return convertToDTO(todo);
    }
    
    @Override
    public TodoDTO createTodo(TodoDTO todoDTO) {
        User currentUser = getCurrentUser();
        Todo todo = new Todo();
        todo.setTitle(todoDTO.getTitle());
        todo.setDescription(todoDTO.getDescription());
        todo.setCompleted(todoDTO.getCompleted() != null ? todoDTO.getCompleted() : false);
        todo.setUser(currentUser);
        
        Todo savedTodo = todoRepository.save(todo);
        return convertToDTO(savedTodo);
    }
    
    @Override
    public TodoDTO updateTodo(Long id, TodoDTO todoDTO) {
        User currentUser = getCurrentUser();
        Todo todo = todoRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        
        todo.setTitle(todoDTO.getTitle());
        todo.setDescription(todoDTO.getDescription());
        todo.setCompleted(todoDTO.getCompleted());
        
        Todo updatedTodo = todoRepository.save(todo);
        return convertToDTO(updatedTodo);
    }
    
    @Override
    public void deleteTodo(Long id) {
        User currentUser = getCurrentUser();
        Todo todo = todoRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        todoRepository.delete(todo);
    }
    
    @Override
    public List<TodoDTO> getTodosByCompleted(Boolean completed) {
        User currentUser = getCurrentUser();
        return todoRepository.findByUserAndCompleted(currentUser, completed).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<TodoDTO> getTodosWithPagination(Pageable pageable) {
        User currentUser = getCurrentUser();
        return todoRepository.findByUser(currentUser, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<TodoDTO> searchTodos(String keyword, Boolean completed, Pageable pageable) {
        User currentUser = getCurrentUser();
        return todoRepository.searchTodosByUser(currentUser, keyword, completed, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<TodoDTO> getTodosByCompletedWithPagination(Boolean completed, Pageable pageable) {
        User currentUser = getCurrentUser();
        return todoRepository.findByUserAndCompleted(currentUser, completed, pageable)
                .map(this::convertToDTO);
    }
    
    /**
     * 获取当前认证用户
     * @return 当前用户
     */
    private User getCurrentUser() {
        User currentUser = securityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new UnauthorizedException("User not authenticated");
        }
        return currentUser;
    }
    
    private TodoDTO convertToDTO(Todo todo) {
        TodoDTO dto = new TodoDTO();
        dto.setId(todo.getId());
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setCompleted(todo.getCompleted());
        dto.setCreatedAt(todo.getCreatedAt() != null ? todo.getCreatedAt().toString() : null);
        dto.setUpdatedAt(todo.getUpdatedAt() != null ? todo.getUpdatedAt().toString() : null);
        
        // 添加用户信息
        if (todo.getUser() != null) {
            dto.setUserId(todo.getUser().getId());
            dto.setUsername(todo.getUser().getUsername());
        }
        
        return dto;
    }
} 