package com.example.todoapi.repository;

import com.example.todoapi.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // 继承 JpaRepository 后自动获得基本的 CRUD 操作
    // 可以根据需要添加自定义查询方法
    List<Todo> findByCompleted(Boolean completed);
    List<Todo> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT t FROM Todo t WHERE " +
           "(:keyword IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:completed IS NULL OR t.completed = :completed)")
    Page<Todo> searchTodos(@Param("keyword") String keyword, 
                          @Param("completed") Boolean completed, 
                          Pageable pageable);
                          
    Page<Todo> findByCompleted(Boolean completed, Pageable pageable);
    Page<Todo> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Todo> findByTitleContainingIgnoreCaseAndCompleted(
        String title, Boolean completed, Pageable pageable);
} 