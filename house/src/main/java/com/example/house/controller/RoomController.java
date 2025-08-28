package com.example.house.controller;

import com.example.house.dto.ApiResponse;
import com.example.house.entity.RoomsNew;
import com.example.house.entity.RoomsOld;
import com.example.house.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 房间管理控制器
 */
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    
    private final RoomService roomService;
    
    // ==================== 六楼房间管理 ====================
    
    /**
     * 获取六楼房间列表
     */
    @GetMapping("/new")
    public ApiResponse<Page<RoomsNew>> getNewRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer floor) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<RoomsNew> rooms = roomService.getNewRoomsWithFilters(pageable, status, floor);
        return ApiResponse.success(rooms);
    }
    
    /**
     * 根据状态获取六楼房间
     */
    @GetMapping("/new/status/{status}")
    public ApiResponse<List<RoomsNew>> getNewRoomsByStatus(@PathVariable Integer status) {
        List<RoomsNew> rooms = roomService.getNewRoomsByStatus(status);
        return ApiResponse.success(rooms);
    }
    
    /**
     * 根据ID获取六楼房间详情
     */
    @GetMapping("/new/{id}")
    public ApiResponse<RoomsNew> getNewRoomById(@PathVariable Integer id) {
        // 这里需要在Service中添加根据ID查找的方法
        return ApiResponse.success();
    }
    
    /**
     * 创建六楼房间
     */
    @PostMapping("/new")
    public ApiResponse<RoomsNew> createNewRoom(@RequestBody RoomsNew room) {
        try {
            RoomsNew createdRoom = roomService.createNewRoom(room);
            return ApiResponse.success("房间创建成功", createdRoom);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 更新六楼房间
     */
    @PutMapping("/new/{id}")
    public ApiResponse<RoomsNew> updateNewRoom(@PathVariable Integer id, @RequestBody RoomsNew room) {
        try {
            RoomsNew updatedRoom = roomService.updateNewRoom(id, room);
            return ApiResponse.success("房间更新成功", updatedRoom);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 删除六楼房间
     */
    @DeleteMapping("/new/{id}")
    public ApiResponse<String> deleteNewRoom(@PathVariable Integer id) {
        try {
            roomService.deleteNewRoom(id);
            return ApiResponse.success("房间删除成功");
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 搜索六楼房间
     */
    @GetMapping("/new/search")
    public ApiResponse<List<RoomsNew>> searchNewRooms(@RequestParam String keyword) {
        List<RoomsNew> rooms = roomService.searchNewRooms(keyword);
        return ApiResponse.success(rooms);
    }
    
    /**
     * 获取六楼可用房间（空闲状态）
     */
    @GetMapping("/new/available")
    public ApiResponse<List<RoomsNew>> getAvailableNewRooms() {
        List<RoomsNew> rooms = roomService.getNewRoomsByStatus(1); // 1表示空闲状态
        return ApiResponse.success(rooms);
    }
    
    // ==================== 五楼房间管理 ====================
    
    /**
     * 获取五楼房间列表
     */
    @GetMapping("/old")
    public ApiResponse<Page<RoomsOld>> getOldRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer floor) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<RoomsOld> rooms = roomService.getOldRoomsWithFilters(pageable, status, floor);
        return ApiResponse.success(rooms);
    }
    
    /**
     * 根据状态获取五楼房间
     */
    @GetMapping("/old/status/{status}")
    public ApiResponse<List<RoomsOld>> getOldRoomsByStatus(@PathVariable Integer status) {
        List<RoomsOld> rooms = roomService.getOldRoomsByStatus(status);
        return ApiResponse.success(rooms);
    }
    
    /**
     * 创建五楼房间
     */
    @PostMapping("/old")
    public ApiResponse<RoomsOld> createOldRoom(@RequestBody RoomsOld room) {
        try {
            RoomsOld createdRoom = roomService.createOldRoom(room);
            return ApiResponse.success("房间创建成功", createdRoom);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 更新五楼房间
     */
    @PutMapping("/old/{id}")
    public ApiResponse<RoomsOld> updateOldRoom(@PathVariable Integer id, @RequestBody RoomsOld room) {
        try {
            RoomsOld updatedRoom = roomService.updateOldRoom(id, room);
            return ApiResponse.success("房间更新成功", updatedRoom);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 删除五楼房间
     */
    @DeleteMapping("/old/{id}")
    public ApiResponse<String> deleteOldRoom(@PathVariable Integer id) {
        try {
            roomService.deleteOldRoom(id);
            return ApiResponse.success("房间删除成功");
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    /**
     * 搜索五楼房间
     */
    @GetMapping("/old/search")
    public ApiResponse<List<RoomsOld>> searchOldRooms(@RequestParam String keyword) {
        List<RoomsOld> rooms = roomService.searchOldRooms(keyword);
        return ApiResponse.success(rooms);
    }
    
    /**
     * 获取五楼可用房间（空闲状态）
     */
    @GetMapping("/old/available")
    public ApiResponse<List<RoomsOld>> getAvailableOldRooms() {
        List<RoomsOld> rooms = roomService.getOldRoomsByStatus(1); // 1表示空闲状态
        return ApiResponse.success(rooms);
    }
}