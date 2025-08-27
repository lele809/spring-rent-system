package com.example.house.repository;

import com.example.house.entity.RentalInfoNew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 六楼租房信息数据访问层
 */
@Repository
public interface RentalInfoNewRepository extends JpaRepository<RentalInfoNew, Integer> {
    
    /**
     * 根据房号查找租房信息
     */
    Optional<RentalInfoNew> findByRoomNumber(String roomNumber);
    
    /**
     * 根据租客姓名查找租房信息
     */
    List<RentalInfoNew> findByTenantNameContaining(String tenantName);
    
    /**
     * 根据电话查找租房信息
     */
    Optional<RentalInfoNew> findByPhone(String phone);
    
    /**
     * 根据租住状态查找租房信息
     */
    List<RentalInfoNew> findByRentalStatus(Integer rentalStatus);
    
    /**
     * 根据租住状态分页查询
     */
    Page<RentalInfoNew> findByRentalStatus(Integer rentalStatus, Pageable pageable);
    
    /**
     * 根据关键词进行模糊搜索（房号、租客姓名、电话）
     */
    @Query("SELECT r FROM RentalInfoNew r WHERE r.roomNumber LIKE %:keyword% OR r.tenantName LIKE %:keyword% OR r.phone LIKE %:keyword%")
    List<RentalInfoNew> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 根据关键词进行模糊搜索（分页）
     */
    @Query("SELECT r FROM RentalInfoNew r WHERE r.roomNumber LIKE %:keyword% OR r.tenantName LIKE %:keyword% OR r.phone LIKE %:keyword%")
    Page<RentalInfoNew> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * 根据关键词和租住状态进行搜索（分页）
     */
    @Query("SELECT r FROM RentalInfoNew r WHERE (r.roomNumber LIKE %:keyword% OR r.tenantName LIKE %:keyword% OR r.phone LIKE %:keyword%) AND r.rentalStatus = :rentalStatus")
    Page<RentalInfoNew> searchByKeywordAndStatus(@Param("keyword") String keyword, @Param("rentalStatus") Integer rentalStatus, Pageable pageable);
    
    /**
     * 检查房号是否已存在
     */
    boolean existsByRoomNumber(String roomNumber);
    
    /**
     * 检查电话是否已存在
     */
    boolean existsByPhone(String phone);
    
    /**
     * 统计各状态的租房信息数量
     */
    @Query("SELECT r.rentalStatus, COUNT(r) FROM RentalInfoNew r GROUP BY r.rentalStatus")
    List<Object[]> countByRentalStatus();
}