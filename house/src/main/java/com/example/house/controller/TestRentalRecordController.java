package com.example.house.controller;

import com.example.house.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * 测试用的缴费记录控制器 - 使用JDBC直接插入
 */
@RestController
@RequestMapping("/api/test-rental-records")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"}, allowCredentials = "true")
public class TestRentalRecordController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 测试新增缴费记录（新楼）- 使用JDBC
     */
    @PostMapping("/new")
    public ResponseEntity<ApiResponse<String>> testAddNewRecord(@RequestBody Map<String, Object> recordData) {
        try {
            System.out.println("=== 测试新增六楼缴费记录 (JDBC) ===");
            System.out.println("接收到的数据: " + recordData);
            
            String roomNumber = (String) recordData.get("roomNumber");
            String tenantName = (String) recordData.get("tenantName");
            Object totalRentObj = recordData.get("totalRent");
            String paymentDateStr = (String) recordData.get("paymentDate");
            
            // 数据验证
            if (roomNumber == null || roomNumber.trim().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.error("房间号不能为空"));
            }
            if (tenantName == null || tenantName.trim().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.error("租客姓名不能为空"));
            }
            if (totalRentObj == null) {
                return ResponseEntity.ok(ApiResponse.error("缴费金额不能为空"));
            }
            if (paymentDateStr == null || paymentDateStr.trim().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.error("缴费日期不能为空"));
            }
            
            // 转换金额
            BigDecimal totalRent;
            if (totalRentObj instanceof Number) {
                totalRent = new BigDecimal(totalRentObj.toString());
            } else if (totalRentObj instanceof String) {
                totalRent = new BigDecimal((String) totalRentObj);
            } else {
                return ResponseEntity.ok(ApiResponse.error("缴费金额格式错误"));
            }
            
            if (totalRent.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.ok(ApiResponse.error("缴费金额必须大于0"));
            }
            
            // 转换日期
            LocalDate paymentDate = LocalDate.parse(paymentDateStr);
            
            System.out.println("准备插入数据:");
            System.out.println("房间号: " + roomNumber);
            System.out.println("租客姓名: " + tenantName);
            System.out.println("总租金: " + totalRent);
            System.out.println("缴费日期: " + paymentDate);
            
            // 使用JDBC直接插入
            String sql = "INSERT INTO rental_records_new (room_number, tenant_name, total_rent, payment_date) VALUES (?, ?, ?, ?)";
            int rowsAffected = jdbcTemplate.update(sql, roomNumber, tenantName, totalRent, paymentDate);
            
            System.out.println("插入成功，影响行数: " + rowsAffected);
            
            return ResponseEntity.ok(ApiResponse.success("测试新增缴费记录成功", "插入了 " + rowsAffected + " 行数据"));
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("详细错误信息: " + e.toString());
            if (e.getCause() != null) {
                System.err.println("错误原因: " + e.getCause().toString());
            }
            return ResponseEntity.ok(ApiResponse.error("测试新增缴费记录失败: " + e.getMessage()));
        }
    }

    /**
     * 测试新增缴费记录（旧楼）- 使用JDBC
     */
    @PostMapping("/old")
    public ResponseEntity<ApiResponse<String>> testAddOldRecord(@RequestBody Map<String, Object> recordData) {
        try {
            System.out.println("=== 测试新增五楼缴费记录 (JDBC) ===");
            System.out.println("接收到的数据: " + recordData);
            
            String roomNumber = (String) recordData.get("roomNumber");
            String tenantName = (String) recordData.get("tenantName");
            Object totalRentObj = recordData.get("totalRent");
            String paymentDateStr = (String) recordData.get("paymentDate");
            
            // 数据验证
            if (roomNumber == null || roomNumber.trim().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.error("房间号不能为空"));
            }
            if (tenantName == null || tenantName.trim().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.error("租客姓名不能为空"));
            }
            if (totalRentObj == null) {
                return ResponseEntity.ok(ApiResponse.error("缴费金额不能为空"));
            }
            if (paymentDateStr == null || paymentDateStr.trim().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.error("缴费日期不能为空"));
            }
            
            // 转换金额
            BigDecimal totalRent;
            if (totalRentObj instanceof Number) {
                totalRent = new BigDecimal(totalRentObj.toString());
            } else if (totalRentObj instanceof String) {
                totalRent = new BigDecimal((String) totalRentObj);
            } else {
                return ResponseEntity.ok(ApiResponse.error("缴费金额格式错误"));
            }
            
            if (totalRent.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.ok(ApiResponse.error("缴费金额必须大于0"));
            }
            
            // 转换日期
            LocalDate paymentDate = LocalDate.parse(paymentDateStr);
            
            System.out.println("准备插入数据:");
            System.out.println("房间号: " + roomNumber);
            System.out.println("租客姓名: " + tenantName);
            System.out.println("总租金: " + totalRent);
            System.out.println("缴费日期: " + paymentDate);
            
            // 使用JDBC直接插入
            String sql = "INSERT INTO rental_records_old (room_number, tenant_name, total_rent, payment_date) VALUES (?, ?, ?, ?)";
            int rowsAffected = jdbcTemplate.update(sql, roomNumber, tenantName, totalRent, paymentDate);
            
            System.out.println("插入成功，影响行数: " + rowsAffected);
            
            return ResponseEntity.ok(ApiResponse.success("测试新增缴费记录成功", "插入了 " + rowsAffected + " 行数据"));
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("详细错误信息: " + e.toString());
            if (e.getCause() != null) {
                System.err.println("错误原因: " + e.getCause().toString());
            }
            return ResponseEntity.ok(ApiResponse.error("测试新增缴费记录失败: " + e.getMessage()));
        }
    }
} 