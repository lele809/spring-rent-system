package com.example.house.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 五楼租房信息实体类
 */
@Entity
@Table(name = "rental_info_old")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalInfoOld {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "room_number", nullable = false, length = 50)
    private String roomNumber;
    
    @Column(name = "tenant_name", nullable = false, length = 50)
    private String tenantName;
    
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;
    
    @Column(name = "deposit", nullable = false, precision = 10, scale = 2)
    private BigDecimal deposit = BigDecimal.ZERO;
    
    @Column(name = "occupant_count", nullable = false)
    private Integer occupantCount = 1;
    
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;
    
    /**
     * 租住状态：1=正常入住, 2=已退房
     */
    @Column(name = "rental_status", nullable = false)
    private Integer rentalStatus = 1;
    
    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;
    
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