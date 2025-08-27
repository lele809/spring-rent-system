package com.example.house.service;

import com.example.house.entity.ContractsNew;
import com.example.house.entity.ContractsOld;
import com.example.house.repository.ContractsNewRepository;
import com.example.house.repository.ContractsOldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 合同管理服务
 */
@Service
@RequiredArgsConstructor
public class ContractService {
    
    private final ContractsNewRepository contractsNewRepository;
    private final ContractsOldRepository contractsOldRepository;
    
    // ==================== 六楼合同管理 ====================
    
    /**
     * 获取所有六楼合同
     */
    public Page<ContractsNew> getAllNewContracts(Pageable pageable) {
        return contractsNewRepository.findAll(pageable);
    }
    
    /**
     * 根据ID获取六楼合同
     */
    public Optional<ContractsNew> getNewContractById(Integer id) {
        return contractsNewRepository.findById(id);
    }
    
    /**
     * 根据合同编号获取六楼合同
     */
    public Optional<ContractsNew> getNewContractByNumber(String contractNumber) {
        return contractsNewRepository.findByContractNumber(contractNumber);
    }
    
    /**
     * 根据筛选条件获取六楼合同
     */
    public Page<ContractsNew> getNewContractsWithFilters(Pageable pageable, Integer contractStatus, String roomNumber, String keyword) {
        return contractsNewRepository.findWithFilters(contractStatus, roomNumber, keyword, pageable);
    }
    
    /**
     * 根据状态获取六楼合同
     */
    public List<ContractsNew> getNewContractsByStatus(Integer status) {
        return contractsNewRepository.findByContractStatus(status);
    }
    
    /**
     * 搜索六楼合同
     */
    public Page<ContractsNew> searchNewContracts(String keyword, Pageable pageable) {
        return contractsNewRepository.findByContractNumberContainingOrTenantNameContaining(keyword, pageable);
    }
    
    /**
     * 创建六楼合同
     */
    @Transactional
    public ContractsNew createNewContract(ContractsNew contract) {
        // 检查合同编号是否已存在
        if (contractsNewRepository.existsByContractNumber(contract.getContractNumber())) {
            throw new RuntimeException("合同编号已存在");
        }
        
        // 检查房间是否已有有效合同
        if (contractsNewRepository.existsActiveContractByRoomNumber(contract.getRoomNumber())) {
            throw new RuntimeException("该房间已有有效合同，请先终止现有合同");
        }
        
        // 自动生成合同编号（如果没有提供）
        if (contract.getContractNumber() == null || contract.getContractNumber().isEmpty()) {
            contract.setContractNumber(generateContractNumber());
        }
        
        return contractsNewRepository.save(contract);
    }
    
    /**
     * 更新六楼合同
     */
    @Transactional
    public ContractsNew updateNewContract(Integer id, ContractsNew contract) {
        Optional<ContractsNew> existingContract = contractsNewRepository.findById(id);
        if (existingContract.isEmpty()) {
            throw new RuntimeException("合同不存在");
        }
        
        ContractsNew contractToUpdate = existingContract.get();
        
        // 检查合同编号是否被其他合同使用
        if (!contractToUpdate.getContractNumber().equals(contract.getContractNumber()) 
            && contractsNewRepository.existsByContractNumber(contract.getContractNumber())) {
            throw new RuntimeException("合同编号已存在");
        }
        
        // 更新字段
        contractToUpdate.setContractNumber(contract.getContractNumber());
        contractToUpdate.setRoomNumber(contract.getRoomNumber());
        contractToUpdate.setTenantName(contract.getTenantName());
        contractToUpdate.setTenantPhone(contract.getTenantPhone());
        contractToUpdate.setTenantIdCard(contract.getTenantIdCard());
        contractToUpdate.setLandlordName(contract.getLandlordName());
        contractToUpdate.setLandlordPhone(contract.getLandlordPhone());
        contractToUpdate.setMonthlyRent(contract.getMonthlyRent());
        contractToUpdate.setDeposit(contract.getDeposit());
        contractToUpdate.setContractStartDate(contract.getContractStartDate());
        contractToUpdate.setContractEndDate(contract.getContractEndDate());
        contractToUpdate.setContractDuration(contract.getContractDuration());
        contractToUpdate.setPaymentMethod(contract.getPaymentMethod());
        contractToUpdate.setRentDueDate(contract.getRentDueDate());
        contractToUpdate.setContractStatus(contract.getContractStatus());
        contractToUpdate.setUtilitiesIncluded(contract.getUtilitiesIncluded());
        contractToUpdate.setWaterRate(contract.getWaterRate());
        contractToUpdate.setElectricityRate(contract.getElectricityRate());
        contractToUpdate.setContractTerms(contract.getContractTerms());
        contractToUpdate.setSpecialAgreement(contract.getSpecialAgreement());
        contractToUpdate.setRemarks(contract.getRemarks());
        
        return contractsNewRepository.save(contractToUpdate);
    }
    
    /**
     * 删除六楼合同
     */
    @Transactional
    public void deleteNewContract(Integer id) {
        if (!contractsNewRepository.existsById(id)) {
            throw new RuntimeException("合同不存在");
        }
        contractsNewRepository.deleteById(id);
    }
    
    /**
     * 获取六楼合同统计信息
     */
    public Map<String, Object> getNewContractStats() {
        List<Object[]> statusCounts = contractsNewRepository.countByContractStatus();
        
        Map<String, Object> stats = new HashMap<>();
        long totalContracts = 0;
        long activeContracts = 0;
        
        for (Object[] row : statusCounts) {
            Integer status = (Integer) row[0];
            Long count = (Long) row[1];
            totalContracts += count;
            
            if (status == 1) { // 生效中
                activeContracts = count;
            }
        }
        
        // 查询即将到期和已过期的合同
        LocalDate now = LocalDate.now();
        LocalDate thirtyDaysLater = now.plusDays(30);
        
        List<ContractsNew> expiringContracts = contractsNewRepository.findExpiringContracts(now, thirtyDaysLater);
        List<ContractsNew> expiredContracts = contractsNewRepository.findExpiredContracts(now);
        
        stats.put("totalContracts", totalContracts);
        stats.put("activeContracts", activeContracts);
        stats.put("expiringContracts", expiringContracts.size());
        stats.put("expiredContracts", expiredContracts.size());
        
        return stats;
    }
    
    // ==================== 五楼合同管理 ====================
    
    /**
     * 获取所有五楼合同
     */
    public Page<ContractsOld> getAllOldContracts(Pageable pageable) {
        return contractsOldRepository.findAll(pageable);
    }
    
    /**
     * 根据ID获取五楼合同
     */
    public Optional<ContractsOld> getOldContractById(Integer id) {
        return contractsOldRepository.findById(id);
    }
    
    /**
     * 根据合同编号获取五楼合同
     */
    public Optional<ContractsOld> getOldContractByNumber(String contractNumber) {
        return contractsOldRepository.findByContractNumber(contractNumber);
    }
    
    /**
     * 根据筛选条件获取五楼合同
     */
    public Page<ContractsOld> getOldContractsWithFilters(Pageable pageable, Integer contractStatus, String roomNumber, String keyword) {
        return contractsOldRepository.findWithFilters(contractStatus, roomNumber, keyword, pageable);
    }
    
    /**
     * 根据状态获取五楼合同
     */
    public List<ContractsOld> getOldContractsByStatus(Integer status) {
        return contractsOldRepository.findByContractStatus(status);
    }
    
    /**
     * 搜索五楼合同
     */
    public Page<ContractsOld> searchOldContracts(String keyword, Pageable pageable) {
        return contractsOldRepository.findByContractNumberContainingOrTenantNameContaining(keyword, pageable);
    }
    
    /**
     * 创建五楼合同
     */
    @Transactional
    public ContractsOld createOldContract(ContractsOld contract) {
        // 检查合同编号是否已存在
        if (contractsOldRepository.existsByContractNumber(contract.getContractNumber())) {
            throw new RuntimeException("合同编号已存在");
        }
        
        // 检查房间是否已有有效合同
        if (contractsOldRepository.existsActiveContractByRoomNumber(contract.getRoomNumber())) {
            throw new RuntimeException("该房间已有有效合同，请先终止现有合同");
        }
        
        // 自动生成合同编号（如果没有提供）
        if (contract.getContractNumber() == null || contract.getContractNumber().isEmpty()) {
            contract.setContractNumber(generateContractNumber());
        }
        
        return contractsOldRepository.save(contract);
    }
    
    /**
     * 更新五楼合同
     */
    @Transactional
    public ContractsOld updateOldContract(Integer id, ContractsOld contract) {
        Optional<ContractsOld> existingContract = contractsOldRepository.findById(id);
        if (existingContract.isEmpty()) {
            throw new RuntimeException("合同不存在");
        }
        
        ContractsOld contractToUpdate = existingContract.get();
        
        // 检查合同编号是否被其他合同使用
        if (!contractToUpdate.getContractNumber().equals(contract.getContractNumber()) 
            && contractsOldRepository.existsByContractNumber(contract.getContractNumber())) {
            throw new RuntimeException("合同编号已存在");
        }
        
        // 更新字段
        contractToUpdate.setContractNumber(contract.getContractNumber());
        contractToUpdate.setRoomNumber(contract.getRoomNumber());
        contractToUpdate.setTenantName(contract.getTenantName());
        contractToUpdate.setTenantPhone(contract.getTenantPhone());
        contractToUpdate.setTenantIdCard(contract.getTenantIdCard());
        contractToUpdate.setLandlordName(contract.getLandlordName());
        contractToUpdate.setLandlordPhone(contract.getLandlordPhone());
        contractToUpdate.setMonthlyRent(contract.getMonthlyRent());
        contractToUpdate.setDeposit(contract.getDeposit());
        contractToUpdate.setContractStartDate(contract.getContractStartDate());
        contractToUpdate.setContractEndDate(contract.getContractEndDate());
        contractToUpdate.setContractDuration(contract.getContractDuration());
        contractToUpdate.setPaymentMethod(contract.getPaymentMethod());
        contractToUpdate.setRentDueDate(contract.getRentDueDate());
        contractToUpdate.setContractStatus(contract.getContractStatus());
        contractToUpdate.setUtilitiesIncluded(contract.getUtilitiesIncluded());
        contractToUpdate.setWaterRate(contract.getWaterRate());
        contractToUpdate.setElectricityRate(contract.getElectricityRate());
        contractToUpdate.setContractTerms(contract.getContractTerms());
        contractToUpdate.setSpecialAgreement(contract.getSpecialAgreement());
        contractToUpdate.setRemarks(contract.getRemarks());
        
        return contractsOldRepository.save(contractToUpdate);
    }
    
    /**
     * 删除五楼合同
     */
    @Transactional
    public void deleteOldContract(Integer id) {
        if (!contractsOldRepository.existsById(id)) {
            throw new RuntimeException("合同不存在");
        }
        contractsOldRepository.deleteById(id);
    }
    
    /**
     * 获取五楼合同统计信息
     */
    public Map<String, Object> getOldContractStats() {
        List<Object[]> statusCounts = contractsOldRepository.countByContractStatus();
        
        Map<String, Object> stats = new HashMap<>();
        long totalContracts = 0;
        long activeContracts = 0;
        
        for (Object[] row : statusCounts) {
            Integer status = (Integer) row[0];
            Long count = (Long) row[1];
            totalContracts += count;
            
            if (status == 1) { // 生效中
                activeContracts = count;
            }
        }
        
        // 查询即将到期和已过期的合同
        LocalDate now = LocalDate.now();
        LocalDate thirtyDaysLater = now.plusDays(30);
        
        List<ContractsOld> expiringContracts = contractsOldRepository.findExpiringContracts(now, thirtyDaysLater);
        List<ContractsOld> expiredContracts = contractsOldRepository.findExpiredContracts(now);
        
        stats.put("totalContracts", totalContracts);
        stats.put("activeContracts", activeContracts);
        stats.put("expiringContracts", expiringContracts.size());
        stats.put("expiredContracts", expiredContracts.size());
        
        return stats;
    }
    
    // ==================== 公共方法 ====================
    
    /**
     * 生成合同编号
     */
    private String generateContractNumber() {
        LocalDate now = LocalDate.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomSuffix = String.format("%03d", (int) (Math.random() * 1000));
        return "HT" + dateStr + randomSuffix;
    }
    
    /**
     * 获取即将到期的合同列表（用于提醒）
     */
    public List<ContractsNew> getExpiringNewContracts(int days) {
        LocalDate now = LocalDate.now();
        LocalDate futureDate = now.plusDays(days);
        return contractsNewRepository.findExpiringContracts(now, futureDate);
    }
    
    /**
     * 获取即将到期的合同列表（用于提醒）
     */
    public List<ContractsOld> getExpiringOldContracts(int days) {
        LocalDate now = LocalDate.now();
        LocalDate futureDate = now.plusDays(days);
        return contractsOldRepository.findExpiringContracts(now, futureDate);
    }
} 