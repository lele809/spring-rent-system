package com.example.house.dto;

import java.time.LocalDate;

/**
 * 缴费记录查询参数DTO
 */
public class RentalRecordsQuery {
    
    /**
     * 搜索关键字（房号或租客姓名）
     */
    private String keyword;
    
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
    private String sortBy = "paymentDate";
    
    /**
     * 排序方向
     */
    private String sortDirection = "desc";
    
    // 构造函数
    public RentalRecordsQuery() {}
    
    // Getters and Setters
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public String getMonthFilter() {
        return monthFilter;
    }
    
    public void setMonthFilter(String monthFilter) {
        this.monthFilter = monthFilter;
    }
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public String getSortBy() {
        return sortBy;
    }
    
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
    
    public String getSortDirection() {
        return sortDirection;
    }
    
    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
    
    @Override
    public String toString() {
        return "RentalRecordsQuery{" +
                "keyword='" + keyword + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", monthFilter='" + monthFilter + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", sortBy='" + sortBy + '\'' +
                ", sortDirection='" + sortDirection + '\'' +
                '}';
    }
} 