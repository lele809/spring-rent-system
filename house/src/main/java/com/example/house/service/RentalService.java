package com.example.house.service;

import com.example.house.dto.MonthlyIncomeData;
import com.example.house.dto.PaymentQuery;
import com.example.house.dto.PaymentStats;
import com.example.house.entity.RentalNew;
import com.example.house.entity.RentalOld;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * 租房管理服务接口
 */
public interface RentalService {
    
    /**
     * 根据楼层获取租房记录列表（分页）
     */
    Page<?> getRentalsByFloor(String floor, PaymentQuery query);
    
    /**
     * 根据楼层和ID获取租房记录详情
     */
    Optional<?> getRentalByFloorAndId(String floor, Integer id);
    
    /**
     * 根据楼层创建租房记录
     */
    Object createRental(String floor, Object rental);
    
    /**
     * 根据楼层更新租房记录
     */
    Object updateRental(String floor, Integer id, Object rental);
    
    /**
     * 根据楼层删除租房记录
     */
    void deleteRental(String floor, Integer id);
    
    /**
     * 根据楼层获取费用统计信息
     */
    PaymentStats getPaymentStats(String floor);
    
    /**
     * 根据楼层更新支付状态
     */
    void updatePaymentStatus(String floor, Integer id, Integer status);
    
    /**
     * 根据楼层批量更新支付状态
     */
    void batchUpdatePaymentStatus(String floor, List<Integer> ids, Integer status);
    
    /**
     * 根据楼层导出费用数据
     */
    List<?> exportPaymentData(String floor, PaymentQuery query);
    
    /**
     * 根据楼层获取即将到期的合同
     */
    List<?> getExpiringContracts(String floor, int days);
    
    /**
     * 根据楼层获取月度收入趋势数据
     */
    MonthlyIncomeData getMonthlyIncomeData(String floor, int months);
} 