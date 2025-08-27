package com.example.house.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

/**
 * 仪表盘统计数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    
    // 房间统计
    private Integer totalRooms;          // 总房间数
    private Integer availableRooms;      // 空闲房间数
    private Integer occupiedRooms;       // 已出租房间数
    private Integer maintenanceRooms;    // 维修中房间数
    private Double occupancyRate;        // 房间使用率
    
    // 租客统计
    private Integer totalTenants;        // 总租客数
    private Integer activeTenants;       // 在住租客数
    
    // 财务统计
    private BigDecimal monthlyIncome;    // 本月收入
    private BigDecimal unpaidAmount;     // 未缴费金额
    private Integer unpaidCount;         // 未缴费人数
    
    // 合同统计
    private Integer activeContracts;     // 生效中合同数
    private Integer expiringContracts;   // 即将到期合同数
}