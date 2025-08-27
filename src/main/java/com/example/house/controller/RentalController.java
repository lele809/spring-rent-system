package com.example.house.controller;

import com.example.house.dto.ApiResponse;
import com.example.house.dto.MonthlyIncomeData;
import com.example.house.dto.PaymentQuery;
import com.example.house.dto.PaymentStats;
import com.example.house.entity.RentalNew;
import com.example.house.entity.RentalOld;
import com.example.house.repository.RentalNewRepository;
import com.example.house.repository.RentalOldRepository;
import com.example.house.repository.RentalRecordsNewRepository;
import com.example.house.repository.RentalRecordsOldRepository;
import com.example.house.service.RentalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

/**
 * 租房管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/rentals")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"}, allowCredentials = "true")
@RequiredArgsConstructor
public class RentalController {
    
    private final RentalService rentalService;
    private final RentalNewRepository rentalNewRepository;
    private final RentalOldRepository rentalOldRepository;
    private final RentalRecordsNewRepository rentalRecordsNewRepository;
    private final RentalRecordsOldRepository rentalRecordsOldRepository;
    
    /**
     * 测试端点 - 简单的查询所有记录
     */
    @GetMapping("/{floor}/test")
    public ResponseEntity<ApiResponse<List<?>>> testGetAllRentals(@PathVariable String floor) {
        try {
            log.info("测试获取{}楼所有租房记录", floor);
            
            List<?> result;
            if ("6".equals(floor)) {
                result = rentalNewRepository.findAll();
            } else if ("5".equals(floor)) {
                result = rentalOldRepository.findAll();
            } else {
                return ResponseEntity.ok(ApiResponse.error("不支持的楼层: " + floor));
            }
            
            log.info("测试查询完成，返回{}条记录", result.size());
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            log.error("测试查询失败", e);
            return ResponseEntity.ok(ApiResponse.error("测试查询失败: " + e.getMessage()));
        }
    }

    /**
     * 获取租房记录列表（分页）
     */
    @GetMapping("/{floor}")
    public ResponseEntity<ApiResponse<Page<?>>> getRentals(
            @PathVariable String floor,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer paymentStatus,
            @RequestParam(required = false) String feeType,
            @RequestParam(required = false) String monthFilter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        try {
            log.info("开始获取租房记录 - 楼层: {}, 页码: {}, 大小: {}", floor, page, size);
            log.info("请求参数 - keyword: {}, paymentStatus: {}, feeType: {}, monthFilter: {}", 
                     keyword, paymentStatus, feeType, monthFilter);
            
            // 验证楼层参数
            if (!"5".equals(floor) && !"6".equals(floor)) {
                log.error("不支持的楼层参数: {}", floor);
                return ResponseEntity.ok(ApiResponse.error("不支持的楼层: " + floor));
            }
            
            PaymentQuery query = new PaymentQuery();
            query.setKeyword(keyword);
            query.setPaymentStatus(paymentStatus);
            query.setFeeType(feeType);
            query.setMonthFilter(monthFilter);
            query.setPage(page);
            query.setSize(size);
            query.setSortBy(sortBy);
            query.setSortDirection(sortDirection);
            
            log.info("查询参数: {}", query);
            
            Page<?> result = rentalService.getRentalsByFloor(floor, query);
            log.info("查询结果 - 总数: {}, 内容数量: {}", result.getTotalElements(), result.getContent().size());
            
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            log.error("获取租房记录列表失败 - 楼层: " + floor, e);
            return ResponseEntity.ok(ApiResponse.error("获取租房记录列表失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取租房记录详情
     */
    @GetMapping("/{floor}/{id}")
    public ResponseEntity<ApiResponse<Object>> getRentalById(
            @PathVariable String floor,
            @PathVariable Integer id) {
        
        try {
            Optional<?> result = rentalService.getRentalByFloorAndId(floor, id);
            if (result.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(result.get()));
            } else {
                return ResponseEntity.ok(ApiResponse.error("租房记录不存在"));
            }
        } catch (Exception e) {
            log.error("获取租房记录详情失败", e);
            return ResponseEntity.ok(ApiResponse.error("获取租房记录详情失败: " + e.getMessage()));
        }
    }
    
    /**
     * 创建租房记录
     */
    @PostMapping("/{floor}")
    public ResponseEntity<ApiResponse<Object>> createRental(
            @PathVariable String floor,
            @RequestBody Map<String, Object> request) {
        
        try {
            Object rental;
            if ("6".equals(floor)) {
                rental = convertToRentalNew(request);
            } else {
                rental = convertToRentalOld(request);
            }
            
            Object result = rentalService.createRental(floor, rental);
            return ResponseEntity.ok(ApiResponse.success("创建成功", result));
        } catch (Exception e) {
            log.error("创建租房记录失败", e);
            return ResponseEntity.ok(ApiResponse.error("创建租房记录失败: " + e.getMessage()));
        }
    }
    
    /**
     * 更新租房记录
     */
    @PutMapping("/{floor}/{id}")
    public ResponseEntity<ApiResponse<Object>> updateRental(
            @PathVariable String floor,
            @PathVariable Integer id,
            @RequestBody Map<String, Object> request) {
        
        try {
            Object rental;
            if ("6".equals(floor)) {
                rental = convertToRentalNew(request);
            } else {
                rental = convertToRentalOld(request);
            }
            
            Object result = rentalService.updateRental(floor, id, rental);
            return ResponseEntity.ok(ApiResponse.success("更新成功", result));
        } catch (Exception e) {
            log.error("更新租房记录失败", e);
            return ResponseEntity.ok(ApiResponse.error("更新租房记录失败: " + e.getMessage()));
        }
    }
    
    /**
     * 删除租房记录
     */
    @DeleteMapping("/{floor}/{id}")
    public ResponseEntity<ApiResponse<String>> deleteRental(
            @PathVariable String floor,
            @PathVariable Integer id) {
        
        try {
            rentalService.deleteRental(floor, id);
            return ResponseEntity.ok(ApiResponse.success("删除成功"));
        } catch (Exception e) {
            log.error("删除租房记录失败", e);
            return ResponseEntity.ok(ApiResponse.error("删除租房记录失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取费用统计信息
     */
    @GetMapping("/{floor}/stats")
    public ResponseEntity<ApiResponse<PaymentStats>> getRentalStats(
            @PathVariable String floor) {
        
        try {
            PaymentStats stats = rentalService.getPaymentStats(floor);
            return ResponseEntity.ok(ApiResponse.success(stats));
        } catch (Exception e) {
            log.error("获取费用统计失败", e);
            return ResponseEntity.ok(ApiResponse.error("获取费用统计失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取月度收入趋势数据
     */
    @GetMapping("/{floor}/monthly-income")
    public ResponseEntity<ApiResponse<MonthlyIncomeData>> getMonthlyIncomeData(
            @PathVariable String floor,
            @RequestParam(defaultValue = "6") int months) {
        
        try {
            log.info("获取{}楼最近{}个月的收入趋势数据", floor, months);
            
            // 验证参数
            if (months < 1 || months > 24) {
                return ResponseEntity.ok(ApiResponse.error("月份数量必须在1-24之间"));
            }
            
            MonthlyIncomeData incomeData = rentalService.getMonthlyIncomeData(floor, months);
            return ResponseEntity.ok(ApiResponse.success(incomeData));
        } catch (Exception e) {
            log.error("获取月度收入趋势数据失败", e);
            return ResponseEntity.ok(ApiResponse.error("获取月度收入趋势数据失败: " + e.getMessage()));
        }
    }
    
    /**
     * 测试端点 - 调试月度收入数据
     */
    @GetMapping("/{floor}/test-monthly-income")
    public ResponseEntity<ApiResponse<Map<String, Object>>> testMonthlyIncomeData(
            @PathVariable String floor) {
        
        try {
            log.info("测试{}楼月度收入数据查询", floor);
            
            Map<String, Object> testResult = new HashMap<>();
            testResult.put("floor", floor);
            
            // 测试当前月份的数据
            int currentYear = java.time.Year.now().getValue();
            int currentMonth = java.time.MonthDay.now().getMonthValue();
            
            // 直接调用Repository方法测试
            Double currentMonthIncome;
            if ("6".equals(floor)) {
                currentMonthIncome = rentalRecordsNewRepository.getTotalRentByMonth(currentYear, currentMonth);
                testResult.put("newRecordsCount", rentalNewRepository.count());
            } else {
                currentMonthIncome = rentalRecordsOldRepository.getTotalRentByMonth(currentYear, currentMonth);
                testResult.put("oldRecordsCount", rentalOldRepository.count());
            }
            
            testResult.put("currentYear", currentYear);
            testResult.put("currentMonth", currentMonth);
            testResult.put("currentMonthIncome", currentMonthIncome);
            
            // 测试前几个月的数据
            for (int i = 1; i <= 3; i++) {
                int testYear = currentYear;
                int testMonth = currentMonth - i;
                if (testMonth <= 0) {
                    testYear--;
                    testMonth += 12;
                }
                
                Double monthIncome;
                if ("6".equals(floor)) {
                    monthIncome = rentalRecordsNewRepository.getTotalRentByMonth(testYear, testMonth);
                } else {
                    monthIncome = rentalRecordsOldRepository.getTotalRentByMonth(testYear, testMonth);
                }
                
                testResult.put(String.format("%d-%02d", testYear, testMonth), monthIncome);
            }
            
            return ResponseEntity.ok(ApiResponse.success(testResult));
        } catch (Exception e) {
            log.error("测试月度收入数据失败", e);
            return ResponseEntity.ok(ApiResponse.error("测试失败: " + e.getMessage()));
        }
    }
    
    /**
     * 更新支付状态
     */
    @PatchMapping("/{floor}/{id}/status")
    public ResponseEntity<ApiResponse<String>> updateRentalStatus(
            @PathVariable String floor,
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> request) {
        
        try {
            Integer status = request.get("status");
            if (status == null) {
                return ResponseEntity.ok(ApiResponse.error("支付状态不能为空"));
            }
            
            rentalService.updatePaymentStatus(floor, id, status);
            return ResponseEntity.ok(ApiResponse.success("状态更新成功"));
        } catch (Exception e) {
            log.error("更新支付状态失败", e);
            return ResponseEntity.ok(ApiResponse.error("更新支付状态失败: " + e.getMessage()));
        }
    }
    
    /**
     * 批量更新支付状态
     */
    @PatchMapping("/{floor}/batch/status")
    public ResponseEntity<ApiResponse<String>> batchUpdateRentalStatus(
            @PathVariable String floor,
            @RequestBody Map<String, Object> request) {
        
        try {
            @SuppressWarnings("unchecked")
            List<Integer> ids = (List<Integer>) request.get("ids");
            Integer status = (Integer) request.get("status");
            
            if (ids == null || ids.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.error("请选择要更新的记录"));
            }
            if (status == null) {
                return ResponseEntity.ok(ApiResponse.error("支付状态不能为空"));
            }
            
            rentalService.batchUpdatePaymentStatus(floor, ids, status);
            return ResponseEntity.ok(ApiResponse.success("批量更新成功"));
        } catch (Exception e) {
            log.error("批量更新支付状态失败", e);
            return ResponseEntity.ok(ApiResponse.error("批量更新支付状态失败: " + e.getMessage()));
        }
    }
    
    /**
     * 导出租房数据
     */
    @GetMapping("/{floor}/export")
    public ResponseEntity<ApiResponse<List<?>>> exportRentals(
            @PathVariable String floor,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer paymentStatus,
            @RequestParam(required = false) String feeType,
            @RequestParam(required = false) String monthFilter) {
        
        try {
            PaymentQuery query = new PaymentQuery();
            query.setKeyword(keyword);
            query.setPaymentStatus(paymentStatus);
            query.setFeeType(feeType);
            query.setMonthFilter(monthFilter);
            
            List<?> result = rentalService.exportPaymentData(floor, query);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            log.error("导出租房数据失败", e);
            return ResponseEntity.ok(ApiResponse.error("导出租房数据失败: " + e.getMessage()));
        }
    }
    
    /**
     * 获取即将到期的合同
     */
    @GetMapping("/{floor}/expiring")
    public ResponseEntity<ApiResponse<List<?>>> getExpiringContracts(
            @PathVariable String floor,
            @RequestParam(defaultValue = "30") int days) {
        
        try {
            List<?> result = rentalService.getExpiringContracts(floor, days);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            log.error("获取即将到期合同失败", e);
            return ResponseEntity.ok(ApiResponse.error("获取即将到期合同失败: " + e.getMessage()));
        }
    }
    
    /**
     * 转换为RentalNew对象
     */
    private RentalNew convertToRentalNew(Map<String, Object> request) {
        RentalNew rental = new RentalNew();
        // 基本信息
        rental.setRoomNumber((String) request.get("roomNumber"));
        rental.setTenantName((String) request.get("tenantName"));
        
        // 费用信息
        if (request.get("monthlyRent") != null) {
            rental.setMonthlyRent(new java.math.BigDecimal(request.get("monthlyRent").toString()));
        }
        if (request.get("deposit") != null) {
            rental.setDeposit(new java.math.BigDecimal(request.get("deposit").toString()));
        }
        if (request.get("waterFee") != null) {
            rental.setWaterFee(new java.math.BigDecimal(request.get("waterFee").toString()));
        }
        if (request.get("electricityFee") != null) {
            rental.setElectricityFee(new java.math.BigDecimal(request.get("electricityFee").toString()));
        }
        if (request.get("utilitiesFee") != null) {
            rental.setUtilitiesFee(new java.math.BigDecimal(request.get("utilitiesFee").toString()));
        }
        if (request.get("totalDue") != null) {
            rental.setTotalDue(new java.math.BigDecimal(request.get("totalDue").toString()));
        }
        
        // 状态信息
        if (request.get("paymentStatus") != null) {
            rental.setPaymentStatus(Integer.valueOf(request.get("paymentStatus").toString()));
        }
        
        // 日期信息
        if (request.get("checkInDate") != null && !request.get("checkInDate").toString().trim().isEmpty()) {
            rental.setCheckInDate(java.time.LocalDate.parse(request.get("checkInDate").toString()));
        }
        if (request.get("checkOutDate") != null && !request.get("checkOutDate").toString().trim().isEmpty()) {
            rental.setCheckOutDate(java.time.LocalDate.parse(request.get("checkOutDate").toString()));
        }
        if (request.get("contractStartDate") != null && !request.get("contractStartDate").toString().trim().isEmpty()) {
            rental.setContractStartDate(java.time.LocalDate.parse(request.get("contractStartDate").toString()));
        }
        if (request.get("contractEndDate") != null && !request.get("contractEndDate").toString().trim().isEmpty()) {
            rental.setContractEndDate(java.time.LocalDate.parse(request.get("contractEndDate").toString()));
        }
        
        // 备注
        rental.setRemarks((String) request.get("remarks"));
        
        return rental;
    }
    
    /**
     * 转换为RentalOld对象
     */
    private RentalOld convertToRentalOld(Map<String, Object> request) {
        RentalOld rental = new RentalOld();
        // 基本信息
        rental.setRoomNumber((String) request.get("roomNumber"));
        rental.setTenantName((String) request.get("tenantName"));
        
        // 费用信息
        if (request.get("monthlyRent") != null) {
            rental.setMonthlyRent(new java.math.BigDecimal(request.get("monthlyRent").toString()));
        }
        if (request.get("deposit") != null) {
            rental.setDeposit(new java.math.BigDecimal(request.get("deposit").toString()));
        }
        if (request.get("waterFee") != null) {
            rental.setWaterFee(new java.math.BigDecimal(request.get("waterFee").toString()));
        }
        if (request.get("electricityFee") != null) {
            rental.setElectricityFee(new java.math.BigDecimal(request.get("electricityFee").toString()));
        }
        if (request.get("utilitiesFee") != null) {
            rental.setUtilitiesFee(new java.math.BigDecimal(request.get("utilitiesFee").toString()));
        }
        if (request.get("totalDue") != null) {
            rental.setTotalDue(new java.math.BigDecimal(request.get("totalDue").toString()));
        }
        
        // 状态信息
        if (request.get("paymentStatus") != null) {
            rental.setPaymentStatus(Integer.valueOf(request.get("paymentStatus").toString()));
        }
        
        // 日期信息
        if (request.get("checkInDate") != null && !request.get("checkInDate").toString().trim().isEmpty()) {
            rental.setCheckInDate(java.time.LocalDate.parse(request.get("checkInDate").toString()));
        }
        if (request.get("checkOutDate") != null && !request.get("checkOutDate").toString().trim().isEmpty()) {
            rental.setCheckOutDate(java.time.LocalDate.parse(request.get("checkOutDate").toString()));
        }
        if (request.get("contractStartDate") != null && !request.get("contractStartDate").toString().trim().isEmpty()) {
            rental.setContractStartDate(java.time.LocalDate.parse(request.get("contractStartDate").toString()));
        }
        if (request.get("contractEndDate") != null && !request.get("contractEndDate").toString().trim().isEmpty()) {
            rental.setContractEndDate(java.time.LocalDate.parse(request.get("contractEndDate").toString()));
        }
        
        // 备注
        rental.setRemarks((String) request.get("remarks"));
        
        return rental;
    }
}