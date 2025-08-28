package com.example.house.controller;

import com.example.house.dto.ApiResponse;
import com.example.house.entity.RentalInfoNew;
import com.example.house.entity.RentalInfoOld;
import com.example.house.service.RentalInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 租房信息管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/rental-info")
@RequiredArgsConstructor
public class RentalInfoController {
    
    private final RentalInfoService rentalInfoService;
    
    // ==================== 六楼租房信息管理 ====================
    
    /**
     * 获取六楼租房信息列表
     */
    @GetMapping("/new")
    public ApiResponse<Page<RentalInfoNew>> getNewRentalInfoList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer rentalStatus) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<?> result = rentalInfoService.searchRentalInfos("6", keyword, rentalStatus, pageable);
            return ApiResponse.success((Page<RentalInfoNew>) result);
        } catch (Exception e) {
            log.error("获取六楼租房信息列表失败", e);
            return ApiResponse.error("获取租房信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取六楼租房信息
     */
    @GetMapping("/new/{id}")
    public ApiResponse<RentalInfoNew> getNewRentalInfoById(@PathVariable Integer id) {
        try {
            Optional<?> result = rentalInfoService.getRentalInfoByFloorAndId("6", id);
            if (result.isPresent()) {
                return ApiResponse.success((RentalInfoNew) result.get());
            } else {
                return ApiResponse.error("租房信息不存在");
            }
        } catch (Exception e) {
            log.error("获取六楼租房信息详情失败", e);
            return ApiResponse.error("获取租房信息详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建六楼租房信息
     */
    @PostMapping("/new")
    public ApiResponse<RentalInfoNew> createNewRentalInfo(@RequestBody RentalInfoNew rentalInfo) {
        try {
            Object result = rentalInfoService.createRentalInfo("6", rentalInfo);
            return ApiResponse.success((RentalInfoNew) result);
        } catch (Exception e) {
            log.error("创建六楼租房信息失败", e);
            return ApiResponse.error("创建租房信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新六楼租房信息
     */
    @PutMapping("/new/{id}")
    public ApiResponse<RentalInfoNew> updateNewRentalInfo(@PathVariable Integer id, @RequestBody RentalInfoNew rentalInfo) {
        try {
            Object result = rentalInfoService.updateRentalInfo("6", id, rentalInfo);
            return ApiResponse.success((RentalInfoNew) result);
        } catch (Exception e) {
            log.error("更新六楼租房信息失败", e);
            return ApiResponse.error("更新租房信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除六楼租房信息
     */
    @DeleteMapping("/new/{id}")
    public ApiResponse<String> deleteNewRentalInfo(@PathVariable Integer id) {
        try {
            rentalInfoService.deleteRentalInfo("6", id);
            return ApiResponse.success("删除成功");
        } catch (Exception e) {
            log.error("删除六楼租房信息失败", e);
            return ApiResponse.error("删除租房信息失败: " + e.getMessage());
        }
    }
    
    // ==================== 五楼租房信息管理 ====================
    
    /**
     * 获取五楼租房信息列表
     */
    @GetMapping("/old")
    public ApiResponse<Page<RentalInfoOld>> getOldRentalInfoList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer rentalStatus) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<?> result = rentalInfoService.searchRentalInfos("5", keyword, rentalStatus, pageable);
            return ApiResponse.success((Page<RentalInfoOld>) result);
        } catch (Exception e) {
            log.error("获取五楼租房信息列表失败", e);
            return ApiResponse.error("获取租房信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取五楼租房信息详情
     */
    @GetMapping("/old/{id}")
    public ApiResponse<RentalInfoOld> getOldRentalInfoById(@PathVariable Integer id) {
        try {
            Optional<?> result = rentalInfoService.getRentalInfoByFloorAndId("5", id);
            if (result.isPresent()) {
                return ApiResponse.success((RentalInfoOld) result.get());
            } else {
                return ApiResponse.error("租房信息不存在");
            }
        } catch (Exception e) {
            log.error("获取五楼租房信息详情失败", e);
            return ApiResponse.error("获取租房信息详情失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建五楼租房信息
     */
    @PostMapping("/old")
    public ApiResponse<RentalInfoOld> createOldRentalInfo(@RequestBody RentalInfoOld rentalInfo) {
        try {
            Object result = rentalInfoService.createRentalInfo("5", rentalInfo);
            return ApiResponse.success((RentalInfoOld) result);
        } catch (Exception e) {
            log.error("创建五楼租房信息失败", e);
            return ApiResponse.error("创建租房信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新五楼租房信息
     */
    @PutMapping("/old/{id}")
    public ApiResponse<RentalInfoOld> updateOldRentalInfo(@PathVariable Integer id, @RequestBody RentalInfoOld rentalInfo) {
        try {
            Object result = rentalInfoService.updateRentalInfo("5", id, rentalInfo);
            return ApiResponse.success((RentalInfoOld) result);
        } catch (Exception e) {
            log.error("更新五楼租房信息失败", e);
            return ApiResponse.error("更新租房信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除五楼租房信息
     */
    @DeleteMapping("/old/{id}")
    public ApiResponse<String> deleteOldRentalInfo(@PathVariable Integer id) {
        try {
            rentalInfoService.deleteRentalInfo("5", id);
            return ApiResponse.success("删除成功");
        } catch (Exception e) {
            log.error("删除五楼租房信息失败", e);
            return ApiResponse.error("删除租房信息失败: " + e.getMessage());
        }
    }
    
    // ==================== 通用接口 ====================
    
    /**
     * 获取指定楼层的可用房间列表
     */
    @GetMapping("/{floor}/available-rooms")
    public ApiResponse<List<?>> getAvailableRooms(@PathVariable String floor) {
        try {
            List<?> rooms = rentalInfoService.getAvailableRooms(floor);
            return ApiResponse.success(rooms);
        } catch (Exception e) {
            log.error("获取{}楼可用房间列表失败", floor, e);
            return ApiResponse.error("获取可用房间失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取指定楼层的租房信息统计
     */
    @GetMapping("/{floor}/stats")
    public ApiResponse<Object> getRentalInfoStats(@PathVariable String floor) {
        try {
            Object stats = rentalInfoService.getRentalInfoStats(floor);
            return ApiResponse.success(stats);
        } catch (Exception e) {
            log.error("获取{}楼租房信息统计失败", floor, e);
            return ApiResponse.error("获取统计信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查房号是否已被占用
     */
    @GetMapping("/{floor}/check-room/{roomNumber}")
    public ApiResponse<Boolean> checkRoomOccupied(@PathVariable String floor, @PathVariable String roomNumber) {
        try {
            boolean occupied = rentalInfoService.isRoomOccupied(floor, roomNumber);
            return ApiResponse.success(occupied);
        } catch (Exception e) {
            log.error("检查{}楼房号{}占用状态失败", floor, roomNumber, e);
            return ApiResponse.error("检查房号状态失败: " + e.getMessage());
        }
    }
}