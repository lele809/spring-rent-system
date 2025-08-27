package com.example.house.service.impl;

import com.example.house.dto.MonthlyIncomeData;
import com.example.house.dto.PaymentQuery;
import com.example.house.dto.PaymentStats;
import com.example.house.entity.RentalNew;
import com.example.house.entity.RentalOld;
import com.example.house.entity.RentalRecordsNew;
import com.example.house.entity.RentalRecordsOld;
import com.example.house.repository.RentalNewRepository;
import com.example.house.repository.RentalOldRepository;
import com.example.house.repository.RentalRecordsNewRepository;
import com.example.house.repository.RentalRecordsOldRepository;
import com.example.house.service.RentalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 租房管理服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    
    private final RentalNewRepository rentalNewRepository;
    private final RentalOldRepository rentalOldRepository;
    private final RentalRecordsNewRepository rentalRecordsNewRepository;
    private final RentalRecordsOldRepository rentalRecordsOldRepository;
    
    @Override
    public Page<?> getRentalsByFloor(String floor, PaymentQuery query) {
        try {
            log.info("获取{}楼租房记录，查询参数: {}", floor, query);
            
            // 验证楼层参数
            if (!"5".equals(floor) && !"6".equals(floor)) {
                throw new IllegalArgumentException("不支持的楼层: " + floor);
            }
            
            // 验证排序字段（防止SQL注入）
            String sortBy = query.getSortBy();
            if (sortBy != null && !isValidSortField(sortBy)) {
                log.warn("无效的排序字段: {}, 使用默认排序", sortBy);
                query.setSortBy("createdAt");
            }
            
            // 创建分页和排序
            Sort sort = Sort.by(
                "desc".equalsIgnoreCase(query.getSortDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                query.getSortBy()
            );
            Pageable pageable = PageRequest.of(query.getPage(), query.getSize(), sort);
            
            log.info("分页参数: page={}, size={}, sort={}", query.getPage(), query.getSize(), sort);
            
            // 解析月份筛选参数
            Integer year = null;
            Integer month = null;
            if (StringUtils.hasText(query.getMonthFilter())) {
                try {
                    String[] parts = query.getMonthFilter().split("-");
                    if (parts.length == 2) {
                        year = Integer.parseInt(parts[0]);
                        month = Integer.parseInt(parts[1]);
                        log.info("月份筛选: {}年{}月", year, month);
                    }
                } catch (NumberFormatException e) {
                    log.warn("无法解析月份筛选参数: {}", query.getMonthFilter());
                }
            }
            
            // 根据楼层查询
            Page<?> result;
            if ("6".equals(floor)) {
                log.info("查询六楼租房记录");
                result = rentalNewRepository.findRentalsWithFilters(
                    StringUtils.hasText(query.getKeyword()) ? query.getKeyword() : null,
                    query.getPaymentStatus(),
                    query.getMonthFilter(),
                    year,
                    month,
                    pageable
                );
            } else {
                log.info("查询五楼租房记录");
                result = rentalOldRepository.findRentalsWithFilters(
                    StringUtils.hasText(query.getKeyword()) ? query.getKeyword() : null,
                    query.getPaymentStatus(),
                    query.getMonthFilter(),
                    year,
                    month,
                    pageable
                );
            }
            
            log.info("查询完成，返回{}条记录，总计{}条", result.getContent().size(), result.getTotalElements());
            return result;
            
        } catch (Exception e) {
            log.error("查询{}楼租房记录失败: {}", floor, e.getMessage(), e);
            throw new RuntimeException("查询租房记录失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 验证排序字段是否有效
     */
    private boolean isValidSortField(String sortBy) {
        // 允许的排序字段
        String[] validFields = {
            "id", "roomNumber", "tenantName", "monthlyRent", "totalDue", 
            "paymentStatus", "checkInDate", "checkOutDate", "contractStartDate", 
            "contractEndDate", "createdAt", "updatedAt"
        };
        
        for (String field : validFields) {
            if (field.equals(sortBy)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Optional<?> getRentalByFloorAndId(String floor, Integer id) {
        if ("6".equals(floor)) {
            return rentalNewRepository.findById(id);
        } else {
            return rentalOldRepository.findById(id);
        }
    }
    
    @Override
    @Transactional
    public Object createRental(String floor, Object rental) {
        if ("6".equals(floor)) {
            RentalNew rentalNew = (RentalNew) rental;
            // 计算总费用
            calculateTotalDue(rentalNew);
            return rentalNewRepository.save(rentalNew);
        } else {
            RentalOld rentalOld = (RentalOld) rental;
            // 计算总费用
            calculateTotalDue(rentalOld);
            return rentalOldRepository.save(rentalOld);
        }
    }
    
    @Override
    @Transactional
    public Object updateRental(String floor, Integer id, Object rental) {
        if ("6".equals(floor)) {
            Optional<RentalNew> existingOpt = rentalNewRepository.findById(id);
            if (existingOpt.isPresent()) {
                RentalNew existing = existingOpt.get();
                RentalNew updated = (RentalNew) rental;
                // 复制属性，保留ID和时间戳
                BeanUtils.copyProperties(updated, existing, "id", "createdAt", "updatedAt");
                // 重新计算总费用
                calculateTotalDue(existing);
                return rentalNewRepository.save(existing);
            }
            throw new RuntimeException("租房记录不存在");
        } else {
            Optional<RentalOld> existingOpt = rentalOldRepository.findById(id);
            if (existingOpt.isPresent()) {
                RentalOld existing = existingOpt.get();
                RentalOld updated = (RentalOld) rental;
                // 复制属性，保留ID和时间戳
                BeanUtils.copyProperties(updated, existing, "id", "createdAt", "updatedAt");
                // 重新计算总费用
                calculateTotalDue(existing);
                return rentalOldRepository.save(existing);
            }
            throw new RuntimeException("租房记录不存在");
        }
    }
    
    @Override
    @Transactional
    public void deleteRental(String floor, Integer id) {
        if ("6".equals(floor)) {
            if (!rentalNewRepository.existsById(id)) {
                throw new RuntimeException("租房记录不存在");
            }
            rentalNewRepository.deleteById(id);
        } else {
            if (!rentalOldRepository.existsById(id)) {
                throw new RuntimeException("租房记录不存在");
            }
            rentalOldRepository.deleteById(id);
        }
    }
    
    @Override
    public PaymentStats getPaymentStats(String floor) {
        log.info("开始获取{}楼费用统计信息", floor);
        PaymentStats stats = new PaymentStats();
        
        try {
        if ("6".equals(floor)) {
            // 六楼统计
                stats.setTotalRentIncome(rentalNewRepository.getTotalRentIncome() != null ? 
                    rentalNewRepository.getTotalRentIncome() : BigDecimal.ZERO);
                stats.setTotalUtilitiesIncome(rentalNewRepository.getTotalUtilitiesIncome() != null ? 
                    rentalNewRepository.getTotalUtilitiesIncome() : BigDecimal.ZERO);
                stats.setTotalPendingAmount(rentalNewRepository.getTotalPendingAmount() != null ? 
                    rentalNewRepository.getTotalPendingAmount() : BigDecimal.ZERO);
                stats.setTotalOverdueAmount(rentalNewRepository.getTotalOverdueAmount() != null ? 
                    rentalNewRepository.getTotalOverdueAmount() : BigDecimal.ZERO);
            stats.setPaidCount(rentalNewRepository.countByPaymentStatus(1));
            stats.setUnpaidCount(rentalNewRepository.countByPaymentStatus(2));
            stats.setTotalCount(rentalNewRepository.count());
        } else {
            // 五楼统计
                stats.setTotalRentIncome(rentalOldRepository.getTotalRentIncome() != null ? 
                    rentalOldRepository.getTotalRentIncome() : BigDecimal.ZERO);
                stats.setTotalUtilitiesIncome(rentalOldRepository.getTotalUtilitiesIncome() != null ? 
                    rentalOldRepository.getTotalUtilitiesIncome() : BigDecimal.ZERO);
                stats.setTotalPendingAmount(rentalOldRepository.getTotalPendingAmount() != null ? 
                    rentalOldRepository.getTotalPendingAmount() : BigDecimal.ZERO);
                stats.setTotalOverdueAmount(rentalOldRepository.getTotalOverdueAmount() != null ? 
                    rentalOldRepository.getTotalOverdueAmount() : BigDecimal.ZERO);
            stats.setPaidCount(rentalOldRepository.countByPaymentStatus(1));
            stats.setUnpaidCount(rentalOldRepository.countByPaymentStatus(2));
            stats.setTotalCount(rentalOldRepository.count());
        }
        
        // 计算本月收入（租金+水电费）
            BigDecimal rentIncome = stats.getTotalRentIncome() != null ? stats.getTotalRentIncome() : BigDecimal.ZERO;
            BigDecimal utilitiesIncome = stats.getTotalUtilitiesIncome() != null ? stats.getTotalUtilitiesIncome() : BigDecimal.ZERO;
            stats.setMonthlyIncome(rentIncome.add(utilitiesIncome));
        
        // 计算支付完成率
        if (stats.getTotalCount() > 0) {
            double rate = (double) stats.getPaidCount() / stats.getTotalCount() * 100;
            stats.setPaymentRate(BigDecimal.valueOf(rate).setScale(2, RoundingMode.HALF_UP).doubleValue());
            } else {
                stats.setPaymentRate(0.0);
            }
            
            log.info("{}楼统计信息: 总记录数={}, 已支付={}, 未支付={}, 租金收入={}, 水电费收入={}", 
                floor, stats.getTotalCount(), stats.getPaidCount(), stats.getUnpaidCount(), 
                stats.getTotalRentIncome(), stats.getTotalUtilitiesIncome());
            
        } catch (Exception e) {
            log.error("获取{}楼费用统计信息失败", floor, e);
            throw new RuntimeException("获取费用统计信息失败: " + e.getMessage(), e);
        }
        
        return stats;
    }
    
    @Override
    @Transactional
    public void updatePaymentStatus(String floor, Integer id, Integer status) {
        if ("6".equals(floor)) {
            Optional<RentalNew> rentalOpt = rentalNewRepository.findById(id);
            if (rentalOpt.isPresent()) {
                RentalNew rental = rentalOpt.get();
                Integer oldStatus = rental.getPaymentStatus();
                rental.setPaymentStatus(status);
                rentalNewRepository.save(rental);
                
                // 如果从未缴费(2)更新为已缴费(1)，则创建缴费记录
                if (oldStatus != null && oldStatus == 2 && status == 1) {
                    createRentalRecord(rental, "6");
                    log.info("已为六楼房间{}的租客{}创建缴费记录", rental.getRoomNumber(), rental.getTenantName());
                }
            } else {
                throw new RuntimeException("租房记录不存在");
            }
        } else {
            Optional<RentalOld> rentalOpt = rentalOldRepository.findById(id);
            if (rentalOpt.isPresent()) {
                RentalOld rental = rentalOpt.get();
                Integer oldStatus = rental.getPaymentStatus();
                rental.setPaymentStatus(status);
                rentalOldRepository.save(rental);
                
                // 如果从未缴费(2)更新为已缴费(1)，则创建缴费记录
                if (oldStatus != null && oldStatus == 2 && status == 1) {
                    createRentalRecord(rental, "5");
                    log.info("已为五楼房间{}的租客{}创建缴费记录", rental.getRoomNumber(), rental.getTenantName());
                }
            } else {
                throw new RuntimeException("租房记录不存在");
            }
        }
    }
    
    /**
     * 创建缴费记录
     */
    private void createRentalRecord(Object rental, String floor) {
        try {
            if ("6".equals(floor) && rental instanceof RentalNew) {
                RentalNew rentalNew = (RentalNew) rental;
                RentalRecordsNew record = new RentalRecordsNew();
                record.setRoomNumber(rentalNew.getRoomNumber());
                record.setTenantName(rentalNew.getTenantName());
                record.setTotalRent(rentalNew.getTotalDue() != null ? rentalNew.getTotalDue() : rentalNew.getMonthlyRent());
                record.setPaymentDate(LocalDate.now());
                record.setCreatedAt(java.time.LocalDateTime.now());
                
                rentalRecordsNewRepository.save(record);
                log.info("成功创建六楼缴费记录: 房间={}, 租客={}, 金额={}", 
                    record.getRoomNumber(), record.getTenantName(), record.getTotalRent());
                
            } else if ("5".equals(floor) && rental instanceof RentalOld) {
                RentalOld rentalOld = (RentalOld) rental;
                RentalRecordsOld record = new RentalRecordsOld();
                record.setRoomNumber(rentalOld.getRoomNumber());
                record.setTenantName(rentalOld.getTenantName());
                record.setTotalRent(rentalOld.getTotalDue() != null ? rentalOld.getTotalDue() : rentalOld.getMonthlyRent());
                record.setPaymentDate(LocalDate.now());
                record.setCreatedAt(java.time.LocalDateTime.now());
                
                rentalRecordsOldRepository.save(record);
                log.info("成功创建五楼缴费记录: 房间={}, 租客={}, 金额={}", 
                    record.getRoomNumber(), record.getTenantName(), record.getTotalRent());
            }
        } catch (Exception e) {
            log.error("创建缴费记录失败: floor={}, rental={}", floor, rental, e);
            // 不抛出异常，避免影响主要的支付状态更新流程
        }
    }
    
    @Override
    @Transactional
    public void batchUpdatePaymentStatus(String floor, List<Integer> ids, Integer status) {
        for (Integer id : ids) {
            updatePaymentStatus(floor, id, status);
        }
    }
    
    @Override
    public List<?> exportPaymentData(String floor, PaymentQuery query) {
        // 设置大页面大小来获取所有数据
        query.setPage(0);
        query.setSize(Integer.MAX_VALUE);
        Page<?> page = getRentalsByFloor(floor, query);
        return page.getContent();
    }
    
    @Override
    public List<?> getExpiringContracts(String floor, int days) {
        LocalDate endDate = LocalDate.now().plusDays(days);
        
        if ("6".equals(floor)) {
            return rentalNewRepository.findExpiringContracts(endDate);
        } else {
            return rentalOldRepository.findExpiringContracts(endDate);
        }
    }
    
    /**
     * 计算总费用（六楼）
     */
    private void calculateTotalDue(RentalNew rental) {
        BigDecimal totalDue = rental.getMonthlyRent()
            .add(rental.getWaterFee() != null ? rental.getWaterFee() : BigDecimal.ZERO)
            .add(rental.getElectricityFee() != null ? rental.getElectricityFee() : BigDecimal.ZERO)
            .add(rental.getUtilitiesFee() != null ? rental.getUtilitiesFee() : BigDecimal.ZERO);
        rental.setTotalDue(totalDue);
    }
    
    /**
     * 计算总费用（五楼）
     */
    private void calculateTotalDue(RentalOld rental) {
        BigDecimal totalDue = rental.getMonthlyRent()
            .add(rental.getWaterFee() != null ? rental.getWaterFee() : BigDecimal.ZERO)
            .add(rental.getElectricityFee() != null ? rental.getElectricityFee() : BigDecimal.ZERO)
            .add(rental.getUtilitiesFee() != null ? rental.getUtilitiesFee() : BigDecimal.ZERO);
        rental.setTotalDue(totalDue);
    }
    
    @Override
    public MonthlyIncomeData getMonthlyIncomeData(String floor, int months) {
        try {
            log.info("获取{}楼最近{}个月的收入趋势数据", floor, months);
            
            // 验证楼层参数
            if (!"5".equals(floor) && !"6".equals(floor)) {
                throw new IllegalArgumentException("不支持的楼层: " + floor);
            }
            
            List<String> monthLabels = new ArrayList<>();
            List<BigDecimal> incomes = new ArrayList<>();
            
            // 获取当前月份，往前推指定月数
            YearMonth currentMonth = YearMonth.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            
            // 构建月份列表和对应收入数据
            for (int i = months - 1; i >= 0; i--) {
                YearMonth targetMonth = currentMonth.minusMonths(i);
                String monthLabel = targetMonth.format(formatter);
                monthLabels.add(monthLabel);
                
                // 查询该月收入数据
                BigDecimal monthlyIncome = getMonthlyIncome(floor, targetMonth.getYear(), targetMonth.getMonthValue());
                incomes.add(monthlyIncome);
                
                log.info("{}楼 {} 月收入: {}", floor, monthLabel, monthlyIncome);
            }
            
            // 计算统计数据
            BigDecimal totalIncome = incomes.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
            BigDecimal averageIncome = incomes.isEmpty() ? BigDecimal.ZERO : 
                totalIncome.divide(BigDecimal.valueOf(incomes.size()), 2, RoundingMode.HALF_UP);
            
            BigDecimal currentMonthIncome = incomes.isEmpty() ? BigDecimal.ZERO : 
                incomes.get(incomes.size() - 1);
                
            BigDecimal previousMonthIncome = incomes.size() < 2 ? BigDecimal.ZERO : 
                incomes.get(incomes.size() - 2);
            
            // 计算环比增长率
            Double growthRate = 0.0;
            if (previousMonthIncome.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal rate = currentMonthIncome.subtract(previousMonthIncome)
                    .divide(previousMonthIncome, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
                growthRate = rate.doubleValue();
            }
            
            MonthlyIncomeData result = new MonthlyIncomeData();
            result.setMonths(monthLabels);
            result.setIncomes(incomes);
            result.setCurrentMonthIncome(currentMonthIncome);
            result.setPreviousMonthIncome(previousMonthIncome);
            result.setGrowthRate(growthRate);
            result.setAverageIncome(averageIncome);
            result.setTotalIncome(totalIncome);
            
            log.info("收入趋势数据计算完成 - 当前月收入: {}, 环比增长率: {}%", currentMonthIncome, growthRate);
            return result;
            
        } catch (Exception e) {
            log.error("获取月度收入数据失败", e);
            throw new RuntimeException("获取月度收入数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取指定楼层和月份的收入数据
     */
    private BigDecimal getMonthlyIncome(String floor, int year, int month) {
        try {
            Double income;
            if ("6".equals(floor)) {
                // 六楼使用rental_records_new表
                income = rentalRecordsNewRepository.getTotalRentByMonth(year, month);
            } else {
                // 五楼使用rental_records_old表  
                income = rentalRecordsOldRepository.getTotalRentByMonth(year, month);
            }
            
            return income != null ? BigDecimal.valueOf(income) : BigDecimal.ZERO;
        } catch (Exception e) {
            log.error("获取{}楼{}年{}月收入数据失败", floor, year, month, e);
            return BigDecimal.ZERO;
        }
    }
} 