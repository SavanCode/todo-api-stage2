package com.example.todoapi.controller;

import com.example.todoapi.dto.ApiResult;
import com.example.todoapi.dto.TodoDTO;
import com.example.todoapi.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // 允许跨域，实际项目中要配置具体域名
@Tag(name = "Todo Controller", description = "待办事项管理接口")
public class TodoController {
    
    private final TodoService todoService;
    
    @Operation(summary = "获取所有待办事项", description = "返回所有待办事项的列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功获取所有待办事项",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiResult.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResult<List<TodoDTO>>> getAllTodos() {
        return ResponseEntity.ok(ApiResult.success(todoService.getAllTodos()));
    }
    
    @Operation(summary = "根据ID获取待办事项", description = "根据ID获取特定的待办事项")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功获取待办事项",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiResult.class))),
        @ApiResponse(responseCode = "404", description = "待办事项不存在")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<TodoDTO>> getTodoById(
            @Parameter(description = "待办事项ID", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(ApiResult.success(todoService.getTodoById(id)));
    }
    
    @Operation(summary = "创建待办事项", description = "创建新的待办事项")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "成功创建待办事项",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiResult.class))),
        @ApiResponse(responseCode = "400", description = "无效的请求数据")
    })
    @PostMapping
    public ResponseEntity<ApiResult<TodoDTO>> createTodo(
            @Parameter(description = "待办事项数据", required = true)
            @Valid @RequestBody TodoDTO todoDTO) {
        TodoDTO createdTodo = todoService.createTodo(todoDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResult.success("待办事项创建成功", createdTodo));
    }
    
    @Operation(summary = "更新待办事项", description = "更新指定ID的待办事项")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功更新待办事项",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiResult.class))),
        @ApiResponse(responseCode = "404", description = "待办事项不存在"),
        @ApiResponse(responseCode = "400", description = "无效的请求数据")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<TodoDTO>> updateTodo(
            @Parameter(description = "待办事项ID", required = true) @PathVariable Long id,
            @Parameter(description = "待办事项数据", required = true)
            @Valid @RequestBody TodoDTO todoDTO) {
        TodoDTO updatedTodo = todoService.updateTodo(id, todoDTO);
        return ResponseEntity.ok(ApiResult.success("待办事项更新成功", updatedTodo));
    }
    
    @Operation(summary = "删除待办事项", description = "删除指定ID的待办事项")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功删除待办事项",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiResult.class))),
        @ApiResponse(responseCode = "404", description = "待办事项不存在")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> deleteTodo(
            @Parameter(description = "待办事项ID", required = true) @PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok(ApiResult.success("待办事项删除成功", null));
    }
    
    @Operation(summary = "按状态筛选待办事项", description = "根据完成状态筛选待办事项")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功获取筛选后的待办事项列表",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiResult.class)))
    })
    @GetMapping("/status")
    public ResponseEntity<ApiResult<List<TodoDTO>>> getTodosByStatus(
            @Parameter(description = "完成状态", required = true) @RequestParam Boolean completed) {
        return ResponseEntity.ok(ApiResult.success(todoService.getTodosByCompleted(completed)));
    }
}