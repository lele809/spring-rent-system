package com.example.house.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * 费用统计信息DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStats {
    
    /**
     * 总租金收入
     */
    private BigDecimal totalRentIncome;
    
    /**
     * 总水电费收入
     */
    private BigDecimal totalUtilitiesIncome;
    
    /**
     * 待收款金额
     */
    private BigDecimal totalPendingAmount;
    
    /**
     * 逾期金额
     */
    private BigDecimal totalOverdueAmount;
    
    /**
     * 已支付记录数
     */
    private long paidCount;
    
    /**
     * 未支付记录数
     */
    private long unpaidCount;
    
    /**
     * 总记录数
     */
    private long totalCount;
    
    /**
     * 本月收入
     */
    private BigDecimal monthlyIncome;
    
    /**
     * 支付完成率（百分比）
     */
    private double paymentRate;
} 