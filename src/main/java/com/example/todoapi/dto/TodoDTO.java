package com.example.todoapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(description = "待办事项数据传输对象")
public class TodoDTO {
    
    @Schema(description = "待办事项ID", example = "1")
    private Long id;
    
    @Schema(description = "待办事项标题", example = "完成项目开发", required = true)
    @NotBlank(message = "标题不能为空")
    @Size(min = 1, max = 100, message = "标题长度必须在1-100之间")
    private String title;
    
    @Schema(description = "待办事项描述", example = "完成Todo API项目的开发和测试")
    @Size(max = 500, message = "描述不能超过500字符")
    private String description;
    
    @Schema(description = "完成状态", example = "false")
    private Boolean completed;
    
    @Schema(description = "创建时间", example = "2024-01-01T12:00:00")
    private String createdAt;
    
    @Schema(description = "更新时间", example = "2024-01-01T12:00:00")
    private String updatedAt;
}