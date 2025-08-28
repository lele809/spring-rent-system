package com.example.house.service.impl;

import com.example.house.dto.RentalRecordsQuery;
import com.example.house.entity.RentalRecordsNew;
import com.example.house.entity.RentalRecordsOld;
import com.example.house.repository.RentalRecordsNewRepository;
import com.example.house.repository.RentalRecordsOldRepository;
import com.example.house.service.RentalRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * 缴费记录服务实现类
 */
@Service
public class RentalRecordsServiceImpl implements RentalRecordsService {
    
    @Autowired
    private RentalRecordsNewRepository rentalRecordsNewRepository;
    
    @Autowired
    private RentalRecordsOldRepository rentalRecordsOldRepository;
    
    @Override
    public Page<?> getRecordsByFloor(String floor, RentalRecordsQuery query) {
        System.out.println("=== RentalRecordsServiceImpl.getRecordsByFloor ===");
        System.out.println("接收到的楼层参数: '" + floor + "'");
        System.out.println("楼层参数类型: " + floor.getClass().getName());
        System.out.println("是否等于'6': " + "6".equals(floor));
        System.out.println("查询参数: " + query.toString());
        
        // 处理月份筛选
        LocalDate startDate = query.getStartDate();
        LocalDate endDate = query.getEndDate();
        
        // 如果有月份筛选，转换为日期范围
        if (StringUtils.hasText(query.getMonthFilter())) {
            try {
                YearMonth yearMonth = YearMonth.parse(query.getMonthFilter());
                startDate = yearMonth.atDay(1);
                endDate = yearMonth.atEndOfMonth();
                System.out.println("月份筛选: " + query.getMonthFilter() + " -> " + startDate + " 到 " + endDate);
            } catch (Exception e) {
                System.out.println("月份格式错误: " + query.getMonthFilter());
                // 如果月份格式错误，忽略月份筛选
            }
        }
        
        // 创建分页和排序
        Sort sort = Sort.by(
            "desc".equalsIgnoreCase(query.getSortDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC,
            query.getSortBy()
        );
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize(), sort);
        System.out.println("分页参数: " + pageable.toString());
        
        // 根据楼层查询
        if ("6".equals(floor)) {
            System.out.println("使用六楼Repository查询 (RentalRecordsNew)");
            Page<RentalRecordsNew> result = rentalRecordsNewRepository.findRecordsWithFilters(
                StringUtils.hasText(query.getKeyword()) ? query.getKeyword() : null,
                startDate,
                endDate,
                pageable
            );
            System.out.println("六楼查询结果: " + result.getTotalElements() + " 条记录");
            System.out.println("六楼当前页内容数量: " + result.getContent().size());
            if (!result.getContent().isEmpty()) {
                System.out.println("六楼第一条记录: " + result.getContent().get(0).toString());
            }
            return result;
        } else {
            System.out.println("使用五楼Repository查询 (RentalRecordsOld)");
            Page<RentalRecordsOld> result = rentalRecordsOldRepository.findRecordsWithFilters(
                StringUtils.hasText(query.getKeyword()) ? query.getKeyword() : null,
                startDate,
                endDate,
                pageable
            );
            System.out.println("五楼查询结果: " + result.getTotalElements() + " 条记录");
            System.out.println("五楼当前页内容数量: " + result.getContent().size());
            if (!result.getContent().isEmpty()) {
                System.out.println("五楼第一条记录: " + result.getContent().get(0).toString());
            }
            return result;
        }
    }
    
    @Override
    public List<?> exportRecordsByFloor(String floor, RentalRecordsQuery query) {
        // 处理月份筛选
        LocalDate startDate = query.getStartDate();
        LocalDate endDate = query.getEndDate();
        
        if (StringUtils.hasText(query.getMonthFilter())) {
            try {
                YearMonth yearMonth = YearMonth.parse(query.getMonthFilter());
                startDate = yearMonth.atDay(1);
                endDate = yearMonth.atEndOfMonth();
            } catch (Exception e) {
                // 如果月份格式错误，忽略月份筛选
            }
        }
        
        // 根据楼层查询
        if ("6".equals(floor)) {
            return rentalRecordsNewRepository.findAllRecordsWithFilters(
                StringUtils.hasText(query.getKeyword()) ? query.getKeyword() : null,
                startDate,
                endDate
            );
        } else {
            return rentalRecordsOldRepository.findAllRecordsWithFilters(
                StringUtils.hasText(query.getKeyword()) ? query.getKeyword() : null,
                startDate,
                endDate
            );
        }
    }
    
    @Override
    public RentalRecordsNew addNewRecord(RentalRecordsNew record) {
        if (record.getCreatedAt() == null) {
            record.setCreatedAt(java.time.LocalDateTime.now());
        }
        return rentalRecordsNewRepository.save(record);
    }
    
    @Override
    public RentalRecordsOld addOldRecord(RentalRecordsOld record) {
        if (record.getCreatedAt() == null) {
            record.setCreatedAt(java.time.LocalDateTime.now());
        }
        return rentalRecordsOldRepository.save(record);
    }
    
    @Override
    public void deleteNewRecord(Integer id) {
        rentalRecordsNewRepository.deleteById(id);
    }
    
    @Override
    public void deleteOldRecord(Integer id) {
        rentalRecordsOldRepository.deleteById(id);
    }
    
    @Override
    public List<?> getRecordsByRoomNumber(String floor, String roomNumber) {
        if ("6".equals(floor)) {
            return rentalRecordsNewRepository.findByRoomNumber(roomNumber);
        } else {
            return rentalRecordsOldRepository.findByRoomNumber(roomNumber);
        }
    }
    
    @Override
    public List<?> getRecordsByTenantName(String floor, String tenantName) {
        if ("6".equals(floor)) {
            return rentalRecordsNewRepository.findByTenantName(tenantName);
        } else {
            return rentalRecordsOldRepository.findByTenantName(tenantName);
        }
    }
    
    @Override
    public Double getMonthlyTotal(String floor, int year, int month) {
        if ("6".equals(floor)) {
            return rentalRecordsNewRepository.getTotalRentByMonth(year, month);
        } else {
            return rentalRecordsOldRepository.getTotalRentByMonth(year, month);
        }
    }
    
    @Override
    public List<?> getRecordsByDateRange(String floor, LocalDate startDate, LocalDate endDate) {
        if ("6".equals(floor)) {
            return rentalRecordsNewRepository.findByPaymentDateBetween(startDate, endDate);
        } else {
            return rentalRecordsOldRepository.findByPaymentDateBetween(startDate, endDate);
        }
    }
    
    @Override
    public List<RentalRecordsNew> getAllNewRecords() {
        System.out.println("=== 查询所有六楼记录 ===");
        List<RentalRecordsNew> records = rentalRecordsNewRepository.findAll();
        System.out.println("从数据库查询到 " + records.size() + " 条六楼记录");
        return records;
    }
    
    @Override
    public List<RentalRecordsOld> getAllOldRecords() {
        System.out.println("=== 查询所有五楼记录 ===");
        List<RentalRecordsOld> records = rentalRecordsOldRepository.findAll();
        System.out.println("从数据库查询到 " + records.size() + " 条五楼记录");
        return records;
    }
} 