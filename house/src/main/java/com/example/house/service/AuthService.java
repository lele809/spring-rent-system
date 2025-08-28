package com.example.house.service;

import com.example.house.dto.LoginRequest;
import com.example.house.entity.Admin;
import com.example.house.repository.AdminRepository;
import com.example.house.util.PasswordVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 认证服务
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final AdminRepository adminRepository;
    private final PasswordVerifier passwordVerifier;
    
    /**
     * 管理员登录 - 支持多种密码格式验证
     */
    public Optional<Admin> login(LoginRequest loginRequest) {
        Optional<Admin> adminOpt = adminRepository.findByAdminName(loginRequest.getAdminName());
        
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            String storedHash = admin.getPassword();
            String inputPassword = loginRequest.getPassword();
            
            // 使用密码验证工具验证密码
            boolean passwordMatches = passwordVerifier.verifyPassword(inputPassword, storedHash);
            
            if (passwordMatches) {
                // 更新最后登录时间
                admin.setLastLogin(LocalDateTime.now());
                adminRepository.save(admin);
                return Optional.of(admin);
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * 创建管理员账号（初始化用）- 暂时使用明文密码
     */
    public Admin createAdmin(String adminName, String password) {
        Admin admin = new Admin();
        admin.setAdminName(adminName);
        admin.setPassword(password); // 暂时使用明文密码，便于测试
        admin.setLastLogin(LocalDateTime.now());
        return adminRepository.save(admin);
    }
    
    /**
     * 检查管理员是否存在
     */
    public boolean adminExists(String adminName) {
        return adminRepository.findByAdminName(adminName).isPresent();
    }
    
    /**
     * 获取管理员总数
     */
    public long countAdmins() {
        return adminRepository.count();
    }
    
    /**
     * 获取所有管理员列表
     */
    public java.util.List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
    
    /**
     * 更新管理员信息
     */
    public Admin updateAdmin(Integer adminId, String adminName, String password) {
        Optional<Admin> adminOpt = adminRepository.findById(adminId);
        if (adminOpt.isEmpty()) {
            throw new RuntimeException("管理员不存在");
        }
        
        Admin admin = adminOpt.get();
        
        // 检查用户名是否被其他管理员占用
        Optional<Admin> existingAdmin = adminRepository.findByAdminName(adminName);
        if (existingAdmin.isPresent() && !existingAdmin.get().getId().equals(adminId)) {
            throw new RuntimeException("管理员名称已被其他用户使用");
        }
        
        admin.setAdminName(adminName);
        
        // 如果提供了新密码，则更新密码
        if (password != null && !password.trim().isEmpty()) {
            admin.setPassword(password);
        }
        
        return adminRepository.save(admin);
    }
    
    /**
     * 删除管理员
     */
    public void deleteAdmin(Integer adminId) {
        if (!adminRepository.existsById(adminId)) {
            throw new RuntimeException("管理员不存在");
        }
        
        // 检查是否为最后一个管理员
        long adminCount = adminRepository.count();
        if (adminCount <= 1) {
            throw new RuntimeException("不能删除最后一个管理员");
        }
        
        adminRepository.deleteById(adminId);
    }
    
    /**
     * 根据ID查找管理员
     */
    public Optional<Admin> findById(Integer adminId) {
        return adminRepository.findById(adminId);
    }
}