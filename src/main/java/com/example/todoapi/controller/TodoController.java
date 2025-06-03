package com.example.todoapi.controller;

import com.example.todoapi.dto.ApiResult;
import com.example.todoapi.dto.PageResult;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    
    @Operation(summary = "搜索待办事项", description = "根据关键词搜索待办事项并返回分页结果")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功获取搜索结果",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiResult.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<ApiResult<PageResult<TodoDTO>>> searchTodos(
            @Parameter(description = "搜索关键词", example = "项目") @RequestParam(required = false) String keyword,
            @Parameter(description = "完成状态", example = "false") @RequestParam(required = false) Boolean completed,
            @Parameter(description = "页码(从0开始)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段", example = "createdAt") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "排序方向(asc/desc)", example = "desc") @RequestParam(defaultValue = "desc") String direction) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<TodoDTO> todoPage = todoService.searchTodos(keyword, completed, pageRequest);
        return ResponseEntity.ok(ApiResult.success(PageResult.of(todoPage)));
    }
    
    @Operation(summary = "分页获取待办事项", description = "返回分页后的待办事项列表")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功获取分页待办事项",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiResult.class)))
    })
    @GetMapping("/page")
    public ResponseEntity<ApiResult<PageResult<TodoDTO>>> getTodosWithPagination(
            @Parameter(description = "页码(从0开始)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段", example = "createdAt") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "排序方向(asc/desc)", example = "desc") @RequestParam(defaultValue = "desc") String direction) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<TodoDTO> todoPage = todoService.getTodosWithPagination(pageRequest);
        return ResponseEntity.ok(ApiResult.success(PageResult.of(todoPage)));
    }
    
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
    
    @Operation(summary = "根据完成状态分页获取待办事项", description = "根据完成状态获取待办事项并返回分页结果")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "成功获取待办事项列表",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiResult.class)))
    })
    @GetMapping("/status/page")
    public ResponseEntity<ApiResult<PageResult<TodoDTO>>> getTodosByStatusWithPagination(
            @Parameter(description = "完成状态", required = true) @RequestParam Boolean completed,
            @Parameter(description = "页码(从0开始)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段", example = "createdAt") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "排序方向(asc/desc)", example = "desc") @RequestParam(defaultValue = "desc") String direction) {
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<TodoDTO> todoPage = todoService.getTodosByCompletedWithPagination(completed, pageRequest);
        return ResponseEntity.ok(ApiResult.success(PageResult.of(todoPage)));
    }
}