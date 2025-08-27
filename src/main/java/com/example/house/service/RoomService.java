package com.example.house.service;

import com.example.house.entity.RoomsNew;
import com.example.house.entity.RoomsOld;
import com.example.house.repository.RoomsNewRepository;
import com.example.house.repository.RoomsOldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 房间管理服务
 */
@Service
@RequiredArgsConstructor
public class RoomService {
    
    private final RoomsNewRepository roomsNewRepository;
    private final RoomsOldRepository roomsOldRepository;
    
    // ==================== 六楼房间管理 ====================
    
    /**
     * 获取所有六楼房间
     */
    public Page<RoomsNew> getAllNewRooms(Pageable pageable) {
        return roomsNewRepository.findAll(pageable);
    }
    
    /**
     * 根据筛选条件获取六楼房间
     */
    public Page<RoomsNew> getNewRoomsWithFilters(Pageable pageable, Integer status, Integer floor) {
        if (status != null) {
            return roomsNewRepository.findByRoomStatus(status, pageable);
        } else {
            return roomsNewRepository.findAll(pageable);
        }
    }
    
    /**
     * 根据状态获取六楼房间
     */
    public List<RoomsNew> getNewRoomsByStatus(Integer status) {
        return roomsNewRepository.findByRoomStatus(status);
    }
    
    /**
     * 根据房号获取六楼房间
     */
    public Optional<RoomsNew> getNewRoomByNumber(String roomNumber) {
        return roomsNewRepository.findByRoomNumber(roomNumber);
    }
    
    /**
     * 创建六楼房间
     */
    @Transactional
    public RoomsNew createNewRoom(RoomsNew room) {
        if (roomsNewRepository.existsByRoomNumber(room.getRoomNumber())) {
            throw new RuntimeException("房号已存在");
        }
        return roomsNewRepository.save(room);
    }
    
    /**
     * 更新六楼房间
     */
    @Transactional
    public RoomsNew updateNewRoom(Integer id, RoomsNew room) {
        Optional<RoomsNew> existingRoom = roomsNewRepository.findById(id);
        if (existingRoom.isEmpty()) {
            throw new RuntimeException("房间不存在");
        }
        
        RoomsNew roomToUpdate = existingRoom.get();
        // 检查房号是否被其他房间使用
        if (!roomToUpdate.getRoomNumber().equals(room.getRoomNumber()) 
            && roomsNewRepository.existsByRoomNumber(room.getRoomNumber())) {
            throw new RuntimeException("房号已存在");
        }
        
        roomToUpdate.setRoomNumber(room.getRoomNumber());
        roomToUpdate.setRoomType(room.getRoomType());
        roomToUpdate.setDeposit(room.getDeposit());
        roomToUpdate.setBaseRent(room.getBaseRent());
        roomToUpdate.setRoomStatus(room.getRoomStatus());
        roomToUpdate.setWaterMeterNumber(room.getWaterMeterNumber());
        roomToUpdate.setElectricityMeterNumber(room.getElectricityMeterNumber());
        
        return roomsNewRepository.save(roomToUpdate);
    }
    
    /**
     * 删除六楼房间
     */
    @Transactional
    public void deleteNewRoom(Integer id) {
        if (!roomsNewRepository.existsById(id)) {
            throw new RuntimeException("房间不存在");
        }
        roomsNewRepository.deleteById(id);
    }
    
    /**
     * 搜索六楼房间
     */
    public List<RoomsNew> searchNewRooms(String keyword) {
        return roomsNewRepository.findByRoomNumberContaining(keyword);
    }
    
    // ==================== 五楼房间管理 ====================
    
    /**
     * 获取所有五楼房间
     */
    public Page<RoomsOld> getAllOldRooms(Pageable pageable) {
        return roomsOldRepository.findAll(pageable);
    }
    
    /**
     * 根据筛选条件获取五楼房间
     */
    public Page<RoomsOld> getOldRoomsWithFilters(Pageable pageable, Integer status, Integer floor) {
        if (status != null) {
            return roomsOldRepository.findByRoomStatus(status, pageable);
        } else {
            return roomsOldRepository.findAll(pageable);
        }
    }
    
    /**
     * 根据状态获取五楼房间
     */
    public List<RoomsOld> getOldRoomsByStatus(Integer status) {
        return roomsOldRepository.findByRoomStatus(status);
    }
    
    /**
     * 根据房号获取五楼房间
     */
    public Optional<RoomsOld> getOldRoomByNumber(String roomNumber) {
        return roomsOldRepository.findByRoomNumber(roomNumber);
    }
    
    /**
     * 创建五楼房间
     */
    @Transactional
    public RoomsOld createOldRoom(RoomsOld room) {
        if (roomsOldRepository.existsByRoomNumber(room.getRoomNumber())) {
            throw new RuntimeException("房号已存在");
        }
        return roomsOldRepository.save(room);
    }
    
    /**
     * 更新五楼房间
     */
    @Transactional
    public RoomsOld updateOldRoom(Integer id, RoomsOld room) {
        Optional<RoomsOld> existingRoom = roomsOldRepository.findById(id);
        if (existingRoom.isEmpty()) {
            throw new RuntimeException("房间不存在");
        }
        
        RoomsOld roomToUpdate = existingRoom.get();
        // 检查房号是否被其他房间使用
        if (!roomToUpdate.getRoomNumber().equals(room.getRoomNumber()) 
            && roomsOldRepository.existsByRoomNumber(room.getRoomNumber())) {
            throw new RuntimeException("房号已存在");
        }
        
        roomToUpdate.setRoomNumber(room.getRoomNumber());
        roomToUpdate.setRoomType(room.getRoomType());
        roomToUpdate.setDeposit(room.getDeposit());
        roomToUpdate.setBaseRent(room.getBaseRent());
        roomToUpdate.setRoomStatus(room.getRoomStatus());
        roomToUpdate.setWaterMeterNumber(room.getWaterMeterNumber());
        roomToUpdate.setElectricityMeterNumber(room.getElectricityMeterNumber());
        
        return roomsOldRepository.save(roomToUpdate);
    }
    
    /**
     * 删除五楼房间
     */
    @Transactional
    public void deleteOldRoom(Integer id) {
        if (!roomsOldRepository.existsById(id)) {
            throw new RuntimeException("房间不存在");
        }
        roomsOldRepository.deleteById(id);
    }
    
    /**
     * 搜索五楼房间
     */
    public List<RoomsOld> searchOldRooms(String keyword) {
        return roomsOldRepository.findByRoomNumberContaining(keyword);
    }
}