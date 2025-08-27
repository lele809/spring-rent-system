package com.example.house.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 五楼房间管理实体类
 */
@Entity
@Table(name = "rooms_old")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomsOld {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "room_number", nullable = false, length = 50, unique = true)
    private String roomNumber;
    
    @Column(name = "room_type", nullable = false, length = 50)
    private String roomType = "单间";
    
    @Column(name = "deposit", nullable = false, precision = 10, scale = 2)
    private BigDecimal deposit = BigDecimal.ZERO;
    
    @Column(name = "base_rent", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseRent = BigDecimal.ZERO;
    
    /**
     * 房间状态：1=空闲, 2=已出租, 3=维修中, 4=停用
     */
    @Column(name = "room_status", nullable = false)
    private Integer roomStatus = 1;
    
    @Column(name = "water_meter_number", length = 50)
    private String waterMeterNumber;
    
    @Column(name = "electricity_meter_number", length = 50)
    private String electricityMeterNumber;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}