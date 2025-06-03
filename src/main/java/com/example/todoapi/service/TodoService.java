package com.example.todoapi.service;

import com.example.todoapi.dto.TodoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 待办事项服务接口
 */
public interface TodoService {
    /**
     * 获取所有待办事项
     * @return 待办事项列表
     */
    List<TodoDTO> getAllTodos();

    /**
     * 根据ID获取待办事项
     * @param id 待办事项ID
     * @return 待办事项详情
     */
    TodoDTO getTodoById(Long id);

    /**
     * 创建新的待办事项
     * @param todoDTO 待办事项数据
     * @return 创建后的待办事项详情
     */
    TodoDTO createTodo(TodoDTO todoDTO);

    /**
     * 更新待办事项
     * @param id 待办事项ID
     * @param todoDTO 更新的待办事项数据
     * @return 更新后的待办事项详情
     */
    TodoDTO updateTodo(Long id, TodoDTO todoDTO);

    /**
     * 删除待办事项
     * @param id 待办事项ID
     */
    void deleteTodo(Long id);

    /**
     * 根据完成状态获取待办事项列表
     * @param completed 完成状态
     * @return 待办事项列表
     */
    List<TodoDTO> getTodosByCompleted(Boolean completed);

    /**
     * 分页获取所有待办事项
     * @param pageable 分页参数
     * @return 分页后的待办事项列表
     */
    Page<TodoDTO> getTodosWithPagination(Pageable pageable);

    /**
     * 搜索待办事项（支持分页）
     * @param keyword 搜索关键词
     * @param completed 完成状态
     * @param pageable 分页参数
     * @return 分页后的搜索结果
     */
    Page<TodoDTO> searchTodos(String keyword, Boolean completed, Pageable pageable);

    /**
     * 根据完成状态分页获取待办事项
     * @param completed 完成状态
     * @param pageable 分页参数
     * @return 分页后的待办事项列表
     */
    Page<TodoDTO> getTodosByCompletedWithPagination(Boolean completed, Pageable pageable);
}