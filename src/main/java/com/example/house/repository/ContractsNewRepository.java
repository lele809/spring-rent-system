package com.example.house.repository;

import com.example.house.entity.ContractsNew;
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
 * 六楼合同管理数据访问层
 */
@Repository
public interface ContractsNewRepository extends JpaRepository<ContractsNew, Integer> {
    
    /**
     * 根据合同编号查找合同
     */
    Optional<ContractsNew> findByContractNumber(String contractNumber);
    
    /**
     * 根据房间号查找合同
     */
    List<ContractsNew> findByRoomNumber(String roomNumber);
    
    /**
     * 根据租客姓名查找合同
     */
    List<ContractsNew> findByTenantName(String tenantName);
    
    /**
     * 根据租客电话查找合同
     */
    List<ContractsNew> findByTenantPhone(String tenantPhone);
    
    /**
     * 根据合同状态查找合同
     */
    List<ContractsNew> findByContractStatus(Integer contractStatus);
    
    /**
     * 根据合同状态分页查询
     */
    Page<ContractsNew> findByContractStatus(Integer contractStatus, Pageable pageable);
    
    /**
     * 检查合同编号是否存在
     */
    boolean existsByContractNumber(String contractNumber);
    
    /**
     * 检查房间是否已有有效合同
     */
    @Query("SELECT COUNT(c) > 0 FROM ContractsNew c WHERE c.roomNumber = :roomNumber AND c.contractStatus = 1")
    boolean existsActiveContractByRoomNumber(@Param("roomNumber") String roomNumber);
    
    /**
     * 统计各状态合同数量
     */
    @Query("SELECT c.contractStatus, COUNT(c) FROM ContractsNew c GROUP BY c.contractStatus")
    List<Object[]> countByContractStatus();
    
    /**
     * 根据合同编号或租客姓名模糊搜索
     */
    @Query("SELECT c FROM ContractsNew c WHERE c.contractNumber LIKE %:keyword% OR c.tenantName LIKE %:keyword%")
    Page<ContractsNew> findByContractNumberContainingOrTenantNameContaining(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * 查找即将到期的合同（30天内到期）
     */
    @Query("SELECT c FROM ContractsNew c WHERE c.contractEndDate BETWEEN :startDate AND :endDate AND c.contractStatus = 1")
    List<ContractsNew> findExpiringContracts(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    /**
     * 查找已过期的合同
     */
    @Query("SELECT c FROM ContractsNew c WHERE c.contractEndDate < :currentDate AND c.contractStatus = 1")
    List<ContractsNew> findExpiredContracts(@Param("currentDate") LocalDate currentDate);
    
    /**
     * 根据日期范围查找合同
     */
    @Query("SELECT c FROM ContractsNew c WHERE c.contractStartDate >= :startDate AND c.contractStartDate <= :endDate")
    Page<ContractsNew> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
    
    /**
     * 多条件筛选查询
     */
    @Query("SELECT c FROM ContractsNew c WHERE " +
           "(:contractStatus IS NULL OR c.contractStatus = :contractStatus) AND " +
           "(:roomNumber IS NULL OR c.roomNumber = :roomNumber) AND " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "    c.contractNumber LIKE CONCAT('%', :keyword, '%') OR " +
           "    c.tenantName LIKE CONCAT('%', :keyword, '%') OR " +
           "    c.roomNumber LIKE CONCAT('%', :keyword, '%'))")
    Page<ContractsNew> findWithFilters(@Param("contractStatus") Integer contractStatus, 
                                       @Param("roomNumber") String roomNumber, 
                                       @Param("keyword") String keyword, 
                                       Pageable pageable);
} 