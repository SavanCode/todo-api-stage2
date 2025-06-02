package com.example.todoapi.service;

import com.example.todoapi.dto.TodoDTO;
import java.util.List;

public interface TodoService {
    List<TodoDTO> getAllTodos();
    TodoDTO getTodoById(Long id);
    TodoDTO createTodo(TodoDTO todoDTO);
    TodoDTO updateTodo(Long id, TodoDTO todoDTO);
    void deleteTodo(Long id);
    List<TodoDTO> getTodosByCompleted(Boolean completed);
}