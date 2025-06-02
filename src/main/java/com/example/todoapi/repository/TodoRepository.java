package com.example.todoapi.repository;

import com.example.todoapi.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // 继承 JpaRepository 后自动获得基本的 CRUD 操作
    // 可以根据需要添加自定义查询方法
    List<Todo> findByCompleted(Boolean completed);
    List<Todo> findByTitleContainingIgnoreCase(String title);
} 