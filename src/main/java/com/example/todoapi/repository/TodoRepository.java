package com.example.todoapi.repository;

import com.example.todoapi.entity.Todo;
import com.example.todoapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // 继承 JpaRepository 后自动获得基本的 CRUD 操作
    // 可以根据需要添加自定义查询方法
    
    // 用户相关的查询方法
    List<Todo> findByUser(User user);
    List<Todo> findByUserAndCompleted(User user, Boolean completed);
    Page<Todo> findByUser(User user, Pageable pageable);
    Page<Todo> findByUserAndCompleted(User user, Boolean completed, Pageable pageable);
    
    // 根据用户ID查询
    List<Todo> findByUserId(Long userId);
    List<Todo> findByUserIdAndCompleted(Long userId, Boolean completed);
    Page<Todo> findByUserId(Long userId, Pageable pageable);
    Page<Todo> findByUserIdAndCompleted(Long userId, Boolean completed, Pageable pageable);
    
    // 检查TODO是否属于指定用户
    Optional<Todo> findByIdAndUser(Long id, User user);
    Optional<Todo> findByIdAndUserId(Long id, Long userId);
    
    // 原有的查询方法（保留向后兼容）
    List<Todo> findByCompleted(Boolean completed);
    List<Todo> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT t FROM Todo t WHERE " +
           "(:keyword IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:completed IS NULL OR t.completed = :completed)")
    Page<Todo> searchTodos(@Param("keyword") String keyword, 
                          @Param("completed") Boolean completed, 
                          Pageable pageable);
    
    // 用户特定的搜索方法
    @Query("SELECT t FROM Todo t WHERE t.user = :user AND " +
           "(:keyword IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:completed IS NULL OR t.completed = :completed)")
    Page<Todo> searchTodosByUser(@Param("user") User user,
                                @Param("keyword") String keyword, 
                                @Param("completed") Boolean completed, 
                                Pageable pageable);
                          
    Page<Todo> findByCompleted(Boolean completed, Pageable pageable);
    Page<Todo> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Todo> findByTitleContainingIgnoreCaseAndCompleted(
        String title, Boolean completed, Pageable pageable);
} 