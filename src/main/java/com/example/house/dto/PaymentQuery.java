package com.example.house.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

/**
 * 费用查询参数DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentQuery {
    
    /**
     * 关键词（房号、租客姓名）
     */
    private String keyword;
    
    /**
     * 支付状态（1=已支付, 2=未支付）
     */
    private Integer paymentStatus;
    
    /**
     * 费用类型（rent=租金, utilities=水电费, maintenance=维修费, other=其他）
     */
    private String feeType;
    
    /**
     * 开始日期
     */
    private LocalDate startDate;
    
    /**
     * 结束日期
     */
    private LocalDate endDate;
    
    /**
     * 月份筛选（格式: YYYY-MM）
     */
    private String monthFilter;
    
    /**
     * 页码（从0开始）
     */
    private int page = 0;
    
    /**
     * 每页大小
     */
    private int size = 20;
    
    /**
     * 排序字段
     */
    private String sortBy = "createdAt";
    
    /**
     * 排序方向（asc, desc）
     */
    private String sortDirection = "desc";
} 