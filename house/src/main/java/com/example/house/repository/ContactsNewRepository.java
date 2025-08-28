package com.example.house.repository;

import com.example.house.entity.ContactsNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 六楼租客联系人数据访问层
 */
@Repository
public interface ContactsNewRepository extends JpaRepository<ContactsNew, Integer> {
    
    /**
     * 根据房号查找租客
     */
    List<ContactsNew> findByRoomId(String roomId);
    
    /**
     * 根据姓名查找租客
     */
    List<ContactsNew> findByNameContaining(String name);
    
    /**
     * 根据电话查找租客
     */
    Optional<ContactsNew> findByPhone(String phone);
    
    /**
     * 根据身份证号查找租客
     */
    Optional<ContactsNew> findByIdCard(String idCard);
    
    /**
     * 根据姓名、房号或电话进行模糊搜索
     */
    @Query("SELECT c FROM ContactsNew c WHERE c.name LIKE %:keyword% OR c.roomId LIKE %:keyword% OR c.phone LIKE %:keyword%")
    List<ContactsNew> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 检查身份证号是否存在
     */
    boolean existsByIdCard(String idCard);
}