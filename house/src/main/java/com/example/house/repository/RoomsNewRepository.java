package com.example.house.repository;

import com.example.house.entity.RoomsNew;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 六楼房间管理数据访问层
 */
@Repository
public interface RoomsNewRepository extends JpaRepository<RoomsNew, Integer> {
    
    /**
     * 根据房号查找房间
     */
    Optional<RoomsNew> findByRoomNumber(String roomNumber);
    
    /**
     * 根据房间状态查找房间
     */
    List<RoomsNew> findByRoomStatus(Integer roomStatus);
    
    /**
     * 根据房间类型查找房间
     */
    List<RoomsNew> findByRoomType(String roomType);
    
    /**
     * 根据房间状态和房间类型查找房间
     */
    List<RoomsNew> findByRoomStatusAndRoomType(Integer roomStatus, String roomType);
    
    /**
     * 检查房号是否存在
     */
    boolean existsByRoomNumber(String roomNumber);
    
    /**
     * 统计各状态房间数量
     */
    @Query("SELECT r.roomStatus, COUNT(r) FROM RoomsNew r GROUP BY r.roomStatus")
    List<Object[]> countByRoomStatus();
    
    /**
     * 根据房号模糊搜索
     */
    List<RoomsNew> findByRoomNumberContaining(String roomNumber);
    
    /**
     * 根据楼层筛选房间（房号第一位数字代表楼层）
     */
    @Query("SELECT r FROM RoomsNew r WHERE SUBSTRING(r.roomNumber, 1, 1) = :floor")
    List<RoomsNew> findByFloor(String floor);
    
    /**
     * 根据状态和楼层筛选房间
     */
    @Query("SELECT r FROM RoomsNew r WHERE r.roomStatus = :status AND SUBSTRING(r.roomNumber, 1, 1) = :floor")
    List<RoomsNew> findByRoomStatusAndFloor(Integer status, String floor);
    
    /**
     * 分页查询 - 根据楼层筛选
     */
    @Query("SELECT r FROM RoomsNew r WHERE SUBSTRING(r.roomNumber, 1, 1) = :floor")
    Page<RoomsNew> findByFloor(String floor, Pageable pageable);
    
    /**
     * 分页查询 - 根据状态和楼层筛选
     */
    @Query("SELECT r FROM RoomsNew r WHERE r.roomStatus = :status AND SUBSTRING(r.roomNumber, 1, 1) = :floor")
    Page<RoomsNew> findByRoomStatusAndFloor(Integer status, String floor, Pageable pageable);
    
    /**
     * 分页查询 - 根据状态筛选
     */
    Page<RoomsNew> findByRoomStatus(Integer status, Pageable pageable);
}