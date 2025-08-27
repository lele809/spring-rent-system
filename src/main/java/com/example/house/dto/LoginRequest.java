package com.example.house.dto;

import lombok.Data;

/**
 * 登录请求DTO
 */
@Data
public class LoginRequest {
    
    private String adminName;
    private String password;
}