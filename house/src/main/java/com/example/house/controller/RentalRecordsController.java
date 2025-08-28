package com.example.house.controller;

import com.example.house.dto.ApiResponse;
import com.example.house.dto.RentalRecordsQuery;
import com.example.house.entity.RentalRecordsNew;
import com.example.house.entity.RentalRecordsOld;
import com.example.house.service.RentalRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

/**
 * 缴费记录控制器
 */
@RestController
@RequestMapping("/api/rental-records")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"}, allowCredentials = "true")
public class RentalRecordsController {
    
    @Autowired
    private RentalRecordsService rentalRecordsService;
    
    /**
     * 分页查询缴费记录
     */
    @GetMapping("/{floor}")
    public ResponseEntity<ApiResponse<Page<?>>> getRecords(
            @PathVariable String floor,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String monthFilter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        try {
            RentalRecordsQuery query = new RentalRecordsQuery();
            query.setKeyword(keyword);
            query.setStartDate(startDate);
            query.setEndDate(endDate);
            query.setMonthFilter(monthFilter);
            query.setPage(page);
            query.setSize(size);
            query.setSortBy(sortBy);
            query.setSortDirection(sortDirection);
            
            System.out.println("=== 查询缴费记录 ===");
            System.out.println("楼层: " + floor);
            System.out.println("关键字: " + keyword);
            System.out.println("页码: " + page + ", 大小: " + size);
            System.out.println("排序: " + sortBy + " " + sortDirection);
            
            Page<?> records = rentalRecordsService.getRecordsByFloor(floor, query);
            System.out.println("查询结果: " + records.getTotalElements() + " 条记录");
            
            return ResponseEntity.ok(ApiResponse.success("获取缴费记录成功", records));
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ApiResponse.error("获取缴费记录失败: " + e.getMessage()));
        }
    }
    
    /**
     * 导出缴费记录
     */
    @GetMapping("/{floor}/export")
    public ResponseEntity<ApiResponse<List<?>>> exportRecords(
            @PathVariable String floor,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String monthFilter) {
        
        try {
            RentalRecordsQuery query = new RentalRecordsQuery();
            query.setKeyword(keyword);
            query.setStartDate(startDate);
            query.setEndDate(endDate);
            query.setMonthFilter(monthFilter);
            
            List<?> records = rentalRecordsService.exportRecordsByFloor(floor, query);
            return ResponseEntity.ok(ApiResponse.success("导出缴费记录成功", records));
            
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("导出缴费记录失败: " + e.getMessage()));
        }
    }
    
    /**
     * 新增缴费记录（新楼）
     */
    @PostMapping("/new")
    public ResponseEntity<ApiResponse<RentalRecordsNew>> addNewRecord(@RequestBody Map<String, Object> requestData) {
        try {
            System.out.println("=== 新增六楼缴费记录 ===");
            System.out.println("接收到的数据: " + requestData);
            
            RentalRecordsNew record = new RentalRecordsNew();
            record.setRoomNumber((String) requestData.get("roomNumber"));
            record.setTenantName((String) requestData.get("tenantName"));
            
            // 处理totalRent数据类型转换
            Object totalRentObj = requestData.get("totalRent");
            if (totalRentObj instanceof Number) {
                record.setTotalRent(new BigDecimal(totalRentObj.toString()));
            } else if (totalRentObj instanceof String) {
                record.setTotalRent(new BigDecimal((String) totalRentObj));
            }
            
            // 处理paymentDate
            Object paymentDateObj = requestData.get("paymentDate");
            if (paymentDateObj instanceof String) {
                record.setPaymentDate(LocalDate.parse((String) paymentDateObj));
            }
            
            System.out.println("转换后的记录: " + record.toString());
            
            // 确保创建时间为null，让数据库自动设置
            record.setId(null);
            record.setCreatedAt(null);
            
            // 保存记录
            RentalRecordsNew savedRecord = rentalRecordsService.addNewRecord(record);
            System.out.println("保存成功，生成ID: " + savedRecord.getId());
            return ResponseEntity.ok(ApiResponse.success("新增缴费记录成功", savedRecord));
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("详细错误信息: " + e.toString());
            if (e.getCause() != null) {
                System.err.println("错误原因: " + e.getCause().toString());
                if (e.getCause().getCause() != null) {
                    System.err.println("根本原因: " + e.getCause().getCause().toString());
                }
            }
            return ResponseEntity.ok(ApiResponse.error("新增缴费记录失败: " + e.getMessage()));
        }
    }
    
    /**
     * 新增缴费记录（旧楼）
     */
    @PostMapping("/old")
    public ResponseEntity<ApiResponse<RentalRecordsOld>> addOldRecord(@RequestBody Map<String, Object> requestData) {
        try {
            System.out.println("=== 新增五楼缴费记录 ===");
            System.out.println("接收到的数据: " + requestData);
            
            RentalRecordsOld record = new RentalRecordsOld();
            record.setRoomNumber((String) requestData.get("roomNumber"));
            record.setTenantName((String) requestData.get("tenantName"));
            
            // 处理totalRent数据类型转换
            Object totalRentObj = requestData.get("totalRent");
            if (totalRentObj instanceof Number) {
                record.setTotalRent(new BigDecimal(totalRentObj.toString()));
            } else if (totalRentObj instanceof String) {
                record.setTotalRent(new BigDecimal((String) totalRentObj));
            }
            
            // 处理paymentDate
            Object paymentDateObj = requestData.get("paymentDate");
            if (paymentDateObj instanceof String) {
                record.setPaymentDate(LocalDate.parse((String) paymentDateObj));
            }
            
            System.out.println("转换后的记录: " + record.toString());
            
            // 确保创建时间为null，让数据库自动设置
            record.setId(null);
            record.setCreatedAt(null);
            
            // 保存记录
            RentalRecordsOld savedRecord = rentalRecordsService.addOldRecord(record);
            System.out.println("保存成功，生成ID: " + savedRecord.getId());
            return ResponseEntity.ok(ApiResponse.success("新增缴费记录成功", savedRecord));
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("详细错误信息: " + e.toString());
            if (e.getCause() != null) {
                System.err.println("错误原因: " + e.getCause().toString());
                if (e.getCause().getCause() != null) {
                    System.err.println("根本原因: " + e.getCause().getCause().toString());
                }
            }
            return ResponseEntity.ok(ApiResponse.error("新增缴费记录失败: " + e.getMessage()));
        }
    }
    
    /**
     * 删除缴费记录
     */
    @DeleteMapping("/{floor}/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(@PathVariable String floor, @PathVariable Integer id) {
        try {
            if ("6".equals(floor)) {
                rentalRecordsService.deleteNewRecord(id);
            } else {
                rentalRecordsService.deleteOldRecord(id);
            }
            return ResponseEntity.ok(ApiResponse.success("删除缴费记录成功", null));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("删除缴费记录失败: " + e.getMessage()));
        }
    }
    
    /**
     * 根据房号查询缴费记录
     */
    @GetMapping("/{floor}/room/{roomNumber}")
    public ResponseEntity<ApiResponse<List<?>>> getRecordsByRoomNumber(
            @PathVariable String floor,
            @PathVariable String roomNumber) {
        try {
            List<?> records = rentalRecordsService.getRecordsByRoomNumber(floor, roomNumber);
            return ResponseEntity.ok(ApiResponse.success("获取房号缴费记录成功", records));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("获取房号缴费记录失败: " + e.getMessage()));
        }
    }
    
    /**
     * 根据租客姓名查询缴费记录
     */
    @GetMapping("/{floor}/tenant/{tenantName}")
    public ResponseEntity<ApiResponse<List<?>>> getRecordsByTenantName(
            @PathVariable String floor,
            @PathVariable String tenantName) {
        try {
            List<?> records = rentalRecordsService.getRecordsByTenantName(floor, tenantName);
            return ResponseEntity.ok(ApiResponse.success("获取租客缴费记录成功", records));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("获取租客缴费记录失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取月度缴费统计
     */
    @GetMapping("/{floor}/monthly-total")
    public ResponseEntity<ApiResponse<Double>> getMonthlyTotal(
            @PathVariable String floor,
            @RequestParam int year,
            @RequestParam int month) {
        try {
            Double total = rentalRecordsService.getMonthlyTotal(floor, year, month);
            return ResponseEntity.ok(ApiResponse.success("获取月度统计成功", total));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("获取月度统计失败: " + e.getMessage()));
        }
    }
    
    /**
     * 测试端点 - 直接查询六楼数据
     */
    @GetMapping("/test/floor6")
    public ResponseEntity<ApiResponse<List<RentalRecordsNew>>> testFloor6Data() {
        try {
            System.out.println("=== 测试六楼数据查询 ===");
            
            // 直接使用Repository查询所有六楼数据
            List<RentalRecordsNew> allRecords = rentalRecordsService.getAllNewRecords();
            System.out.println("六楼总记录数: " + allRecords.size());
            
            for (RentalRecordsNew record : allRecords) {
                System.out.println("记录: " + record.toString());
            }
            
            return ResponseEntity.ok(ApiResponse.success("测试六楼数据成功", allRecords));
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ApiResponse.error("测试六楼数据失败: " + e.getMessage()));
        }
    }
    
    /**
     * 测试端点 - 直接查询五楼数据
     */
    @GetMapping("/test/floor5")
    public ResponseEntity<ApiResponse<List<RentalRecordsOld>>> testFloor5Data() {
        try {
            System.out.println("=== 测试五楼数据查询 ===");
            
            // 直接使用Repository查询所有五楼数据
            List<RentalRecordsOld> allRecords = rentalRecordsService.getAllOldRecords();
            System.out.println("五楼总记录数: " + allRecords.size());
            
            for (RentalRecordsOld record : allRecords) {
                System.out.println("记录: " + record.toString());
            }
            
            return ResponseEntity.ok(ApiResponse.success("测试五楼数据成功", allRecords));
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ApiResponse.error("测试五楼数据失败: " + e.getMessage()));
        }
    }
    
    /**
     * 根据日期范围查询缴费记录
     */
    @GetMapping("/{floor}/date-range")
    public ResponseEntity<ApiResponse<List<?>>> getRecordsByDateRange(
            @PathVariable String floor,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<?> records = rentalRecordsService.getRecordsByDateRange(floor, startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success("获取日期范围缴费记录成功", records));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("获取日期范围缴费记录失败: " + e.getMessage()));
        }
    }
} 