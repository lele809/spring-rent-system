package com.example.house.repository;

import com.example.house.entity.RentalOld;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 五楼租房费用管理数据访问层
 */
@Repository
public interface RentalOldRepository extends JpaRepository<RentalOld, Integer> {
    
    /**
     * 根据房号查找租房记录
     */
    Optional<RentalOld> findByRoomNumber(String roomNumber);
    
    /**
     * 根据租客姓名查找租房记录
     */
    List<RentalOld> findByTenantName(String tenantName);
    
    /**
     * 根据支付状态查找租房记录
     */
    List<RentalOld> findByPaymentStatus(Integer paymentStatus);
    
    /**
     * 根据房号和租客姓名查找租房记录
     */
    Optional<RentalOld> findByRoomNumberAndTenantName(String roomNumber, String tenantName);
    
    /**
     * 分页查询租房记录
     */
    @Query("SELECT r FROM RentalOld r WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR r.roomNumber LIKE CONCAT('%', :keyword, '%') OR r.tenantName LIKE CONCAT('%', :keyword, '%')) " +
           "AND (:paymentStatus IS NULL OR r.paymentStatus = :paymentStatus) " +
           "AND (:monthFilter IS NULL OR :monthFilter = '' OR " +
           "    (YEAR(r.createdAt) = :year AND MONTH(r.createdAt) = :month))")
    Page<RentalOld> findRentalsWithFilters(@Param("keyword") String keyword,
                                          @Param("paymentStatus") Integer paymentStatus,
                                          @Param("monthFilter") String monthFilter,
                                          @Param("year") Integer year,
                                          @Param("month") Integer month,
                                          Pageable pageable);
    
    /**
     * 获取费用统计信息
     */
    @Query("SELECT COUNT(r) FROM RentalOld r WHERE r.paymentStatus = :status")
    long countByPaymentStatus(@Param("status") Integer status);
    
    /**
     * 计算总租金收入
     */
    @Query("SELECT COALESCE(SUM(r.monthlyRent), 0) FROM RentalOld r WHERE r.paymentStatus = 1")
    BigDecimal getTotalRentIncome();
    
    /**
     * 计算总水电费收入
     */
    @Query("SELECT COALESCE(SUM(r.waterFee + r.electricityFee + r.utilitiesFee), 0) FROM RentalOld r WHERE r.paymentStatus = 1")
    BigDecimal getTotalUtilitiesIncome();
    
    /**
     * 计算待收款金额
     */
    @Query("SELECT COALESCE(SUM(r.totalDue), 0) FROM RentalOld r WHERE r.paymentStatus = 2")
    BigDecimal getTotalPendingAmount();
    
    /**
     * 计算逾期金额（假设合同结束日期已过但未支付）
     */
    @Query("SELECT COALESCE(SUM(r.totalDue), 0) FROM RentalOld r WHERE r.paymentStatus = 2 AND r.contractEndDate < CURRENT_DATE")
    BigDecimal getTotalOverdueAmount();
    
    /**
     * 根据日期范围查询
     */
    @Query("SELECT r FROM RentalOld r WHERE r.createdAt BETWEEN :startDate AND :endDate ORDER BY r.createdAt DESC")
    List<RentalOld> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * 查找即将到期的合同（30天内）
     */
    @Query("SELECT r FROM RentalOld r WHERE r.contractEndDate BETWEEN CURRENT_DATE AND :endDate ORDER BY r.contractEndDate ASC")
    List<RentalOld> findExpiringContracts(@Param("endDate") LocalDate endDate);
} 