package com.example.todoapi.service.impl;

import com.example.todoapi.dto.TodoDTO;
import com.example.todoapi.entity.Todo;
import com.example.todoapi.repository.TodoRepository;
import com.example.todoapi.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoServiceImpl implements TodoService {
    
    private final TodoRepository todoRepository;
    
    @Override
    public List<TodoDTO> getAllTodos() {
        return todoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public TodoDTO getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        return convertToDTO(todo);
    }
    
    @Override
    public TodoDTO createTodo(TodoDTO todoDTO) {
        Todo todo = new Todo();
        todo.setTitle(todoDTO.getTitle());
        todo.setDescription(todoDTO.getDescription());
        todo.setCompleted(false);
        
        Todo savedTodo = todoRepository.save(todo);
        return convertToDTO(savedTodo);
    }
    
    @Override
    public TodoDTO updateTodo(Long id, TodoDTO todoDTO) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        
        todo.setTitle(todoDTO.getTitle());
        todo.setDescription(todoDTO.getDescription());
        todo.setCompleted(todoDTO.getCompleted());
        
        Todo updatedTodo = todoRepository.save(todo);
        return convertToDTO(updatedTodo);
    }
    
    @Override
    public void deleteTodo(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("Todo not found with id: " + id);
        }
        todoRepository.deleteById(id);
    }
    
    @Override
    public List<TodoDTO> getTodosByCompleted(Boolean completed) {
        return todoRepository.findByCompleted(completed).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private TodoDTO convertToDTO(Todo todo) {
        TodoDTO dto = new TodoDTO();
        dto.setId(todo.getId());
        dto.setTitle(todo.getTitle());
        dto.setDescription(todo.getDescription());
        dto.setCompleted(todo.getCompleted());
        dto.setCreatedAt(todo.getCreatedAt() != null ? todo.getCreatedAt().toString() : null);
        dto.setUpdatedAt(todo.getUpdatedAt() != null ? todo.getUpdatedAt().toString() : null);
        return dto;
    }
} 