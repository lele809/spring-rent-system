package com.example.house.controller;

import com.example.house.dto.ApiResponse;
import com.example.house.entity.ContactsNew;
import com.example.house.entity.ContactsOld;
import com.example.house.repository.ContactsNewRepository;
import com.example.house.repository.ContactsOldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 租客管理控制器
 */
@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantsController {
    
    private final ContactsOldRepository contactsOldRepository;
    private final ContactsNewRepository contactsNewRepository;
    
    // ==================== 五楼租客管理 ====================
    
    /**
     * 获取五楼租客列表（分页）
     */
    @GetMapping("/old")
    public ApiResponse<Page<ContactsOld>> getOldTenants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ContactsOld> tenants = contactsOldRepository.findAll(pageable);
        return ApiResponse.success(tenants);
    }
    
    /**
     * 搜索五楼租客
     */
    @GetMapping("/old/search")
    public ApiResponse<List<ContactsOld>> searchOldTenants(@RequestParam String keyword) {
        List<ContactsOld> tenants = contactsOldRepository.searchByKeyword(keyword);
        return ApiResponse.success(tenants);
    }
    
    /**
     * 根据ID获取五楼租客详情
     */
    @GetMapping("/old/{id}")
    public ApiResponse<ContactsOld> getOldTenantById(@PathVariable Integer id) {
        Optional<ContactsOld> tenant = contactsOldRepository.findById(id);
        if (tenant.isPresent()) {
            return ApiResponse.success(tenant.get());
        } else {
            return ApiResponse.error(404, "租客不存在");
        }
    }
    
    /**
     * 创建五楼租客
     */
    @PostMapping("/old")
    public ApiResponse<ContactsOld> createOldTenant(@RequestBody @Validated ContactsOld tenant) {
        try {
            System.out.println("=== 创建五楼联系人请求 ===");
            System.out.println("接收到的数据: " + tenant);
            System.out.println("姓名: " + tenant.getName());
            System.out.println("房号: " + tenant.getRoomId());
            System.out.println("电话: " + tenant.getPhone());
            System.out.println("身份证: " + tenant.getIdCard());
            
            // 检查身份证号是否已存在
            if (contactsOldRepository.existsByIdCard(tenant.getIdCard())) {
                System.out.println("身份证号已存在: " + tenant.getIdCard());
                return ApiResponse.error(400, "身份证号已存在");
            }
            
            // 检查电话号码是否已存在
            if (contactsOldRepository.findByPhone(tenant.getPhone()).isPresent()) {
                System.out.println("电话号码已存在: " + tenant.getPhone());
                return ApiResponse.error(400, "电话号码已存在");
            }
            
            System.out.println("开始保存到数据库...");
            ContactsOld savedTenant = contactsOldRepository.save(tenant);
            System.out.println("保存成功，ID: " + savedTenant.getId());
            return ApiResponse.success("创建成功", savedTenant);
        } catch (Exception e) {
            System.err.println("创建失败，错误详情: ");
            e.printStackTrace();
            return ApiResponse.error(500, "创建失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新五楼租客
     */
    @PutMapping("/old/{id}")
    public ApiResponse<ContactsOld> updateOldTenant(@PathVariable Integer id, @RequestBody @Validated ContactsOld tenant) {
        try {
            Optional<ContactsOld> existingTenant = contactsOldRepository.findById(id);
            if (existingTenant.isEmpty()) {
                return ApiResponse.error(404, "租客不存在");
            }
            
            ContactsOld existing = existingTenant.get();
            
            // 检查身份证号是否已被其他租客使用
            Optional<ContactsOld> tenantWithSameIdCard = contactsOldRepository.findByIdCard(tenant.getIdCard());
            if (tenantWithSameIdCard.isPresent() && !tenantWithSameIdCard.get().getId().equals(id)) {
                return ApiResponse.error(400, "身份证号已被其他租客使用");
            }
            
            // 检查电话号码是否已被其他租客使用
            Optional<ContactsOld> tenantWithSamePhone = contactsOldRepository.findByPhone(tenant.getPhone());
            if (tenantWithSamePhone.isPresent() && !tenantWithSamePhone.get().getId().equals(id)) {
                return ApiResponse.error(400, "电话号码已被其他租客使用");
            }
            
            // 更新字段
            existing.setName(tenant.getName());
            existing.setRoomId(tenant.getRoomId());
            existing.setPhone(tenant.getPhone());
            existing.setIdCard(tenant.getIdCard());
            
            ContactsOld savedTenant = contactsOldRepository.save(existing);
            return ApiResponse.success("更新成功", savedTenant);
        } catch (Exception e) {
            return ApiResponse.error(500, "更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除五楼租客
     */
    @DeleteMapping("/old/{id}")
    public ApiResponse<String> deleteOldTenant(@PathVariable Integer id) {
        try {
            if (!contactsOldRepository.existsById(id)) {
                return ApiResponse.error(404, "租客不存在");
            }
            
            contactsOldRepository.deleteById(id);
            return ApiResponse.success("删除成功");
        } catch (Exception e) {
            return ApiResponse.error(500, "删除失败: " + e.getMessage());
        }
    }
    
    // ==================== 六楼租客管理 ====================
    
    /**
     * 获取六楼租客列表（分页）
     */
    @GetMapping("/new")
    public ApiResponse<Page<ContactsNew>> getNewTenants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ContactsNew> tenants = contactsNewRepository.findAll(pageable);
        return ApiResponse.success(tenants);
    }
    
    /**
     * 搜索六楼租客
     */
    @GetMapping("/new/search")
    public ApiResponse<List<ContactsNew>> searchNewTenants(@RequestParam String keyword) {
        List<ContactsNew> tenants = contactsNewRepository.searchByKeyword(keyword);
        return ApiResponse.success(tenants);
    }
    
    /**
     * 根据ID获取六楼租客详情
     */
    @GetMapping("/new/{id}")
    public ApiResponse<ContactsNew> getNewTenantById(@PathVariable Integer id) {
        Optional<ContactsNew> tenant = contactsNewRepository.findById(id);
        if (tenant.isPresent()) {
            return ApiResponse.success(tenant.get());
        } else {
            return ApiResponse.error(404, "租客不存在");
        }
    }
    
    /**
     * 创建六楼租客
     */
    @PostMapping("/new")
    public ApiResponse<ContactsNew> createNewTenant(@RequestBody @Validated ContactsNew tenant) {
        try {
            // 检查身份证号是否已存在
            if (contactsNewRepository.existsByIdCard(tenant.getIdCard())) {
                return ApiResponse.error(400, "身份证号已存在");
            }
            
            // 检查电话号码是否已存在
            if (contactsNewRepository.findByPhone(tenant.getPhone()).isPresent()) {
                return ApiResponse.error(400, "电话号码已存在");
            }
            
            ContactsNew savedTenant = contactsNewRepository.save(tenant);
            return ApiResponse.success("创建成功", savedTenant);
        } catch (Exception e) {
            return ApiResponse.error(500, "创建失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新六楼租客
     */
    @PutMapping("/new/{id}")
    public ApiResponse<ContactsNew> updateNewTenant(@PathVariable Integer id, @RequestBody @Validated ContactsNew tenant) {
        try {
            Optional<ContactsNew> existingTenant = contactsNewRepository.findById(id);
            if (existingTenant.isEmpty()) {
                return ApiResponse.error(404, "租客不存在");
            }
            
            ContactsNew existing = existingTenant.get();
            
            // 检查身份证号是否已被其他租客使用
            Optional<ContactsNew> tenantWithSameIdCard = contactsNewRepository.findByIdCard(tenant.getIdCard());
            if (tenantWithSameIdCard.isPresent() && !tenantWithSameIdCard.get().getId().equals(id)) {
                return ApiResponse.error(400, "身份证号已被其他租客使用");
            }
            
            // 检查电话号码是否已被其他租客使用
            Optional<ContactsNew> tenantWithSamePhone = contactsNewRepository.findByPhone(tenant.getPhone());
            if (tenantWithSamePhone.isPresent() && !tenantWithSamePhone.get().getId().equals(id)) {
                return ApiResponse.error(400, "电话号码已被其他租客使用");
            }
            
            // 更新字段
            existing.setName(tenant.getName());
            existing.setRoomId(tenant.getRoomId());
            existing.setPhone(tenant.getPhone());
            existing.setIdCard(tenant.getIdCard());
            
            ContactsNew savedTenant = contactsNewRepository.save(existing);
            return ApiResponse.success("更新成功", savedTenant);
        } catch (Exception e) {
            return ApiResponse.error(500, "更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除六楼租客
     */
    @DeleteMapping("/new/{id}")
    public ApiResponse<String> deleteNewTenant(@PathVariable Integer id) {
        try {
            if (!contactsNewRepository.existsById(id)) {
                return ApiResponse.error(404, "租客不存在");
            }
            
            contactsNewRepository.deleteById(id);
            return ApiResponse.success("删除成功");
        } catch (Exception e) {
            return ApiResponse.error(500, "删除失败: " + e.getMessage());
        }
    }
}
