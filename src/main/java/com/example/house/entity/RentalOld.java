package com.example.house.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 五楼租房管理实体类
 */
@Entity
@Table(name = "rental_old")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalOld {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "room_number", nullable = false, length = 50)
    private String roomNumber;
    
    @Column(name = "tenant_name", nullable = false, length = 50)
    private String tenantName;
    
    @Column(name = "deposit", nullable = false, precision = 10, scale = 2)
    private BigDecimal deposit = BigDecimal.ZERO;
    
    @Column(name = "monthly_rent", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyRent = BigDecimal.ZERO;
    
    @Column(name = "water_usage", precision = 10, scale = 2)
    private BigDecimal waterUsage = BigDecimal.ZERO;
    
    @Column(name = "electricity_usage", precision = 10, scale = 2)
    private BigDecimal electricityUsage = BigDecimal.ZERO;
    
    @Column(name = "water_fee", precision = 10, scale = 2)
    private BigDecimal waterFee = BigDecimal.ZERO;
    
    @Column(name = "electricity_fee", precision = 10, scale = 2)
    private BigDecimal electricityFee = BigDecimal.ZERO;
    
    @Column(name = "utilities_fee", precision = 10, scale = 2)
    private BigDecimal utilitiesFee = BigDecimal.ZERO;
    
    @Column(name = "total_due", precision = 10, scale = 2)
    private BigDecimal totalDue = BigDecimal.ZERO;
    
    /**
     * 租赁状态：1=已缴费, 2=未缴费
     */
    @Column(name = "payment_status", nullable = false)
    private Integer paymentStatus = 1;
    
    @Column(name = "check_in_date")
    private LocalDate checkInDate;
    
    @Column(name = "check_out_date")
    private LocalDate checkOutDate;
    
    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;
    
    @Column(name = "contract_end_date")
    private LocalDate contractEndDate;
    
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