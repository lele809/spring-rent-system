package com.example.house.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 六楼租赁合同实体类
 */
@Entity
@Table(name = "contracts_new")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractsNew {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "contract_number", nullable = false, length = 50, unique = true)
    private String contractNumber;
    
    @Column(name = "room_number", nullable = false, length = 50)
    private String roomNumber;
    
    @Column(name = "tenant_name", nullable = false, length = 50)
    private String tenantName;
    
    @Column(name = "tenant_phone", nullable = false, length = 20)
    private String tenantPhone;
    
    @Column(name = "tenant_id_card", nullable = false, length = 18)
    private String tenantIdCard;
    
    @Column(name = "landlord_name", nullable = false, length = 50)
    private String landlordName = "房东";
    
    @Column(name = "landlord_phone", length = 20)
    private String landlordPhone;
    
    @Column(name = "monthly_rent", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyRent = BigDecimal.ZERO;
    
    @Column(name = "deposit", nullable = false, precision = 10, scale = 2)
    private BigDecimal deposit = BigDecimal.ZERO;
    
    @Column(name = "contract_start_date", nullable = false)
    private LocalDate contractStartDate;
    
    @Column(name = "contract_end_date", nullable = false)
    private LocalDate contractEndDate;
    
    @Column(name = "contract_duration", nullable = false)
    private Integer contractDuration;
    
    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod = "月付";
    
    @Column(name = "rent_due_date", nullable = false)
    private Integer rentDueDate = 1;
    
    /**
     * 合同状态：1=生效中, 2=已到期, 3=提前终止, 4=续签
     */
    @Column(name = "contract_status", nullable = false)
    private Integer contractStatus = 1;
    
    /**
     * 是否包含水电费：0=不包含, 1=包含
     */
    @Column(name = "utilities_included", nullable = false)
    private Integer utilitiesIncluded = 0;
    
    @Column(name = "water_rate", precision = 6, scale = 2)
    private BigDecimal waterRate = new BigDecimal("3.50");
    
    @Column(name = "electricity_rate", precision = 6, scale = 2)
    private BigDecimal electricityRate = new BigDecimal("0.60");
    
    @Column(name = "contract_terms", columnDefinition = "TEXT")
    private String contractTerms;
    
    @Column(name = "special_agreement", columnDefinition = "TEXT")
    private String specialAgreement;
    
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