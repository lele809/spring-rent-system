package com.example.house.service;

import com.example.house.dto.RentalRecordsQuery;
import com.example.house.entity.RentalRecordsNew;
import com.example.house.entity.RentalRecordsOld;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 缴费记录服务接口
 */
public interface RentalRecordsService {
    
    /**
     * 根据楼层获取缴费记录（分页）
     * @param floor 楼层标识（"6"表示新楼，其他表示旧楼）
     * @param query 查询参数
     * @return 分页结果
     */
    Page<?> getRecordsByFloor(String floor, RentalRecordsQuery query);
    
    /**
     * 根据楼层导出缴费记录
     * @param floor 楼层标识
     * @param query 查询参数
     * @return 记录列表
     */
    List<?> exportRecordsByFloor(String floor, RentalRecordsQuery query);
    
    /**
     * 新增缴费记录（新楼）
     * @param record 缴费记录
     * @return 保存后的记录
     */
    RentalRecordsNew addNewRecord(RentalRecordsNew record);
    
    /**
     * 新增缴费记录（旧楼）
     * @param record 缴费记录
     * @return 保存后的记录
     */
    RentalRecordsOld addOldRecord(RentalRecordsOld record);
    
    /**
     * 根据ID删除缴费记录（新楼）
     * @param id 记录ID
     */
    void deleteNewRecord(Integer id);
    
    /**
     * 根据ID删除缴费记录（旧楼）
     * @param id 记录ID
     */
    void deleteOldRecord(Integer id);
    
    /**
     * 根据房号获取缴费记录
     * @param floor 楼层标识
     * @param roomNumber 房号
     * @return 记录列表
     */
    List<?> getRecordsByRoomNumber(String floor, String roomNumber);
    
    /**
     * 根据租客姓名获取缴费记录
     * @param floor 楼层标识
     * @param tenantName 租客姓名
     * @return 记录列表
     */
    List<?> getRecordsByTenantName(String floor, String tenantName);
    
    /**
     * 获取月度缴费总金额
     * @param floor 楼层标识
     * @param year 年份
     * @param month 月份
     * @return 总金额
     */
    Double getMonthlyTotal(String floor, int year, int month);
    
    /**
     * 根据日期范围获取缴费记录
     * @param floor 楼层标识
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 记录列表
     */
    List<?> getRecordsByDateRange(String floor, LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取所有新楼记录（测试用）
     * @return 新楼记录列表
     */
    List<RentalRecordsNew> getAllNewRecords();
    
    /**
     * 获取所有旧楼记录（测试用）
     * @return 旧楼记录列表
     */
    List<RentalRecordsOld> getAllOldRecords();
} 