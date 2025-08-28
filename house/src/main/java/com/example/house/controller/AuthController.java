package com.example.house.controller;

import com.example.house.dto.ApiResponse;
import com.example.house.dto.LoginRequest;
import com.example.house.entity.Admin;
import com.example.house.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"}, allowCredentials = "true")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public ApiResponse<Admin> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("收到登录请求: " + loginRequest.getAdminName());
            
            Optional<Admin> admin = authService.login(loginRequest);
            
            if (admin.isPresent()) {
                // 不返回密码
                Admin adminData = admin.get();
                adminData.setPassword(null);
                System.out.println("登录成功: " + adminData.getAdminName());
                return ApiResponse.success("登录成功", adminData);
            } else {
                System.out.println("登录失败: 用户名或密码错误");
                return ApiResponse.error(401, "用户名或密码错误");
            }
        } catch (Exception e) {
            System.err.println("登录过程中发生异常: " + e.getMessage());
            e.printStackTrace();
            return ApiResponse.error(500, "登录失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建管理员账号（测试用）
     */
    @PostMapping("/create-admin")
    public ApiResponse<String> createAdmin(@RequestParam String adminName, @RequestParam String password) {
        if (authService.adminExists(adminName)) {
            return ApiResponse.error("管理员已存在");
        }
        
        authService.createAdmin(adminName, password);
        return ApiResponse.success("管理员账号创建成功");
    }
    
    /**
     * 测试接口
     */
    @GetMapping("/test")
    public ApiResponse<String> test() {
        return ApiResponse.success("API正常运行");
    }
    
    /**
     * 测试数据库连接
     */
    @GetMapping("/test-db")
    public ApiResponse<String> testDatabase() {
        try {
            // 简单测试：查找所有管理员
            long adminCount = authService.countAdmins();
            return ApiResponse.success("数据库连接正常，管理员数量: " + adminCount);
        } catch (Exception e) {
            System.err.println("数据库连接测试失败: " + e.getMessage());
            e.printStackTrace();
            return ApiResponse.error("数据库连接失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取密码提示（仅用于开发调试）
     */
    @GetMapping("/password-hint/{username}")
    public ApiResponse<String> getPasswordHint(@PathVariable String username) {
        String hint = "请尝试以下常见密码：\n" +
                     "1. 123456\n" +
                     "2. admin\n" +
                     "3. password\n" +
                     "4. 用户名相同的密码\n" +
                     "5. 123, 111111, 000000";
        return ApiResponse.success(hint);
    }
    
    /**
     * 获取所有管理员列表
     */
    @GetMapping("/admin-list")
    public ApiResponse<java.util.List<Admin>> getAdminList() {
        try {
            java.util.List<Admin> adminList = authService.getAllAdmins();
            return ApiResponse.success(adminList);
        } catch (Exception e) {
            System.err.println("获取管理员列表失败: " + e.getMessage());
            e.printStackTrace();
            return ApiResponse.error("获取管理员列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新管理员信息
     */
    @PostMapping("/update-admin/{id}")
    public ApiResponse<String> updateAdmin(@PathVariable Integer id, @RequestBody AdminUpdateRequest request) {
        try {
            System.out.println("收到更新管理员请求，ID: " + id + ", 管理员名: " + request.getAdminName());
            authService.updateAdmin(id, request.getAdminName(), request.getPassword());
            return ApiResponse.success("管理员信息更新成功");
        } catch (Exception e) {
            System.err.println("更新管理员失败: " + e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 删除管理员
     */
    @PostMapping("/delete-admin/{id}")
    public ApiResponse<String> deleteAdmin(@PathVariable Integer id) {
        try {
            System.out.println("收到删除管理员请求，ID: " + id);
            authService.deleteAdmin(id);
            return ApiResponse.success("管理员删除成功");
        } catch (Exception e) {
            System.err.println("删除管理员失败: " + e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 管理员更新请求DTO
     */
    public static class AdminUpdateRequest {
        private String adminName;
        private String password;
        
        public String getAdminName() {
            return adminName;
        }
        
        public void setAdminName(String adminName) {
            this.adminName = adminName;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
    }
}