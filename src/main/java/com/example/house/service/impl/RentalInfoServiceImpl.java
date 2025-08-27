package com.example.house.service.impl;

import com.example.house.entity.RentalInfoNew;
import com.example.house.entity.RentalInfoOld;
import com.example.house.entity.RoomsNew;
import com.example.house.entity.RoomsOld;
import com.example.house.repository.RentalInfoNewRepository;
import com.example.house.repository.RentalInfoOldRepository;
import com.example.house.repository.RoomsNewRepository;
import com.example.house.repository.RoomsOldRepository;
import com.example.house.service.RentalInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 租房信息服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RentalInfoServiceImpl implements RentalInfoService {
    
    private final RentalInfoNewRepository rentalInfoNewRepository;
    private final RentalInfoOldRepository rentalInfoOldRepository;
    private final RoomsNewRepository roomsNewRepository;
    private final RoomsOldRepository roomsOldRepository;
    
    @Override
    public Page<?> getRentalInfosByFloor(String floor, Pageable pageable) {
        log.info("获取{}楼租房信息列表，分页参数: {}", floor, pageable);
        
        if ("6".equals(floor)) {
            return rentalInfoNewRepository.findAll(pageable);
        } else if ("5".equals(floor)) {
            return rentalInfoOldRepository.findAll(pageable);
        } else {
            throw new IllegalArgumentException("不支持的楼层: " + floor);
        }
    }
    
    @Override
    public Page<?> searchRentalInfos(String floor, String keyword, Integer rentalStatus, Pageable pageable) {
        log.info("搜索{}楼租房信息，关键词: {}, 状态: {}, 分页参数: {}", floor, keyword, rentalStatus, pageable);
        
        if ("6".equals(floor)) {
            if (StringUtils.hasText(keyword) && rentalStatus != null) {
                return rentalInfoNewRepository.searchByKeywordAndStatus(keyword, rentalStatus, pageable);
            } else if (StringUtils.hasText(keyword)) {
                return rentalInfoNewRepository.searchByKeyword(keyword, pageable);
            } else if (rentalStatus != null) {
                return rentalInfoNewRepository.findByRentalStatus(rentalStatus, pageable);
            } else {
                return rentalInfoNewRepository.findAll(pageable);
            }
        } else if ("5".equals(floor)) {
            if (StringUtils.hasText(keyword) && rentalStatus != null) {
                return rentalInfoOldRepository.searchByKeywordAndStatus(keyword, rentalStatus, pageable);
            } else if (StringUtils.hasText(keyword)) {
                return rentalInfoOldRepository.searchByKeyword(keyword, pageable);
            } else if (rentalStatus != null) {
                return rentalInfoOldRepository.findByRentalStatus(rentalStatus, pageable);
            } else {
                return rentalInfoOldRepository.findAll(pageable);
            }
        } else {
            throw new IllegalArgumentException("不支持的楼层: " + floor);
        }
    }
    
    @Override
    public Optional<?> getRentalInfoByFloorAndId(String floor, Integer id) {
        log.info("获取{}楼租房信息详情，ID: {}", floor, id);
        
        if ("6".equals(floor)) {
            return rentalInfoNewRepository.findById(id);
        } else if ("5".equals(floor)) {
            return rentalInfoOldRepository.findById(id);
        } else {
            throw new IllegalArgumentException("不支持的楼层: " + floor);
        }
    }
    
    @Override
    @Transactional
    public Object createRentalInfo(String floor, Object rentalInfo) {
        log.info("创建{}楼租房信息: {}", floor, rentalInfo);
        
        if ("6".equals(floor)) {
            RentalInfoNew newRentalInfo = new RentalInfoNew();
            BeanUtils.copyProperties(rentalInfo, newRentalInfo);
            
            // 检查房号是否已被占用
            if (rentalInfoNewRepository.existsByRoomNumber(newRentalInfo.getRoomNumber())) {
                throw new IllegalArgumentException("房号 " + newRentalInfo.getRoomNumber() + " 已被占用");
            }
            
            // 检查电话是否已存在
            if (rentalInfoNewRepository.existsByPhone(newRentalInfo.getPhone())) {
                throw new IllegalArgumentException("电话号码 " + newRentalInfo.getPhone() + " 已存在");
            }
            
            RentalInfoNew saved = rentalInfoNewRepository.save(newRentalInfo);
            
            // 更新房间状态为已出租
            updateRoomStatus(floor, newRentalInfo.getRoomNumber(), 2);
            
            return saved;
        } else if ("5".equals(floor)) {
            RentalInfoOld oldRentalInfo = new RentalInfoOld();
            BeanUtils.copyProperties(rentalInfo, oldRentalInfo);
            
            // 检查房号是否已被占用
            if (rentalInfoOldRepository.existsByRoomNumber(oldRentalInfo.getRoomNumber())) {
                throw new IllegalArgumentException("房号 " + oldRentalInfo.getRoomNumber() + " 已被占用");
            }
            
            // 检查电话是否已存在
            if (rentalInfoOldRepository.existsByPhone(oldRentalInfo.getPhone())) {
                throw new IllegalArgumentException("电话号码 " + oldRentalInfo.getPhone() + " 已存在");
            }
            
            RentalInfoOld saved = rentalInfoOldRepository.save(oldRentalInfo);
            
            // 更新房间状态为已出租
            updateRoomStatus(floor, oldRentalInfo.getRoomNumber(), 2);
            
            return saved;
        } else {
            throw new IllegalArgumentException("不支持的楼层: " + floor);
        }
    }
    
    @Override
    @Transactional
    public Object updateRentalInfo(String floor, Integer id, Object rentalInfo) {
        log.info("更新{}楼租房信息，ID: {}, 数据: {}", floor, id, rentalInfo);
        
        if ("6".equals(floor)) {
            Optional<RentalInfoNew> existingOpt = rentalInfoNewRepository.findById(id);
            if (existingOpt.isEmpty()) {
                throw new IllegalArgumentException("租房信息不存在，ID: " + id);
            }
            
            RentalInfoNew existing = existingOpt.get();
            String oldRoomNumber = existing.getRoomNumber();
            
            BeanUtils.copyProperties(rentalInfo, existing, "id", "createdAt");
            
            // 如果房号发生变化，需要检查新房号是否可用
            if (!oldRoomNumber.equals(existing.getRoomNumber())) {
                if (rentalInfoNewRepository.existsByRoomNumber(existing.getRoomNumber())) {
                    throw new IllegalArgumentException("房号 " + existing.getRoomNumber() + " 已被占用");
                }
                // 释放旧房间，占用新房间
                updateRoomStatus(floor, oldRoomNumber, 1);
                updateRoomStatus(floor, existing.getRoomNumber(), 2);
            }
            
            return rentalInfoNewRepository.save(existing);
        } else if ("5".equals(floor)) {
            Optional<RentalInfoOld> existingOpt = rentalInfoOldRepository.findById(id);
            if (existingOpt.isEmpty()) {
                throw new IllegalArgumentException("租房信息不存在，ID: " + id);
            }
            
            RentalInfoOld existing = existingOpt.get();
            String oldRoomNumber = existing.getRoomNumber();
            
            BeanUtils.copyProperties(rentalInfo, existing, "id", "createdAt");
            
            // 如果房号发生变化，需要检查新房号是否可用
            if (!oldRoomNumber.equals(existing.getRoomNumber())) {
                if (rentalInfoOldRepository.existsByRoomNumber(existing.getRoomNumber())) {
                    throw new IllegalArgumentException("房号 " + existing.getRoomNumber() + " 已被占用");
                }
                // 释放旧房间，占用新房间
                updateRoomStatus(floor, oldRoomNumber, 1);
                updateRoomStatus(floor, existing.getRoomNumber(), 2);
            }
            
            return rentalInfoOldRepository.save(existing);
        } else {
            throw new IllegalArgumentException("不支持的楼层: " + floor);
        }
    }
    
    @Override
    @Transactional
    public void deleteRentalInfo(String floor, Integer id) {
        log.info("删除{}楼租房信息，ID: {}", floor, id);
        
        if ("6".equals(floor)) {
            Optional<RentalInfoNew> existingOpt = rentalInfoNewRepository.findById(id);
            if (existingOpt.isEmpty()) {
                throw new IllegalArgumentException("租房信息不存在，ID: " + id);
            }
            
            RentalInfoNew existing = existingOpt.get();
            rentalInfoNewRepository.deleteById(id);
            
            // 释放房间状态
            updateRoomStatus(floor, existing.getRoomNumber(), 1);
        } else if ("5".equals(floor)) {
            Optional<RentalInfoOld> existingOpt = rentalInfoOldRepository.findById(id);
            if (existingOpt.isEmpty()) {
                throw new IllegalArgumentException("租房信息不存在，ID: " + id);
            }
            
            RentalInfoOld existing = existingOpt.get();
            rentalInfoOldRepository.deleteById(id);
            
            // 释放房间状态
            updateRoomStatus(floor, existing.getRoomNumber(), 1);
        } else {
            throw new IllegalArgumentException("不支持的楼层: " + floor);
        }
    }
    
    @Override
    public List<?> getAvailableRooms(String floor) {
        log.info("获取{}楼可用房间列表", floor);
        
        if ("6".equals(floor)) {
            return roomsNewRepository.findByRoomStatus(1); // 1=空闲
        } else if ("5".equals(floor)) {
            return roomsOldRepository.findByRoomStatus(1); // 1=空闲
        } else {
            throw new IllegalArgumentException("不支持的楼层: " + floor);
        }
    }
    
    @Override
    public boolean isRoomOccupied(String floor, String roomNumber) {
        if ("6".equals(floor)) {
            return rentalInfoNewRepository.existsByRoomNumber(roomNumber);
        } else if ("5".equals(floor)) {
            return rentalInfoOldRepository.existsByRoomNumber(roomNumber);
        } else {
            throw new IllegalArgumentException("不支持的楼层: " + floor);
        }
    }
    
    @Override
    public boolean isPhoneExists(String floor, String phone) {
        if ("6".equals(floor)) {
            return rentalInfoNewRepository.existsByPhone(phone);
        } else if ("5".equals(floor)) {
            return rentalInfoOldRepository.existsByPhone(phone);
        } else {
            throw new IllegalArgumentException("不支持的楼层: " + floor);
        }
    }
    
    @Override
    public Object getRentalInfoStats(String floor) {
        log.info("获取{}楼租房信息统计", floor);
        
        Map<String, Object> stats = new HashMap<>();
        
        if ("6".equals(floor)) {
            List<Object[]> statusCounts = rentalInfoNewRepository.countByRentalStatus();
            long totalCount = rentalInfoNewRepository.count();
            
            stats.put("totalCount", totalCount);
            stats.put("statusCounts", statusCounts);
        } else if ("5".equals(floor)) {
            List<Object[]> statusCounts = rentalInfoOldRepository.countByRentalStatus();
            long totalCount = rentalInfoOldRepository.count();
            
            stats.put("totalCount", totalCount);
            stats.put("statusCounts", statusCounts);
        } else {
            throw new IllegalArgumentException("不支持的楼层: " + floor);
        }
        
        return stats;
    }
    
    /**
     * 更新房间状态
     * @param floor 楼层
     * @param roomNumber 房号
     * @param status 状态（1=空闲, 2=已出租）
     */
    private void updateRoomStatus(String floor, String roomNumber, Integer status) {
        try {
            if ("6".equals(floor)) {
                Optional<RoomsNew> roomOpt = roomsNewRepository.findByRoomNumber(roomNumber);
                if (roomOpt.isPresent()) {
                    RoomsNew room = roomOpt.get();
                    room.setRoomStatus(status);
                    roomsNewRepository.save(room);
                    log.info("更新六楼房间{}状态为: {}", roomNumber, status);
                }
            } else if ("5".equals(floor)) {
                Optional<RoomsOld> roomOpt = roomsOldRepository.findByRoomNumber(roomNumber);
                if (roomOpt.isPresent()) {
                    RoomsOld room = roomOpt.get();
                    room.setRoomStatus(status);
                    roomsOldRepository.save(room);
                    log.info("更新五楼房间{}状态为: {}", roomNumber, status);
                }
            }
        } catch (Exception e) {
            log.warn("更新房间状态失败: {}", e.getMessage());
        }
    }
}