package com.example.house.repository;

import com.example.house.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 管理员数据访问层
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    
    /**
     * 根据管理员名称查找管理员
     */
    Optional<Admin> findByAdminName(String adminName);
    
    /**
     * 根据管理员名称和密码查找管理员
     */
    @Query("SELECT a FROM Admin a WHERE a.adminName = :adminName AND a.password = :password")
    Optional<Admin> findByAdminNameAndPassword(@Param("adminName") String adminName, @Param("password") String password);
}