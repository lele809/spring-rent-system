package com.example.house.repository;

import com.example.house.entity.RentalRecordsOld;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 租房缴费记录（旧）Repository
 */
@Repository
public interface RentalRecordsOldRepository extends JpaRepository<RentalRecordsOld, Integer> {
    
    /**
     * 分页查询缴费记录，支持关键字搜索和月份筛选
     */
    @Query("SELECT r FROM RentalRecordsOld r WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR r.roomNumber LIKE %:keyword% OR r.tenantName LIKE %:keyword%) " +
           "AND (:startDate IS NULL OR r.paymentDate >= :startDate) " +
           "AND (:endDate IS NULL OR r.paymentDate <= :endDate)")
    Page<RentalRecordsOld> findRecordsWithFilters(@Param("keyword") String keyword,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate,
                                                   Pageable pageable);
    
    /**
     * 查询指定房号的缴费记录
     */
    @Query("SELECT r FROM RentalRecordsOld r WHERE r.roomNumber = :roomNumber ORDER BY r.paymentDate DESC")
    List<RentalRecordsOld> findByRoomNumber(@Param("roomNumber") String roomNumber);
    
    /**
     * 查询指定租客的缴费记录
     */
    @Query("SELECT r FROM RentalRecordsOld r WHERE r.tenantName = :tenantName ORDER BY r.paymentDate DESC")
    List<RentalRecordsOld> findByTenantName(@Param("tenantName") String tenantName);
    
    /**
     * 查询指定日期范围的缴费记录
     */
    @Query("SELECT r FROM RentalRecordsOld r WHERE r.paymentDate BETWEEN :startDate AND :endDate ORDER BY r.paymentDate DESC")
    List<RentalRecordsOld> findByPaymentDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * 统计指定月份的缴费总金额
     */
    @Query("SELECT COALESCE(SUM(r.totalRent), 0) FROM RentalRecordsOld r WHERE " +
           "YEAR(r.paymentDate) = :year AND MONTH(r.paymentDate) = :month")
    Double getTotalRentByMonth(@Param("year") int year, @Param("month") int month);
    
    /**
     * 导出所有缴费记录（支持筛选）
     */
    @Query("SELECT r FROM RentalRecordsOld r WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR r.roomNumber LIKE %:keyword% OR r.tenantName LIKE %:keyword%) " +
           "AND (:startDate IS NULL OR r.paymentDate >= :startDate) " +
           "AND (:endDate IS NULL OR r.paymentDate <= :endDate) " +
           "ORDER BY r.paymentDate DESC, r.createdAt DESC")
    List<RentalRecordsOld> findAllRecordsWithFilters(@Param("keyword") String keyword,
                                                      @Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);
} 