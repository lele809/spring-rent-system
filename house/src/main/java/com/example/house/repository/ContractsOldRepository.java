package com.example.house.repository;

import com.example.house.entity.ContractsOld;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 五楼合同管理数据访问层
 */
@Repository
public interface ContractsOldRepository extends JpaRepository<ContractsOld, Integer> {
    
    /**
     * 根据合同编号查找合同
     */
    Optional<ContractsOld> findByContractNumber(String contractNumber);
    
    /**
     * 根据房间号查找合同
     */
    List<ContractsOld> findByRoomNumber(String roomNumber);
    
    /**
     * 根据租客姓名查找合同
     */
    List<ContractsOld> findByTenantName(String tenantName);
    
    /**
     * 根据租客电话查找合同
     */
    List<ContractsOld> findByTenantPhone(String tenantPhone);
    
    /**
     * 根据合同状态查找合同
     */
    List<ContractsOld> findByContractStatus(Integer contractStatus);
    
    /**
     * 根据合同状态分页查询
     */
    Page<ContractsOld> findByContractStatus(Integer contractStatus, Pageable pageable);
    
    /**
     * 检查合同编号是否存在
     */
    boolean existsByContractNumber(String contractNumber);
    
    /**
     * 检查房间是否已有有效合同
     */
    @Query("SELECT COUNT(c) > 0 FROM ContractsOld c WHERE c.roomNumber = :roomNumber AND c.contractStatus = 1")
    boolean existsActiveContractByRoomNumber(@Param("roomNumber") String roomNumber);
    
    /**
     * 统计各状态合同数量
     */
    @Query("SELECT c.contractStatus, COUNT(c) FROM ContractsOld c GROUP BY c.contractStatus")
    List<Object[]> countByContractStatus();
    
    /**
     * 根据合同编号或租客姓名模糊搜索
     */
    @Query("SELECT c FROM ContractsOld c WHERE c.contractNumber LIKE %:keyword% OR c.tenantName LIKE %:keyword%")
    Page<ContractsOld> findByContractNumberContainingOrTenantNameContaining(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * 查找即将到期的合同（30天内到期）
     */
    @Query("SELECT c FROM ContractsOld c WHERE c.contractEndDate BETWEEN :startDate AND :endDate AND c.contractStatus = 1")
    List<ContractsOld> findExpiringContracts(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * 查找已过期的合同
     */
    @Query("SELECT c FROM ContractsOld c WHERE c.contractEndDate < :currentDate AND c.contractStatus = 1")
    List<ContractsOld> findExpiredContracts(@Param("currentDate") LocalDate currentDate);
    
    /**
     * 根据日期范围查找合同
     */
    @Query("SELECT c FROM ContractsOld c WHERE c.contractStartDate >= :startDate AND c.contractStartDate <= :endDate")
    Page<ContractsOld> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    
    /**
     * 多条件筛选查询
     */
    @Query("SELECT c FROM ContractsOld c WHERE " +
           "(:contractStatus IS NULL OR c.contractStatus = :contractStatus) AND " +
           "(:roomNumber IS NULL OR c.roomNumber = :roomNumber) AND " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "    c.contractNumber LIKE CONCAT('%', :keyword, '%') OR " +
           "    c.tenantName LIKE CONCAT('%', :keyword, '%') OR " +
           "    c.roomNumber LIKE CONCAT('%', :keyword, '%'))")
    Page<ContractsOld> findWithFilters(@Param("contractStatus") Integer contractStatus, 
                                       @Param("roomNumber") String roomNumber, 
                                       @Param("keyword") String keyword, 
                                       Pageable pageable);
} 