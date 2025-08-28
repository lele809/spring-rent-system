package com.example.house.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 月度收入趋势数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyIncomeData {
    
    /**
     * 月份标签列表（如：["2024-01", "2024-02", ...]）
     */
    private List<String> months;
    
    /**
     * 对应月份的收入数据
     */
    private List<BigDecimal> incomes;
    
    /**
     * 当前月收入
     */
    private BigDecimal currentMonthIncome;
    
    /**
     * 上月收入
     */
    private BigDecimal previousMonthIncome;
    
    /**
     * 环比增长率（百分比）
     */
    private Double growthRate;
    
    /**
     * 平均月收入
     */
    private BigDecimal averageIncome;
    
    /**
     * 总收入
     */
    private BigDecimal totalIncome;
} 