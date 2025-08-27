package com.example.house.controller;

import com.example.house.dto.ApiResponse;
import com.example.house.entity.RentalNew;
import com.example.house.entity.RentalOld;
import com.example.house.entity.ContactsNew;
import com.example.house.entity.ContactsOld;
import com.example.house.entity.RoomsNew;
import com.example.house.entity.RoomsOld;
import com.example.house.repository.RentalNewRepository;
import com.example.house.repository.RentalOldRepository;
import com.example.house.repository.ContactsNewRepository;
import com.example.house.repository.ContactsOldRepository;
import com.example.house.repository.RoomsNewRepository;
import com.example.house.repository.RoomsOldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 测试控制器 - 用于验证数据库连接和数据
 */
@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    
    private final RentalNewRepository rentalNewRepository;
    private final RentalOldRepository rentalOldRepository;
    private final ContactsNewRepository contactsNewRepository;
    private final ContactsOldRepository contactsOldRepository;
    private final RoomsNewRepository roomsNewRepository;
    private final RoomsOldRepository roomsOldRepository;
    
    /**
     * 测试数据库连接
     */
    @GetMapping("/db-connection")
    public ResponseEntity<ApiResponse<String>> testDatabaseConnection() {
        try {
            long count = contactsNewRepository.count();
            return ResponseEntity.ok(ApiResponse.success("数据库连接正常，六楼联系人数量: " + count));
        } catch (Exception e) {
            log.error("数据库连接测试失败", e);
            return ResponseEntity.ok(ApiResponse.error("数据库连接失败: " + e.getMessage()));
        }
    }
    
    /**
     * 测试六楼联系人数据
     */
    @GetMapping("/contacts-new")
    public ResponseEntity<ApiResponse<List<ContactsNew>>> testContactsNew() {
        try {
            List<ContactsNew> contacts = contactsNewRepository.findAll();
            log.info("六楼联系人数据数量: {}", contacts.size());
            if (!contacts.isEmpty()) {
                ContactsNew first = contacts.get(0);
                log.info("第一条数据: id={}, name={}, roomId={}, phone={}", 
                    first.getId(), first.getName(), first.getRoomId(), first.getPhone());
            }
            return ResponseEntity.ok(ApiResponse.success("六楼联系人数据获取成功", contacts));
        } catch (Exception e) {
            log.error("获取六楼联系人数据失败", e);
            return ResponseEntity.ok(ApiResponse.error("获取六楼联系人数据失败: " + e.getMessage()));
        }
    }
    
    /**
     * 测试五楼联系人数据
     */
    @GetMapping("/contacts-old")
    public ResponseEntity<ApiResponse<List<ContactsOld>>> testContactsOld() {
        try {
            List<ContactsOld> contacts = contactsOldRepository.findAll();
            log.info("五楼联系人数据数量: {}", contacts.size());
            return ResponseEntity.ok(ApiResponse.success("五楼联系人数据获取成功", contacts));
        } catch (Exception e) {
            log.error("获取五楼联系人数据失败", e);
            return ResponseEntity.ok(ApiResponse.error("获取五楼联系人数据失败: " + e.getMessage()));
        }
    }
    
    /**
     * 测试六楼数据
     */
    @GetMapping("/rental-new")
    public ResponseEntity<ApiResponse<List<RentalNew>>> testRentalNew() {
        try {
            List<RentalNew> rentals = rentalNewRepository.findAll();
            log.info("六楼数据数量: {}", rentals.size());
            return ResponseEntity.ok(ApiResponse.success("六楼数据获取成功", rentals));
        } catch (Exception e) {
            log.error("获取六楼数据失败", e);
            return ResponseEntity.ok(ApiResponse.error("获取六楼数据失败: " + e.getMessage()));
        }
    }
    
    /**
     * 测试五楼数据
     */
    @GetMapping("/rental-old")
    public ResponseEntity<ApiResponse<List<RentalOld>>> testRentalOld() {
        try {
            List<RentalOld> rentals = rentalOldRepository.findAll();
            log.info("五楼数据数量: {}", rentals.size());
            return ResponseEntity.ok(ApiResponse.success("五楼数据获取成功", rentals));
        } catch (Exception e) {
            log.error("获取五楼数据失败", e);
            return ResponseEntity.ok(ApiResponse.error("获取五楼数据失败: " + e.getMessage()));
        }
    }

    /**
     * 测试六楼房间数据
     */
    @GetMapping("/rooms-new")
    public ResponseEntity<ApiResponse<List<RoomsNew>>> testRoomsNew() {
        try {
            List<RoomsNew> rooms = roomsNewRepository.findAll();
            log.info("六楼房间数据数量: {}", rooms.size());
            if (!rooms.isEmpty()) {
                RoomsNew first = rooms.get(0);
                log.info("第一条房间数据: id={}, roomNumber={}, roomType={}, roomStatus={}", 
                    first.getId(), first.getRoomNumber(), first.getRoomType(), first.getRoomStatus());
            }
            return ResponseEntity.ok(ApiResponse.success("六楼房间数据获取成功", rooms));
        } catch (Exception e) {
            log.error("获取六楼房间数据失败", e);
            return ResponseEntity.ok(ApiResponse.error("获取六楼房间数据失败: " + e.getMessage()));
        }
    }
    
    /**
     * 测试五楼房间数据
     */
    @GetMapping("/rooms-old")
    public ResponseEntity<ApiResponse<List<RoomsOld>>> testRoomsOld() {
        try {
            List<RoomsOld> rooms = roomsOldRepository.findAll();
            log.info("五楼房间数据数量: {}", rooms.size());
            return ResponseEntity.ok(ApiResponse.success("五楼房间数据获取成功", rooms));
        } catch (Exception e) {
            log.error("获取五楼房间数据失败", e);
            return ResponseEntity.ok(ApiResponse.error("获取五楼房间数据失败: " + e.getMessage()));
        }
    }
} 