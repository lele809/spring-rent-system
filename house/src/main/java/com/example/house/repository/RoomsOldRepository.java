package com.example.house.repository;

import com.example.house.entity.RoomsOld;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 五楼房间管理数据访问层
 */
@Repository
public interface RoomsOldRepository extends JpaRepository<RoomsOld, Integer> {
    
    /**
     * 根据房号查找房间
     */
    Optional<RoomsOld> findByRoomNumber(String roomNumber);
    
    /**
     * 根据房间状态查找房间
     */
    List<RoomsOld> findByRoomStatus(Integer roomStatus);
    
    /**
     * 根据房间类型查找房间
     */
    List<RoomsOld> findByRoomType(String roomType);
    
    /**
     * 根据房间状态和房间类型查找房间
     */
    List<RoomsOld> findByRoomStatusAndRoomType(Integer roomStatus, String roomType);
    
    /**
     * 检查房号是否存在
     */
    boolean existsByRoomNumber(String roomNumber);
    
    /**
     * 统计各状态房间数量
     */
    @Query("SELECT r.roomStatus, COUNT(r) FROM RoomsOld r GROUP BY r.roomStatus")
    List<Object[]> countByRoomStatus();
    
    /**
     * 根据房号模糊搜索
     */
    List<RoomsOld> findByRoomNumberContaining(String roomNumber);
    
    /**
     * 根据楼层筛选房间（房号第一位数字代表楼层）
     */
    @Query("SELECT r FROM RoomsOld r WHERE SUBSTRING(r.roomNumber, 1, 1) = :floor")
    List<RoomsOld> findByFloor(String floor);
    
    /**
     * 根据状态和楼层筛选房间
     */
    @Query("SELECT r FROM RoomsOld r WHERE r.roomStatus = :status AND SUBSTRING(r.roomNumber, 1, 1) = :floor")
    List<RoomsOld> findByRoomStatusAndFloor(Integer status, String floor);
    
    /**
     * 分页查询 - 根据楼层筛选
     */
    @Query("SELECT r FROM RoomsOld r WHERE SUBSTRING(r.roomNumber, 1, 1) = :floor")
    Page<RoomsOld> findByFloor(String floor, Pageable pageable);
    
    /**
     * 分页查询 - 根据状态和楼层筛选
     */
    @Query("SELECT r FROM RoomsOld r WHERE r.roomStatus = :status AND SUBSTRING(r.roomNumber, 1, 1) = :floor")
    Page<RoomsOld> findByRoomStatusAndFloor(Integer status, String floor, Pageable pageable);
    
    /**
     * 分页查询 - 根据状态筛选
     */
    Page<RoomsOld> findByRoomStatus(Integer status, Pageable pageable);
}