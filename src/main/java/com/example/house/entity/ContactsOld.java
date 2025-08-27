package com.example.house.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 五楼租客联系人实体类
 */
@Entity
@Table(name = "contacts_old")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactsOld {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
        @Column(name = "roomId", nullable = false, length = 20)
    private String roomId;
    
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;
    
    @Column(name = "id_card", nullable = false, length = 18, unique = true)
    private String idCard;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}