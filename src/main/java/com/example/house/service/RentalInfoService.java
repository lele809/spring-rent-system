package com.example.house.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 租房信息服务接口
 */
public interface RentalInfoService {
    
    /**
     * 根据楼层获取租房信息列表（分页）
     * @param floor 楼层（6或5）
     * @param pageable 分页参数
     * @return 租房信息分页数据
     */
    Page<?> getRentalInfosByFloor(String floor, Pageable pageable);
    
    /**
     * 根据楼层和条件查询租房信息（分页）
     * @param floor 楼层
     * @param keyword 搜索关键词
     * @param rentalStatus 租住状态
     * @param pageable 分页参数
     * @return 租房信息分页数据
     */
    Page<?> searchRentalInfos(String floor, String keyword, Integer rentalStatus, Pageable pageable);
    
    /**
     * 根据楼层和ID获取租房信息
     * @param floor 楼层
     * @param id 租房信息ID
     * @return 租房信息
     */
    Optional<?> getRentalInfoByFloorAndId(String floor, Integer id);
    
    /**
     * 创建租房信息
     * @param floor 楼层
     * @param rentalInfo 租房信息数据
     * @return 创建的租房信息
     */
    Object createRentalInfo(String floor, Object rentalInfo);
    
    /**
     * 更新租房信息
     * @param floor 楼层
     * @param id 租房信息ID
     * @param rentalInfo 更新的租房信息数据
     * @return 更新后的租房信息
     */
    Object updateRentalInfo(String floor, Integer id, Object rentalInfo);
    
    /**
     * 删除租房信息
     * @param floor 楼层
     * @param id 租房信息ID
     */
    void deleteRentalInfo(String floor, Integer id);
    
    /**
     * 根据楼层获取可用房间列表
     * @param floor 楼层
     * @return 可用房间列表
     */
    List<?> getAvailableRooms(String floor);
    
    /**
     * 检查房号是否已被占用
     * @param floor 楼层
     * @param roomNumber 房号
     * @return 是否已被占用
     */
    boolean isRoomOccupied(String floor, String roomNumber);
    
    /**
     * 检查电话是否已存在
     * @param floor 楼层
     * @param phone 电话号码
     * @return 是否已存在
     */
    boolean isPhoneExists(String floor, String phone);
    
    /**
     * 获取租房信息统计
     * @param floor 楼层
     * @return 统计信息
     */
    Object getRentalInfoStats(String floor);
}