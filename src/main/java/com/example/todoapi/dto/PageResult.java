package com.example.todoapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页响应数据封装类
 * @param <T> 数据类型
 */
@Data
@Schema(description = "分页响应数据")
public class PageResult<T> {
    
    @Schema(description = "当前页数据列表")
    private List<T> content;
    
    @Schema(description = "当前页码(从0开始)")
    private int pageNumber;
    
    @Schema(description = "每页大小")
    private int pageSize;
    
    @Schema(description = "总记录数")
    private long totalElements;
    
    @Schema(description = "总页数")
    private int totalPages;
    
    @Schema(description = "是否有下一页")
    private boolean hasNext;
    
    @Schema(description = "是否有上一页")
    private boolean hasPrevious;
    
    @Schema(description = "是否是第一页")
    private boolean isFirst;
    
    @Schema(description = "是否是最后一页")
    private boolean isLast;
    
    /**
     * 将Spring Data的Page对象转换为PageResult
     * @param page Spring Data的Page对象
     * @return PageResult对象
     */
    public static <T> PageResult<T> of(Page<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setContent(page.getContent());
        result.setPageNumber(page.getNumber());
        result.setPageSize(page.getSize());
        result.setTotalElements(page.getTotalElements());
        result.setTotalPages(page.getTotalPages());
        result.setHasNext(page.hasNext());
        result.setHasPrevious(page.hasPrevious());
        result.setFirst(page.isFirst());
        result.setLast(page.isLast());
        return result;
    }
} 