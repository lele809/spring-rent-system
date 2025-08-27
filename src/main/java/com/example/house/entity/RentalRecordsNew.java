package com.example.house.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 租房缴费记录表（新）
 */
@Entity
@Table(name = "rental_records_new")
public class RentalRecordsNew {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    /**
     * 房号
     */
    @Column(name = "room_number", nullable = false, length = 50)
    @JsonProperty("roomNumber")
    private String roomNumber;
    
    /**
     * 租客姓名
     */
    @Column(name = "tenant_name", nullable = false, length = 50)
    @JsonProperty("tenantName")
    private String tenantName;
    
    /**
     * 总租金
     */
    @Column(name = "total_rent", nullable = false, precision = 10, scale = 2)
    @JsonProperty("totalRent")
    private BigDecimal totalRent = BigDecimal.ZERO;
    
    /**
     * 缴费时间
     */
    @Column(name = "payment_date", nullable = false)
    @JsonProperty("paymentDate")
    private LocalDate paymentDate;
    
    /**
     * 创建时间
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    // 构造函数
    public RentalRecordsNew() {}
    
    public RentalRecordsNew(String roomNumber, String tenantName, BigDecimal totalRent, LocalDate paymentDate) {
        this.roomNumber = roomNumber;
        this.tenantName = tenantName;
        this.totalRent = totalRent;
        this.paymentDate = paymentDate;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public String getTenantName() {
        return tenantName;
    }
    
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
    
    public BigDecimal getTotalRent() {
        return totalRent;
    }
    
    public void setTotalRent(BigDecimal totalRent) {
        this.totalRent = totalRent;
    }
    
    public LocalDate getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "RentalRecordsNew{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                ", tenantName='" + tenantName + '\'' +
                ", totalRent=" + totalRent +
                ", paymentDate=" + paymentDate +
                ", createdAt=" + createdAt +
                '}';
        }
 
}